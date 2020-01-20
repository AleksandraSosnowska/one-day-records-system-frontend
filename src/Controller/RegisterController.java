package Controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class RegisterController {
	private boolean correctData = true;
	@FXML
	public TextField name_input;
	@FXML
	public TextField lastname_input;
	@FXML
	public TextField login_input;
	@FXML
	public TextField pesel_input;
	@FXML
	public PasswordField new_password_input;
	@FXML
	public PasswordField confirm_password_input;
	@FXML
	public TextField new_password;
	@FXML
	public TextField confirm_password;
	@FXML
	public Text error_login;
	@FXML
	public Text error_password;
	@FXML
	public Text error_pesel;
	@FXML
	private CheckBox check_box_new_pass;
	@FXML
	private CheckBox check_box_conf_pass;

	private MainController mainController;

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	public void initialize() {
		this.setVisiblePassword(null);
		error_login.setText("");
		error_password.setText("");
		error_pesel.setText("");
	}

	@FXML
	public void setVisiblePassword(ActionEvent event) {
		checkBoxShowPass(check_box_new_pass, new_password, new_password_input);
		checkBoxShowPass(check_box_conf_pass, confirm_password, confirm_password_input);
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

	@FXML
	public void registerAccount() throws InterruptedException {

		ExecutorService exec = Executors.newFixedThreadPool(3);

		String new_name = name_input.getText();
		String new_lastname = lastname_input.getText();

		/*wczytujemy login i w tle sprawdzamy poprawność*/
		String new_login = login_input.getText();

		UsernameValidation usernameValidation = new UsernameValidation(new_login);
		exec.execute(usernameValidation);

		/*wczytujemy dwie wersje hasła i sprawdzamy poprawność oraz porównujemy do siebie*/
		String new_password = new_password_input.getText();
		String confirm_password = confirm_password_input.getText();

		PasswordValidation passwordValidation = new PasswordValidation(new_password, confirm_password);
		exec.execute(passwordValidation);

		/*wczytujemy pesel i uruchamiamy wątek walidacyjny*/
		String new_pesel = pesel_input.getText();

		PeselValidation peselValidation = new PeselValidation(new_pesel);
		exec.execute(peselValidation);

		exec.shutdown();
		try {
			exec.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException exc) {
			exc.printStackTrace();
		}

		if (correctData) {
			MainController.apiConnector.addNewUser(new_login, new_password, new_name, new_lastname, new_pesel);
			mainController.switchScreen("menu", true);
		}
	}

	@FXML
	public void backMenu() {
		mainController.switchScreen("menu", true);
	}

	class PeselValidation implements Runnable {
		boolean isValid = false;
		String pesel;

		PeselValidation(String pesel) {
			this.pesel = pesel;
		}

		@Override
		public synchronized void run() {
			if (Pattern.compile("^[0-9]{2}[0-3][0-9][0-3][0-9]{6}$").matcher(pesel).matches()) {
				int controlSum = 0;
				for (int i = 0; i < 10; ++i) {
					switch (i % 4) {
						case 0:
							controlSum += Character.getNumericValue(pesel.charAt(i));
							break;
						case 1:
							controlSum += 3 * Character.getNumericValue(pesel.charAt(i));
							break;
						case 2:
							controlSum += 7 * Character.getNumericValue(pesel.charAt(i));
							break;
						case 3:
							controlSum += 9 * Character.getNumericValue(pesel.charAt(i));
							break;
					}
				}
				controlSum = 10 - (controlSum % 10);
				isValid = controlSum % 10 == Character.getNumericValue(pesel.charAt(10));
			}
			if (!isValid) error_pesel.setText("Błędny pesel");
			correctData = (correctData && isValid);
		}
	}

	class UsernameValidation implements Runnable {
		boolean isValid = false;
		String username;

		UsernameValidation(String username) {
			this.username = username;
		}

		@Override
		public synchronized void run() {
			isValid = Pattern.compile("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9]+(?<![_.])$").matcher(username).matches();
			if (isValid) {
				isValid = !MainController.apiConnector.validateUsername(username);
				if (!isValid) error_login.setText("Wybrany login jest już zajęty");
			} else {
				error_login.setText("Login powinien zawierać 8-20 liter bądź cyfr");
			}
			correctData = (correctData && isValid);
		}
	}

	class PasswordValidation implements Runnable {
		boolean isValid = false;
		String password1;
		String password2;

		PasswordValidation(String password1, String password2) {
			this.password1 = password1;
			this.password2 = password2;
		}

		@Override
		public synchronized void run() {
			isValid = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,20}$").matcher(password1).matches();
			if (isValid) {

				isValid = password1.equals(password2);
				if (!isValid) error_password.setText("Wpisane hasła nie są identyczne");
			} else {
				error_password.setText("Hasło musi zawierać 8-20 znaków, w tym min 1 wielką literę, 1 małą, cyfrę oraz znak specjalny");
			}
			correctData = (correctData && isValid);
		}
	}

}
