public class SubTask extends Task {

    private int parentID;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

}