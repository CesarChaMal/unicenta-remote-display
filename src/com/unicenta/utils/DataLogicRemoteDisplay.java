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

package com.unicenta.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import com.unicenta.dto.Orders;
import com.unicenta.forms.AppConfig;
import com.unicenta.hibernate.HibernateUtil;

public class DataLogicRemoteDisplay {

    private Session session;
    private String sql_query;
    private Query query;

    public DataLogicRemoteDisplay() {
    }

    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public List<String> readDistinctOrders() {
        if (Boolean.valueOf(AppConfig.getInstance().getProperty("screen.allorders"))) {
            sql_query = "SELECT DISTINCT orderid, ordertime "
                    + "FROM orders "
                    + "ORDER BY ordertime ";
        } else {
            sql_query = "SELECT DISTINCT orderid, ordertime "
                    + "FROM ORDERS "
                    + "WHERE displayid = " 
                        + Integer.parseInt(AppConfig.getInstance().getProperty("screen.displaynumber")) 
                    + " ORDER BY ordertime";
        }
        SQLQuery squery = HibernateUtil.getSessionFactory().openSession().createSQLQuery(sql_query);
        squery.addScalar("orderid");
        List results = squery.list();
        results = new ArrayList<>(new LinkedHashSet<>(results));
        return results;
    }

    public void removeOrder(String orderid) {
        init();
        session.beginTransaction();
        if (Boolean.valueOf(AppConfig.getInstance().getProperty("screen.allorders"))) {
            query = session.createQuery("DELETE FROM orders "
                    + "WHERE orderid = :id ");
        } else {
            query = session.createQuery("DELETE FROM orders "
                    + "WHERE orderid = :id AND DISPLAYID = :display");
            query.setParameter("display", Integer.parseInt(AppConfig.getInstance().getProperty("screen.displaynumber")));
        }

        query.setParameter("id", orderid);
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void removeAllOrders() {
        init();
        session.beginTransaction();
        Query squery = session.createQuery("DELETE FROM orders ");
        int result = squery.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    
    /**
     * Remove all orders for current display only
     */
    public void removeAllOrdersDisplay() {
        init();
        session.beginTransaction();
        Query squery = session.createQuery("DELETE FROM orders "
                + "WHERE displayid = :display");
        squery.setParameter("display", Integer.parseInt(AppConfig.getInstance().getProperty("screen.displaynumber")));
        int result = squery.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public List<Orders> selectByOrderId(String orderid) {
        if (Boolean.valueOf(AppConfig.getInstance().getProperty("screen.allorders"))) {
            sql_query = "SELECT "
                    + "id, orderid, qty, details, attributes, notes, "
                    + "ticketid, ordertime, displayid, auxiliary, completetime "                    
                    + "FROM orders WHERE orderid ='" 
                        + orderid + "' "
                    + " ORDER BY auxiliary ";
        } else {
            sql_query = "SELECT "
                    + "id, orderid, qty, details, attributes, notes, "
                    + "ticketid, ordertime, displayid, auxiliary, completetime "
                    + "FROM orders WHERE orderid ='"                     
                        + orderid + "' AND displayid = " 
                        + Integer.parseInt(AppConfig.getInstance().getProperty("screen.displaynumber")) 
                    + " ORDER BY auxiliary ";
        }

        SQLQuery squery = HibernateUtil.getSessionFactory().openSession().createSQLQuery(sql_query);
        squery.addEntity(Orders.class);
        List<Orders> results = squery.list();
        return results;
    }
	 
	 
	 /* N Deppe Sept 2015 - Added to be able to create new order records for recall function */
	 public void createOrder(Orders orderData) {
		String newId = UUID.randomUUID().toString();
		sql_query = "INSERT INTO orders "
                        + "(id, orderid, qty, details, attributes, notes, "
                        + "ticketid, ordertime, displayid, auxiliary, completetime)"
                            + " VALUES ( :id, :orderid, :qty, :details, :attributes, :notes, "
                            + ":ticketid, :ordertime, :displayid, :auxiliaryid, :completetime )";
                
		init();
		session.beginTransaction();
		query = session.createSQLQuery(sql_query);
		query.setParameter("id", newId);
		query.setParameter("orderid", orderData.getOrderid());
		query.setParameter("qty", orderData.getQty());
		query.setParameter("details", orderData.getDetails());
		query.setParameter("attributes", orderData.getAttributes());
		query.setParameter("notes", orderData.getNotes());
		query.setParameter("ticketid", orderData.getTicketid());
		query.setParameter("ordertime", orderData.getOrdertime());
		query.setParameter("displayid", orderData.getDisplayid());
                query.setParameter("auxiliaryid", orderData.getAuxiliary());
                query.setParameter("completetime", orderData.getCompletetime());
                
		int result = query.executeUpdate();
		session.getTransaction().commit();
		session.close();		 
	}	 
}