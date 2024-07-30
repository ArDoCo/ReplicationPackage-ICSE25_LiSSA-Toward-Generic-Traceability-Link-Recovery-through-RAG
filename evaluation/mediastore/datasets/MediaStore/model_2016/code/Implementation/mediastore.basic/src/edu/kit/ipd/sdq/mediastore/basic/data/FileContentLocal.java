package edu.kit.ipd.sdq.mediastore.basic.data;

import java.nio.file.Path;

public class FileContentLocal extends FileContent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2227858509812298329L;
	private Path path;
	
	
	
	public FileContentLocal(Path path) {
		super();
		this.path = path;
	}



	public Path getPath() {
		return path;
	}



	public void setPath(Path path) {
		this.path = path;
	}



	@Override
	public boolean isLocal() {
		return true;
	}
	
}
