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
package edu.kit.ipd.sdq.mediastore.ejb.facade;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.NamingException;

import edu.kit.ipd.sdq.mediastore.basic.BaseEJB;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFileInfo;
import edu.kit.ipd.sdq.mediastore.basic.data.CurrentUser;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContent;
import edu.kit.ipd.sdq.mediastore.basic.data.FileContentRemote;
import edu.kit.ipd.sdq.mediastore.basic.data.UserRegData;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.BadLoginDataException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedDownloadException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.FailedUploadException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.UserAlreadyExistsException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IFacadeLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IFacadeRemote;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IMediaManagement;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IUserManagement;
import edu.kit.ipd.sdq.mediastore.basic.utils.FSUtil;

/**
 * @author Anastasia
 */

@Stateless
public class FacadeImpl extends BaseEJB implements IFacadeRemote, IFacadeLocal {

    private static final long serialVersionUID = 308982198674607428L;

    IUserManagement userManagement;

    IMediaManagement mediaManagement;

//    /*
//     * Funktioniert nur mit den local EJB!!!
//     *
//     * @EJB(lookup="ejb/UserAdapterImpl")
//     *  private IUserDBAdapter userAdapter;
//     */
//    @PostConstruct
//    public void init() {
//        /*
//         * JNDI wird in GlassFish console als external JNDI resource definiert!
//         *
//         * InitialContext ctx = new InitialContext();
//         * IUserDBAdapter userAdapter = (IUserDBAdapter) ctx.lookup("ejb/UserAdapterImpl");
//         */
//    }

    public FacadeImpl() {
		super();
		ejbName = "facade";
	}
    
    private void initMediaManager() {
    	this.mediaManagement = initRequiredInterface("IMediaManagement", IMediaManagement.class);
    }

    private void initUserManagement() {
        this.userManagement = initRequiredInterface("IUserManagement", IUserManagement.class);
    }

    @Override
    public Long register(final UserRegData user) throws UserAlreadyExistsException, NamingException {
        this.initUserManagement();
        return this.userManagement.register(user);
    }

    @Override
    public CurrentUser login(final String username, final String password) throws BadLoginDataException,
            NamingException {
        this.initUserManagement();
        return this.userManagement.login(username, password);
    }

    @Override
    public Long upload(AudioFile file)  throws FailedUploadException, NamingException, RemoteException {
        this.initMediaManager();
        FileContent content = file.getContent();
        String fileName = file.getUploader() + file.getFilename() + "Facade-MM";
        file.setContent(content.convertIfNeeded(isLocal("IMediaManagement"), fileName, "mp3"));
        return this.mediaManagement.upload(file);
    }

    @Override
    public List<AudioFileInfo> getFileList() throws NamingException {
        this.initMediaManager();
        return this.mediaManagement.getFileList();
    }

    @Override
    public void downloadTest(final List<Long> id, final List<Integer> bitrates, final String downloaderLogin)
            throws FailedDownloadException, NamingException {
        this.initMediaManager();
        final FileContentRemote content = (FileContentRemote) this.mediaManagement.download(id, bitrates, downloaderLogin, isLocal("IMediaManagement"));
        
        content.getBytes().equals("42"); // prevent optimization
    }

    @Override
    public FileContent download(final List<Long> id, final List<Integer> bitrates, final String downloaderLogin, final boolean localCall)
            throws FailedDownloadException, NamingException {
    	
        System.out.println("Bitrates " + bitrates.toString());
        this.initMediaManager();
        FileContent content = this.mediaManagement.download(id, bitrates, downloaderLogin, isLocal("IMediaManagement"));

        String fileName = downloaderLogin + "Facade-Download";
        content = content.convertIfNeeded(localCall, fileName, FSUtil.getExtension(id.size()));
        return content;
         
        
    }
	
}
