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

import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.unicenta.forms.AppConfig;
import com.unicenta.forms.AppLocal;
import com.unicenta.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Version;

/**
 *
 * @author uniCenta
 */
public class RemoteDisplay extends Application {

    private int width = 1024;
    private int height = 700;
    private int scrXpos = 20;
    private int scrYpos = 20;

    public static String parameter;
    public static Stage publicStage;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            parameter = args[0];
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            HibernateUtil.getSessionFactory().openSession();
        } catch (HibernateException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to connect to the database.");
            ButtonType buttonOK = new ButtonType("OK");
            alert.getButtonTypes().setAll(buttonOK);
            Optional<ButtonType> result = alert.showAndWait();
            Stage secondaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/unicenta/configuration/database.fxml"));
            secondaryStage.setTitle("Database Configuration - v" + AppLocal.APP_VERSION);
            secondaryStage.setScene(new Scene(root, 600, 500));
            setUserAgentStylesheet(STYLESHEET_MODENA);
            secondaryStage.showAndWait();
        }

        try {
            if (AppConfig.getInstance().getProperty("screen.width") != null) {
                width = Integer.parseInt(AppConfig.getInstance().getProperty("screen.width"));
            }
            if (AppConfig.getInstance().getProperty("screen.height") != null) {
                height = Integer.parseInt(AppConfig.getInstance().getProperty("screen.height"));
            }
        } catch (IllegalArgumentException e) {
            width = 1024;
            height = 768;
        }

        try {
            if (AppConfig.getInstance().getProperty("screen.xpos") != null) {
                width = Integer.parseInt(AppConfig.getInstance().getProperty("screen.width"));
            }
            if (AppConfig.getInstance().getProperty("screen.ypos") != null) {
                height = Integer.parseInt(AppConfig.getInstance().getProperty("screen.height"));
            }
        } catch (IllegalArgumentException e) {
            scrXpos = 0;
            scrYpos = 0;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("remotedisplay.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        RemoteDisplayController myController = (RemoteDisplayController) fxmlLoader.getController();

        Scene myScene = new Scene(root, width, height);

        List<Screen> allScreens = Screen.getScreens();
        if ((Boolean.valueOf(AppConfig.getInstance().getProperty("screen.secondscr")))
                && (allScreens.size() > 1)) {
            if (allScreens.size() > 1) {
                Screen secondaryScreen = allScreens.get(1);
                javafx.geometry.Rectangle2D bounds = secondaryScreen.getVisualBounds();
                Stage stage = new Stage();
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setScene(myScene);
                myController.setScene(myScene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                publicStage = stage;
                publicStage.show();
            } else {
                Stage stage = new Stage();
                stage.setX(scrXpos);
                stage.setY(scrYpos);
                stage.setScene(myScene);
                myController.setScene(myScene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                publicStage = stage;
                publicStage.show();
            }
        } else {
            primaryStage.setTitle("Remote Orders");
            primaryStage.setX(scrXpos);
            primaryStage.setY(scrYpos);
            primaryStage.setScene(myScene);
            myController.setScene(myScene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            publicStage = primaryStage;
            publicStage.show();
        }
    }
}
