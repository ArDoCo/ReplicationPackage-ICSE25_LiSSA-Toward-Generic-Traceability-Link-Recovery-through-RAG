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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

/**
 * The persistent class for the AUDIO database table.
 */
@Entity
@TableGenerator(name = "audio")
@NamedQueries({ @NamedQuery(name = "clearExceptFromUser", query = "DELETE FROM Audio a WHERE a.userId != :userId"),
        @NamedQuery(name = "clear", query = "DELETE FROM Audio"),
        @NamedQuery(name = "findAll", query = "SELECT e FROM Audio e"),
        @NamedQuery(name = "findByTitle", query = "SELECT e FROM Audio e WHERE e.title = :title"),
        @NamedQuery(name = "findByID", query = "SELECT e FROM Audio e WHERE e.id = :id"),
        @NamedQuery(name = "getCount", query = "SELECT count(a) FROM Audio a") })
public class Audio implements Serializable {

    private static final long serialVersionUID = -1655097640656971957L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String album;

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false)
    private Integer bitrate;

    private String genre;

    private Integer releaseyear;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long userId;

    public Audio() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(final String album) {
        this.album = album;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(final String artist) {
        this.artist = artist;
    }

    public Integer getBitrate() {
        return this.bitrate;
    }

    public void setBitrate(final Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public Integer getReleaseyear() {
        return this.releaseyear;
    }

    public void setReleaseyear(final Integer releaseyear) {
        this.releaseyear = releaseyear;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}