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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import com.unicenta.forms.AppConfig;
import com.unicenta.hibernate.HibernateUtil;
import com.unicenta.utils.AltEncrypter;
import com.unicenta.utils.DirtyManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.hibernate.HibernateException;

/**
 * FXML Controller class
 *
 */
public class DatabaseController implements Initializable {

    public ComboBox<String> jcboDBDriver;
    public TextField jtxtDbDriver;
    public TextField jtxtDbURL;
    public TextField jtxtDbUser;
    public TextField jtxtDbPassword;
    public TextField jtxtDialect;
    
    public TextField jtxtWidth;
    public TextField jtxtHeight;

    public Button save;

    public TextField jtxtClockFormat;
    public Spinner historyCount;
    public CheckBox jSecondscr;
    
    public Spinner limitWait;
    public Spinner limitOverdue;
    public Spinner limitUrgent;
    public Spinner displayNumber;
    
    private final DirtyManager dirty = new DirtyManager();
    private String display;
    private AltEncrypter cypher;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    private String sLimitWait;    
    private String sLimitOverdue;    
    private String sLimitUrgent;        
    private String strHistoryCount;
    
    @FXML
    private ChoiceBox jchcExitAction;
    private Integer selectedExitActionIndex = null;
    @FXML
    private Button exit;
    @FXML
    private Label database;
    @FXML
    private Label driverclass;
    @FXML
    private Label url;
    @FXML
    private Label user;
    @FXML
    private Label password;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        jcboDBDriver.valueProperty().addListener(dirty);
        displayNumber.valueProperty().addListener(dirty);
        jtxtDbDriver.textProperty().addListener(dirty);
        jtxtDbURL.textProperty().addListener(dirty);
        jtxtDbUser.textProperty().addListener(dirty);
        jtxtDbPassword.textProperty().addListener(dirty);
        jtxtWidth.textProperty().addListener(dirty);
        jtxtHeight.textProperty().addListener(dirty);
        jtxtClockFormat.textProperty().addListener(dirty);
        historyCount.valueProperty().addListener(dirty);
        jchcExitAction.valueProperty().addListener(dirty);
        jSecondscr.selectedProperty().addListener(dirty);

        limitWait.valueProperty().addListener(dirty);
        limitOverdue.valueProperty().addListener(dirty);
        limitUrgent.valueProperty().addListener(dirty);        
        
