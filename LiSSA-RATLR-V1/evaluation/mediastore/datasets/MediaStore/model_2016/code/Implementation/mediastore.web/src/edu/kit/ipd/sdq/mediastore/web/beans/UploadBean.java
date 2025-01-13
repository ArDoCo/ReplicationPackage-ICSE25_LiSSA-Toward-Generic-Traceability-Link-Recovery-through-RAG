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
package edu.kit.ipd.sdq.mediastore.web.beans;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.naming.NamingException;

import org.primefaces.model.UploadedFile;

import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFileInfo;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentLocal;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentRemote;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedUploadException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IFacade;
import edu.kit.ipd.sdq.mediastore.basic.utils.FSUtil;
import edu.kit.ipd.sdq.mediastore.web.utils.MessageUtil;

@ManagedBean(name = "uploadBean")
@ViewScoped
public class UploadBean implements Serializable {

    private static final long serialVersionUID = 14224877237481385L;

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    @ManagedProperty(value = "#{downloadBean}")
    private DownloadBean downloadBean;

    private String album;
    private String artist;
    private int bitrate;
    private String genre;
    private int releaseyear;
    private String title;
    private Long uploader;
    

    private IFacade facade;

    private UploadedFile file;

    private void initFacade() {
    	facade = WebBeanManager.initRequiredInterface("IFacade", IFacade.class);
    }
    
    public UploadedFile getFile() {
        return this.file;
    }

    public void setFile(final UploadedFile file) {
        this.file = file;
    }

    public void upload() {
        final Long start = System.currentTimeMillis();
        final byte[] contents = this.file.getContents();

        this.setUploader(this.sessionBean.getCurrentUser().getId());
        final AudioFileInfo audioFileInfo = new AudioFileInfo(null, this.getAlbum(), this.getArtist(),
                this.getBitrate(), this.getGenre(), this.getReleaseyear(), this.getTitle(), this.getUploader());

        Long id = null;

        try {
        	initFacade();
        	AudioFile audioFile = new AudioFile();
        	Path filePath = null;
        	if (!WebBeanManager.isLocal("IFacade")) {
                audioFile.setContent(new FileContentRemote(contents));
        	} else {
        		String fileName = this.sessionBean.getCurrentUser().getEmail() + "Upload-Facade";
        		filePath = FSUtil.bytesToPath(contents, fileName, "mp3");
        		audioFile.setContent(new FileContentLocal(filePath));
        	}
        	
            audioFile.setInfo(audioFileInfo);
            id = facade.upload(audioFile);
            if (filePath != null) {
                try {
    				Files.deleteIfExists(filePath);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
            }
            System.out.println("Time for upload(in ms):" + (System.currentTimeMillis() - start));

            audioFileInfo.setId(id);
            MessageUtil.showSuccess("User: " + this.sessionBean.getCurrentUser().getFirstname() + " "
                    + this.sessionBean.getCurrentUser().getLastname() + " hat " + this.toString() + " uploaded.");
            this.downloadBean.addAudioToList(audioFileInfo);

        } catch (final NamingException e) {
            MessageUtil.showError("NamingException");
            e.printStackTrace();
        } catch (final FailedUploadException e) {
            MessageUtil.showError("FailedUploadException: " + e.getMessage());
            e.printStackTrace();
        } catch (final RemoteException e) {
            MessageUtil.showError("RemoteException");
            e.printStackTrace();
        }

        System.out.println("Upload dauerte: " + (System.currentTimeMillis() - start) + " ms");
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

    public int getBitrate() {
        return this.bitrate;
    }

    public void setBitrate(final int bitrate) {
        this.bitrate = bitrate;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public int getReleaseyear() {
        return this.releaseyear;
    }

    public void setReleaseyear(final int releaseyear) {
        this.releaseyear = releaseyear;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Long getUploader() {
        return this.uploader;
    }

    public void setUploader(final Long uploader) {
        this.uploader = uploader;
    }

    public SessionBean getSessionBean() {
        return this.sessionBean;
    }

    public void setSessionBean(final SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public DownloadBean getDownloadBean() {
        return this.downloadBean;
    }

    public void setDownloadBean(final DownloadBean downloadBean) {
        this.downloadBean = downloadBean;
    }

    @Override
    public String toString() {
        return "Audio  " + this.file.getFileName() + "  mit dem Title=" + this.title;
    }
}
