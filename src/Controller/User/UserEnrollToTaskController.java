package Controller.User;

import Controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class UserEnrollToTaskController {

    @FXML
    private Text hotelName;

    @FXML
    private Text address;

    @FXML
    private Text startDate;

    @FXML
    private Text endDate;

    @FXML
    private Text error;

    @FXML
    private TextField show_password;

    @FXML
    private CheckBox pass_toggle;

    @FXML
    private PasswordField password_input;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private int pasTries = 3;

    @FXML
    public void enrollTask() {
        String new_password = readPassword();

        if(new_password.length() > 1){
            if(new_password.equals(MainController.apiConnector.getPass(mainController.getCurrentUserId()))){
                if ( MainController.apiConnector.saveToTask(mainController.getCurrentUserId(), mainController.getTempTaskId()   ) == 1){
                    System.out.println("Correctly enrolled to this task");
                    mainController.switchScreen("user_tasks", true);
                }
                else{
                    System.out.println("Sorry, there are troubles with enrolling you to this task");
                }
            } else {
                pasTries -= 1;
                error.setText("Błędne hasło, pozostało " + pasTries + " prób");
                if(pasTries == 0)
                    mainController.switchScreen("user_show_new_tasks", true);
            }
        } else {
            error.setText("Wprowadź hasło");
        }
    }

    private String readPassword(){
        if ( password_input != null ){
            return password_input.getText();
        }
        if ( show_password != null ){
            return show_password.getText();
        }
        return null;
    }

    /*private String getUserPassword(){
        try {
            DataBase.rs = DataBase.stmt.executeQuery("select * from users_data where user_id = " + mainController.getCurrentUserId());
            if (DataBase.rs.next())
                return DataBase.rs.getString("password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }*/

    /*private int saveMeToTask(){
        try{

            DataBase.preparedStatement = DataBase.connection.prepareStatement("INSERT INTO records (user_id, task_id)" + "VALUES(?, ?)");
            DataBase.preparedStatement.setInt(1, mainController.getCurrentUserId());
            DataBase.preparedStatement.setInt(2, mainController.getTempTaskId());
            DataBase.preparedStatement.execute();

            DataBase.stat = DataBase.connection.prepareCall("{CALL changePeopleNum(?)}");
            DataBase.stat.setInt(1, mainController.getTempTaskId());
            DataBase.rs = DataBase.stat.executeQuery();

            return 1;

        } catch (SQLException e) {
            System.out.println("Sorry, program encountered some problems. Please try again later");
            e.printStackTrace();
        }
        return 0;
    }*/

    @FXML
    public void backMenu() {
        mainController.switchScreen("user_menu", true);
    }

    @FXML
    public void togglevisiblePassword() {
        if (pass_toggle.isSelected()) {
            show_password.setText(password_input.getText());
            show_password.setVisible(true);
            password_input.setVisible(false);
            return;
        }
        password_input.setText(show_password.getText());
        password_input.setVisible(true);
        show_password.setVisible(false);
    }

    public void loadData(){
        this.togglevisiblePassword();
        error.setText("");

        /*try {
            DataBase.rs = DataBase.stmt.executeQuery("select * from tasks_data where task_id = " + mainController.getTempTaskId());
            if(DataBase.rs.next()){
                hotelName.setText(DataBase.rs.getString("hotel_name"));
                address.setText(DataBase.rs.getString("address"));
                startDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("start_date")));
                endDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("end_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        String taskData = MainController.apiConnector.getTaskData(mainController.getTempTaskId());
        if (taskData.equals("")) throw new RuntimeException();
        String[] splittedTaskData = taskData.split("\\;+");

        hotelName.setText(splittedTaskData[0]);
        address.setText(splittedTaskData[1]);
        startDate.setText(splittedTaskData[2]);
        endDate.setText(splittedTaskData[3]);



    }
}




