package classes;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksID = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubTasksID() {
        return subTasksID;
    }

    public void setSubTasksID(ArrayList<Integer> subTasksID) {
        this.subTasksID = subTasksID;
    }

    public void addSubTaskToList(int subTaskId) {
        subTasksID.add(subTaskId);
    }

    @Override
    public TaskTypes getType() {
        return TaskTypes.EPIC;
    }
}
