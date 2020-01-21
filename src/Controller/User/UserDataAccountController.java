package Controller.User;

import Controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.concurrent.*;
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
		initialize();
		int temp = 0;
		ExecutorService executor = Executors.newFixedThreadPool(2);

		try {
			String new_name = change_name.getText();
			String new_lastname = change_lastname.getText();
			String new_login = change_login.getText();
			UsernameValidation usernameValidation = new UsernameValidation(new_login);
			FutureTask<Boolean> usernameTask = new FutureTask<>(usernameValidation);
			executor.submit(usernameTask);

			String new_password = new_password_input.getText();
			String old_password = old_password_input.getText();
			PasswordValidation passwordValidation = new PasswordValidation(new_password, old_password);
			FutureTask<Boolean> passwordTask = new FutureTask<>(passwordValidation);
			executor.submit(passwordTask);

			if (new_name.length() > 1 || new_lastname.length() > 1 || new_login.length() > 1 || new_password != null) {

				/* Dokonujemy zmiany w koncie usera*/

				if (new_name.length() > 1) {
					MainController.apiConnector.changeUserData(mainController.getCurrentUserId(), 1, new_name);
					temp = 1;
				}
				if (new_lastname.length() > 1) {
					MainController.apiConnector.changeUserData(mainController.getCurrentUserId(), 2, new_lastname);
					temp = 2;
				}
				if (usernameTask.get(250, TimeUnit.MILLISECONDS)) {
						MainController.apiConnector.changeUserData(mainController.getCurrentUserId(), 3, new_login);
						temp = 3;
				}
				if (passwordTask.get(250, TimeUnit.MILLISECONDS)) {
					MainController.apiConnector.changeUserData(mainController.getCurrentUserId(), 4, new_password);
					temp = 4;
				}
				executor.shutdown();

				/* Załadowanie strony od nowa */
				if (temp > 0)
					mainController.switchScreen("user_account_data", true);
			} else {
				error.setText("Brak danych");
			}
		} catch (Exception e) {
			error.setText("Błędne dane!");
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


	class UsernameValidation implements Callable<Boolean> {
		String username;

		UsernameValidation(String username) {
			this.username = username;
		}

		@Override
		public synchronized Boolean call() {
			boolean isValid = false;
			if (username.length() > 0) {

				isValid = Pattern.compile("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9]+(?<![_.])$").matcher(username).matches();
				if (isValid) {
					isValid = !MainController.apiConnector.validateUsername(username);
					if (!isValid) error.setText("Login niedostępny");
				} else {
					error.setText("Niepoprawny login");
				}
				if (!isValid) image_err1.setVisible(true);
			}
			return isValid;
		}
	}

	class PasswordValidation implements Callable<Boolean> {
		String new_pass;
		String old_pass;

		PasswordValidation(String new_pass, String old_pass) {
			this.new_pass = new_pass;
			this.old_pass = old_pass;
		}

		@Override
		public synchronized Boolean call() {
			boolean isValid = false;
			if (new_pass.length() > 0 || old_pass.length() > 0) {

				isValid = MainController.apiConnector.getPass(mainController.getCurrentUserId()).equals(old_pass);
				if (isValid) {
					image_err2.setVisible(false);

					isValid = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,20}$").matcher(new_pass).matches();
					if (!isValid) {
						error.setText("Nowe hasło nie spełnia wymogów");
						image_err3.setVisible(true);
					}

				} else {
					error.setText("Wpisano niepoprawne hasło");
					image_err2.setVisible(true);
				}
			}
			return (isValid);
		}
	}
}