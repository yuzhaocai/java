package com.lczy.media.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.mapper.JsonMapper;

import com.google.common.collect.Maps;
import com.lczy.common.web.HttpRequests;


public class FileServerUtils {

	private static Logger logger = LoggerFactory.getLogger(FileServerUtils.class);
	
	private static String appId;
	private static String appKey;
	private static String serverUrl;
	
	/**
	 * @param fileId 文件 ID, 第一次上传时为 null.
	 * @param fileName 文件名
	 * @param b 文件内容
	 * @param watermark 是否加水印
	 * @param file_type 文件类型
	 * 
	 * @return 文件 ID
	 */
	public static String upload(String fileId, String fileName, byte[] b, boolean watermark, String file_type) throws Exception {
		return upload(fileId,fileName,b,watermark,file_type,false);
	}
	
	/**
	 * @param fileId 文件 ID, 第一次上传时为 null.
	 * @param fileName 文件名
	 * @param b 文件内容
	 * @param watermark 是否加水印
	 * @param file_type 文件类型
	 * @param auth 访问时是否需要授权
	 * @return 文件 ID
	 * @throws Exception
	 */
	public static String upload(String fileId, String fileName, byte[] b,
			boolean watermark, String file_type, boolean auth) throws Exception {
		
		String url = getProperty("fileserver.path", serverUrl)+"upload/";
		Map<String, String> params = getParams(fileId, fileName, watermark, file_type, auth);
		
		String res = HttpRequests.doPost(url, params, "file", b);
		FileServerResponse fsr = JsonMapper.nonEmptyMapper().fromJson(res, FileServerResponse.class);
		
		return fsr.getEntity().getId();

	}
	
	/**
	 * 上传文件到文件服务器.
	 * 
	 * @param fileId 文件 ID, 第一次上传时为 null.
	 * @param fileName 文件名
	 * @param file 待上传文件
	 * @param watermark 是否加水印
	 * @param file_type 文件类型
	 * @param auth 访问时是否需要授权
	 * @return 文件 ID
	 * @throws IOException
	 */
	public static String upload(String fileId, String fileName, File file,
			boolean watermark, String file_type, boolean auth) throws IOException  {
		
		String url = getProperty("fileserver.path", serverUrl)+"upload/";
		Map<String, String> params = getParams(fileId, fileName, watermark, file_type, auth);
		
		String res = HttpRequests.doPost(url, params, "file", file);
		FileServerResponse fsr = JsonMapper.nonEmptyMapper().fromJson(res, FileServerResponse.class);
		
		return fsr.getEntity().getId();
	}

	private static Map<String, String> getParams(String fileId, String fileName,
			boolean watermark, String file_type, boolean auth) {
		
		Map<String, String> params = Maps.newHashMap();
		if(StringUtils.isBlank(fileId)){
			fileId = "";
		}
		params.put("id", fileId);
		params.put("appid", getProperty("appid", appId));
		params.put("appkey", getProperty("appkey", appKey));
		params.put("file_type", file_type);
		params.put("file_name", fileName);
		params.put("auth", Boolean.toString(auth));
		params.put("watermark", Boolean.toString(watermark));
		
		return params;
	}
	
	public static String getFileUrl(String fileId) {
		if( StringUtils.isBlank(fileId) )
			return "";
		
		return String.format("%sget/%s/", serverUrl, fileId);
	}
	
	public static String token() throws Exception{
		String url = getProperty("fileserver.path", serverUrl)+"token/";

		Map<String, String> params = Maps.newHashMap();
		params.put("appid", getProperty("appid", appId));
		params.put("appkey", getProperty("appkey", appKey));

		String res = HttpRequests.doPost(url, params);
		JSONObject json = new JSONObject(res);
		if(json.getBoolean("success")){
			return json.getJSONObject("entity").getString("token");
		}else{
			throw new Exception(res);
		}
		
	}

	
	/**
	 * 删除文件
	 * 
	 * @param fileId
	 * @throws Exception
	 */
	public static void delete(String fileId) throws Exception{
		delete(fileId,null);
	}
	
