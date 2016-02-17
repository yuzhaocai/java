package com.lczy.media.util;

public class HtmlUtil {

	public static final String htmlToCode(String s) {
		if (s == null) {
			return "";
		} else {
			s = s.replace("\n\r", "<br>&nbsp;&nbsp;");
			s = s.replace("\r\n", "<br>&nbsp;&nbsp;");// 这才是正确的！
			s = s.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
			s = s.replace(" ", "&nbsp;");

			s = s.replace("\"", "\\" + "\"");// 如果原文含有双引号，这一句最关键！！！！！！
			return s;
		}
	}
}
