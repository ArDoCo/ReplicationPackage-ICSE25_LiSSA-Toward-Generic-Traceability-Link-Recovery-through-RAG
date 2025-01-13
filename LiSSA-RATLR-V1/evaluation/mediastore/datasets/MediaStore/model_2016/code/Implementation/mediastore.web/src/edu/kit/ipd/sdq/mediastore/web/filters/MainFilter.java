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
package edu.kit.ipd.sdq.mediastore.web.filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kit.ipd.sdq.mediastore.web.beans.SessionBean;

/**
 * @author Anastasia @date: 09.12.2014
 */
public class MainFilter implements Filter {

    private static final String INDEX_URL = "index.xhtml";
    private static final String REGISTER_URL = "register";
    private static final String LOGOUT_URL = "logout";
    private static final String LOGIN_URL = "login.xhtml";

    private static final String SESSION_BEAN = "sessionBean";

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void destroy() {

    }

    /**
     * Check: if user logged --> redirect to main page if user not logged --> redirect to login page
     */
    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final SessionBean session = (SessionBean) request.getSession().getAttribute(SESSION_BEAN);
        final String url = request.getRequestURI();
        this.logger.info("URL: " + url);

        if (url.endsWith(".js.xhtml") || url.endsWith(".css.xhtml") || url.endsWith(".png.xhtml")) {
            chain.doFilter(req, res);
            return;
        }

        if (session == null || !session.isLoggedIn) {
            if (url.indexOf(LOGIN_URL) < 0 && url.indexOf(REGISTER_URL) < 0) {
                this.logger.info("Redirect to login page");
                response.sendRedirect(LOGIN_URL);
            } else {
                chain.doFilter(req, res);
            }
        } else {
            if (url.indexOf(LOGIN_URL) >= 0 || url.indexOf(REGISTER_URL) >= 0) {
                this.logger.info("Redirect to main page");
                response.sendRedirect(INDEX_URL);
            } else if (url.indexOf(LOGOUT_URL) >= 0) {
                request.getSession().removeAttribute(SESSION_BEAN);
                this.logger.info("Redirect to login page");
                response.sendRedirect(LOGIN_URL);
            } else {
                chain.doFilter(req, res);
            }
        }
    }

    @Override
    public void init(final FilterConfig arg0) throws ServletException {

    }

}
