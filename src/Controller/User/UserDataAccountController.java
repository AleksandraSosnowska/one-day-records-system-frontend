package Controller.User;

import Controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.regex.Pattern;

public class UserDataAccountController {
	@FXML
	public PasswordField old_password_input;
	@FXML
	public PasswordField new_password_input;
	@FXML
	private Text userName;
	@FXML
	private Text userLastname;
	@FXML
	private Text userLogin;
	@FXML
	private Text userPesel;
	@FXML
	private Text error;
	@FXML
	private ImageView image_err1;
	@FXML
	private ImageView image_err2;
	@FXML
	private ImageView image_err3;
	@FXML
	private TextField change_name;
	@FXML
	private TextField change_lastname;
	@FXML
	private TextField change_login;
	@FXML
	private TextField old_password;
	@FXML
	private TextField new_password;
	@FXML
	private CheckBox old_pass_toggle;
	@FXML
	private CheckBox new_pass_toggle;

	private MainController mainController;

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	@FXML
	public void doChange() {
		int temp = 0;
		try {
			String new_name = change_name.getText();
			String new_lastname = change_lastname.getText();
			String new_login = change_login.getText();
			UsernameValidation usernameValidation = new UsernameValidation(new_login);
			Thread usernameThread = new Thread(usernameValidation);
			usernameThread.start();

			String new_password = new_password_input.getText();
			String old_password = old_password_input.getText();
			PasswordValidation passwordValidation = new PasswordValidation(new_password, old_password);
			Thread passwordThread = new Thread(passwordValidation);
			passwordThread.start();

			if (new_name.length() > 1 || new_lastname.length() > 1 || new_login.length() > 1 || new_password != null) {

				/* Dokonujemy zmiany w koncie usera*/
				usernameThread.join();
				passwordThread.join();
				if (new_name.length() > 1) {
					MainController.apiConnector.changeUserData(mainController.getCurrentUserId(), 1, new_name);
					temp = 1;
				}
				if (new_lastname.length() > 1) {
					MainController.apiConnector.changeUserData(mainController.getCurrentUserId(), 2, new_lastname);
					temp = 2;
				}
				if (new_login.length() > 1) {
					if (usernameValidation.isValid) {
						MainController.apiConnector.changeUserData(mainController.getCurrentUserId(), 3, new_login);
						temp = 3;
					} else {
						image_err1.setVisible(true);
					}
				}
				if (new_password.length() > 1 || old_password.length() > 1) {
					if (passwordValidation.isCorrect) {
						image_err2.setVisible(false);
						if (passwordValidation.isValid) {
							MainController.apiConnector.changeUserData(mainController.getCurrentUserId(), 4, new_password);
							temp = 4;
						} else {
							image_err3.setVisible(true);
						}
					} else {
						error.setText("Wpisano niepoprawne hasło");
						image_err2.setVisible(true);
					}

				}
				/* Załadowanie strony od nowa */
				if (temp > 0)
					mainController.switchScreen("user_account_data", true);
			} else {
				error.setText("Brak danych");
				System.out.println("Brak danych");
			}
		} catch (Exception e) {
			error.setText("Błędne dane!");
			System.out.println("Błędne dane!");
			//może np nazwisko wywołało błąd a imię się zmieniło - należy załadować opcjonalne zmiany
			mainController.switchScreen("user_account_data", true);
		}
	}

	@FXML
	public void backMenu() {
		mainController.switchScreen("user_menu", true);
	}

	@FXML
	public void setVisiblePassword() {
		checkBoxShowPass(new_pass_toggle, new_password, new_password_input);
		checkBoxShowPass(old_pass_toggle, old_password, old_password_input);
	}

	private void checkBoxShowPass(CheckBox box, TextField pass, PasswordField pass_in) {
		if (box.isSelected()) {
			pass.setText(pass_in.getText());
			pass.setVisible(true);
			pass_in.setVisible(false);
		} else {
			pass_in.setText(pass.getText());
			pass_in.setVisible(true);
			pass.setVisible(false);
		}
	}

	public void initialize() {
		this.setVisiblePassword();
		error.setText("");
		image_err1.setVisible(false);
		image_err2.setVisible(false);
		image_err3.setVisible(false);
	}

	public void loadData() {

        String userData = MainController.apiConnector.getUserData(mainController.getCurrentUserId());
		if (userData.equals("")) throw new RuntimeException();
		String[] splittedUserData = userData.split("\\;+");

		userLogin.setText(splittedUserData[0]);
		userName.setText(splittedUserData[2]);
		userLastname.setText(splittedUserData[3]);
		userPesel.setText(splittedUserData[4]);
	}

	static class UsernameValidation implements Runnable {
		boolean isValid = false;
		boolean isCorrect = false;
		String username;

		UsernameValidation(String username) {
			this.username = username;
		}

		@Override
		public void run() {
			isCorrect = Pattern.compile("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9]+(?<![_.])$").matcher(username).matches();
			if (isCorrect) {
				isValid = !MainController.apiConnector.validateUsername(username);
			}
		}
	}

	class PasswordValidation implements Runnable {
		boolean isValid = false;
		boolean isCorrect = false;
		String new_pass;
		String old_pass;

		PasswordValidation(String new_pass, String old_pass) {
			this.new_pass = new_pass;
			this.old_pass = old_pass;
		}

		@Override
		public void run() {

			/*try {
				DataBase.rs = DataBase.stmt.executeQuery("Select password from users_data WHERE user_id = " + mainController.getCurrentUserId());
				if (DataBase.rs.next()) {
					if (DataBase.rs.getString("password").equals(old_pass)) {
						isCorrect = true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}*/

			isCorrect = MainController.apiConnector.getPass(mainController.getCurrentUserId()).equals(old_pass);

			isValid = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,20}$").matcher(new_pass).matches();
		}
	}
}