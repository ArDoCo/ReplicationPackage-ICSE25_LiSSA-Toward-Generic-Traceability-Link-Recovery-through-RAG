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
package edu.kit.ipd.sdq.mediastore.ejb.tagwatermarking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import edu.kit.ipd.sdq.mediastore.basic.BaseEJB;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentLocal;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentRemote;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedDownloadException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownload;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownloadTagWatermarkingLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownloadTagWatermarkingRemote;
import edu.kit.ipd.sdq.mediastore.basic.utils.FSUtil;

/**
 * Session Bean implementation class TagwatermarkingImpl
 */
@Stateless
public class TagWatermarkingImpl extends BaseEJB implements IDownloadTagWatermarkingRemote, IDownloadTagWatermarkingLocal {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7711132340993624916L;
	private IDownload next;

    /**
     * Default constructor.
     */
    public TagWatermarkingImpl() {
    	super();
    	ejbName = "tagwatermarking";
    }

    @PostConstruct
    public void init() {
        this.next = initRequiredInterface("IDownload", IDownload.class);
    }

    @Override
    public List<AudioFile> download(final List<Long> requestedAudioIDs, final List<Integer> bitrates,
            final String downloaderLogin, final boolean localCall) throws FailedDownloadException, NamingException {

        final List<AudioFile> fileList = this.next.download(requestedAudioIDs, bitrates, downloaderLogin, isLocal("IDownload"));

        for (AudioFile audioFile : fileList) {
        	try {
				this.watermark(audioFile, downloaderLogin, localCall);
			} catch (IOException e) {
				throw new FailedDownloadException(audioFile.getFilename());
			} 
        }

        return fileList;
    }

    private void watermark(AudioFile audioFile, String downloaderLogin, boolean localCall) throws FileNotFoundException, IOException {
        final String tempFileName = FSUtil.writeToTempFile(audioFile.getContent(), downloaderLogin + audioFile.getFilename(), "mp3");
        
            MP3File mp3file;
			try {
				mp3file = (MP3File) AudioFileIO.read(new File(tempFileName));
	            final Tag tag = mp3file.getTagOrCreateAndSetDefault();
	            tag.setField(FieldKey.COMMENT, downloaderLogin + " has downloaded this file from Mediastore");
	            mp3file.commit();
	            if (!localCall) {
	                final byte[] bytes = FSUtil.consumeFileToMem(tempFileName);
	                audioFile.setContent(new FileContentRemote(bytes));
	            }
	            else {
	            	audioFile.setContent(new FileContentLocal(Paths.get(tempFileName)));
	            }
			} catch (CannotReadException | IOException | TagException
					| ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException e) {
				e.printStackTrace();
			}

    }

}
