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

public class UserRegData implements Serializable {

    private static final long serialVersionUID = 5539510381127707390L;

    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public UserRegData(final String firstname, final String lastname, final String email, final String password) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRegData [firstname=" + this.firstname + ", lastname=" + this.lastname + ", email=" + this.email
                + ", password=" + this.password + "]";
    }

}
