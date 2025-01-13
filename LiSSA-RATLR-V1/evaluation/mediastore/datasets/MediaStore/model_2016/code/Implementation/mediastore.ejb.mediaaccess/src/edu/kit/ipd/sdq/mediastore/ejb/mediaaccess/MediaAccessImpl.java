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
package edu.kit.ipd.sdq.mediastore.ejb.mediaaccess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;

import edu.kit.ipd.sdq.mediastore.basic.BaseEJB;
import edu.kit.ipd.sdq.mediastore.basic.config.GlobalConstantsContainer;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFileInfo;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContent;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentLocal;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentRemote;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedDownloadException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedUploadException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownloadMediaAccessLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownloadMediaAccessRemote;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IMediaAccessLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IMediaAccessMaintenanceLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IMediaAccessMaintenanceRemote;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IMediaAccessRemote;
import edu.kit.ipd.sdq.mediastore.basic.utils.FSUtil;

/**
 *
 * @author Anastasia
 *
 */
/*
 * Die Audios werden in Ordner unter dem Name FILE_MAIN_DIR/Artist/Album des Files/Filename
 */

@Stateless
public class MediaAccessImpl extends BaseEJB implements IMediaAccessRemote, IMediaAccessLocal, IDownloadMediaAccessRemote, IDownloadMediaAccessLocal, IMediaAccessMaintenanceRemote, IMediaAccessMaintenanceLocal {

	private static final long serialVersionUID = -7677084793452622019L;
	@EJB
    DbManager dbManager;

	public MediaAccessImpl() {
		super();
		ejbName = "mediaaccess";
	}
    
    @Override
    public Long upload(final AudioFile file)  throws FailedUploadException, RemoteException {

        // Create Ordner, falls sie nicht vorhanden sind
        final StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(GlobalConstantsContainer.getFileDir()).append(file.getArtist()).append("\\")
                .append(file.getAlbum());

        final File f = new File(pathBuilder.toString());
        f.mkdirs();

        pathBuilder.append("\\").append(file.getFilename());

        final String filePath = pathBuilder.toString();
        
        FileContent content = file.getContent();
        try {
            FSUtil.writeToFile(content, filePath);
            if (content.isLocal()) {
            	Files.deleteIfExists(((FileContentLocal) content).getPath());
            }
            return this.dbManager.saveAudioFile(file);
        } catch (final IOException e) {
            throw new FailedUploadException(file.getFilename(), e);
        }
    }

    @Override
    public List<AudioFileInfo> getFileList() {

        return this.dbManager.getAllAudios();
    }

    @Override
    public List<AudioFile> download(final List<Long> requestedAudioIDs, final List<Integer> bitrates,
            final String downloaderLogin, final boolean localCall) throws FailedDownloadException, NamingException {

        final String root = GlobalConstantsContainer.getFileDir();
        final List<AudioFile> audioFiles = new ArrayList<AudioFile>();

        for (final Long id : requestedAudioIDs) {
            final AudioFileInfo audioFileInfo = this.dbManager.getAudioByID(id);

            final StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append(root)
            .append(audioFileInfo.getArtist())
            .append("\\")
            .append(audioFileInfo.getAlbum())
            .append("\\")
            .append(audioFileInfo.getFilename());

            final String filePath = pathBuilder.toString();

            audioFiles.add(new AudioFile(audioFileInfo, this.getFileContent(filePath, localCall)));
        }

        return audioFiles;
    }


    private FileContent getFileContent(final String filePath, boolean localCall) throws FailedDownloadException {
        if (localCall) {
        	Path source = Paths.get(filePath);
        	Path target = Paths.get(FSUtil.getTempFileName(source.getFileName().toString(), "mp3"));
        	try {
				Files.copy(source, target);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	return new FileContentLocal(target);
        }
        else {
            try {
                return new FileContentRemote(FSUtil.readFileToMem(filePath));
            } catch (final IOException e) {
                throw new FailedDownloadException(filePath);
            }
        }
    }

    @Override
    public void removeAllData() {
        this.dbManager.clearTable();

        final File audioFileDir = new File(GlobalConstantsContainer.getFileDir());
        try {
            FileUtils.cleanDirectory(audioFileDir);
        } catch (final IOException e) {
            System.out.println("An error occured while removing audio files");
            e.printStackTrace();
        }
    }

    @Override
    public boolean trimToPayload(final long payloadUserId, final int payloadSize) {

        final List<AudioFileInfo> fileList = this.getFileList();
        for (final AudioFileInfo audio : fileList) {
            if (audio.getUploader() != payloadUserId) {
                final StringBuilder pathBuilder = new StringBuilder();
                pathBuilder.append(GlobalConstantsContainer.getFileDir()).append(audio.getArtist()).append("\\")
                        .append(audio.getAlbum()).append("\\").append(audio.getFilename());
                new File(pathBuilder.toString()).delete();
            }
        }

        this.dbManager.clearExceptFromUser(payloadUserId);
        final long audioCount = this.dbManager.getAudioCount();
        if (audioCount == payloadSize) {
            return true;
        } else {
            return false;
        }
    }
}
