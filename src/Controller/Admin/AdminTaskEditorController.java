package Controller.Admin;

import Controller.DataBase;
import Controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class AdminTaskEditorController {

    @FXML
    private Text hotel_out;
    @FXML
    private Text address_out;
    @FXML
    private Text start_out;
    @FXML
    private Text end_out;
    @FXML
    private Text amount_out;
    @FXML
    private TextField change_hotel;
    @FXML
    private TextField change_address;
    @FXML
    private TextField change_start;
    @FXML
    private TextField change_end;
    @FXML
    private TextField change_amount;
    @FXML
    private Text error;
    @FXML
    private ImageView image_err1;
    @FXML
    private ImageView image_err2;
    @FXML
    private ImageView image_err3;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        error.setText("");
        image_err1.setVisible(false);
        image_err2.setVisible(false);
        image_err3.setVisible(false);
    }

    @FXML
    public void doChange() {
        initialize();
        int temp = 0;
        try{
            String new_hotel = change_hotel.getText();

            String new_address = change_address.getText();

            String new_start = change_start.getText();

            DateValidation startDateValidation = new DateValidation(new_start);
            Thread startDateThread = new Thread(startDateValidation);
            startDateThread.start();

            String new_end = change_end.getText();

            DateValidation endDateValidation = new DateValidation(new_end);
            Thread endDateThread = new Thread(endDateValidation);
            endDateThread.start();

            String new_amount = change_amount.getText();

            AmountValidation amountValidation = new AmountValidation(new_amount);
            Thread amountThread = new Thread(amountValidation);
            amountThread.start();

            if (new_hotel.length() > 1 || new_address.length() > 1 || new_start.length() > 1 || new_end.length() > 1 || new_amount.length() > 0) {

                if(new_hotel.length() > 1){
                    DataBase.preparedStatement = DataBase.connection.prepareStatement("UPDATE tasks_data SET hotel_name = ? WHERE task_id = ?");
                    DataBase.preparedStatement.setString(1, new_hotel);
                    DataBase.preparedStatement.setInt(2, mainController.getTempTaskId());
                    DataBase.preparedStatement.execute();
                    temp = 1;
                }
                if(new_address.length() > 1){
                    DataBase.preparedStatement = DataBase.connection.prepareStatement("UPDATE tasks_data SET address = ? WHERE task_id = ?");
                    DataBase.preparedStatement.setString(1, new_address);
                    DataBase.preparedStatement.setInt(2, mainController.getTempTaskId());
                    DataBase.preparedStatement.executeUpdate();
                    temp = 2;
                }
                startDateThread.join();
                if(new_start.length() > 1){
                    if(startDateValidation.isValid) {
                        DataBase.preparedStatement = DataBase.connection.prepareStatement("UPDATE tasks_data SET start_date = ? WHERE task_id = ?");
                        DataBase.preparedStatement.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(new_start).getTime()));
                        DataBase.preparedStatement.setInt(2, mainController.getTempTaskId());
                        DataBase.preparedStatement.executeUpdate();
                        temp = 3;
                    } else {
                        image_err1.setVisible(true);
                    }
                }
                endDateThread.join();
                if(new_end.length() > 1){
                    if(endDateValidation.isValid) {
                        DataBase.preparedStatement = DataBase.connection.prepareStatement("UPDATE tasks_data SET end_date = ? WHERE task_id = ?");
                        DataBase.preparedStatement.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(new_end).getTime()));
                        DataBase.preparedStatement.setInt(2, mainController.getTempTaskId());
                        DataBase.preparedStatement.executeUpdate();
                        temp = 4;
                    } else {
                        image_err2.setVisible(true);
                    }
                }
                amountThread.join();
                if(new_amount.length() > 0){
                    if(amountValidation.isValid) {
                        DataBase.preparedStatement = DataBase.connection.prepareStatement("UPDATE tasks_data SET amount_people_needed = ? WHERE task_id = ?");
                        DataBase.preparedStatement.setInt(1, Integer.parseInt(new_amount));
                        DataBase.preparedStatement.setInt(2, mainController.getTempTaskId());
                        DataBase.preparedStatement.executeUpdate();
                        temp = 5;
                    } else {
                        image_err3.setVisible(true);
                    }
                }

                /* Załadowanie strony od nowa */
                if (temp > 0)
                    mainController.switchScreen("admin_task_editor", true);
            } else {
                error.setText("Brak danych");
                System.out.println("Brak danych");
            }
        } catch (Exception e){
            error.setText("Błędne dane!");
            System.out.println("Błędne dane!");
            e.printStackTrace();
            //może np nazwisko wywołało błąd a imię się zmieniło - należy załadować opcjonalne zmiany
            mainController.switchScreen("admin_task_editor", true);
        }
    }

    @FXML
    public void backMenu() {
        mainController.switchScreen("admin_menu", true);
    }

    public void loadData(){

        error.setText("");

        try {
            DataBase.rs = DataBase.stmt.executeQuery("select * from tasks_data WHERE task_id = " + mainController.getTempTaskId());
            if(DataBase.rs.next()){
                hotel_out.setText(DataBase.rs.getString("hotel_name"));
                address_out.setText(DataBase.rs.getString("address"));
                start_out.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("start_date")));
                end_out.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("end_date")));
                amount_out.setText(Integer.toString(DataBase.rs.getInt("amount_people_needed")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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