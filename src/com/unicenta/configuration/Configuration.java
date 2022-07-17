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

package com.unicenta.configuration;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.unicenta.forms.AppLocal;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author uniCenta
 * @date December 2016
 */
public class Configuration extends Application {


    public static void main(String[] args) {
        launch(args);
    }

// We'll handle the languages here...    
    static {
        String lang = Locale.getDefault(Locale.Category.DISPLAY).getLanguage();
        System.out.println("App language set to: " + lang);  
        
        if ("ar".equals(lang)) {
            System.setProperty("javafx.scene.nodeOrientation.RTL", "true");
        }

//        ResourceBundle resourceBundle;
//        resourceBundle = ResourceBundle.getBundle("rd_locale_en_GB", Locale.ENGLISH);       
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("database.fxml")); 
        primaryStage.getIcons().add(new Image("com/unicenta/images/favicon.png"));        
        primaryStage.setTitle("Database Configuration - v" + AppLocal.APP_VERSION);
        primaryStage.setScene(new Scene(root, 600, 600));
        setUserAgentStylesheet(STYLESHEET_MODENA);

        primaryStage.show();
        
    }

}
