package Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML
    public TextField name_input;
    @FXML
    public TextField lastname_input;
    @FXML
    public TextField login_input;
    @FXML
    public TextField pesel_input;
    @FXML
    public PasswordField new_password_input;
    @FXML
    public PasswordField confirm_password_input;
    @FXML
    public TextField new_password;
    @FXML
    public TextField confirm_password;
    @FXML
    private CheckBox check_box_new_pass;
    @FXML
    private CheckBox check_box_conf_pass;
    @FXML
    public Text error_login;
    @FXML
    public Text error_password;
    @FXML
    public Text error_pesel;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        this.setVisiblePassword(null);
        error_login.setText("");
        error_password.setText("");
        error_pesel.setText("");
    }

    @FXML
    public void setVisiblePassword(ActionEvent event) {
        checkBoxShowPass(check_box_new_pass, new_password, new_password_input);
        checkBoxShowPass(check_box_conf_pass, confirm_password, confirm_password_input);
    }

    private void checkBoxShowPass(CheckBox box, TextField pass, PasswordField pass_in){
        if(box.isSelected()) {
            pass.setText(pass_in.getText());
            pass.setVisible(true);
            pass_in.setVisible(false);
            return;
        } else {
            pass_in.setText(pass.getText());
            pass_in.setVisible(true);
            pass.setVisible(false);
        }
    }

    @FXML
    public void registerAccount() throws InterruptedException {

        final int threadsCount = 3;
        Thread[] threads = new Thread[threadsCount];

        String new_name = name_input.getText();
        String new_lastname = lastname_input.getText();

        /*wczytujemy login i w tle sprawdzamy poprawność*/
        String new_login = login_input.getText();

        UsernameValidation usernameValidation = new UsernameValidation(new_login);
        threads[0] = new Thread(usernameValidation);
        threads[0].start();

        /*wczytujemy dwie wersje hasła i sprawdzamy poprawność oraz porównujemy do siebie*/
        String new_password = new_password_input.getText();
        String confirm_password = confirm_password_input.getText();

        PasswordValidation passwordValidation = new PasswordValidation(new_password, confirm_password);
        threads[1] = new Thread(passwordValidation);
        threads[1].start();

        /*wczytujemy pesel i uruchamiamy wątek walidacyjny*/
        String new_pesel = pesel_input.getText();

        PeselValidation peselValidation = new PeselValidation(new_pesel);
        threads[2] = new Thread(peselValidation);
        threads[2].start();

        for (int i = 0; i < threadsCount; ++i)
            threads[i].join();

        if (!peselValidation.isValid) error_pesel.setText("Błędny pesel");
        if (!usernameValidation.isCorrect) error_login.setText("Login powinien zawierać 8-20 liter bądź cyfr");
        if (!usernameValidation.isValid) error_login.setText("Wybrany login jest już zajęty");
        if (!passwordValidation.isCorrect) error_password.setText("Hasło musi zawierać 8-20 znaków, w tym min 1 wielką literę, 1 małą, cyfrę oraz znak specjalny");
        if (!passwordValidation.isValid) error_password.setText("Wpisane hasła nie są identyczne");
        if (peselValidation.isValid && usernameValidation.isValid && passwordValidation.isValid){
            addNewUser(new_name, new_lastname, new_login, new_password, new_pesel);
            mainController.switchScreen("menu", true);
        }


    }

    private static void addNewUser(String name, String lastname, String username, String password, String pesel){

        try {
            DataBase.preparedStatement = DataBase.connection.prepareStatement("INSERT INTO users_data (username, password, name, lastname, pesel)" + "VALUES(?, ?, ?, ?, ?)");
            DataBase.preparedStatement.setString(1, username);
            DataBase.preparedStatement.setString(2, password);
            DataBase.preparedStatement.setString(3, name);
            DataBase.preparedStatement.setString(4, lastname);
            DataBase.preparedStatement.setString(5, pesel);
            DataBase.preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Troubles with connecting to database. Please try one more time later");
            e.printStackTrace();
        }
    }

    @FXML
    public void backMenu() {
        mainController.switchScreen("user_menu", true);
    }

    static class PeselValidation implements Runnable {
        boolean isValid = false;
        String pesel;

        PeselValidation(String pesel) {
            this.pesel = pesel;
        }

        @Override
        public void run() {
            /*if (!Pattern.compile("^([0-9]{2}[02468][0-9][0-3][0-9]{6})$").matcher(pesel).matches()){
                System.out.println("pesel niepoprawny");
                return;
            }*/
            if( Pattern.compile("^[0-9]{2}[0-3][0-9][0-3][0-9]{6}$").matcher(pesel).matches()){
                int controlSum = 0;
                for (int i = 0; i < 10; ++i) {
                    switch (i % 4) {
                        case 0:
                            controlSum += Character.getNumericValue(pesel.charAt(i));
                            break;
                        case 1:
                            controlSum += 3 * Character.getNumericValue(pesel.charAt(i));
                            break;
                        case 2:
                            controlSum += 7 * Character.getNumericValue(pesel.charAt(i));
                            break;
                        case 3:
                            controlSum += 9 * Character.getNumericValue(pesel.charAt(i));
                            break;
                    }
                }
                controlSum = 10 - ( controlSum % 10 );
                isValid = controlSum % 10 == Character.getNumericValue(pesel.charAt(10));
            }
        }
    }

    static class UsernameValidation implements Runnable {
        boolean isValid = false;
        boolean isCorrect = false;
        String username;

        UsernameValidation(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            isCorrect = Pattern.compile("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9]+(?<![_.])$").matcher(username).matches();
            if (isCorrect) {
                isValid = true;
                try {
                    DataBase.rs = DataBase.stmt.executeQuery("Select * from users_data");
                    while(DataBase.rs.next()){
                        if(DataBase.rs.getString("password").equals(username)){
                            isValid = false;
                            break;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class PasswordValidation implements Runnable {
        boolean isValid = false;
        boolean isCorrect = false;
        String password1;
        String password2;

        PasswordValidation(String password1, String password2) {
            this.password1 = password1;
            this.password2 = password2;
        }

        @Override
        public void run() {
            isCorrect = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,20}$").matcher(password1).matches();
            if (isCorrect){
                isValid = password1.equals(password2);
            }
        }
    }

}
