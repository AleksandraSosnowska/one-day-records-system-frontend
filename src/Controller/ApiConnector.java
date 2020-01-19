package Controller;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ApiConnector {
	Socket socket;
	OutputStream outputStream;
	InputStream inputStream;
	PrintWriter serverOut;
	Scanner serverIn;

	public ApiConnector() throws IOException {
		socket = new Socket("149.202.31.190", 8080);
		outputStream = socket.getOutputStream();
		inputStream = socket.getInputStream();
		serverOut = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
		serverIn = new Scanner(inputStream, StandardCharsets.UTF_8);
		if (serverIn.hasNextLine())
			if (!serverIn.nextLine().equals("ready"))
				throw new RuntimeException();
	}

	public String getUserData(int userId) {
		serverOut.println("getuserdata;" + userId);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine();
		}
		return "";
	}

	public boolean validateUsername(String username) {
		serverOut.println("validateusername;" + username);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine().equals("true");
		}
		return true;
	}

	public String validLoginData(String username, String password) {
		serverOut.println("validlogindata;" + username + ';' + password);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine();
		}
		return "";
	}

	public void changeUserData(int userId, int whatToChange, String newData) {
		serverOut.println("changeuserdata;" + userId + ';' + whatToChange + ';' + newData);
		if (serverIn.hasNextLine()) {
			if (serverIn.nextLine().equals("false")) throw new RuntimeException();
		}
	}

	public void addNewTask(String hotel_name, String address, Timestamp start_date, Timestamp end_date, String amount_people_needed) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
		String start = dateFormat.format(start_date);
		String end = dateFormat.format(end_date);
		serverOut.println("addnewtask;" + hotel_name + ';' + address + ';' + start + ';' + end + ';' + amount_people_needed);
		if (serverIn.hasNextLine()) {
			if (serverIn.nextLine().equals("false")) throw new RuntimeException();
		}
	}

	public String getPass(int userId) {
		serverOut.println("getpass;" + userId);
		if (serverIn.hasNextLine()){
			return serverIn.nextLine();
		}
		return "";
	}

	public int saveToTask(int userId, int taskId) {
		serverOut.println("savetotask;" + userId + ';' + taskId);
		if (serverIn.hasNextLine()) {
			if (serverIn.nextLine().equals("true"))
				return 1;
		}
		return 0;
	}

	public String getTaskData(int taskId) {
		serverOut.println("gettaskdata;" + taskId);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine();
		}
		return "";
	}

	public boolean ifJoinYet(int userId, int taskId) {
		serverOut.println("ifjoinyet;" + userId + ';' + taskId);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine().equals("true");
		}
		return true;
	}

	public String getFutureTasksUser(int userId) {
		serverOut.println("getfuturetasksuser;" + userId);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine();
		}
		return "";
	}

	public String getFutureTasks(int userId) {
		serverOut.println("getfuturetasks;" + userId);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine();
		}
		return "";
	}

	public String getHistoryTasks(int userId) {
		serverOut.println("gethistorytasks;" + userId);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine();
		}
		return "";
	}

	public boolean deleteTaskAndRecords(int taskId) {
		serverOut.println("deletetaskandrecords;" + taskId);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine().equals("true");
		}
		return true;
	}

	public String getAllFutureTasks() {
		serverOut.println("getallfuturetasks;");
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine();
		}
		return "";
	}

	public String getNoAdminUsers() {
		serverOut.println("getnoadminusers;");
		while (serverIn.hasNextLine()) {
			return serverIn.nextLine();
		}
		return "";
	}

	public boolean updateTask(int taskId, int toChange, String newData) {

		serverOut.println("updatetask;" + taskId + ';' + toChange + ';' + newData);
		if (serverIn.hasNextLine()) {
			return serverIn.nextLine().equals("true");
		}
		return true;
	}

	void close() {
		serverOut.println("exit");
	}
}
