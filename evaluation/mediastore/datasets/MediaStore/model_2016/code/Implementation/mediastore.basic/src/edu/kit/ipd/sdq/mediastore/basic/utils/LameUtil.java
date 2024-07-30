package edu.kit.ipd.sdq.mediastore.basic.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import edu.kit.ipd.sdq.mediastore.basic.exceptions.ConversionException;

public class LameUtil {

    /**
     * Copy lame.exe from the classpath to the working directory, if not already done
     *
     * @throws ConversionException
     *             if lame.exe cannot be accessed or copied
     */
    public static void initLame() throws ConversionException {
        System.out.println("Initializing LAME");
        final File lame = new File("lame.exe");
        if (!lame.exists() || lame.length() == 0) {
            // get the input stream to lame.exe (in the classpath directory)
            final InputStream is = new BufferedInputStream(LameUtil.class.getClassLoader().getResourceAsStream(
                    "lame.exe"));
            if (is != null) {
                // create a file and a file output stream into which the data from the input stream
// should be written
                FileOutputStream fos = null;
                try {
                    // create lame.exe in the working directory
                    lame.createNewFile();
                    fos = new FileOutputStream(lame);
                    // write the contents of the input stream to the file (output stream)
                    int read = 0;
                    final byte[] bytes = new byte[8192];

                    while ((read = is.read(bytes)) != -1) {
                        fos.write(bytes, 0, read);
                    }
                    fos.flush();
                } catch (final IOException e) {
                    e.printStackTrace();
                    throw new ConversionException("Failed to initialize lame");
                } finally {
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(fos);
                }
            }
        }
    }

    /**
     * encodes a file with a bitrate using LAME
     *
     * @param input
     *            input file path
     * @param output
     *            output file path
     * @param bitrate
     *            bitrate of destination file
     * @return true if conversion succeeded
     */
    public static boolean encode(final String input, final String output, final int bitrate) {
        System.out.println("Encoding " + new File(input).getAbsolutePath() + " to "
                + new File(output).getAbsolutePath());
        final String options = "-h --cbr -b " + bitrate + " \"" + input + "\" \"" + output + "\"";
        execLame(options);
        return new File(output).exists() && new File(output).length() > 0;
    }

    /**
     * Decodes a file using LAME
     *
     * @param input
     *            input file path
     * @param output
     *            output file path
     * @return true if decoding succeeds
     */
    public static boolean decode(final String input, final String output) {
        System.out.println("Decoding " + new File(input).getAbsolutePath() + " to "
                + new File(output).getAbsolutePath());
        final String options = "--decode \"" + input + "\" \"" + output + "\"";
        execLame(options);
        return new File(output).exists() && new File(output).length() > 0;
    }

    /**
     * Executes lame
     *
     * @param options
     *            to be passed to lame
     */
    public static void execLame(final String options) {
        Process p;
        try {
            // Launch lame encoder
            String lame = "lame";
            if (SystemUtils.IS_OS_WINDOWS) {
                lame += ".exe";
            }
            p = Runtime.getRuntime().exec(lame + " " + options);
            final InputStreamReader pIsrErr = new InputStreamReader(p.getErrorStream());
            final InputStreamReader pIsrIn = new InputStreamReader(p.getInputStream());
            while (pIsrErr.read() != -1) {
            }
            while (pIsrIn.read() != -1) {
            }
            p.waitFor();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
