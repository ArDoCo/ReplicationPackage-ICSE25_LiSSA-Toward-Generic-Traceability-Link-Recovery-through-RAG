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
package edu.kit.ipd.sdq.mediastore.ejb.audiowatermarking;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import org.apache.commons.lang3.SystemUtils;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import edu.kit.ipd.sdq.mediastore.basic.BaseEJB;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFileInfo;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContent;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentLocal;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentRemote;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.ConversionException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedDownloadException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownload;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownloadAudioWatermarkingLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IDownloadAudioWatermarkingRemote;
import edu.kit.ipd.sdq.mediastore.basic.utils.FSUtil;
import edu.kit.ipd.sdq.mediastore.basic.utils.LameUtil;

/**
 * Session Bean implementation class Watermarking. This program uses third party free and opensource
 * APIs and software - API to manipulate wav files :
 * http://www.labbookpages.co.uk/audio/javaWavFiles.html - API to manipulate mp3 tags :
 * http://www.jthink.net/jaudiotagger/ - LAME to encode/decode audio files
 *
 * @author Amine Kechaou
 */

@Stateless
public class AudioWatermarkingImpl extends BaseEJB implements IDownloadAudioWatermarkingRemote, IDownloadAudioWatermarkingLocal {

    private IDownload next;

    private final static double secPerBit = 0.1; // number of seconds required for each bit
    private final static int hf = 10000; // high frequency in Hz
    private final static int lf = 1; // low frequency in Hz
    private final static double amplitude = 0.0001; // amplitude of the watermark signal. At this amplitude the signal is not audible

    /**
     * Default constructor.
     */
    public AudioWatermarkingImpl() {
    	super();
    	ejbName = "audiowatermarking";
    }

    @PostConstruct
    public void init() {
    	this.next = initRequiredInterface("IDownload", IDownload.class);
    }

