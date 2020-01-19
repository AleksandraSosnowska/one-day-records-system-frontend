package Controller;

import java.sql.*;

public class DataBase {

	public static Connection connection;
	public static Statement stmt;
	public static CallableStatement stat;
	public static PreparedStatement preparedStatement;
	public static ResultSet rs;

	public void DBConnect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://149.202.31.190:3306/data", "bazodanowiec", "OlaKuc17");
			stmt = connection.createStatement();

		} catch (Exception ex) {
			System.out.println("Blad DBConnect(): " + ex);
		}
	}
/*

    static void getAllUserData(){
        try {
            stat = connection.prepareCall("{CALL GetAllUserData(?)}");
            stat.setInt(1, FirstFrame.curUserIndex);
            rs = stat.executeQuery();
            if(rs.next()) {
                System.out.println("Your current data:");
                System.out.println("Username: " + rs.getString("username") + "\n"
                        + "Password: " + rs.getString("password") + "\n"
                        + "Name: " + rs.getString("name") + "\n"
                        + "Lastname: " + rs.getString("Lastname") + "\n"
                        + "Pesel: " + rs.getInt("pesel"));
            }
        } catch (SQLException e) {
            System.out.println("Troubles with connecting to database. Please try one more time later");
            e.printStackTrace();
        }
    }*/

    /*public static void changeUserData(int user_id, int col_no, String new_data){
        try {
            stat = connection.prepareCall("{CALL ChangeUserData(?, ?, ?)}");
            stat.setInt(1, user_id);
            stat.setInt(2, col_no);
            stat.setString(3, new_data);
            rs = DataBase.stat.executeQuery();
        } catch (SQLException e) {
            System.out.println("Troubles with connecting to database. Please try one more time later");
            e.printStackTrace();
        }
    }*/
/*
    static void getAllTaskData(int task_id){
        try {
            rs = stmt.executeQuery("Select * from tasks_data where task_id = " + task_id);
            if(rs.next()) {
                System.out.println("Takes place in: " + rs.getString("hotel_name")
                        + ",\nhotel address: " + rs.getString("address")
                        + ",\ntask starts on: " + rs.getTimestamp("start_date")
                        + ",\nand end on: " + rs.getTimestamp("end_date") + "\n\n");
            }
        } catch (SQLException e) {
            System.out.println("Troubles with connecting to database. Please try one more time later");
            e.printStackTrace();
        }
    }

    static void showAllTasks() {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());

        try {
            rs = stmt.executeQuery("Select * from tasks_data");
            while(rs.next()){
                if(rs.getTimestamp("start_date").after(currentDate)){
                    if(rs.getInt("amount_people_needed") != 0){
                        System.out.println("Task's id: " + DataBase.rs.getInt("task_id"));
                        System.out.println("Takes place in: " + rs.getString("hotel_name")
                                + ",\nhotel address: " + rs.getString("address")
                                + ",\ntask starts on: " + rs.getTimestamp("start_date")
                                + ",\nand end on: " + rs.getTimestamp("end_date") + "\n");
                    }
                    else{
                        System.out.println("brak miejsc");
                    }
                }
                else{
                    System.out.println("Było! ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
*/
}