import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("fxml/MainFrame.fxml"));
		stage.setTitle("Go4Work HR Specialist");
		stage.getIcons().add(new Image("assets/pobrane.jpg"));
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}


}
