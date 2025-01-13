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

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.NamingException;

import edu.kit.ipd.sdq.mediastore.basic.data.CurrentUser;

/**
 * @author Anastasia
 * @date 28.11.2014
 */
@ManagedBean(name = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

    private static final long serialVersionUID = 3127338997048577039L;

    public Boolean isLoggedIn = false;
    private CurrentUser currentUser = null;

    public SessionBean() throws NamingException {

    }

    public CurrentUser getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(final CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

}
