package com.blogspot.mikelaud.je.agent.loader.common;

public interface AgentLoader {

	boolean loadAgent();
	boolean loadAgentById(String aJvmId);
	//
	RemoteLoader newRemoteLoader(String aHost, String aUserName, String aPassword);

}
