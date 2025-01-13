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
import javax.naming.NamingException;

import edu.kit.ipd.sdq.mediastore.basic.data.UserRegData;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.UserAlreadyExistsException;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IFacade;
import edu.kit.ipd.sdq.mediastore.web.utils.MessageUtil;

/**
 * @author Anastasia
 */
@ManagedBean
public class RegisterBean implements Serializable {

    private static final long serialVersionUID = -5229414856228963671L;

    private String email;
    private String firstName;
    private String lastName;
    private String password;

    
    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String doRegister() {
        System.out.println("RegisterBean.doRegister()");
//        System.err.println(String.format("RegisterBean.doRegister: plain=%s", this.getPassword() ));
        final Long start = System.currentTimeMillis();
        final UserRegData regData = new UserRegData(this.getFirstName(), this.getLastName(), this.getEmail(),
                this.getPassword());
        try {
            final Long saveNewUser = WebBeanManager.initRequiredInterface("IFacade", IFacade.class).register(regData);
            System.out.println(saveNewUser);
            System.out.println("Time for registration(in ms): " + (System.currentTimeMillis() - start));
            return "login";

        } catch (final NamingException e) {
            MessageUtil.showError("Server ERROR");
            e.printStackTrace();
        } catch (final UserAlreadyExistsException e) {
            MessageUtil.showError(e.getMessage());
        }
        return "register";
    }

}
