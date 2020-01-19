package Controller.Admin;

import Controller.MainController;
import Controller.ShowUsersData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

        String users = MainController.apiConnector.getNoAdminUsers();
        if (!users.equals("")) {
            String[] splittedUsers = users.split("=+");
            System.out.println(splittedUsers);
            for(int i = 0; i < splittedUsers.length; i++) {
                String[] splitted = splittedUsers[i].split("\\;+");
                users_list.add(new ShowUsersData(Integer.parseInt(splitted[0]), splitted[1],
                        splitted[2], splitted[3], splitted[4]));
            }
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
