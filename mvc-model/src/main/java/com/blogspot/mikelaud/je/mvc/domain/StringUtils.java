package com.blogspot.mikelaud.je.mvc.domain;

public interface StringUtils {

	static String nvl(String aString) {
		return (null == aString ? "" : aString);
	}
	
}