        jcboDBDriver.setOnAction(e -> {
            if (null == jcboDBDriver.getValue()) {
                displayNumber.setValueFactory(
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("");
                jtxtDbURL.setText("");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                jtxtDialect.setText("");
            } else switch (jcboDBDriver.getValue()) {
                case "MySQL":
                    displayNumber.setValueFactory(
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                    jtxtDbDriver.setText("com.mysql.jdbc.Driver");
                    jtxtDbURL.setText("jdbc:mysql://localhost:3306/unicentaopos");
                    jtxtDbUser.setText("");
                    jtxtDbPassword.setText("");
                    jtxtDialect.setText("org.hibernate.dialect.MySQLDialect");
                    break;
                case "PostgreSQL":
                    displayNumber.setValueFactory(
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                    jtxtDbDriver.setText("org.postgresql.Driver");
                    jtxtDbURL.setText("jdbc:postgresql://localhost:5432/unicentaopos");
                    jtxtDbUser.setText("");
                    jtxtDbPassword.setText("");
                    jtxtDialect.setText("org.hibernate.dialect.PostgreSQLDialect");
                    break;
                default:
                    displayNumber.setValueFactory(
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                    jtxtDbDriver.setText("");
                    jtxtDbURL.setText("");
                    jtxtDbUser.setText("");
                    jtxtDbPassword.setText("");
                    jtxtDialect.setText("");
                    break;
            }
        });
        jtxtWidth.textProperty().addListener((ObservableValue<? 
                extends String> observable, String oldValue, String newValue) -> {
            if (!checkNumber(newValue)) {
                jtxtWidth.setText(oldValue);
            }
        });

        jtxtHeight.textProperty().addListener((ObservableValue<? 
                extends String> observable, String oldValue, String newValue) -> {
            if (!checkNumber(newValue)) {
                jtxtHeight.setText(oldValue);
            }
        });
        
        jchcExitAction.getSelectionModel()
                .selectedIndexProperty()
                .addListener((ObservableValue<? 
                        extends Number> observable, Number oldValue, Number newValue) -> {
            if (oldValue != newValue) {
                selectedExitActionIndex = newValue.intValue();
                switch(newValue.intValue()) {
                    case 0:  // Do not perform additional action
                        break;
                    case 1:  // Prompt for action
                        break;
                    case 2:  // Automatically close orders for entire kitchen
                        break;
                    case 3:  // Automatically close orders for this display only
                        break;
                }
            }
        });

	
        loadProperties();

    }

    public void loadProperties() {
        jcboDBDriver.setValue(AppConfig.getInstance().getProperty("db.engine"));
        jSecondscr.setSelected(Boolean.valueOf(AppConfig.getInstance().getProperty("screen.secondscr")));
        jtxtDbDriver.setText(AppConfig.getInstance().getProperty("db.driver"));
        jtxtDbURL.setText(AppConfig.getInstance().getProperty("db.URL"));
        jtxtDialect.setText(AppConfig.getInstance().getProperty("db.dialect"));
        jtxtWidth.setText(AppConfig.getInstance().getProperty("screen.width"));
        jtxtHeight.setText(AppConfig.getInstance().getProperty("screen.height"));
        String sDBUser = AppConfig.getInstance().getProperty("db.user");
        String sDBPassword = AppConfig.getInstance().getProperty("db.password");
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }
        jtxtDbUser.setText(sDBUser);
        jtxtDbPassword.setText(sDBPassword);

        display = (AppConfig.getInstance().getProperty("screen.displaynumber"));
        if (display == null || "".equals(display)) {
            displayNumber.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
        } else {
            displayNumber.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, Integer.parseInt(display)));
        }

        strHistoryCount = (AppConfig.getInstance().getProperty("recall.historycount"));
        if (strHistoryCount == null || "".equals(strHistoryCount)) {
            historyCount.setValueFactory( 
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 10));
        } else {
            historyCount.setValueFactory( 
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, Integer.parseInt(strHistoryCount)));
        }

/* ***********
 * LIMTS        
*/        
//  Low
        sLimitWait = (AppConfig.getInstance().getProperty("limit.wait"));
        if (sLimitWait == null || "".equals(sLimitWait)) {
            limitWait.setValueFactory( 
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 10));
        } else {
            limitWait.setValueFactory( 
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, Integer.parseInt(sLimitWait)));
        }
//  Mid
        sLimitOverdue = (AppConfig.getInstance().getProperty("limit.overdue"));
        if (sLimitOverdue == null || "".equals(sLimitOverdue)) {
            limitOverdue.setValueFactory( 
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 10));
        } else {
            limitOverdue.setValueFactory( 
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, Integer.parseInt(sLimitOverdue)));
        }
//  High
        sLimitUrgent = (AppConfig.getInstance().getProperty("limit.urgent"));
        if (sLimitUrgent == null || "".equals(sLimitUrgent)) {
            limitUrgent.setValueFactory( 
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 10));
        } else {
            limitUrgent.setValueFactory( 
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, Integer.parseInt(sLimitUrgent)));
        }
