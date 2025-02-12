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
package edu.kit.ipd.sdq.mediastore.ejb.reencoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import org.apache.commons.lang3.SystemUtils;

import edu.kit.ipd.sdq.mediastore.basic.BaseEJB;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContent;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentLocal;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentRemote;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.ConversionException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedDownloadException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownload;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownloadReEncoderLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownloadReEncoderRemote;
import edu.kit.ipd.sdq.mediastore.basic.utils.FSUtil;
import edu.kit.ipd.sdq.mediastore.basic.utils.LameUtil;

/**
 * Session Bean implementation class Reencoding
 */

@Stateless
public class ReEncoderImpl extends BaseEJB implements IDownloadReEncoderRemote, IDownloadReEncoderLocal {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7949934777121714398L;
	private IDownload next;

    /**
     * Default constructor.
     */
    public ReEncoderImpl() {
    	super();
    	ejbName = "reencoder";
    }

    @PostConstruct
    public void init() {
    	this.next = initRequiredInterface("IDownload", IDownload.class);
    }

    /**
     * Encode an mp3 file to a given bitrate using lame encoder. Note that lame is used here as an
     * .exe file, i.e. the implementation works only under Windows.
     * 
     * @param audioFile
     *            The audioFile object which references the file to be encoded
     * @param bitrate
     *            Target bitrate
     * @param timeStamp
     * @throws MediaStoreException
     *             Thrown if lame cannot be initialized, if an error happens during encoding or if
     *             there is any IO exception
     */
    @Override
    public List<AudioFile> download(final List<Long> requestedAudioIDs, final List<Integer> bitrates,
            final String downloaderLogin, final boolean localCall) throws FailedDownloadException, NamingException {

        // get files from next EJB
        final List<AudioFile> audios = this.next.download(requestedAudioIDs, bitrates, downloaderLogin, isLocal("IDownload"));

        // copy lame encoder from the classpath to the working directory
        if (SystemUtils.IS_OS_WINDOWS) {
            try {
                LameUtil.initLame();
            } catch (final ConversionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        int i = 0;
        for (final AudioFile audio : audios) {
            final int bitrate = bitrates.get(i);

            // file path of the file which should be encoded
            String inputFilePath;
            FileContent content = audio.getContent();
            try {
                inputFilePath = FSUtil.writeToTempFile(content, downloaderLogin, "mp3");
            } catch (final IOException e) {
                // TODO propper exc handling
                throw new RuntimeException("Could not create temp file", e);
            }

            // file name (including directory name) of the encoded file
            final String outputFilePath = FSUtil.getTempFileName(downloaderLogin, "mp3");

            // reencode
            LameUtil.encode(inputFilePath, outputFilePath, bitrate);

            // delete temporary input file
            new File(inputFilePath).delete();

            if (!localCall) {
                // read output to memory and save in return set
                byte[] bytes;
                try {
                    bytes = FSUtil.consumeFileToMem(outputFilePath);
                } catch (final IOException e) {
                    // TODO propper exc handling
                    throw new RuntimeException("Could not read file to memory", e);
                }
                audio.setContent(new FileContentRemote(bytes));
            }
            else {
            	audio.setContent(new FileContentLocal(Paths.get(outputFilePath)));
            }

        }

        return audios;
    }
}