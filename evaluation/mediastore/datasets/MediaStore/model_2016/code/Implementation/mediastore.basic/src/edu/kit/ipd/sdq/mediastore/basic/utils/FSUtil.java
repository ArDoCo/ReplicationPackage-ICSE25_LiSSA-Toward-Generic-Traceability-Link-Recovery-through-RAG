/**
 * Media Store V3
 * Copyright (C) 2015 Software Design and Quality Group (SDQ), KIT, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sdq.mediastore.basic.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.kit.ipd.sdq.mediastore.basic.config.GlobalConstantsContainer;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContent;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentLocal;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentRemote;

public class FSUtil {

    public static String getTempFileName(final String basePath, final String fileName, final String fileExtension) {
        return basePath + GlobalConstantsContainer.SEPARATOR + fileName.replace("@", "").replace(".", "")
                + System.nanoTime() + '.' + fileExtension;
    }

    public static String getTempFileName(final String fileName, final String fileExtension) {
        return getTempFileName(GlobalConstantsContainer.getTempDirPath(), fileName, fileExtension);
    }

    
	public static void writeToFile(FileContent content, String inputPath) throws FileNotFoundException, IOException {
		if (content.isLocal()) {
			FileContentLocal localContent = (FileContentLocal) content;
			Files.copy(localContent.getPath(), Paths.get(inputPath));
		} 
		else {
			FileContentRemote remoteContent = (FileContentRemote) content;
			byte[] bytes = remoteContent.getBytes();
			writeToFile(bytes, inputPath);
			
		}
		
	}
    
    public static void writeToFile(final byte[] fileContent, final String filePath) throws FileNotFoundException,
            IOException {

        // create folders if necessary
        final File f = new File(filePath);
        f.getParentFile().mkdirs();

        // write to file
        final FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(fileContent);
        fos.close();
    }

    public static String writeToTempFile(final FileContent fileContent, final String fileName, final String fileExtension)
            throws FileNotFoundException, IOException {
        final String tempFilePath = getTempFileName(fileName, fileExtension);
        writeToFile(fileContent, tempFilePath);
        return tempFilePath;
    }

    public static byte[] readFileToMem(final String filePath) throws FileNotFoundException, IOException {
        final File file = new File(filePath);
        final FileInputStream inputStream = new FileInputStream(file);
        final BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
        final byte[] bytes = new byte[inputStream.available()];
        bufferedInput.read(bytes);
        bufferedInput.close();
        inputStream.close();
        return bytes;
    }

    public static byte[] consumeFileToMem(final String filePath) throws FileNotFoundException, IOException {
        final byte[] bytes = readFileToMem(filePath);
        try {
            new File(filePath).delete();
        } catch (final Exception e) {
            System.out.println("Deletion failed");
            e.printStackTrace();
        }
        return bytes;
    }
    
    /**
     * Creates a temporary file based on its content and its full name
     * @param bytes Content of the file
     * @param fileName filename
     * @param extension file extension
     * @return path to the temporary file
     */
	public static Path bytesToPath(byte[] bytes, String fileName, String extension) {
		String filePath = FSUtil.getTempFileName(fileName, extension);
		try {
			FSUtil.writeToFile(bytes, filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Paths.get(filePath);
	}
	
	/**
	 * Converts a file to a byte array
	 * @param filePath file path
	 * @return byte array 
	 */
	public static byte[] pathToBytes(Path filePath) {
		byte[] bytes = null;
		try {
			bytes = Files.readAllBytes(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	/**
	 * Determines whether the downloaded file is a mp3 or zip file based on the number of requested audio
	 * files to be downloaded
	 * @param requestedAudioCount number of audio files to be downloaded
	 * @return string containing the extension of the resulting mp3 or zip file
	 */
	public static String getExtension(int requestedAudioCount) {
		if (requestedAudioCount == 1)
			return "mp3";
		else
			return "zip";		
	}



}
 