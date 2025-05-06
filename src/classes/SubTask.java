package classes;

import java.time.Duration;

public class SubTask extends Task {

    private int parentID;

    public SubTask(String name, String description, Duration inMinutes, int parentID) {
        super(name, description, inMinutes);
        this.parentID = parentID;
    }

    public SubTask(String name, String description, Status status, int id, Duration inMinutes, int parentID) {
        super(name, description, status, id, inMinutes);
        this.parentID = parentID;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    @Override
    public TaskTypes getType() {
        return TaskTypes.SUBTASK;
    }
}