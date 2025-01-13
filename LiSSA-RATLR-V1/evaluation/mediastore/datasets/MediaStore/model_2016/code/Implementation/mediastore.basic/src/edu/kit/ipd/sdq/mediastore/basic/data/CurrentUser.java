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

/**
 * @author Anastasia @date: 07.12.2014
 */
public class CurrentUser implements Serializable {

    private static final long serialVersionUID = 1016233743738408827L;

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String passwordHash;

    public CurrentUser() {
    }

    public CurrentUser(final Long id, final String firstname, final String lastname, final String email,
            final String passwordHash) {
        super();
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "CurrentUser [id=" + this.id + ", firstname=" + this.firstname + ", lastname=" + this.lastname
                + ", email=" + this.email + "]";
    }

}
