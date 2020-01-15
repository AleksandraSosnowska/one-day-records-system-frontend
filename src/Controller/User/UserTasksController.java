package Controller.User;

import Controller.DataBase;
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

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class UserTasksController {

    @FXML
    public Text screenHeader;

    @FXML
    public TableView<ShowTasksData> tasks_table;
    @FXML
    public TableColumn<ShowTasksData, Integer> task_id;
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
        try {
            DataBase.rs = DataBase.stmt.executeQuery("Select * from tasks_data join records on records.task_id = tasks_data.task_id where records.user_id = " + mainController.getCurrentUserId());
            while (DataBase.rs.next()) {

                if (DataBase.rs.getInt("amount_people_needed") > 0 && DataBase.rs.getTimestamp("start_date").before(new Timestamp(System.currentTimeMillis()))) {

                    task_list.add(new ShowTasksData(DataBase.rs.getInt("task_id"),
                            DataBase.rs.getString("hotel_name"),
                            DataBase.rs.getString("address"),
                            new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("start_date")),
                            new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("end_date")),
                            Integer.toString(DataBase.rs.getInt("amount_people_needed"))));
                }
            }
        } catch (SQLException e) {
            System.out.println("Brak wizyt");
        }

        setCellValuesFactory();
    }


    public void loadData(){
        task_list.clear();
        buttonShowNew.setVisible(false);
        buttonShowOld.setVisible(true);
        screenHeader.setText("Zlecenia do realizacji");
        try {

            DataBase.rs = DataBase.stmt.executeQuery("Select * from tasks_data join records on records.task_id = tasks_data.task_id where records.user_id = " + mainController.getCurrentUserId());
            while(DataBase.rs.next()){
                if(DataBase.rs.getInt("amount_people_needed") > 0 && DataBase.rs.getTimestamp("start_date").after(new Timestamp(System.currentTimeMillis()))){
                    task_list.add(new ShowTasksData(DataBase.rs.getInt("task_id"),
                            DataBase.rs.getString("hotel_name"),
                            DataBase.rs.getString("address"),
                            new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("start_date")),
                            new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("end_date")),
                            Integer.toString(DataBase.rs.getInt("amount_people_needed"))));
                }
            }
        } catch (SQLException e) {
            System.out.println("Brak zleceń");
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
