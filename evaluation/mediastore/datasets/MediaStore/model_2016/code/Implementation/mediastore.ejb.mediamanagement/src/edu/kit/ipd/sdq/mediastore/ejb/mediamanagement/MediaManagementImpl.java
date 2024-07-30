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
package edu.kit.ipd.sdq.mediastore.ejb.mediamanagement;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.NamingException;

import edu.kit.ipd.sdq.mediastore.basic.BaseEJB;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFileInfo;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContent;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedDownloadException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedUploadException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownload;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IMediaAccess;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IMediaManagementLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IMediaManagementRemote;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IPackaging;
import edu.kit.ipd.sdq.mediastore.basic.utils.FSUtil;

/**
 * @author Anastasia
 */
@Stateless
public class MediaManagementImpl extends BaseEJB implements IMediaManagementRemote, IMediaManagementLocal {

    private static final long serialVersionUID = -8294016625795719929L;

    IDownload nextInDowloadChain;
    IMediaAccess mediaAccess;
    IPackaging packaging;

	public MediaManagementImpl() {
		super();
		ejbName = "mediamanagement";
	}

    private void initMediaAccess() {
    	this.mediaAccess = initRequiredInterface("IMediaAccess", IMediaAccess.class);
    }

    private void initNext() {
        this.nextInDowloadChain = initRequiredInterface("IDownload", IDownload.class);
    }

    
    private void initPackaging() {
    	this.packaging = initRequiredInterface("IPackaging", IPackaging.class);
    }

    @Override
    public Long upload(AudioFile file)  throws FailedUploadException, NamingException, RemoteException {
        this.initMediaAccess();
        FileContent content = file.getContent();
        String fileName = file.getUploader() + file.getFilename() + "MM-MA";
        file.setContent(content.convertIfNeeded(isLocal("IMediaAccess"), fileName, "mp3"));
        return this.mediaAccess.upload(file);
    }

    @Override
    public List<AudioFileInfo> getFileList() throws NamingException {
        this.initMediaAccess();
        return this.mediaAccess.getFileList();
    }

    @Override
    public FileContent download(final List<Long> requestedAudioIDs, final List<Integer> bitrates,
            final String downloaderLogin, final boolean localCall) throws FailedDownloadException, NamingException {

        if (requestedAudioIDs.size() != bitrates.size()) {
            throw new RuntimeException("Number of requested audios (" + requestedAudioIDs.size()
                    + ") does not equal number of bitrates (" + bitrates.size() + ')');
        }

//        System.out.println("MediaManagerImpl.download()" + requestedAudioIDs);

        this.initNext();
        final List<AudioFile> audioFiles = this.nextInDowloadChain.download(requestedAudioIDs, bitrates,
                downloaderLogin, isLocal("IDownload"));
//        System.out.println("MediaManagerImpl. AudioFile count:" + audioFiles.size());

        FileContent content;
        int size = audioFiles.size();
        if (size == 1) {
            content = audioFiles.get(0).getContent();
        } else {
            this.initPackaging();
            boolean localPackaging = isLocal("IPackaging");
            for (AudioFile audioFile : audioFiles) {
                String fileName = downloaderLogin + audioFile.getFilename() + "MM-Packaging";
                FileContent audioFileContent = audioFile.getContent();
                audioFile.setContent(audioFileContent.convertIfNeeded(localPackaging, fileName, "mp3"));
            }
            content = this.packaging.zip(audioFiles, localPackaging);
        }
        
        String fileName = downloaderLogin + "MM-Facade";
        content = content.convertIfNeeded(localCall, fileName, FSUtil.getExtension(size));
        
        return content;
    }


}
