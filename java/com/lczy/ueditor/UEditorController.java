package com.lczy.ueditor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.ueditor.define.ActionMap;
import com.baidu.ueditor.define.State;

@Controller
@RequestMapping("/ueditor")
public class UEditorController {
	
	private UEditorConfigManager configManager;

	@RequestMapping("upload")
	@ResponseBody
	public String upload(HttpServletRequest request) {
		String actionType = request.getParameter( "action" );
		Map<String, Object> conf = getConfigManager(request).getConfig( ActionMap.getType( actionType ) );
		
		State state = new ZYUploader( request, conf ).doExec();
		
		return state.toJSONString();
	}
	
	@RequestMapping("catchimage")
	@ResponseBody
	public String catchimage(HttpServletRequest request) {
		State state = null;
		
		Map<String, Object> conf = getConfigManager(request).getConfig(ActionMap.getType( "catchimage" ));
		String[] list = request.getParameterValues( (String)conf.get( "fieldName" ) );
		state = new ZYImageHunter( conf ).capture( list );
		
		return state.toJSONString();
	}

	private UEditorConfigManager getConfigManager(HttpServletRequest request) {
		if (configManager == null) {
			configManager = UEditorConfigManager.getInstance(request.getSession().getServletContext().getRealPath("/"), request.getContextPath());
		}
		return configManager;
	}
}
