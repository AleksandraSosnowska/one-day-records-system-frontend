package Controller;

public class ShowUsersData {

    private int user_id;
    private String name;
    private String lastname;
    private String login;
    private String pesel;
    //private int tasks_amount;

    public ShowUsersData(int user_id, String name, String lastname, String login, String pesel){
        this.name = name;
        this.lastname = lastname;
        this.login = login;
        this.pesel = pesel;
        this.user_id = user_id;
        //this.tasks_amount = tasks_amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

 /*   public int getTasks_amount() {
        return tasks_amount;
    }

    public void setTasks_amount(int tasks_amount) {
        this.tasks_amount = tasks_amount;
    }*/

    @Override
    public String toString() {
        return "ShowUsersData {" +
                " user_id = " + user_id +
                ", name = " + name +
                ", lastname = " + lastname +
                ", login: " + login +
                ", pesel = " + pesel +
                '}';
    }
}
