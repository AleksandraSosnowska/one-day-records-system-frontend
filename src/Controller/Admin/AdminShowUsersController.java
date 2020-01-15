package Controller.Admin;

import Controller.DataBase;
import Controller.MainController;
import Controller.ShowUsersData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class AdminShowUsersController {

    @FXML
    public TableView<ShowUsersData> users_table;

    @FXML
    public TableColumn<ShowUsersData, String> name;

    @FXML
    public TableColumn <ShowUsersData, String> lastname;

    @FXML
    public TableColumn <ShowUsersData, String> login;

    @FXML
    public TableColumn <ShowUsersData, String> pesel;

    private ObservableList<ShowUsersData> users_list = FXCollections.observableArrayList();

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void backMenu() {
        mainController.switchScreen("admin_menu", true);
    }

    public void loadData(){

        try {
            DataBase.rs = DataBase.stmt.executeQuery("Select * from users_data WHERE ifAdmin is NULL");
            while(DataBase.rs.next()){
                users_list.add(new ShowUsersData(DataBase.rs.getInt("user_id"),
                        DataBase.rs.getString("name"),
                        DataBase.rs.getString("lastname"),
                        DataBase.rs.getString("username"),
                        Integer.toString(DataBase.rs.getInt("pesel"))));
            }
        } catch (SQLException e) {
            System.out.println("Brak zleceń");
        }
        setCellValuesFactory();
    }
    private void setCellValuesFactory() {
        login.setCellValueFactory(new PropertyValueFactory<>("login"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        pesel.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        users_table.setItems(users_list);
    }

}
