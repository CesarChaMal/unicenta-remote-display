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

package com.unicenta.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import com.unicenta.dto.Orders;

import com.unicenta.forms.AppConfig;
import com.unicenta.utils.AltEncrypter;
import org.hibernate.HibernateException;

public class HibernateUtil {

    private static SessionFactory sessionFactory = buildSessionFactory();
    private static ServiceRegistry serviceRegistry;

    private static SessionFactory buildSessionFactory() {

        String sDBUser = AppConfig.getInstance().getProperty("db.user");
        String sDBPassword = AppConfig.getInstance().getProperty("db.password");
        
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }

        Configuration configuration = new Configuration();

        // Set up the database details ready for the connections
        configuration.setProperty("hibernate.connection.driver_class"
                , AppConfig.getInstance().getProperty("db.driver"));
        configuration.setProperty("hibernate.connection.url"
                , AppConfig.getInstance().getProperty("db.URL"));
        configuration.setProperty("hibernate.connection.username"
                , AppConfig.getInstance().getProperty("db.user"));
        configuration.setProperty("hibernate.connection.password"
                , sDBPassword);
        configuration.setProperty("hibernate.dialect"
                , AppConfig.getInstance().getProperty("db.dialect"));
        /*
         // Set up connection pooling to use c3p0 rather than hibernates built in pooling
         configuration.setProperty("hibernate.connection.provider_class", 
                 "org.hibernate.connection.C3P0ConnectionProvider");
         // configuration.setProperty("hibernate.connection.provider_class", 
                 "org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider");
       
         configuration.setProperty("hibernate.c3p0.initialPoolSize", "5");
         configuration.setProperty("hibernate.c3p0.min", "5");
         configuration.setProperty("hibernate.c3p0.max", "10");
         configuration.setProperty("hibernate.c3p0.timeout", "5000");
         configuration.setProperty("hibernate.c3p0.max_statements", "30");
         configuration.setProperty("hibernate.c3p0.idle_test_period", "300");
         configuration.setProperty("hibernate.c3p0.aquire_increment", "2");
        */
        
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.connection.pool_size", "5");

        configuration.addAnnotatedClass(Orders.class);

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        try {
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException ex) {
            return null;
        }

        return sessionFactory;

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}