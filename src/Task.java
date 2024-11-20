import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private Status status = Status.NEW;
    private final int ID;


    public Task(String name, String description, int counter) {
        this.name = name;
        this.description = description;
        this.ID = counter;
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

    public int getID() {
        return ID;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return ID == task.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }
}
