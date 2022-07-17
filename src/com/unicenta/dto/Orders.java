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

package com.unicenta.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "orders")
@Table(name = "orders", uniqueConstraints = {
    @UniqueConstraint(columnNames = "id")})
public class Orders implements Serializable {

/*    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, length = 50)
    private String id;
*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, length = 11)
    private Integer id;
    
    @Column(name = "orderid", nullable = true, length = 50)
    private String orderid;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "details", nullable = true, length = 255)
    private String details;

    @Column(name = "attributes", nullable = true, length = 255)
    private String attributes;

    @Column(name = "notes", nullable = true, length = 255)
    private String notes;

    @Column(name = "ticketid", nullable = true, length = 50)
    private String ticketid;

    @Column(name = "ordertime", nullable = true)
    private Timestamp ordertime;

    @Column(name = "displayid", nullable = true)
    private Integer displayid;

    @Column(name = "auxiliary")
    private Integer auxiliaryid;
    
    @Column(name = "completetime", nullable = true)
    private Timestamp completetime;    

    /**
     * @return the orderid
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * @param orderid the orderid to set
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    /**
     * @return the qty
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return the attributes
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    
    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the ticketid
     */
    public String getTicketid() {
        return ticketid;
    }

    /**
     * @param ticketid the ticketid to set
     */
    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    /**
     * @return the ordertime
     */
    public Timestamp getOrdertime() {
        return ordertime;
    }

    /**
     * @param ordertime the ordertime to set
     */
    public void setOrdertime(Timestamp ordertime) {
        this.ordertime = ordertime;
    }

    /**
     * @return the completetime
     */
    public Timestamp getCompletetime() {
        return completetime;
    }

    /**
     * @param completetime the completetime to set
     */
    public void setCompletetime(Timestamp completetime) {
        this.completetime = completetime;
    }    
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the displayid
     */
    public Integer getDisplayid() {
        return displayid;
    }

    /**
     * @param displayid the displayid to set
     */
    public void setDisplayid(Integer displayid) {
        this.displayid = displayid;
    }

    public Integer getAuxiliary() {
        return auxiliaryid;
    }


    public void setAuxiliary(Integer auxiliaryid) {
        this.auxiliaryid = auxiliaryid;
    }

}