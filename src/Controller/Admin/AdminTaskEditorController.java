package Controller.Admin;

import Controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
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
                    if (MainController.apiConnector.updateTask(mainController.getTempTaskId(), 1, new_hotel))
                        temp = 1;
                }
                if(new_address.length() > 1){
                    if (MainController.apiConnector.updateTask(mainController.getTempTaskId(), 2, new_address))
                        temp = 2;
                }
                startDateThread.join();
                if(new_start.length() > 1){
                    if(startDateValidation.isValid) {
                        if (MainController.apiConnector.updateTask(mainController.getTempTaskId(), 3, new_start))
                            temp = 3;
                    } else {
                        image_err1.setVisible(true);
                    }
                }
                endDateThread.join();
                if(new_end.length() > 1){
                    if(endDateValidation.isValid) {
                        if (MainController.apiConnector.updateTask(mainController.getTempTaskId(), 4, new_end))
                            temp = 4;
                    } else {
                        image_err2.setVisible(true);
                    }
                }
                amountThread.join();
                if(new_amount.length() > 0){
                    if(amountValidation.isValid) {
                        if (MainController.apiConnector.updateTask(mainController.getTempTaskId(), 5, new_amount))
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

        String taskData = MainController.apiConnector.getTaskData(mainController.getTempTaskId());
        if (taskData.equals("")) throw new RuntimeException();
        String[] splittedTaskData = taskData.split("\\;+");
        hotel_out.setText(splittedTaskData[0]);
        address_out.setText(splittedTaskData[1]);
        start_out.setText(splittedTaskData[2]);
        end_out.setText(splittedTaskData[3]);
        amount_out.setText(splittedTaskData[4]);

    }

    class DateValidation implements Runnable {
        boolean isValid = false;
        String date;

        DateValidation(String date) {
            this.date = date;
        }

        @Override
        public synchronized void run() {
            isValid = Pattern.compile("^(?=.{16}$)[0-3][0-9][-][0-1][0-9][-][0-9]{4}[ ][0-2][0-9][:][0-5][0-9]$").matcher(date).matches();
            if (!isValid){
                error.setText("Błędny format daty");
            }
        }
    }

    class AmountValidation implements Runnable {
        boolean isValid = false;
        String amount;

        AmountValidation(String amount) {
            this.amount = amount;
        }

        @Override
        public synchronized void run() {

            isValid = Pattern.compile("^([0-9]{1,2})$").matcher(amount).matches();
            if (!isValid)
                error.setText("Błędna liczba osób");
        }
    }
}