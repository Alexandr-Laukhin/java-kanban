public class Task {

    private String name;
    private String description;
    private Status status = Status.NEW;
    private final int number = (int) (Math.round(Math.random() * 1000000));

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

}
