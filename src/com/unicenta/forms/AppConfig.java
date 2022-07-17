//  uniCenta oPOS  - Touch Friendly Point Of Sale
//  Copyright (c) 2017 uniCenta
//  https://unicenta.com
//
//  This file is part of uniCenta Remote Display
//
//  uniCenta Remote Display is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  uniCenta Remote Display is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.
//
//  uniCenta Remote Display is based on previous other work in the Public Domain
//  and which is not shown as containing any restrictive or proprietary license
//  Thanks to : N. Deppe & J. Lewis   

package com.unicenta.forms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public final class AppConfig {

    private static AppConfig instance = null;
    private final Properties m_propsconfig;
    private final File configFile;
    private static final Logger logger = Logger.getLogger("com.unicenta.AppConfig");

    protected AppConfig(File configFile) {
        this.configFile = configFile;
        m_propsconfig = new Properties();
        load();
        logger.log(Level.INFO, "Reading configuration file: {0}"
            , configFile.getAbsolutePath());
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig(new File(System.getProperty("user.home")
                , AppLocal.APP_ID + ".properties"));
        }
        return instance;
    }

    private File getDefaultConfig() {
        return new File(new File(System.getProperty("user.home"))
            , AppLocal.APP_ID + ".properties");
    }

    public String getDirPath() {
        String dirname = System.getProperty("dirname.path");
        return (dirname == null ? "./" : dirname);
    }

    public void setProperty(String sKey, String sValue) {
        if (sValue == null) {
            m_propsconfig.remove(sKey);
        } else {
            m_propsconfig.setProperty(sKey, sValue);
        }
    }

    public String getProperty(String sKey) {
        return m_propsconfig.getProperty(sKey);
    }

    public boolean delete() {
        loadDefault();
        return configFile.delete();
    }

    public void load() {
        loadDefault();
        try {
            InputStream in = new FileInputStream(configFile);
            if (in != null) {
                m_propsconfig.load(in);
                in.close();
            }
        } catch (IOException e) {
            loadDefault();
        }
    }

    public void save() throws IOException {
        OutputStream out = new FileOutputStream(configFile);
        if (out != null) {
            m_propsconfig.store(out, AppLocal.APP_NAME + ". Configuration file.");
            out.close();
        }
    }

    private void loadDefault() {
        m_propsconfig.setProperty("db.engine", "MySql");
        m_propsconfig.setProperty("db.driver", "com.mysql.jdbc.Driver");
        m_propsconfig.setProperty("db.URL", "jdbc:mysql://localhost:3306/unicentaopos");
        m_propsconfig.setProperty("db.user", "root");
        m_propsconfig.setProperty("db.password", "mysql");
        m_propsconfig.setProperty("screen.displaynumber", "1");
        m_propsconfig.setProperty("db.dialect", "org.hibernate.dialect.MySQLDialect");
        m_propsconfig.setProperty("limit.wait", "5");        
        m_propsconfig.setProperty("limit.wait", "10");        
        m_propsconfig.setProperty("limit.wait", "15");                   
    }
}