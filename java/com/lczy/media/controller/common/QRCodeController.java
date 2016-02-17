package com.lczy.media.controller.common;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lczy.media.util.RoundLineQRCodeUtils;

@Controller
@RequestMapping("/common")
public class QRCodeController {
	
	@RequestMapping("/qrcode")
	public String qrcode(HttpServletRequest request, HttpServletResponse response) {
		try {
			ServletOutputStream out = response.getOutputStream();
			//RoundLineQRCodeUtils.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
