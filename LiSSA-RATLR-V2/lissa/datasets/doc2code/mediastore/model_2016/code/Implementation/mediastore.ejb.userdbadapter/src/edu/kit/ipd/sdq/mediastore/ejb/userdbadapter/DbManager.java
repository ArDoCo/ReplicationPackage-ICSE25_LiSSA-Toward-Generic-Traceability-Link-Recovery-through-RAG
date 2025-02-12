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

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.kit.ipd.sdq.mediastore.basic.data.CurrentUser;
import edu.kit.ipd.sdq.mediastore.basic.data.UserRegData;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.BadLoginDataException;
import edu.kit.ipd.sdq.mediastore.basic.exceptions.UserAlreadyExistsException;

/**
 * @author Anastasia @date: 06.12.2014
 */
@Stateless
public class DbManager {

    @PersistenceContext(name = "Mediastore")
    private EntityManager em;

    public Long saveUser(final UserRegData userData) throws UserAlreadyExistsException {

//        System.out.println("DbManager.saveUser()");

        boolean userExists = this.em.createNamedQuery("findByEmail", User.class)
                .setParameter("email", userData.getEmail()).getResultList().isEmpty();
        userExists = false; // TODO: Remove me after performance tests
        if (!userExists) {

            final User user = new User(userData.getFirstname(), userData.getLastname(), userData.getEmail(),
                    userData.getPassword());
            this.em.persist(user);
            this.em.flush(); // make possible to get id
            return user.getId();

        } else {
            throw new UserAlreadyExistsException(userData.getEmail());
        }

    }

    public CurrentUser findUser(final String username, final String password) throws BadLoginDataException {

        final List<User> users = this.em.createNamedQuery("findByEmail", User.class).setParameter("email", username)
                .getResultList();

        if (!users.isEmpty()) {
            final User u = users.get(0);
            return new CurrentUser(u.getId(), u.getFirstname(), u.getLastname(), u.getEmail(), u.getPassword());
        }
        throw new BadLoginDataException();
    }

    public void clearTable() {
        this.em.createNamedQuery("clear").executeUpdate();
    }
}
