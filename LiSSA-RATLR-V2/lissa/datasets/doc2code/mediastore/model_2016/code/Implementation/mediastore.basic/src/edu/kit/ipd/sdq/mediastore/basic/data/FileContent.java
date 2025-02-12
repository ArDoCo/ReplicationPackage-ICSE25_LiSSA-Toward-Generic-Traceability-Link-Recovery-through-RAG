package edu.kit.ipd.sdq.mediastore.basic.data;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.kit.ipd.sdq.mediastore.basic.utils.FSUtil;

public abstract class FileContent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6365409533974513480L;

	public abstract boolean isLocal();
	
	
	/**
	 * Converts a FileContentLocal to a FileContentRemote or vice versa when needed
	 * @param local true if the desired FileContent is local, false otherwise
	 * @param fileName Name of the temporary file to be created if the current FileContent is remote
	 * and the desired FileContent is local 
	 * @param extension Extension of the local file
	 * @return A new FileContentRemote/Local or this object if no conversion needed, that is when 
	 * this object is a FileContentRemote (resp. Local) and local=false (resp. true)
	 */
	public FileContent convertIfNeeded(boolean local, String fileName, String extension) {
		FileContent content = this;
        if (isLocal() && !local) { 
    		FileContentLocal localContent = (FileContentLocal) this;
    		byte[] data = FSUtil.pathToBytes(localContent.getPath());
    		content = new FileContentRemote(data);
    		try {
				Files.deleteIfExists(localContent.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else if (!isLocal() && local) {
    		FileContentRemote remoteContent = (FileContentRemote) content;
        	Path filePath = FSUtil.bytesToPath(remoteContent.getBytes(), fileName, extension);
    		content = new FileContentLocal(filePath);
        }
        return content;
	}
}
