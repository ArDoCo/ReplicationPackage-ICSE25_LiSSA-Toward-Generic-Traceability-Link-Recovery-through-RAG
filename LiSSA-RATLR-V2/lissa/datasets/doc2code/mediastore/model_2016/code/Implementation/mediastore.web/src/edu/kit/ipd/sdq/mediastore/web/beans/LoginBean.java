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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.naming.NamingException;

import edu.kit.ipd.sdq.mediastore.basic.data.CurrentUser;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.BadLoginDataException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IFacade;
import edu.kit.ipd.sdq.mediastore.web.utils.MessageUtil;

/**
 *
 * @author Anastasia
 *
 */
@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 6536288910967304447L;

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    private String username;
    private String password;

    public String dologin() {

        try {
//            System.err.println(String.format("LoginBean.dologin: plain=%s", this.getPassword() ));
            CurrentUser registeredUser = WebBeanManager.initRequiredInterface("IFacade", IFacade.class).login(this.getUsername(), this.getPassword());
            this.sessionBean.setCurrentUser(registeredUser);
            this.sessionBean.isLoggedIn = true;

            return "index";

        } catch (final NamingException e) {
            MessageUtil.showError("Server cannot be found");
            e.printStackTrace();
        } catch (final BadLoginDataException e) {
            MessageUtil.showError(e.getMessage());
            e.printStackTrace();
        }

        return "login";
    }

    public SessionBean getSessionBean() {
        return this.sessionBean;
    }

    public void setSessionBean(final SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
