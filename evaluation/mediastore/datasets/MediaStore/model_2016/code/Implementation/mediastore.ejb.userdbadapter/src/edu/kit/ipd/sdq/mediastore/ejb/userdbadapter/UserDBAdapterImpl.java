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
package edu.kit.ipd.sdq.mediastore.ejb.userdbadapter;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import edu.kit.ipd.sdq.mediastore.basic.BaseEJB;
import edu.kit.ipd.sdq.mediastore.basic.data.CurrentUser;
import edu.kit.ipd.sdq.mediastore.basic.data.UserRegData;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.BadLoginDataException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.UserAlreadyExistsException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IUserDBAdapterLocal;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IUserDBAdapterRemote;

/**
 * @author Anastasia @date: 09.12.2014
 */

@Stateless
public class UserDBAdapterImpl extends BaseEJB implements IUserDBAdapterRemote, IUserDBAdapterLocal {

    /**
	 * 
	 */
	private static final long serialVersionUID = -36797393228502249L;
	@EJB
    DbManager dbManager;
    
    public UserDBAdapterImpl() {
		super();
		ejbName = "userdbadapter";
	}
    
    @Override
    public Long addUser(final UserRegData userData) throws UserAlreadyExistsException {
//        System.out.println("Remote");
        return this.dbManager.saveUser(userData);
    }

    @Override
    public CurrentUser getUserData(final String username, final String password) throws BadLoginDataException {
//        System.out.println("Server: UserAdapterImpl.login()");
        return this.dbManager.findUser(username, password);
    }

    @Override
    public void removeAllData() {
        this.dbManager.clearTable();
    }
}