    /**
     * This method watermarks all files for download and return them as a list of AudioFile with the
     * desired bitrate
     */
    @Override
    public List<AudioFile> download(final List<Long> requestedAudioIDs,
            final List<Integer> bitrates, final String downloaderLogin, final boolean localCall) throws FailedDownloadException, NamingException {

        if (SystemUtils.IS_OS_WINDOWS) {
            try {
                LameUtil.initLame();
            } catch (final ConversionException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        final List<AudioFile> result = new ArrayList<AudioFile>();
        final List<AudioFile> fileList = this.next.download(requestedAudioIDs,
                bitrates, downloaderLogin, isLocal("IDownload")); // fetch files from database
        final ListIterator<AudioFile> audioFileIterator = fileList.listIterator(); // AudioFile iterator
        final ListIterator<Integer> bitrateIterator = bitrates.listIterator(); // Integer iterator

        while (audioFileIterator.hasNext() && bitrateIterator.hasNext()) { // watermark each file then return the resulting list
            final AudioFile currentAudioFile = audioFileIterator.next();
            final AudioFileInfo info = currentAudioFile.getInfo(); // save file info for later use
            final Integer bitrate = bitrateIterator.next();
            String watermarkedFilePath = "";
            final String inputPath = FSUtil.getTempFileName(currentAudioFile.getId() + "[Input]", "mp3"); // create temporary mp3 file

            try {
                FSUtil.writeToFile(currentAudioFile.getContent(), inputPath); // write the content of the current file in the temp file
            } catch (final FileNotFoundException e) {
                System.out.println("Writing bytes to file failed : temp file not found");
                e.printStackTrace();
            } catch (final IOException e) {
                System.out.println("Writing bytes to file failed : IO Error");
                e.printStackTrace();
            }

            // This section saves the mp3 tags for later use using jaudiotagger api
            org.jaudiotagger.audio.AudioFile inputFile = new org.jaudiotagger.audio.AudioFile();
            try {
                inputFile = org.jaudiotagger.audio.AudioFileIO.read(new File(inputPath));
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException
                    | InvalidAudioFrameException e1) {
                e1.printStackTrace();
            }
            final org.jaudiotagger.tag.Tag tag = inputFile.getTag();

            // watermark current file, with the downloader login and convert the song to the desired bitrate
            try {
                watermarkedFilePath = this.watermark(inputPath, currentAudioFile, downloaderLogin, bitrate.intValue());
                Files.delete(Paths.get(inputPath)); //delete temporary input file
            } catch (final ConversionException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
				e.printStackTrace();
			}

            // This section writes the mp3 tags on the output mp3 file, due to their loss during the conversion to wav format
            org.jaudiotagger.audio.AudioFile outputFile = new org.jaudiotagger.audio.AudioFile();
            try {
                outputFile = org.jaudiotagger.audio.AudioFileIO.read(new File(watermarkedFilePath));
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException
                    | InvalidAudioFrameException e1) {
                e1.printStackTrace();
            }
            outputFile.setTag(tag);
            try {
                outputFile.commit();
            } catch (final CannotWriteException e1) {
                e1.printStackTrace();
            }

            
            info.setBitrate(bitrate); // change the info bitrate to the current bitrate
            
            FileContent content = null;


            final Path path = Paths.get(watermarkedFilePath);
            if (!localCall) {
                byte[] data = null;
                try {
                    data = Files.readAllBytes(path);
                } catch (final IOException e) {
                    System.out.println("Reading bytes from watermarked file " + currentAudioFile.getTitle() + " failed");
                    e.printStackTrace();
                }
                content = new FileContentRemote(data);
                
	            try {
					Files.delete(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            else {
            	content = new FileContentLocal(path);
            }
            
            
            final AudioFile watermarkedFile = new AudioFile(info, content); // create new AudioFile containing the output data and info
            result.add(watermarkedFile);
        }

        return result;
    }

    /**
     * This function watermarks a file and sets its bitrate
     *
     * @param inputPath
     *            path of the input mp3 file
     * @param file
     *            input AudioFile
     * @param downloaderLogin
     *            Downloader login : this string will be hidden inside the song
     * @param bitrate
     *            desired bitrate
     * @return path of the output mp3 file
     * @throws ConversionException
     *             if the conversion fails
     */
    private String watermark(final String inputPath, final AudioFile file,
            final String downloaderLogin, final int bitrate) throws ConversionException {

        final File inputWav = new File(FSUtil.getTempFileName(file.getId() + "input", "wav")); // create new temporary IO wav files
        final File outputWav = new File(FSUtil.getTempFileName(file.getId() + "output", "wav"));

        if (!LameUtil.decode(inputPath, inputWav.getAbsolutePath())) {
            throw new ConversionException("Failed to convert file " + inputPath + " to wav format");
        }
        // Reading section
        WavFile input = null;
        WavFile output = null;
        long sampleRate = 44100; // default sample rate
        long numFrames;
        try {
            input = WavFile.openWavFile(inputWav);
            sampleRate = input.getSampleRate(); // Samples per second
            numFrames = input.getNumFrames();
            output = WavFile.newWavFile(outputWav, 2, numFrames, 16, sampleRate); // create the output wav file with the same sampleRate and number of frames of the input file
        } catch (IOException | WavFileException e) {
            e.printStackTrace();
        }

        // Create a buffer of 100 frames with 2 channels
        final int bufferSize = 100;
        final double[][] buffer = new double[2][bufferSize];

        int framesRead = 0;
        long frameCounter = 0;

        final String bs = this.getBinaryString(downloaderLogin); // convert the downloaderLogin into a binary string of 1s and 0s
        do {
            // Read frames into buffer
            try {
                framesRead = input.readFrames(buffer, bufferSize);
            } catch (IOException | WavFileException e) {
                System.out.println("Reading frames from input wav file " + inputPath + " failed");
                e.printStackTrace();
            }

            final long remaining = output.getFramesRemaining();
            final int toWrite = (remaining > bufferSize) ? bufferSize : (int) remaining; // determines the number of frames to be written : bufferSize or less, if the remaining frames are less than bufferSize

            for (int s = 0; s < toWrite; s++, frameCounter++) {
                final double t = (double) frameCounter / sampleRate; // time position : frameCounter's unit is "frame", sampleRate is frame per second. Thus the quotient is expressed in seconds
                final double value = Math.sin(2.0 * Math.PI * this.getFrequency(t, bs) * t); // add the value of the sine wave to current time position with the calculated frequency
                buffer[0][s] = buffer[0][s] + amplitude * value;
                buffer[1][s] = buffer[1][s] + amplitude * value;
            }
            try {
                output.writeFrames(buffer, framesRead);
            } catch (IOException | WavFileException e) {
                System.out.println("Writing frames to output wav file " + inputPath + " failed");
                e.printStackTrace();
            }
        } while (framesRead != 0); // until all frames are read

        try {
            input.close();
            output.close();
        } catch (final IOException e) {
            System.out.println("Closing input/output wav files failed");
            e.printStackTrace();
        }

        final String outputPath = FSUtil.getTempFileName(file.getId() + "[Output]", "mp3"); // create temporary output mp3 file

        if (!LameUtil.encode(outputWav.getAbsolutePath(), outputPath, bitrate)) { // wav file to mp3 and set its bitrate
            throw new ConversionException("Failed to convert file " + inputPath + " from wav back to mp3 format");
        }

        // delete IO wav files
        System.out.println("Deleting temporary input wav file " + inputWav.getAbsolutePath() + " returned : "
                + inputWav.delete());
        System.out.println("Deleting temporary output wav file " + inputWav.getAbsolutePath() + " returned : "
                + outputWav.delete());

        return outputPath; // return the output mp3 file path
    }

    /**
     * Converts a string to binary form
     * @param s string to be converted
     * @return binary representation of the string
     */
    private String getBinaryString(final String s) {
        String bs = "";
        for (int i = 0; i < s.length(); ++i) {
            final int c = s.charAt(i);
            String bin = Integer.toBinaryString(c);
            while (bin.length() < 8) { // add padding
                bin = "0" + bin;
            }
            bs = bs + bin;
        }
        return bs;
    }

    /**
     * Calculates the frequency corresponding to a binary String
     * @param t time position
     * @param bs binary string
     * @return frequency of the given bit
     * 
     */
    private int getFrequency(final double t, final String bs) {
        final int index = ((int) (t / secPerBit)) % bs.length(); // calculate current index
        final int f = (bs.charAt(index) == '0') ? lf : hf; // if bit = 0, f = low frequency, otherwise f = high frequency
        return f;
    }
}
