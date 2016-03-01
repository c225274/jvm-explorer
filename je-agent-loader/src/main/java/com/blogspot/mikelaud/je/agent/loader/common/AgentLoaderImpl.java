package com.blogspot.mikelaud.je.agent.loader.common;

import java.nio.file.Path;
import java.util.Objects;

import com.blogspot.mikelaud.je.agent.bios.common.AgentBios;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class AgentLoaderImpl implements AgentLoader {

	private final AgentBios AGENT_BIOS;
	private final RemoteLoaderFactory REMOTE_LOADER_FACTORY;
	//
	private final Path AGENT_HEAD_JAR;
	private final Path AGENT_BODY_JAR;
	
	@Inject
	private AgentLoaderImpl
	(	AgentBios aAgentBios
	,	RemoteLoaderFactory aRemoteLoaderFactory
	,	@Assisted Path aAgentHeadJar
	,	@Assisted Path aAgentBodyJar
	) {
		AGENT_BIOS = aAgentBios;
		REMOTE_LOADER_FACTORY = aRemoteLoaderFactory;
		//
		AGENT_HEAD_JAR = Objects.requireNonNull(aAgentHeadJar);
		AGENT_BODY_JAR = Objects.requireNonNull(aAgentBodyJar);
	}
	
	@Override
	public boolean loadAgent() {
		return AGENT_BIOS.loadAgent(AGENT_HEAD_JAR, AGENT_BODY_JAR);
	}

	@Override
	public boolean loadAgent(String aJvmId) {
		return AGENT_BIOS.loadAgent(AGENT_HEAD_JAR, AGENT_BODY_JAR, aJvmId);
	}

	@Override
	public RemoteLoader newRemoteLoader(String aHost, String aUserName, String aPassword) {
		return REMOTE_LOADER_FACTORY.newRemoteLoaderSsh(AGENT_BIOS, aHost, aUserName, aPassword);
	}

}
