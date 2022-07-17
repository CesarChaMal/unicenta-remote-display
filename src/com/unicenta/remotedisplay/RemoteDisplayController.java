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

package com.unicenta.remotedisplay;

import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import com.unicenta.dto.Orders;
import com.unicenta.forms.AppConfig;
import com.unicenta.utils.DataLogicRemoteDisplay;
import com.unicenta.utils.FixedStack;

/**
 * FXML Controller class
 *
 * @author uniCenta
 */
public class RemoteDisplayController implements Initializable {

    public Button exit;
    public Button completed;
    public Button recall;
    public Label clock;

    public Label order0id;
    public Label order0time;
    public Label order1id;
    public Label order1time;
    public Label order2id;
    public Label order2time;
    public Label order3id;
    public Label order3time;
    public Label order4id;
    public Label order4time;
    public Label order5id;
    public Label order5time;
    public Label order6id;
    public Label order6time;
    public Label order7id;
    public Label order7time;
    public Label odwaiting;

    public ListView order0items;
    public ListView order1items;
    public ListView order2items;
    public ListView order3items;
    public ListView order4items;
    public ListView order5items;
    public ListView order6items;
    public ListView order7items;

    public ListView orderlist;

    private Label tmpLabel;
    private DateFormat dateFormat;
    private String hms;
    public static String selectedOrderId;
    public static List<Orders> selectedOrder;
    public static Integer selectedOrderNum;
    private DataLogicRemoteDisplay dl_remotedisplay;
    private List<String> distinct;
    private List<Orders> orders;
    private double xOffset = 0;
    private double yOffset = 0;

    public static HashMap<Integer, Object> idLabels = new HashMap<>();
    public static HashMap<Integer, String> ticketIds = new HashMap<>();

    public static HashMap<Integer, Object> timeLabels = new HashMap<>();
    public static HashMap<Integer, Long> startTimes = new HashMap<>();

    public static HashMap<Integer, ObservableList> orderLists = new HashMap<>();

    public static HashMap<Integer, String> orderIds = new HashMap<>();

    public static HashMap<Integer, List<Orders>> orderDataList = new HashMap<>();
    public static HashMap<Integer, Orders> orderData = new HashMap<>();

    public static ObservableList ordersWaiting = FXCollections.observableArrayList();
    public static ObservableList order0list = FXCollections.observableArrayList();
    public static ObservableList order1list = FXCollections.observableArrayList();
    public static ObservableList order2list = FXCollections.observableArrayList();
    public static ObservableList order3list = FXCollections.observableArrayList();
    public static ObservableList order4list = FXCollections.observableArrayList();
    public static ObservableList order5list = FXCollections.observableArrayList();
    public static ObservableList order6list = FXCollections.observableArrayList();
    public static ObservableList order7list = FXCollections.observableArrayList();

    private FixedStack<List<Orders>> closedOrders;
    private Scene thisScene;

