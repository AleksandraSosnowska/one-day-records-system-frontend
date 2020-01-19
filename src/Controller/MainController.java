package Controller;

import Controller.Admin.*;
import Controller.User.*;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

	public static ApiConnector apiConnector;
	private int currentUserId;
	private int ifAdmin;
	private int tempTaskId;
	@FXML
	private StackPane MainStackPane;

	public static void FadeIn(int time, Node X) {
		FadeTransition fadein = new FadeTransition();
		fadein.setDuration(Duration.millis(time));
		fadein.setNode(X);
		fadein.setFromValue(0);
		fadein.setToValue(1);
		fadein.play();
	}

	@FXML
	public void initialize() throws IOException {
		apiConnector = new ApiConnector();
        /*DataBase connect = new DataBase();
        connect.DBConnect();*/
		switchScreen("menu", true);
	}

	public void setScreen(Pane pane) {
		MainStackPane.getChildren().clear();
		MainStackPane.getChildren().add(pane);
	}

	public FXMLLoader getFXMLLoader(String loaderResource) {

		FXMLLoader loader = null;
		if (loaderResource.equals("menu"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/LoginFrame.fxml"));

		if (loaderResource.equals("registration_menu"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/RegisterFrame.fxml"));

		if (loaderResource.equals("user_menu"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/User/UserMainScreen.fxml"));
		if (loaderResource.equals("user_account_data"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/User/UserDataAccountScreen.fxml"));
		if (loaderResource.equals("user_tasks"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/User/UserTasksScreen.fxml"));
		if (loaderResource.equals("user_show_new_tasks"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/User/UserNewTasksScreen.fxml"));
		if (loaderResource.equals("user_show_task_details"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/User/UserShowTaskDetailsScreen.fxml"));
		if (loaderResource.equals("user_enroll_to_task"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/User/UserEnrollToTaskScreen.fxml"));

		if (loaderResource.equals("admin_menu"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/Admin/AdminMainScreen.fxml"));
		if (loaderResource.equals("admin_add_task"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/Admin/AdminAddTaskScreen.fxml"));
		if (loaderResource.equals("admin_show_tasks"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/Admin/AdminShowTasksScreen.fxml"));
		if (loaderResource.equals("admin_task_editor"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/Admin/AdminTaskEditorScreen.fxml"));
		if (loaderResource.equals("admin_show_users"))
			loader = new FXMLLoader(this.getClass().getResource("/fxml/Admin/AdminShowUsersScreen.fxml"));

		return loader;
	}

	public void switchScreen(String loaderResource, boolean fadeAnimation) {

		FXMLLoader loader = getFXMLLoader(loaderResource);
		Pane pane = null;

		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (loaderResource.equals("menu")) {
			LoginController menuController = loader.getController();
			menuController.setMainController(this);
		}

		if (loaderResource.equals("registration_menu")) {
			RegisterController menuController = loader.getController();
			menuController.setMainController(this);
		}

		if (loaderResource.equals("user_menu")) {
			UserMainController menuController = loader.getController();
			menuController.setMainController(this);
		}

		if (loaderResource.equals("user_account_data")) {
			UserDataAccountController menuController = loader.getController();
			menuController.setMainController(this);
			menuController.loadData();
		}

		if (loaderResource.equals("user_tasks")) {
			UserTasksController menuController = loader.getController();
			menuController.setMainController(this);
			menuController.loadData();
		}

		if (loaderResource.equals("user_show_new_tasks")) {
			UserNewTasksController menuController = loader.getController();
			menuController.setMainController(this);
			menuController.loadData();
		}

		if (loaderResource.equals("user_enroll_to_task")) {
			UserEnrollToTaskController menuController = loader.getController();
			menuController.setMainController(this);
			menuController.loadData();
		}

		if (loaderResource.equals("admin_menu")) {
			AdminMainController menuController = loader.getController();
			menuController.setMainController(this);
		}

		if (loaderResource.equals("admin_add_task")) {
			AdminAddTaskController menuController = loader.getController();
			menuController.setMainController(this);
		}

		if (loaderResource.equals("admin_show_tasks")) {
			AdminShowTasksController menuController = loader.getController();
			menuController.setMainController(this);
			menuController.loadData();
		}

		if (loaderResource.equals("admin_task_editor")) {
			AdminTaskEditorController menuController = loader.getController();
			menuController.setMainController(this);
			menuController.loadData();
		}

		if (loaderResource.equals("admin_show_users")) {
			AdminShowUsersController menuController = loader.getController();
			menuController.setMainController(this);
			menuController.loadData();
		}

		setScreen(pane);
		if (fadeAnimation) {
			FadeIn(1000, MainStackPane.getChildren().get(0));
		}
	}


	public int getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}

	public int getIfAdmin() {
		return ifAdmin;
	}

	public void setIfAdmin(int ifAdmin) {
		this.ifAdmin = ifAdmin;
	}

	public int getTempTaskId() {
		return tempTaskId;
	}

	public void setTempTaskId(int tempTaskId) {
		this.tempTaskId = tempTaskId;
	}
}
