package classes;

public class SubTask extends Task {

    private int parentID;

    public SubTask(String name, String description, int parentID) {
        super(name, description);
        this.parentID = parentID;
    }

    public SubTask(String name, String description, Status status, int id, int parentID) {
        super(name, description, status, id);
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