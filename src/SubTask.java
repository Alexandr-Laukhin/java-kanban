public class SubTask extends Task {

    private final int parentID;

    public SubTask(String name, String description, int counter, int parentID) {
        super(name, description, counter);
        this.parentID = parentID;
    }

    public int getParentID() {
        return parentID;
    }
}