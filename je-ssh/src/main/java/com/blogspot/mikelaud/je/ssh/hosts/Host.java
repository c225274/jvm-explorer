package com.blogspot.mikelaud.je.ssh.hosts;

import java.nio.file.Path;

import com.blogspot.mikelaud.je.ssh.common.Endpoint;

public interface Host {

	Endpoint getEndpoint();
	String getUserName();
	//
	boolean login(String aUserName, String aPassword);
	void logout();
	//
	boolean isOnline();
	//
	int exec(String aCommand);
	int copyFromLocal(Path aFileLocal, Path aFileRemote);
	int copyToLocal(Path aFileRemote, Path aFileLocal);

}