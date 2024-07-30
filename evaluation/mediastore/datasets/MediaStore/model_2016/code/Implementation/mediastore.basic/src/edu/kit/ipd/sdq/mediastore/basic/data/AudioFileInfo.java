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
package edu.kit.ipd.sdq.mediastore.basic.data;

import java.io.Serializable;

/**
 * Class containing the information for a given song. It is used to transfer information between the
 * client and the DB. It is almost identical to the Audio entity. The entity was not used, in order
 * to reduce dependencies on it (entites are used only in the DB classes).
 *
 * @author Emil Rakadjiev
 */
public class AudioFileInfo implements Serializable {

    private static final long serialVersionUID = -6170216542852727972L;

    private Long id;
    private String album;
    private String artist;
    private Integer bitrate;
    private String genre;
    private Integer releaseyear;
    private String title;
    private Long uploader;

    public AudioFileInfo() {
    }

    public AudioFileInfo(final Long id, final String album, final String artist, final Integer bitrate,
            final String genre, final Integer releaseyear, final String title, final Long uploader) {
        super();
        this.id = id;
        this.album = album;
        this.artist = artist;
        this.bitrate = bitrate;
        this.genre = genre;
        this.releaseyear = releaseyear;
        this.title = title;
        this.uploader = uploader;
    }

    /**
     * @return the id in the DB
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id
     *            the id in the DB to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the album
     */
    public String getAlbum() {
        return this.album;
    }

    /**
     * @param album
     *            the album to set
     */
    public void setAlbum(final String album) {
        this.album = album;
    }

    /**
     * @return the artist
     */
    public String getArtist() {
        return this.artist;
    }

    /**
     * @param artist
     *            the artist to set
     */
    public void setArtist(final String artist) {
        this.artist = artist;
    }

    /**
     * @return the bitrate
     */
    public Integer getBitrate() {
        return this.bitrate;
    }

    /**
     * @param bitrate
     *            the bitrate to set
     */
    public void setBitrate(final Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getFilename() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.artist);
        sb.append(" - ");
        sb.append(this.title);
        sb.append(".mp3");
        final String filename = sb.toString().replace(" ", "_");
        return filename;
    }

    /**
     * @return the genre
     */
    public String getGenre() {
        return this.genre;
    }

    /**
     * @param genre
     *            the genre to set
     */
    public void setGenre(final String genre) {
        this.genre = genre;
    }

    /**
     * @return the releaseyear
     */
    public Integer getReleaseyear() {
        return this.releaseyear;
    }

    /**
     * @param releaseyear
     *            the releaseyear to set
     */
    public void setReleaseyear(final Integer releaseyear) {
        this.releaseyear = releaseyear;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @return the uploader
     */
    public Long getUploader() {
        return this.uploader;
    }

    /**
     * @param uploader
     *            the uploader to set
     */
    public void setUploader(final Long uploader) {
        this.uploader = uploader;
    }

    @Override
    public String toString() {
        return "AudioFileInfo [album=" + this.album + ", artist=" + this.artist + ", bitrate=" + this.bitrate
                + ", genre=" + this.genre + ", releaseyear=" + this.releaseyear + ", title=" + this.title
                + ", uploader=" + this.uploader + "]";
    }

}
