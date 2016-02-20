package com.blogspot.mikelaud.je.ssh.operations;

import java.time.Duration;
import java.util.Objects;

import com.blogspot.mikelaud.je.ssh.common.OperationStatus;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public abstract class AbstractOperation implements SshOperation {

	private final String EXEC_CHANNEL_TYPE;
	private final Duration POLL_INTERVAL;

	protected abstract int executeOperation(Session aSession) throws Exception;

	protected AbstractOperation() {
		EXEC_CHANNEL_TYPE = "exec";
		POLL_INTERVAL = Duration.ofMillis(100);
	}

	protected final ChannelExec newChannelExec(Session aSession) throws JSchException {
		Objects.requireNonNull(aSession);
		return (ChannelExec) aSession.openChannel(EXEC_CHANNEL_TYPE);
	}

	protected final void waitEof(Channel aChannel) {
		Objects.requireNonNull(aChannel);
		while (!aChannel.isEOF()) {
			try {
				Thread.sleep(POLL_INTERVAL.toMillis());
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public final int execute(Session aSession) {
		Objects.requireNonNull(aSession);
		try {
			System.out.println(toString());
			return executeOperation(aSession);
		}
		catch (Exception e) {
			e.printStackTrace();
			return OperationStatus.EXIT_FAILURE.getValue();
		}
	}

}
