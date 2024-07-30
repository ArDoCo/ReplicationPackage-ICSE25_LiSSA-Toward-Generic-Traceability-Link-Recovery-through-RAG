package edu.kit.ipd.sdq.mediastore.basic.data;

public class FileContentRemote extends FileContent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2989985117433061244L;
	private byte[] bytes;
	
	public FileContentRemote(byte[] bytes) {
		super();
		this.bytes = bytes;
	}
	
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public boolean isLocal() {
		return false;
	}
	
}
