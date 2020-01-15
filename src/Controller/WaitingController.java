package Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WaitingController {

    @FXML
    private Text info1;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void load_data(){
        System.out.println("weszliśmy");
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (Exception e){

        }
        mainController.switchScreen("menu", false);
        /*ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        Callable<String> callableTask = () -> {
            mainController.switchScreen("menu", false);
            TimeUnit.MILLISECONDS.sleep(300);
            return "Task's execution";
        };
        long delayInSeconds = 30;
        executorService.schedule(callableTask, delayInSeconds, TimeUnit.SECONDS);
        System.out.println("tutaj");*/

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

}
