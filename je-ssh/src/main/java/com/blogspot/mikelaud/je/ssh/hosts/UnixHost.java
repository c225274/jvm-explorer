package com.blogspot.mikelaud.je.ssh.hosts;

import java.nio.file.Path;
import java.util.Objects;

import com.blogspot.mikelaud.je.ssh.common.Endpoint;
import com.blogspot.mikelaud.je.ssh.common.ExitStatus;
import com.blogspot.mikelaud.je.ssh.operations.CopyFromLocalOperation;
import com.blogspot.mikelaud.je.ssh.operations.CopyToLocalOperation;
import com.blogspot.mikelaud.je.ssh.operations.ExecOperation;
import com.blogspot.mikelaud.je.ssh.operations.SshOperation;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class UnixHost implements Host {

	private final Endpoint ENDPOINT;
	private Session mSession;

	private boolean hasSession() {
		return (null != mSession);
	}

	private int execute(SshOperation aOperation) {
		if (hasSession()) {
			return aOperation.execute(mSession);
		}
		else {
			System.out.println(String.format("[ssh]: ERROR: No ssh session for: %s", aOperation.toString()));
			return ExitStatus.ABORT.get();
		}
	}

	public UnixHost(String aHostName, int aPort) {
		Objects.requireNonNull(aHostName);
		ENDPOINT = new Endpoint(aHostName, aPort);
		mSession = null;
	}

	@Override
	public Endpoint getEndpoint() {
		return ENDPOINT;
	}

	@Override
	public String getUserName() {
		return (hasSession() ? mSession.getUserName() : "");
	}

	@Override
	public boolean login(String aUserName, String aPassword) {
		Objects.requireNonNull(aUserName);
		Objects.requireNonNull(aPassword);
		try {
			JSch ssh = new JSch();
			Session session = ssh.getSession(aUserName, ENDPOINT.getHost(), ENDPOINT.getPort());
			session.setPassword(aPassword);
			session.setConfig("StrictHostKeyChecking", "no");
			try {
				System.out.println(String.format("[ssh]: ssh %s@%s", aUserName, ENDPOINT.toString()));
				session.connect();
			}
			catch (JSchException e) {
				System.out.println(String.format("[ssh]: ERROR: %s", e.getMessage()));
			}
			mSession = session.isConnected() ? session : null;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return isOnline();
	}

	@Override
	public void logout() {
		if (hasSession()) {
			if (mSession.isConnected()) {
				System.out.println(String.format("[ssh]: logout"));
				mSession.disconnect();
			}
			mSession = null;
		}
	}

	@Override
	public boolean isOnline() {
		return (hasSession() ? mSession.isConnected() : false);
	}

	@Override
	public int exec(String aCommand) {
		return execute(new ExecOperation(aCommand));
	}

	@Override
	public int copyFromLocal(Path aFileLocal, Path aFileRemote) {
		return execute(new CopyFromLocalOperation(aFileLocal, aFileRemote));
	}

	@Override
	public int copyToLocal(Path aFileRemote, Path aFileLocal) {
		return execute(new CopyToLocalOperation(aFileRemote, aFileLocal));
	}

	@Override
	public String toString() {
		return ENDPOINT.toString();
	}

}