package Controller.User;

import Controller.MainController;
import Controller.ShowTasksData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Timestamp;

public class UserTasksController {

    @FXML
    public Text screenHeader;

    @FXML
    public TableView<ShowTasksData> tasks_table;
    @FXML
    public TableColumn <ShowTasksData, String> hotelName;
    @FXML
    public TableColumn <ShowTasksData, String> address;
    @FXML
    public TableColumn <ShowTasksData, Timestamp> startDate;
    @FXML
    public TableColumn <ShowTasksData, Timestamp> endDate;

    private ObservableList<ShowTasksData> task_list = FXCollections.observableArrayList();

    @FXML
    public Button buttonShowOld;
    @FXML
    public Button buttonShowNew;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void backMenu() {
        mainController.switchScreen("user_menu", true);
    }

    @FXML
    public void showPastTasks(){

        task_list.clear();

        buttonShowNew.setVisible(true);
        buttonShowOld.setVisible(false);
        screenHeader.setText("Zrealizowane zlecenia");

        String tasks = MainController.apiConnector.getHistoryTasks(mainController.getCurrentUserId());
        splitReceivedData(tasks);
    }


    public void loadData(){
        task_list.clear();
        buttonShowNew.setVisible(false);
        buttonShowOld.setVisible(true);
        screenHeader.setText("Zlecenia do realizacji");

        String tasks = MainController.apiConnector.getFutureTasks(mainController.getCurrentUserId());
        splitReceivedData(tasks);
    }

    private void splitReceivedData(String tasks) {
        if (!tasks.equals("")) {
            System.out.println(tasks);
            String[] splittedTasksData = tasks.split("\\=+");

            for(int i = 0; i < splittedTasksData.length; i++){
                System.out.println(splittedTasksData[i]);
                String[] splittedTask = splittedTasksData[i].split("\\;+");
                task_list.add(new ShowTasksData(Integer.parseInt(splittedTask[0]), splittedTask[1], splittedTask[2], splittedTask[3], splittedTask[4], splittedTask[5]));
            }
        }

        setCellValuesFactory();
    }

    private void setCellValuesFactory() {
        hotelName.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tasks_table.setItems(task_list);
    }

}
