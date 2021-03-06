package com.blogspot.mikelaud.je.ssh.common;

import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UnixPath {

	private final boolean HAS_ROOT;
	private final String FILE_NAME;
	private final String FILE_PATH;

	private String replaceSeparator(Path aFilePath, String aNewSeparator) {
		return StreamSupport
			.stream(aFilePath.spliterator(), false)
			.map(String::valueOf)
			.collect(Collectors.joining(aNewSeparator));
	}

	public UnixPath(Path aFilePath) {
		Objects.requireNonNull(aFilePath);
		HAS_ROOT = (null != aFilePath.getRoot());
		FILE_NAME = Objects.toString(aFilePath.getFileName(), "");
		FILE_PATH = (HAS_ROOT ? UnixConst.SEPARATOR : "").concat(replaceSeparator(aFilePath, UnixConst.SEPARATOR));
	}

	public boolean hasRoot() { return HAS_ROOT; }
	public String getFileName() { return FILE_NAME; }
	public String getFilePath() { return FILE_PATH; }

	@Override
	public String toString() {
		return FILE_PATH;
	}

}
