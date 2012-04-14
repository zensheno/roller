package org.apache.roller.weblogger.ui.core.security;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.UserManager;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.pojos.UserAttribute;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Spring Security UserDetailsService implemented using Weblogger API.
 */
public class RollerUserDetailsService implements UserDetailsService {
    private static Log log = LogFactory.getLog(RollerUserDetailsService.class);
    
    public UserDetails loadUserByUsername(String userName) 
            throws UsernameNotFoundException, DataAccessException {
        Weblogger roller = null;
        try {
            roller = WebloggerFactory.getWeblogger();
        } catch (Exception e) {
            // Should only happen in case of 1st time startup, setup required
            log.debug("Ignorabale error getting Roller instance", e);
            // Thowing a "soft" exception here allows setup to procede
            throw new UsernameNotFoundException("User info not available yet.");
        }
        try {
            UserManager umgr = roller.getUserManager();
            User userData = null;  
            if (userName.startsWith("http://")) {
                if (userName.endsWith("/")) {
                    userName = userName.substring(0, userName.length() -1 );
                }
                try {
                    userData = umgr.getUserByAttribute(
                        UserAttribute.Attributes.OPENID_URL.toString(), 
                        userName);
                } catch (WebloggerException ex) {
                    throw new DataRetrievalFailureException("ERROR in user lookup", ex);
                }
                String name;
                String password;
                GrantedAuthority[] authorities;
                
                // We are not throwing UsernameNotFound exception in case of 
                // openid authentication in order to recieve user SREG attributes 
                // from the authentication filter and save them                
                if (userData == null) {
                     authorities = new GrantedAuthority[1];
                     GrantedAuthority g = new SimpleGrantedAuthority("openidLogin");
                     authorities[0] = g;
                     name = "openid";
                     password = "openid";
                } else {
                     authorities =  getAuthorities(userData, umgr);
                     name = userData.getUserName();
                     password = userData.getPassword();
                }
                UserDetails usr = new org.springframework.security.core.userdetails.User(name, password, /*true, */Arrays.asList(authorities));
                return  usr;
                
            } else {
                try {
                    userData = umgr.getUserByUserName(userName);
                } catch (WebloggerException ex) {
                    throw new DataRetrievalFailureException("ERROR in user lookup", ex);
                }
                if (userData == null) {
                    throw new UsernameNotFoundException("ERROR no user: " + userName);
                }
                GrantedAuthority[] authorities =  getAuthorities(userData, umgr);        
                return new org.springframework.security.core.userdetails.User(userData.getUserName(), userData.getPassword(),/* true,*/Arrays.asList(authorities));
            }            
        } catch (WebloggerException ex) {
            throw new DataAccessResourceFailureException("ERROR: fetching roles", ex);
        }
        

    }
        
     private GrantedAuthority[] getAuthorities(User userData, UserManager umgr) throws WebloggerException {
             List<String> roles = umgr.getRoles(userData);
            GrantedAuthority[] authorities = new SimpleGrantedAuthority[roles.size()];
            int i = 0;
            for (String role : roles) {
                authorities[i++] = new SimpleGrantedAuthority(role);
            }
            return authorities;
        }
    
}
