package Controller.Admin;

import Controller.MainController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminMainController {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void showAllTasks() {
        mainController.switchScreen("admin_show_tasks", true);
    }

    @FXML
    public void createNewTask() {
        mainController.switchScreen("admin_add_task", true);
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
