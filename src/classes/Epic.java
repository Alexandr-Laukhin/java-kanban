package classes;

import java.time.Duration;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksID = new ArrayList<>();

    public Epic(String name, String description, Duration inMinutes) {
        super(name, description, inMinutes);
    }

    public Epic(String name, String description, Status status, int id, Duration inMinutes) {
        super(name, description, status, id, inMinutes);
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
