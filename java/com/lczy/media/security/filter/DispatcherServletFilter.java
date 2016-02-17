package com.lczy.media.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

public class DispatcherServletFilter extends DispatcherServlet{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 4450251853690506880L;

	@Override
	  protected void noHandlerFound(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {		
	    response.sendRedirect(request.getContextPath() + "/notFound");
	  }

}