// END of Limits

        if (jtxtWidth.getText() == null || "".equals(jtxtWidth.getText())) {
            jtxtWidth.setText("1024");
            jtxtHeight.setText("768");
        }
       

        jtxtClockFormat.setText(AppConfig.getInstance().getProperty("clock.time"));
        
        String exitAction = AppConfig.getInstance().getProperty("misc.exitaction");
        if (exitAction == null || "".equals(exitAction))
            jchcExitAction.getSelectionModel().select(1);
        else
            jchcExitAction.getSelectionModel().select(Integer.parseInt(exitAction));
        
        dirty.resetDirty();
        
    }
    
    @FXML
    public void docreateTable() throws Exception {
        String sDBUser = AppConfig.getInstance().getProperty("db.user");
        String sDBPassword = AppConfig.getInstance().getProperty("db.password");

        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }
        
        String url = AppConfig.getInstance().getProperty("db.URL");
        Session session = HibernateUtil.getSessionFactory().openSession();
        SessionImpl sessionImpl = (SessionImpl)session;
        Connection connection = sessionImpl.connection();

        try {
            String changelog = "com/unicenta/configuration/OrderTable.xml";
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changelog, (ResourceAccessor)new ClassLoaderResourceAccessor(), database);
            liquibase.update("implement");
        } catch (DatabaseException changelog) {
        }     
    }

    @FXML
    public void handleSaveClick() throws IOException {
        AppConfig.getInstance().setProperty("screen.secondscr", Boolean.toString(jSecondscr.isSelected()));
        AppConfig.getInstance().setProperty("db.engine", jcboDBDriver.getValue());
        AppConfig.getInstance().setProperty("screen.displaynumber", displayNumber.getValue().toString());
        AppConfig.getInstance().setProperty("db.driver", jtxtDbDriver.getText());
        AppConfig.getInstance().setProperty("db.URL", jtxtDbURL.getText());
        AppConfig.getInstance().setProperty("db.user", jtxtDbUser.getText());
        AltEncrypter cypher = new AltEncrypter("cypherkey" + jtxtDbUser.getText());
        AppConfig.getInstance().setProperty("db.password", "crypt:" + cypher.encrypt(jtxtDbPassword.getText()));
        AppConfig.getInstance().setProperty("db.dialect", jtxtDialect.getText());
        if (Integer.parseInt(jtxtHeight.getText()) > screenSize.height) {
            jtxtHeight.setText(String.valueOf(screenSize.height));
        }

        if (Integer.parseInt(jtxtWidth.getText()) > screenSize.width) {
            jtxtWidth.setText(String.valueOf(screenSize.width));
        }

        AppConfig.getInstance().setProperty("screen.width", jtxtWidth.getText());
        AppConfig.getInstance().setProperty("screen.height", jtxtHeight.getText());
        AppConfig.getInstance().setProperty("clock.time", jtxtClockFormat.getText());

        AppConfig.getInstance().setProperty("limit.wait", limitWait.getValue().toString());        
        AppConfig.getInstance().setProperty("limit.overdue", limitOverdue.getValue().toString());        
        AppConfig.getInstance().setProperty("limit.urgent", limitUrgent.getValue().toString());                
        
        AppConfig.getInstance().setProperty("recall.historycount", historyCount.getValue().toString());
        
        
        if(selectedExitActionIndex != null) {
            AppConfig.getInstance().setProperty("misc.exitaction", selectedExitActionIndex.toString());
        }
        
        // Save the keyboard mappings
        AppConfig.getInstance().save();        
        
        dirty.resetDirty();

        Boolean error = false;
        try {
            HibernateUtil.getSessionFactory().openSession();
        } catch (HibernateException ex) {
            error = true;
        }
        if (error == false) {
            String sDBUser = AppConfig.getInstance().getProperty("db.user");
            String sDBPassword = AppConfig.getInstance().getProperty("db.password");
            if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
                cypher = new AltEncrypter("cypherkey" + sDBUser);
                sDBPassword = cypher.decrypt(sDBPassword.substring(6));
            }
            String url = AppConfig.getInstance().getProperty("db.URL");
            Session session = HibernateUtil.getSessionFactory().openSession();
            SessionImpl sessionImpl = (SessionImpl) session;

            Connection connection = sessionImpl.connection();
            try {
                String changelog = "com/unicenta/configuration/OrderTable.xml";
            } catch (HibernateException ex) {
            }
        }

    }

    @FXML
    public void handleExitClick() throws IOException {                    
        if (dirty.isDirty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Exit Configuration");
            alert.setHeaderText(null);
            alert.setContentText("Save Changes?");
            ButtonType buttonSaveExit = new ButtonType("Save & Exit");
            ButtonType buttonExit = new ButtonType("Exit");
            alert.getButtonTypes().setAll(buttonSaveExit, buttonExit);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonSaveExit) {
                handleSaveClick();
                System.exit(0);
            } else {
                System.exit(0);
            }
        }
        System.exit(0);

    }

    public Boolean checkNumber(String number) {
        if (number == null) {
            return true;
        }
        String regex = "^$|[0-9]+";
        return number.matches(regex);
    }
}