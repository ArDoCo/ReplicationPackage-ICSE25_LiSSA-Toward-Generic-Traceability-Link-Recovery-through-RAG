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
package edu.kit.ipd.sdq.mediastore.ejb.packaging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.Stateless;

import edu.kit.ipd.sdq.mediastore.basic.BaseEJB;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContent;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentLocal;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentRemote;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IPackagingLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IPackagingRemote;
import edu.kit.ipd.sdq.mediastore.basic.utils.FSUtil;

/**
 * Session Bean implementation class Packaging
 */

@Stateless
public class PackagingImpl extends BaseEJB implements IPackagingRemote, IPackagingLocal {

    /**
     *
     */
    private static final long serialVersionUID = 7507720547495222404L;

    static final int BUFFER = 2048;

    /**
     * Default constructor.
     */
    public PackagingImpl() {
    	super();
    	ejbName = "packaging";
    }

    private static void addFileToZip(final ZipOutputStream zos, final String szName, final FileContent content) throws Exception {
        ZipEntry entry;
        entry = new ZipEntry(szName);
        zos.putNextEntry(entry);

        // copy byte array (file content) into input stream
        InputStream is = null;
        if (content.isLocal()) {
        	FileContentLocal localContent = (FileContentLocal) content;
        	is = Files.newInputStream(localContent.getPath());
        }
        else {
        	FileContentRemote remoteContent = (FileContentRemote) content;
        	is = new ByteArrayInputStream(remoteContent.getBytes());
        }

        final byte[] buf = new byte[8000];
        int nLength;
        while (true) {
            nLength = is.read(buf);
            if (nLength < 0) {
                break;
            }
            zos.write(buf, 0, nLength);
        }

        is.close();
        zos.closeEntry();
    }

    @Override
    public FileContent zip(final List<AudioFile> fileList, final boolean localCall) {

    	
        OutputStream outputStream = null;
        Path filePath = null;
        if (!localCall) { //if the files are not locally located (i.e. are remote) 
        	outputStream = new ByteArrayOutputStream();
        } 
        else {
        	filePath = Paths.get(FSUtil.getTempFileName("Packaging", "zip"));
        	try {
				outputStream = Files.newOutputStream(filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        try {

            // creates a ZipOutputStream object, to which we pass the output stream we wish to write to
            final ZipOutputStream zipOut = new ZipOutputStream(outputStream);

            for (final ListIterator<AudioFile> i = fileList.listIterator(); i.hasNext();) {

                // get next AudioFile and moving forward in List
                final AudioFile audioFileToAdd = i.next();

                addFileToZip(zipOut, audioFileToAdd.getFilename(), audioFileToAdd.getContent());
            }

            zipOut.close();

        } catch (final Exception e) { 
            e.printStackTrace();
        }

        if (!localCall) {
        	return new FileContentRemote(((ByteArrayOutputStream) outputStream).toByteArray());
        }
        else {
        	return new FileContentLocal(filePath);
        }
        	
    }
}
