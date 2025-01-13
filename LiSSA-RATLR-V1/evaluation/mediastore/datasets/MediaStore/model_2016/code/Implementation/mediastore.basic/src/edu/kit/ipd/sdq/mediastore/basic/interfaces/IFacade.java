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
package edu.kit.ipd.sdq.mediastore.basic.interfaces;

import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;

import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFileInfo;
import edu.kit.ipd.sdq.mediastore.basic.data.CurrentUser;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContent;
import edu.kit.ipd.sdq.mediastore.basic.data.UserRegData;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.BadLoginDataException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedDownloadException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedUploadException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.UserAlreadyExistsException;

public interface IFacade extends IBusinessInterface {

    public Long register(UserRegData user) throws UserAlreadyExistsException, NamingException;

    public CurrentUser login(String username, String password) throws BadLoginDataException, NamingException;

    public List<AudioFileInfo> getFileList() throws NamingException;

    void downloadTest(List<Long> id, List<Integer> bitrates, String downloaderLogin) throws FailedDownloadException,
            NamingException;
    
    public Long upload(AudioFile file) throws FailedUploadException, NamingException, RemoteException;

    public FileContent download(List<Long> id, List<Integer> bitrates, String downloaderLogin, boolean local)
            throws FailedDownloadException, NamingException;
    
}
