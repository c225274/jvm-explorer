package com.blogspot.mikelaud.je.agent.loader.common;

import java.util.stream.Stream;

import com.blogspot.mikelaud.je.agent.bios.domain.JvmIdentity;
import com.blogspot.mikelaud.je.common.file_source.FileSource;

public interface AgentLoader {

	FileSource getHeadJar();
	FileSource getBodyJar();
	//
	String getJavaHome();
	Stream<JvmIdentity> getJvmList();
	//
	boolean loadAgentById(String aJvmId);
	boolean loadAgentByName(String aJvmName);

}
