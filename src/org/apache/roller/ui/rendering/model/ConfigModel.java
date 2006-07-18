/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */

package org.apache.roller.ui.rendering.model;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.PageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.RollerException;
import org.apache.roller.config.RollerRuntimeConfig;
import org.apache.roller.pojos.wrapper.WeblogEntryDataWrapper;
import org.apache.roller.pojos.wrapper.WebsiteDataWrapper;
import org.apache.roller.ui.core.RequestConstants;
import org.apache.roller.ui.core.RollerContext;
import org.apache.struts.util.RequestUtils;


/**
 * Model which provides access to application config data like site URLs and 
 * config properties.
 */
public class ConfigModel implements Model {
    
   private static Log log = LogFactory.getLog(ConfigModel.class); 
        
    /** TODO 3.0: remove dependency on pageContext */
    private PageContext pageContext = null;
    
    /** Hand-picked list of runtime properties to be made available */
    private static List allowedProperties = 
        Arrays.asList(new String[] {
            "site.name",
            "site.shortName",
            "site.description",
            "site.adminemail",
            "users.registration.enabled",
            "users.registration.url",
            "users.comments.enabled",
            "users.trackbacks.enabled",
            "users.comments.autoformat",
            "users.comments.escapehtml",
            "users.comments.emailnotify",
            "site.linkbacks.enabled",
            "site.newsfeeds.defaultEntries",
            "site.newsfeeds.maxEntries"
    });
    
    
    /** Template context name to be used for model */
    public String getModelName() {
        return "config";
    }
    
    
    /** Init model */
    public void init(Map map) throws RollerException {
        pageContext = (PageContext)map.get("pageContext");
    }
    
    
    /** Relative URL of Roller, e.g. /roller */
    public String getContextURL() {
        return RollerRuntimeConfig.getRelativeContextURL();
    }
    
    
    /** Absolute URL of Roller, e.g. http://localhost:8080/roller */
    public String getAbsoluteContextURL() {
        return RollerRuntimeConfig.getAbsoluteContextURL();
    }
    
    
    /** 
     * Get Roller string runtime configuration property.
     * @return Property value or null if not found 
     */
    public String getProperty(String name) {
        String ret = null;
        if (allowedProperties.contains(name)) {
            ret = RollerRuntimeConfig.getProperty(name);
        }
        return ret;
    }
    
    
    /** 
     * Get Roller integer runtime configuration property 
     * @return Property value or -999 if not found 
     */
    public int getIntProperty(String name) {
        int ret = -999;
        if (allowedProperties.contains(name)) {
            ret = RollerRuntimeConfig.getIntProperty(name);
        }
        return ret;
    }
    
    
    /** 
     * Get Roller boolean runtime configuration property.
     * @return Property value or false if not found 
     */
    public boolean getBooleanProperty(String name) {
        boolean ret = false;
        if (allowedProperties.contains(name)) {
            return RollerRuntimeConfig.getBooleanProperty(name);
        } 
        return ret;
    }
    
    
    /** Get Roller version string */
    public String getRollerVersion() {
        return RollerContext.getRollerContext().getRollerVersion();
    }
    
    
    /** Get timestamp of Roller build */
    public String getRollerBuildTimestamp() {
        return RollerContext.getRollerContext().getRollerBuildTime();
    }
    
    
    /** Get username who created Roller build */
    public String getRollerBuildUser() {
        return RollerContext.getRollerContext().getRollerBuildUser();
    }
    
    
    /** URL for editing a weblog entry */
    public String getWeblogEntryEditURL(WeblogEntryDataWrapper entry) {
        String ret = null;
        Map params = new HashMap();
        params.put(RequestConstants.WEBLOGENTRY_ID, entry.getId());
        params.put(RequestConstants.ANCHOR, entry.getAnchor());
        try {
            ret = RequestUtils.computeURL(pageContext,
                "weblogEdit", null, null, null, params, null, false);
        } catch (MalformedURLException mue) {
            log.error("ERROR forming Struts URL: ", mue);
        }
        return ret;
    } 
    
    
    /** URL for creating a new weblog entry */
    public String getWeblogEntryCreateURL(WebsiteDataWrapper weblog) {
        String returnURL = null;
        Map params = new HashMap();
        params.put(RequestConstants.WEBLOG, weblog.getHandle());
        try {
            returnURL = RequestUtils.computeURL(pageContext,
                "weblogCreate", null, null, null, params, null, false);
        } catch (MalformedURLException mue) {
            log.error("ERROR forming Struts URL: ", mue);
        }
        return returnURL;
    }
    
    
    /** URL for editing weblog settings */
    public String getWeblogSettingsURL(WebsiteDataWrapper weblog) {
        String returnURL = null;
        Map params = new HashMap();
        params.put(RequestConstants.WEBLOG, weblog.getHandle());        
        try {
            returnURL = RequestUtils.computeURL(pageContext,
                "editWebsite", null, null, null, params, null, false);
        } catch (MalformedURLException mue) {
            log.error("ERROR forming Struts URL: ", mue);
        }
        return returnURL;
    }
    
    
    /** URL for logging in */  
    public String getLoginURL() {
        String returnURL = null;
        try {
            returnURL = RequestUtils.computeURL(pageContext,
                "login-redirect", null, null, null, null, null, false);
        } catch (MalformedURLException mue) {
            log.error("ERROR forming Struts URL: ", mue);
        }
        return returnURL;
    }
    
    
    /** URL for logging out */
    public String getLogoutURL() {
        String returnURL = null;
        try {
            returnURL = RequestUtils.computeURL(pageContext,
                "logout-redirect", null, null, null, null, null, false);
        } catch (MalformedURLException mue) {
            log.error("ERROR forming Struts URL: ", mue);
        }
        return returnURL;
    }    
}
