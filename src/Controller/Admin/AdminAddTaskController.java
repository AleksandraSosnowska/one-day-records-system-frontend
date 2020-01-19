package Controller.Admin;

import Controller.DataBase;
import Controller.MainController;
import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class AdminAddTaskController {

    @FXML
    public TextField hotel_input;
    @FXML
    public TextField address_input;
    @FXML
    public TextField start_input;
    @FXML
    public TextField end_input;
    @FXML
    public TextField amount_input;
    @FXML
    public Text error_start;
    @FXML
    public Text error_end;
    @FXML
    public Text error_amount;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        error_start.setText("");
        error_end.setText("");
        error_amount.setText("");
    }

    @FXML
    public void createTask() throws InterruptedException {

        final int threadsCount = 3;
        Thread[] threads = new Thread[threadsCount];

        String new_hotel = hotel_input.getText();
        String new_address = address_input.getText();

        String start_date = start_input.getText();

        DateValidation startDateValidation = new DateValidation(start_date);
        threads[0] = new Thread(startDateValidation);
        threads[0].start();

        String end_date = end_input.getText();

        DateValidation endDateValidation = new DateValidation(end_date);
        threads[1] = new Thread(endDateValidation);
        threads[1].start();

        String new_amount = amount_input.getText();

        AmountValidation amountValidation = new AmountValidation(new_amount);
        threads[2] = new Thread(amountValidation);
        threads[2].start();

        for (int i = 0; i < threadsCount; ++i)
            threads[i].join();

        if (!startDateValidation.isValid) error_start.setText("Błędny format daty!");
        if (!endDateValidation.isValid) error_end.setText("Błędny format daty!");
        if (!amountValidation.isValid) error_amount.setText("Nieprawidłowe dane!");

        if (startDateValidation.isValid && endDateValidation.isValid) {
            try {
                Timestamp startTimestamp = new Timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(start_date).getTime());
                Timestamp endTimestamp = new Timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(end_date).getTime());

                MainController.apiConnector.addNewTask(new_hotel, new_address, startTimestamp, endTimestamp, new_amount);
                mainController.switchScreen("admin_menu", true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

/*    private static void addNewTask(String hotel_name, String address, Timestamp start_date, Timestamp end_date, String amount_people_needed){

        try {
            DataBase.preparedStatement = DataBase.connection.prepareStatement("INSERT INTO tasks_data (hotel_name, address, start_date, end_date, amount_people_needed)" + "VALUES(?, ?, ?, ?, ?)");
            DataBase.preparedStatement.setString(1, hotel_name);
            DataBase.preparedStatement.setString(2, address);
            DataBase.preparedStatement.setTimestamp(3, start_date);
            DataBase.preparedStatement.setTimestamp(4, end_date);
            DataBase.preparedStatement.setInt(5, Integer.parseInt(amount_people_needed));
            DataBase.preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Troubles with connecting to database. Please try one more time later");
            e.printStackTrace();
        }
    }*/

    @FXML
    public void backMenu() {
        mainController.switchScreen("admin_menu", true);
    }

    static class DateValidation implements Runnable {
        boolean isValid = false;
        String date;

        DateValidation(String date) {
            this.date = date;
        }

        @Override
        public void run() {
            isValid = Pattern.compile("^(?=.{16}$)[0-3][0-9][-][0-1][0-9][-][0-9]{4}[ ][0-2][0-9][:][0-5][0-9]$").matcher(date).matches();
        }
    }

    static class AmountValidation implements Runnable {
        boolean isValid = false;
        String amount;

        AmountValidation(String amount) {
            this.amount = amount;
        }

        @Override
        public void run() {
            isValid = Pattern.compile("^(?=.{2}$)(?=.*[0-9])$").matcher(amount).matches();
        }
    }

}
