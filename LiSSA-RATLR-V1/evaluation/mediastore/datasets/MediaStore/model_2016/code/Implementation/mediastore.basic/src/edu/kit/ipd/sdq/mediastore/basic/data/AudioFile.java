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
 * Class represents an audio file including an input stream to access the data and a @see
 * {@link AudioFileInfo} object. The fields of the info object can be accessed directly through
 * getters and setters in this class.
 *
 * @author Emil Rakadjiev
 * @author Amine Kechaou
 */
public class AudioFile implements Serializable {

	private static final long serialVersionUID = 492084138637784340L;
	private AudioFileInfo info;
    private FileContent content;
    
    public AudioFile() {
        this.info = new AudioFileInfo();
    }

    public AudioFile(final AudioFileInfo info, final FileContent content) {
        super();
        this.info = info;
        this.content = content;
    }

    /**
     * @param info
     *            the audio file infos to set
     */
    public void setInfo(final AudioFileInfo info) {
        this.info = info;
    }

    /**
     * @return the audio file infos
     */
    public AudioFileInfo getInfo() {
        return this.info;
    }

    /**
     * @return the id in the DB
     */
    public Long getId() {
        return this.info.getId();
    }

    /**
     * @param id
     *            the id in the DB to set
     */
    public void setId(final Long id) {
        this.info.setId(id);
    }

    /**
     * @return the album
     */
    public String getAlbum() {
        return this.info.getAlbum();
    }

    /**
     * @param album
     *            the album to set
     */
    public void setAlbum(final String album) {
        this.info.setAlbum(album);
    }

    /**
     * @return the artist
     */
    public String getArtist() {
        return this.info.getArtist();
    }

    /**
     * @param artist
     *            the artist to set
     */
    public void setArtist(final String artist) {
        this.info.setArtist(artist);
    }

    /**
     * @return the bitrate
     */
    public Integer getBitrate() {
        return this.info.getBitrate();
    }

    /**
     * @param bitrate
     *            the bitrate to set
     */
    public void setBitrate(final Integer bitrate) {
        this.info.setBitrate(bitrate);
    }

    public String getFilename() {
        return this.info.getFilename();
    }

    /**
     * @return the genre
     */
    public String getGenre() {
        return this.info.getGenre();
    }

    /**
     * @param genre
     *            the genre to set
     */
    public void setGenre(final String genre) {
        this.info.setGenre(genre);
    }

    /**
     * @return the releaseyear
     */
    public Integer getReleaseyear() {
        return this.info.getReleaseyear();
    }

    /**
     * @param releaseyear
     *            the releaseyear to set
     */
    public void setReleaseyear(final Integer releaseyear) {
        this.info.setReleaseyear(releaseyear);
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.info.getTitle();
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(final String title) {
        this.info.setTitle(title);
    }

    /**
     * @return the uploader
     */
    public Long getUploader() {
        return this.info.getUploader();
    }

    /**
     * @param uploader
     *            the uploader to set
     */
    public void setUploader(final Long uploader) {
        this.info.setUploader(uploader);
    }
    
    public void setContent(FileContent content) {
    	this.content = content;
    }
    
    public FileContent getContent() {
    	return content;
    }
}
