package Controller;


public class ShowTasksData {

    private int task_id;
    private String hotelName;
    private String address;
    private String startDate;
    private String endDate;
    private String peopleNeeded;

    public ShowTasksData(int task_id, String hotelName, String address,
                         String startDate, String endDate, String peopleNeeded){
        this.task_id = task_id;
        this.hotelName = hotelName;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peopleNeeded = peopleNeeded;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPeopleNeeded() {
        return peopleNeeded;
    }

    public void setPeopleNeeded(String peopleNeeded) {
        this.peopleNeeded = peopleNeeded;
    }

    @Override
    public String toString() {
        return "ShowTasksData {" +
                " task_id = " + task_id +
                ", hotelName = " + hotelName +
                ", address = " + address +
                ", startDate: " + startDate +
                ", endDate = " + endDate +
                ", peopleNeeded = " + peopleNeeded +
                '}';
    }
}