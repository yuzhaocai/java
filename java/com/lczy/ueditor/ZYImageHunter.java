/**
 * 
 */
package com.lczy.ueditor;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.MIMEType;
import com.baidu.ueditor.define.MultiState;
import com.baidu.ueditor.define.State;
import com.lczy.media.util.FileServerResponse;
import com.lczy.media.util.FileServerUtils;


/**
 * @author wu
 *
 */
public class ZYImageHunter {
	
	private static Logger log = LoggerFactory.getLogger(ZYImageHunter.class);

	private List<String> allowTypes = null;
	private long maxSize = -1;
	
	private List<String> filters = null;
	
	public ZYImageHunter ( Map<String, Object> conf ) {
		this.maxSize = (Long)conf.get( "maxSize" );
		this.allowTypes = Arrays.asList( (String[])conf.get( "allowFiles" ) );
		this.filters = Arrays.asList( (String[])conf.get( "filter" ) );
		
	}
	
	public State capture ( String[] list ) {
		
		MultiState state = new MultiState( true );
		
		for ( String source : list ) {
			state.addState( captureRemoteData( source ) );
		}
		
		return state;
		
	}

	public State captureRemoteData ( String urlStr ) {
		
		HttpURLConnection connection = null;
		URL url = null;
		String suffix = null;
		
		try {
			url = new URL( urlStr );

			if ( !validHost( url.getHost() ) ) {
				return new BaseState( false, AppInfo.PREVENT_HOST );
			}
			
			connection = (HttpURLConnection) url.openConnection();
		
			connection.setInstanceFollowRedirects( true );
			connection.setUseCaches( true );
		
			if ( !validContentState( connection.getResponseCode() ) ) {
				return new BaseState( false, AppInfo.CONNECTION_ERROR );
			}
			
			suffix = MIMEType.getSuffix( connection.getContentType() );
			
			if ( !validFileType( suffix ) ) {
				return new BaseState( false, AppInfo.NOT_ALLOW_FILE_TYPE );
			}
			
			if ( !validFileSize( connection.getContentLength() ) ) {
				return new BaseState( false, AppInfo.MAX_SIZE );
			}
			
			return catchImage( urlStr );
			
		} catch ( Exception e ) {
			log.warn("抓取远程图片发生错误", e);
			return new BaseState( false, AppInfo.REMOTE_FAIL );
		}
		
	}
	
	private State catchImage(String url) throws Exception {
		
		State state = null;
		FileServerResponse resp = FileServerUtils.catchImage(url);
		if ( resp.isSuccess() ) {
			state = new BaseState(true);
			state.putInfo( "size", resp.getEntity().getSize() );
			state.putInfo( "title", url.substring(url.lastIndexOf('/')) );
			state.putInfo( "url", resp.getInfo("url"));
			state.putInfo( "source", url );
		} else {
			state = new BaseState( false, AppInfo.REMOTE_FAIL );
		}

		return state;
	}
	
	private boolean validHost ( String hostname ) {
		
		return !filters.contains( hostname );
		
	}
	
	private boolean validContentState ( int code ) {
		
		return HttpURLConnection.HTTP_OK == code;
		
	}
	
	private boolean validFileType ( String type ) {
		
		return this.allowTypes.contains( type );
		
	}
	
	private boolean validFileSize ( int size ) {
		return size < this.maxSize;
	}
	
	
}
