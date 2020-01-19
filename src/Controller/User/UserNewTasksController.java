package Controller.User;

import Controller.MainController;
import Controller.ShowTasksData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Timestamp;

public class UserNewTasksController {

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

    @FXML
    public Text error;

    private ObservableList<ShowTasksData> task_list = FXCollections.observableArrayList();

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        error.setText("");
    }


    @FXML
    public void backMenu() {
        mainController.switchScreen("user_menu", true);
    }

    @FXML
    public void enrollToTask(){

        try {
            ShowTasksData selected = tasks_table.getSelectionModel().getSelectedItem();
            System.out.println("Zaznaczone zlecenie: " + selected);
            //sprawdzam, czy zostało coś zaznaczone
            if (selected.getTask_id() > 0) {
                //sprawdzam, czy nie jestem już na to zapisana - proforma
                if( !MainController.apiConnector.ifJoinYet(mainController.getCurrentUserId(), selected.getTask_id()) ){
                    //sprawdzam, czy są jeszcze wolne miejsca
                    if(!selected.getPeopleNeeded().equals("0")){
                        //jest git, można podjąć próbę zapisu
                        mainController.setTempTaskId(selected.getTask_id());
                        mainController.switchScreen("user_enroll_to_task", true);
                    }
                }
            }
        } catch (NullPointerException e){
            System.out.println("Nic nie zaznaczono");
        }
    }

    /*private boolean ifJoinYet(int taskId){

        try {
            DataBase.preparedStatement = DataBase.connection.prepareStatement("Select * from records where task_id = ? AND user_id = ?");
            DataBase.preparedStatement.setInt(1, taskId);
            DataBase.preparedStatement.setInt(2, mainController.getCurrentUserId());
            DataBase.rs = DataBase.preparedStatement.executeQuery();

            if(DataBase.rs.next()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }*/

    public void loadData(){

        /*try {
            DataBase.preparedStatement = DataBase.connection.prepareStatement("Select * from tasks_data WHERE tasks_data.task_id NOT IN (SELECT task_id FROM records WHERE user_id = ?)");
            DataBase.preparedStatement.setInt(1, mainController.getCurrentUserId());
            DataBase.rs = DataBase.preparedStatement.executeQuery();
            while(DataBase.rs.next()){
                if(DataBase.rs.getInt("amount_people_needed") > 0){
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
        }*/

        String tasks = MainController.apiConnector.getFutureTasksUser(mainController.getCurrentUserId());
        if (!tasks.equals("")) {
            String[] splittedTasksData = tasks.split("\n+"); //dzielenie po enterze

            for(int i = 0; i < splittedTasksData.length; i++){
                String[] splittedTask = splittedTasksData[i].split("\\;+"); //dzielenie po średniku
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
        peopleNeeded.setCellValueFactory(new PropertyValueFactory<>("peopleNeeded"));
        tasks_table.setItems(task_list);
    }

}
