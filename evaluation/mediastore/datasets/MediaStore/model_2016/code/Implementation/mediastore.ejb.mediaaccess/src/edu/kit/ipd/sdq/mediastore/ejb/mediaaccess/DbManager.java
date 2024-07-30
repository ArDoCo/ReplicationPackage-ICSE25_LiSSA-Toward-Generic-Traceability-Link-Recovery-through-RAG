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

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFileInfo;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedDownloadException;

/**
 * @author Anastasia @date: 06.12.2014
 */
@Stateless
public class DbManager {

    @PersistenceContext(name = "Mediastore")
    private EntityManager em;

    public <T extends AudioFile> Long saveAudioFile(final T file) {
//        System.out.println("DbManager.saveAudioFile()");

        final Audio audio = new Audio();
        audio.setTitle(file.getTitle());
        audio.setArtist(file.getArtist());
        audio.setAlbum(file.getAlbum());
        audio.setBitrate(file.getBitrate());
        audio.setGenre(file.getGenre());
        audio.setReleaseyear(file.getReleaseyear());
        audio.setUserId(file.getUploader());

        this.em.persist(audio);
        this.em.flush();

        return audio.getId();
    }

    public List<AudioFileInfo> getAllAudios() {

        final List<AudioFileInfo> listInfos = new ArrayList<AudioFileInfo>();

        final List<Audio> audies = this.em.createNamedQuery("findAll", Audio.class).getResultList();
        for (final Audio audio : audies) {
            listInfos.add(new AudioFileInfo(audio.getId(), audio.getAlbum(), audio.getArtist(), audio.getBitrate(),
                    audio.getGenre(), audio.getReleaseyear(), audio.getTitle(), audio.getUserId()));
        }

        return listInfos;
    }

    public AudioFileInfo getAudioByID(final Long id) throws FailedDownloadException {

        final List<Audio> audies = this.em.createNamedQuery("findByID", Audio.class).setParameter("id", id)
                .getResultList();

        if (audies == null || audies.size() == 0) {
            throw new FailedDownloadException("Audio File wurde in DatenBank nicht gefunden.");
        }

        final Audio audio = audies.get(0);
        final AudioFileInfo audioInfos = new AudioFileInfo(audio.getId(), audio.getAlbum(), audio.getArtist(),
                audio.getBitrate(), audio.getGenre(), audio.getReleaseyear(), audio.getTitle(), audio.getUserId());

        return audioInfos;
    }

    public void clearTable() {
        this.em.createNamedQuery("clear", Audio.class).executeUpdate();
    }

    public void clearExceptFromUser(final long userId) {
        this.em.createNamedQuery("clearExceptFromUser").setParameter("userId", userId).executeUpdate();
    }

    public long getAudioCount() {
        final Long singleResult = this.em.createNamedQuery("getCount", Long.class).getSingleResult();
        final long count = singleResult;
        return count;
    }
}
