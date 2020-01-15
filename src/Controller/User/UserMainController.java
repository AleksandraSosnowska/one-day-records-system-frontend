package Controller.User;

import Controller.MainController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UserMainController {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void showAllTasks() {
        mainController.switchScreen("user_show_new_tasks", true);
    }

    @FXML
    public void myTasks() {
        mainController.switchScreen("user_tasks", true);
    }

    @FXML
    public void viewAccountData() {
        mainController.switchScreen("user_account_data", true);
    }

    @FXML
    public void log_out(ActionEvent actionEvent) {
        mainController.setCurrentUserId(-1);
        mainController.switchScreen("menu", true);
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

}