	public static void delete(String fileId, String token) throws Exception{
//		删除地址 http://fileserver.lczybj.com/fileserver/del/ 只接收post请求，参数 id、appid、appkey ，如果是需要鉴权的资源则必须填写token参数并忽略appid、appkey
//			不需要鉴权时，post 传递参数 id、appid、appkey 完成删除
//			需要鉴权时，post 传递参数 id、token 完成删除
		String url = getProperty("fileserver.path", serverUrl)+"del/";

		Map<String, String> params = Maps.newHashMap();
		
		if(token!=null){
			params.put("id", fileId);
		}else{
			params.put("appid", getProperty("appid", appId));
			params.put("appkey", getProperty("appkey", appKey));
		}
		
		String res = HttpRequests.doPost(url, params);
		
		logger.debug("delete__res == "+res);
		JSONObject json = new JSONObject(res);
		if (!json.getBoolean("success")) {
			throw new Exception("删除失败!");
		}
	
	}
	
	/**
	 * 将word文档转换为html
	 * 
	 * @param fileId
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public static String toHtml(String fileId) throws IOException {
		String url = getProperty("fileserver.path", serverUrl) + "doc2html/" + fileId;
		
		String res = HttpRequests.doGet(url, null);
		
		JSONObject json = new JSONObject(res);
		if (json.getBoolean("success")) {
			return json.getJSONObject("entity").getString("html");
		} else {
			throw new RuntimeException("转换失败!");
		}
			
	}
	
	private static String getProperty(String key,String def){
		return def;
	}
	
	
	/**
	 * 抓取远程图片.
	 * @param imgUrl 远程图片的 url.
	 * @return 文件服务器响应结果.
	 * @throws Exception
	 */
	public static FileServerResponse catchImage(String imgUrl) throws Exception{
		String path = getProperty("fileserver.path", serverUrl);
		String url = path+"upload/";

		Map<String, String> fields = getUploadOptions();
		fields.put("url", imgUrl);
		String res = HttpRequests.doPost(url, fields);
		
		FileServerResponse resp = getFileServerResponse(path, res);
		
		return resp;
	}
	
	
	/**
	 * 上传图片文件.
	 * @param file 被上传的文件.
	 * @return 文件服务器响应结果.
	 * @throws Exception
	 */
	public static FileServerResponse uploadImage(File file) throws Exception{
		String path = getProperty("fileserver.path", serverUrl);
		String url = path+"upload/";

		Map<String, String> fields = getUploadOptions();
		String res = HttpRequests.doPost(url, fields, "file", file);
		
		FileServerResponse resp = getFileServerResponse(path, res);
		
		return resp;
	}
	
	/**
	 * 上传图片文件.
	 * @param ins 图片文件输入流.
	 * @param filename 文件名.
	 * @return 文件服务器响应结果.
	 * @throws Exception
	 */
	public static FileServerResponse uploadImage(InputStream ins, String filename) throws Exception{
		String path = getProperty("fileserver.path", serverUrl);
		String url = path+"upload/";
		
		Map<String, String> fields = getUploadOptions();
		String res = HttpRequests.doPost(url, fields, "file", ins, filename);
		
		FileServerResponse resp = getFileServerResponse(path, res);
		
		return resp;
	}
	
	
	/**
	 * 上传图片文件.
	 * @param data 图片数据.
	 * @return 文件服务器响应结果.
	 * @throws Exception
	 */
	public static FileServerResponse uploadImage(byte[] data) throws Exception{
		String path = getProperty("fileserver.path", serverUrl);
		String url = path+"upload/";
		
		Map<String, String> fields = getUploadOptions();
		String res = HttpRequests.doPost(url, fields, "file", data);
		
		FileServerResponse resp = getFileServerResponse(path, res);
		
		return resp;
	}

	private static FileServerResponse getFileServerResponse(String path, String res) throws Exception {
		FileServerResponse resp = JsonMapper.nonEmptyMapper().fromJson(res, FileServerResponse.class);
		if (resp.isSuccess()) {
			resp.setInfo("url", path + "get/"+resp.getEntity().getId());
		} else {
			throw new Exception("上传失败!");
		}
		return resp;
	}

	private static Map<String, String> getUploadOptions() {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("appid", getProperty("appid",appId));
		fields.put("appkey", getProperty("appkey", appKey));
		fields.put("watermark", "false");
		fields.put("file_type", "image");
		return fields;
	}

	public static void setAppId(String appId) {
		FileServerUtils.appId = appId;
	}

	public static void setAppKey(String appKey) {
		FileServerUtils.appKey = appKey;
	}

	public static void setServerUrl(String serverUrl) {
		FileServerUtils.serverUrl = serverUrl;
	}
	
	
}
