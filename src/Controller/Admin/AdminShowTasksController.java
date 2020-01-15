package Controller.Admin;

import Controller.DataBase;
import Controller.MainController;
import Controller.ShowTasksData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class AdminShowTasksController {

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

    @FXML
    public TableColumn <ShowTasksData, String> peopleNeeded;

    private ObservableList<ShowTasksData> task_list = FXCollections.observableArrayList();

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void backMenu() {
        mainController.switchScreen("admin_menu", true);
    }

    @FXML
    public void editTask() {
        try {
            ShowTasksData selected = tasks_table.getSelectionModel().getSelectedItem();
            if (selected.getTask_id() > 0) {
                mainController.setTempTaskId(selected.getTask_id());
                mainController.switchScreen("admin_task_editor", true);
            } else {
                System.out.println("Nic nie zaznaczono");
            }
        } catch (Exception e){
            System.out.println("nic nie zaznaczono");
        }
    }

    @FXML
    public void deleteTask(){

        try {
            ShowTasksData selected = tasks_table.getSelectionModel().getSelectedItem();

            if (selected.getTask_id() > 0) {

                DataBase.rs = DataBase.stmt.executeQuery("SELECT * FROM records WHERE task_id = " + selected.getTask_id());
                while (DataBase.rs.next()){
                    DataBase.preparedStatement = DataBase.connection.prepareStatement("DELETE FROM records WHERE record_id = ?");
                    DataBase.preparedStatement.setInt(1, DataBase.rs.getInt("record_id"));
                    DataBase.preparedStatement.execute();
                }

                DataBase.preparedStatement = DataBase.connection.prepareStatement("DELETE FROM tasks_data WHERE task_id = ?");
                DataBase.preparedStatement.setInt(1, selected.getTask_id());
                DataBase.preparedStatement.execute();

                mainController.switchScreen("admin_show_tasks", true);

            }
        } catch (NullPointerException e){
            System.out.println("Nic nie zaznaczono");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void loadData(){

        try {
            DataBase.rs = DataBase.stmt.executeQuery("Select * from tasks_data");
            while(DataBase.rs.next()){
                task_list.add(new ShowTasksData(DataBase.rs.getInt("task_id"),
                            DataBase.rs.getString("hotel_name"),
                            DataBase.rs.getString("address"),
                            new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("start_date")),
                            new SimpleDateFormat("dd-MM-yyyy HH:mm").format(DataBase.rs.getTimestamp("end_date")),
                            Integer.toString(DataBase.rs.getInt("amount_people_needed"))));
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
        peopleNeeded.setCellValueFactory(new PropertyValueFactory<>("peopleNeeded"));
        tasks_table.setItems(task_list);
    }

}
