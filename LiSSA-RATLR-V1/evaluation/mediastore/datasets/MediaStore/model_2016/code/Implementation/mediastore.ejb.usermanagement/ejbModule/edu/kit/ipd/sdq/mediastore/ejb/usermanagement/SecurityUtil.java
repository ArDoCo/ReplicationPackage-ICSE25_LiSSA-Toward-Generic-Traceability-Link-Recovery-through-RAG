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
package edu.kit.ipd.sdq.mediastore.ejb.usermanagement;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * @author Anastasia @date: 07.12.2014
 */
public class SecurityUtil {

    private static final String HASH_ALGORITHMUS = "SHA-512";
    private static final String CHARSET = "UTF-8";

    /*
     * Algorithm: 
     * 1. Generate Secure random: Salt 
     * 2. Hashing password with Salt (salted hash): SHA-512 
     * 3. Salt added to hash(the last 10 Bytes) 
     * 4. Password will be saved in DB as: Salted hash + Salt
     */
    public static String computeHash(final String string) {

        byte[] bytesOfMessage;
        byte[] hash;
        final byte[] salt = getSalt(10);

        MessageDigest md;

        try {
            bytesOfMessage = string.getBytes(CHARSET);
            md = MessageDigest.getInstance(HASH_ALGORITHMUS);
            md.reset();
            md.update(salt);
            md.update(bytesOfMessage);
            hash = md.digest();
            final byte[] t = concatenate(hash, salt);
            return base64FromBytes(t);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String computeHash(final String string, final byte[] salt) {
        byte[] bytesOfMessage;
        byte[] hash;
        try {
            bytesOfMessage = string.getBytes(CHARSET);
            MessageDigest md;
            md = MessageDigest.getInstance(HASH_ALGORITHMUS);
            md.reset();
            md.update(salt);
            md.update(bytesOfMessage);
            hash = md.digest();
            final byte[] t = concatenate(hash, salt);
            return base64FromBytes(t);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getSalt(final int length) {
        final SecureRandom random = new SecureRandom();
        final byte bytes[] = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    /*
     * Compare password from login form with password from DB
     */
    public static Boolean isEqual(final String loginPassword, final String dbPassword) {
        try {
            final byte[] dbPass = bytesFrombase64(dbPassword);
            final byte[] salt = Arrays.copyOfRange(dbPass, dbPass.length - 10, dbPass.length);
            final String tempPass = computeHash(loginPassword, salt);
            
//            System.err.println(String.format("SecurityUtil.isEqual: plain:%s, hashed:%s, db:%s", loginPassword, tempPass, dbPassword));
            if (dbPassword.equals(tempPass)) {
                return true;
            }

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String base64FromBytes(final byte[] text) {
        // final BASE64Encoder encoder = new BASE64Encoder();
        // return encoder.encode(text);
        Encoder encoder = Base64.getMimeEncoder();
        String hash = new String(encoder.encode(text), StandardCharsets.UTF_8);
        // https://stackoverflow.com/questions/35301409/is-java-8-java-util-base64-a-drop-in-replacement-for-sun-misc-base64
        if (hash.length() == 76) {
            return hash + "\n";
        }
        return hash;
    }

    private static byte[] bytesFrombase64(final String text) throws IOException {
        // byte[] textBytes = null;
        // final BASE64Decoder decoder = new BASE64Decoder();
        // textBytes = decoder.decodeBuffer(text);
        Decoder decoder = Base64.getMimeDecoder();
        byte[] textBytes = decoder.decode(text);
        return textBytes;
    }

    private static byte[] concatenate(final byte[] a, final byte[] b) {
        final int lengthA = a.length;
        final int lengthB = b.length;
        final byte[] concat = new byte[lengthA + lengthB];
        System.arraycopy(a, 0, concat, 0, lengthA);
        System.arraycopy(b, 0, concat, lengthA, lengthB);
        return concat;
    }
}
