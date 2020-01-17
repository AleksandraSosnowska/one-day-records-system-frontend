package Controller;

import Controller.Admin.AdminTaskEditorController;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    private TextField pass_text;

    @FXML
    private CheckBox pass_toggle;

    @FXML
    private PasswordField password_input;

    @FXML
    private TextField login_input;

    @FXML
    private Text info;

    @FXML
    private Text info1;

    @FXML
    private Button loginButton;

    private int triesAmount;

    private MainController mainController;

    public void initialize() {
        this.togglevisiblePassword(null);
        info.setText(" ");
        info1.setText(" ");
        loginButton.setVisible(true);
        triesAmount = 0;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void togglevisiblePassword(ActionEvent event) {
        if (pass_toggle.isSelected()) {
            pass_text.setText(password_input.getText());
            pass_text.setVisible(true);
            password_input.setVisible(false);
        } else {
            password_input.setText(pass_text.getText());
            password_input.setVisible(true);
            pass_text.setVisible(false);
        }
    }

    @FXML
    public void log_into_system() {

        String login = login_input.getText();
        String password = getPassword();

        if (login.length() > 0 && password != null) {

            if (validLoginData(login, password)) {
                if (mainController.getIfAdmin() == 1)
                    mainController.switchScreen("admin_menu", true);
                else {
                    mainController.switchScreen("user_menu", true);
                }
            } else {
                triesAmount += 1;
                if(triesAmount == 3){

                    delayAction();
                } else {
                    info.setText("Błędny login lub hasło");
                }
                password_input.clear();
                pass_text.clear();
                login_input.clear();

            }

        } else {
            info.setText("Uzupełnij pola tekstowe");
        }
    }

    private String getPassword(){
        if ( password_input != null ){
            return password_input.getText();
        }
        if ( pass_text != null ){
            return pass_text.getText();
        }
        return null;
    }

    private boolean validLoginData(String login, String password){
        try {
            DataBase.rs = DataBase.stmt.executeQuery("Select * from users_data");
            while(DataBase.rs.next()){

                if (DataBase.rs.getString("username").equals(login) &&
                        DataBase.rs.getString("password").equals(password)) {

                    mainController.setIfAdmin(DataBase.rs.getInt("ifAdmin"));
                    mainController.setCurrentUserId(DataBase.rs.getInt("user_id"));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    public void registration(){
        mainController.switchScreen("registration_menu", true);
    }

    @FXML
    public void go_out() {
        Platform.exit();
        System.exit(0);
    }

    public void delayAction(){
        info1.setText("Zbyt dużo błędnych prób, spróbuj ponownie za 30s");
        info.setText("");
        loginButton.setVisible(false);
        PauseTransition wait = new PauseTransition(Duration.seconds(30));
        wait.setOnFinished((e) -> {
            /*YOUR METHOD*/
            loginButton.setVisible(true);
            info1.setText("");
        });
        wait.play();

    }
}
