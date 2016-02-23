package com.blogspot.mikelaud.je.ssh.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiConsumer;

import com.blogspot.mikelaud.je.ssh.common.ExitStatus;
import com.blogspot.mikelaud.je.ssh.common.Logger;
import com.blogspot.mikelaud.je.ssh.common.UnixPath;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

public class CopyFromLocalOperation extends AbstractOperation {

	private final Path INPUT_FILE_LOCAL;
	private final Path INPUT_FILE_REMOTE;
	//
	private final File FILE_LOCAL;
	private final UnixPath FILE_REMOTE;
	//
	private final int COPY_BUFFER_SIZE;

	private int checkFile(File aFile) {
		if (aFile.exists()) {
			return ExitStatus.SUCCESS.get();
		}
		else {
			Logger.error(String.format("scp: %s: No such file or directory", FILE_LOCAL));
			return ExitStatus.ERROR.get();
		}
	}

	private int connect(ChannelExec aChannel, InputStream aIn) throws Exception {
		aChannel.connect();
		return checkAck(aIn);
	}

	private int write(OutputStream aOut, InputStream aIn, BiConsumer<OutputStream, InputStream> aWriter) throws Exception {
		aWriter.accept(aOut, aIn);
		aOut.flush();
		return checkAck(aIn);
	}

	private void writeTime(OutputStream aOut, InputStream aIn) {
		try {
			// The access time should be sent here, but it is not accessible with JavaAPI
			long lastModified = (FILE_LOCAL.lastModified() / 1000);
			String time = String.format("T%d 0 %d 0\n", lastModified, lastModified);
			aOut.write(time.getBytes());
		}
		catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	private void writeIdentity(OutputStream aOut, InputStream aIn) {
		try {
			// send "C0644 filesize filename", where filename should not include '/'
			long size = FILE_LOCAL.length();
			String identity = String.format("C0644 %d %s\n", size, FILE_REMOTE.getFileName());
			aOut.write(identity.getBytes());
		}
		catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	private void writeContent(OutputStream aOut, InputStream aIn) {
		try {
			// send a content of file
			try (FileInputStream fis = new FileInputStream(FILE_LOCAL)) {
				byte[] buffer = new byte[COPY_BUFFER_SIZE];
				while (true) {
					int readCount = fis.read(buffer, 0, buffer.length);
					if (readCount <= 0) break;
					aOut.write(buffer, 0, readCount);
				}
				writeZero(aOut);
			}
		}
		catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	@Override
	protected int executeOperation(Session aSession) throws Exception {
		ChannelExec channel = newChannelExec(aSession);
		InputStream in = channel.getInputStream();
		String command = String.format("scp -p -t %s", FILE_REMOTE.getFilePath());
		channel.setCommand(command);
		int rcode = ExitStatus.ABORT.get();
		while (true) {
			try (OutputStream out = channel.getOutputStream()) {
				if (hasError(rcode = checkFile(FILE_LOCAL))) break;
				if (hasError(rcode = connect(channel, in))) break;
				if (hasError(rcode = write(out, in, this::writeTime))) break;
				if (hasError(rcode = write(out, in, this::writeIdentity))) break;
				if (hasError(rcode = write(out, in, this::writeContent))) break;
			}
			break;
		}
		channel.disconnect();
		return rcode;
	}

	public CopyFromLocalOperation(Path aFileLocal, Path aFileRemote) {
		INPUT_FILE_LOCAL = Objects.requireNonNull(aFileLocal);
		INPUT_FILE_REMOTE = Objects.requireNonNull(aFileRemote);
		//
		FILE_REMOTE = new UnixPath(aFileRemote);
		FILE_LOCAL = aFileLocal.toFile();
		//
		COPY_BUFFER_SIZE = 1024;
	}

	public Path getFileLocal() {
		return INPUT_FILE_LOCAL;
	}

	public Path getFileRemote() {
		return INPUT_FILE_REMOTE;
	}

	@Override
	public String toString() {
		return String.format("scp %s %s@%s:%s", FILE_LOCAL, getUserName(), getHostName(), FILE_REMOTE.getFilePath());
	}

}