    private class PrintTimeAction implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Platform.runLater(() -> {
                updateClock();
                updateTimers();
            });
        }
    }

    private class updateDisplay implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Platform.runLater(() -> {
                buildOrderPanels();
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if ("monitor".equals(RemoteDisplay.parameter)) {
            completed.setVisible(false);
        }

        dl_remotedisplay = new DataLogicRemoteDisplay();

        int stackSize;
        try {
            stackSize = Integer.parseInt(AppConfig.getInstance().getProperty("recall.historycount"));
        } catch (NumberFormatException e) {
            stackSize = 10;
        }
        if (stackSize >= 1) {
            closedOrders = new FixedStack<>(stackSize);
        } else {
            closedOrders = null;
        }

        displayRecallButton();

        new javax.swing.Timer(1000, new PrintTimeAction()).start();

// add this setting to config properties
        new javax.swing.Timer(5000, new updateDisplay()).start();

        order0items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(0);
        });
        order1items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(1);
        });
        order2items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(2);
        });
        order3items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(3);
        });
        order4items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(4);
        });
        order5items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(5);
        });
        order6items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(6);
        });
        order7items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(7);
        });

        odwaiting.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        odwaiting.setOnMouseDragged((MouseEvent event) -> {
            RemoteDisplay.publicStage.setX(event.getScreenX() - xOffset);
            RemoteDisplay.publicStage.setY(event.getScreenY() - yOffset);
        });

        try {
            if (AppConfig.getInstance().getProperty("clock.time") != null) {
                dateFormat = new SimpleDateFormat(AppConfig.getInstance().getProperty("clock.time"));
            } else {
                dateFormat = new SimpleDateFormat("HH:mm:ss");
            }
        } catch (IllegalArgumentException e) {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
        }

        createMaps();
        buildOrderPanels();
    }

    public void handleExitClick() {

        String exitAction = AppConfig.getInstance().getProperty("misc.exitaction");
        if (exitAction == null) {
            exitAction = "0";
        }
        switch (exitAction) {
            case "0":
                System.exit(0);
                break;
            case "1":
            case "":

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exit Remote Display");
                alert.setX(100);
                alert.setY(150);
                if ("monitor".equals(RemoteDisplay.parameter)) {
                    alert.setHeaderText("");
                    alert.setContentText("Exit Remote Display screen?");
                } else {
                    alert.setHeaderText("Notice :  \nIf you close now unprocessed Orders will be deleted from the list");
                    alert.setContentText("Are you sure?");
                }

                ButtonType buttonClearExit = new ButtonType("Close Orders");
                ButtonType buttonClearDisplayExit = new ButtonType("Close Display");
                ButtonType buttonCancel = new ButtonType("Cancel");
                ButtonType buttonExit = new ButtonType("Exit");
                
                if ("monitor".equals(RemoteDisplay.parameter)) {
                    alert.getButtonTypes().setAll(buttonExit, buttonCancel);
                } else {
                    alert.getButtonTypes().setAll(buttonExit, buttonClearExit, buttonClearDisplayExit, buttonCancel);
                }

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonClearExit) {
                    dl_remotedisplay.removeAllOrders();
                    System.exit(0);
                } else if (result.get() == buttonClearDisplayExit) {
                    dl_remotedisplay.removeAllOrdersDisplay();
                    System.exit(0);
                } else if (result.get() == buttonExit) {
                    System.exit(0);
                }

                break;
            case "2":
                dl_remotedisplay.removeAllOrders();
                System.exit(0);
                break;
            case "3":
                dl_remotedisplay.removeAllOrdersDisplay();
                System.exit(0);
                break;

        }

    }

    public void handleCompleteOrder() {
        if (!"monitor".equals(RemoteDisplay.parameter)) {
            if (selectedOrderId != null) {
                dl_remotedisplay.removeOrder(selectedOrderId);
                closedOrders.push(selectedOrder);
                orderDataList.remove(selectedOrderNum);
                clearSelectedOrder();
            }
            buildOrderPanels();
            displayRecallButton();
        }
    }

    public void clearSelectedOrder() {
        selectedOrderId = null;
        selectedOrder = null;
        selectedOrderNum = null;
        updateButtonText("");
    }

    public void handleRecallOrder() {
        if (!"monitor".equals(RemoteDisplay.parameter)) {
            List<Orders> lastOrder;
            if (!closedOrders.isEmpty()) {
                lastOrder = closedOrders.pop();
                lastOrder.stream().map((currOrder) -> {
                    String strTicketId = currOrder.getTicketid();
                    if (strTicketId.length() < 9 
                            || !strTicketId.substring(0, 9).equals("RECALLED:")) {

                        currOrder.setTicketid("RECALLED: " + currOrder.getTicketid());
                    }
                    return currOrder;
                })
                        .forEachOrdered((currOrder) -> {
                            dl_remotedisplay.createOrder(currOrder);
                        });
                clearSelectedOrder();
                buildOrderPanels();
            }
            displayRecallButton();
        }
    }

    public void displayRecallButton() {
        if ("monitor".equals(RemoteDisplay.parameter)) {
            recall.setVisible(false);
        } else if (closedOrders != null) {
            recall.setVisible(!closedOrders.isEmpty());
        } else {
            recall.setVisible(false);
        }
    }

    private void updateClock() {
        clock.setText(dateFormat.format(new Date()));
    }

    private String getTime(long milliseconds) {
        hms = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) 
                        - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) 
                        - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
        return (hms);
    }

    private void createMaps() {
        idLabels.put(0, order0id);
        idLabels.put(1, order1id);
        idLabels.put(2, order2id);
        idLabels.put(3, order3id);
        idLabels.put(4, order4id);
        idLabels.put(5, order5id);
        idLabels.put(6, order6id);
        idLabels.put(7, order7id);

        timeLabels.put(0, order0time);
        timeLabels.put(1, order1time);
        timeLabels.put(2, order2time);
        timeLabels.put(3, order3time);
        timeLabels.put(4, order4time);
        timeLabels.put(5, order5time);
        timeLabels.put(6, order6time);
        timeLabels.put(7, order7time);

        orderLists.put(0, order0list);
        orderLists.put(1, order1list);
        orderLists.put(2, order2list);
        orderLists.put(3, order3list);
        orderLists.put(4, order4list);
        orderLists.put(5, order5list);
        orderLists.put(6, order6list);
        orderLists.put(7, order7list);
    }

    private void updateTimers() {
        for (int j = 0; j < 8; j++) {
            if (startTimes.get(j) > 0) {
                long elapsed = (System.currentTimeMillis() - startTimes.get(j));
                tmpLabel = (Label) timeLabels.get(j);
                tmpLabel.setText(getTime(elapsed));
            }
        }
    }

    private void updateButtonText(String id) {
        if (selectedOrderId == null) {
            completed.setText("");
        } else {
            completed.setText("Complete Order :  " + id);
        }
    }

    private void updateLabels() {
        for (int j = 0; j < 8; j++) {
            tmpLabel = (Label) idLabels.get(j);
            tmpLabel.setText(ticketIds.get(j));
        }
    }

    private void buildOrderPanels() {

        resetItemDisplays();

        // Get list of unique orders
        distinct = dl_remotedisplay.readDistinctOrders();
        RemoteDisplayController.orderDataList.clear();

        // Populate the panel up to 8 orders
        for (int j = 0; (j < 8 && j < distinct.size()); j++) {

            orders = dl_remotedisplay.selectByOrderId(distinct.get(j));
            RemoteDisplayController.orderDataList.put(j, orders);

            for (Orders order : orders) {
                RemoteDisplayController.ticketIds.put(j, order.getTicketid());
                ((Label) RemoteDisplayController.idLabels.get(j)).setText(order.getTicketid());
                RemoteDisplayController.startTimes.put(j, order.getOrdertime().getTime());
                RemoteDisplayController.orderIds.put(j, order.getOrderid());
                RemoteDisplayController.orderLists.get(j).add((order.getQty() > 1 
                        ? order.getQty() + " - " : "") + order.getDetails());

                if (!"".equals(order.getAttributes())) {
                    RemoteDisplayController.orderLists.get(j).add(" * " + order.getAttributes());
                }
                if (order.getNotes() != null) {
                    RemoteDisplayController.orderLists.get(j).add(" ** " + order.getNotes());
                }

                long now = System.currentTimeMillis(); // See note below
                long then = order.getOrdertime().getTime();

                long minutes = TimeUnit.MILLISECONDS.toMinutes(now - then);

                int iLimitLow;
                int iLimitMid;
                int iLimitHigh;
            
                iLimitLow = Integer.parseInt(AppConfig.getInstance().getProperty("limit.wait"));
                iLimitMid = Integer.parseInt(AppConfig.getInstance().getProperty("limit.overdue"));
                iLimitHigh = Integer.parseInt(AppConfig.getInstance().getProperty("limit.urgent"));
            
                if (minutes >= iLimitLow && minutes < iLimitMid) {
                    ((Label) RemoteDisplayController.idLabels.get(j)).setText(order.getTicketid() + " WAITING ");
                    ((Label) RemoteDisplayController.idLabels.get(j)).setId("textwaiting");
                    ((Label) RemoteDisplayController.timeLabels.get(j)).setId("textwaiting");
                } else if (minutes >= iLimitMid && minutes < iLimitHigh) {
                    ((Label) RemoteDisplayController.idLabels.get(j)).setText(order.getTicketid() + " OVERDUE ");
                    ((Label) RemoteDisplayController.idLabels.get(j)).setId("textoverdue");                    
                    ((Label) RemoteDisplayController.timeLabels.get(j)).setId("textoverdue");                                        
                } else if (minutes >= iLimitHigh) {
                    ((Label) RemoteDisplayController.idLabels.get(j)).setText(order.getTicketid() + " URGENT! ");                    
                    ((Label) RemoteDisplayController.idLabels.get(j)).setId("texturgent");                    
                    ((Label) RemoteDisplayController.timeLabels.get(j)).setId("texturgent");
                } else {
                    ((Label) RemoteDisplayController.idLabels.get(j)).setText(order.getTicketid() + "");                    
                    ((Label) RemoteDisplayController.idLabels.get(j)).setId("");                    
                    ((Label) RemoteDisplayController.timeLabels.get(j)).setId("");                    
                }
            }
        }

        if (distinct.size() < 8) {
            for (int j = distinct.size(); j < 8; j++) {
                ((Label) RemoteDisplayController.idLabels.get(j)).setText("");
                ((Label) RemoteDisplayController.timeLabels.get(j)).setText("");
                RemoteDisplayController.startTimes.put(j, (long) 0);
                RemoteDisplayController.orderLists.get(j).clear();
            }
        }

        if (distinct.size() > 7) {
            for (int j = 8; j < distinct.size(); j++) {
                orders = dl_remotedisplay.selectByOrderId(distinct.get(j));
                RemoteDisplayController.ordersWaiting.add(orders.get(0).getTicketid());
            }
        }
        updateDisplays();
    }

    private void resetItemDisplays() {
        RemoteDisplayController.order0list.clear();
        RemoteDisplayController.order1list.clear();
        RemoteDisplayController.order2list.clear();
        RemoteDisplayController.order3list.clear();
        RemoteDisplayController.order4list.clear();
        RemoteDisplayController.order5list.clear();
        RemoteDisplayController.order6list.clear();
        RemoteDisplayController.order7list.clear();
        RemoteDisplayController.ordersWaiting.clear();

    }

    private void updateDisplays() {
        order0items.setItems(order0list);
        order1items.setItems(order1list);
        order2items.setItems(order2list);
        order3items.setItems(order3list);
        order4items.setItems(order4list);
        order5items.setItems(order5list);
        order6items.setItems(order6list);
        order7items.setItems(order7list);
        orderlist.setItems(ordersWaiting);
    }

    public void setScene(Scene myScene) {
        if (myScene != null) {

            // Finally, set the scene property
            thisScene = myScene;

        }
    }

    private void selectOrder(int orderNum) {
        if (orderDataList.containsKey(orderNum)) {
            selectedOrder = orderDataList.get(orderNum);
            selectedOrderId = orderIds.get(orderNum);
            selectedOrderNum = orderNum;
            updateButtonText(ticketIds.get(orderNum));
        } else {
            selectedOrder = null;
            selectedOrderId = null;
            selectedOrderNum = null;
            updateButtonText("");
        }
    }
}
