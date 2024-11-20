import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subTasksID = new ArrayList<>();

    public Epic(String name, String description, int counter) {
        super(name, description, counter);
    }

    public ArrayList<Integer> getSubTasksID() {
        return subTasksID;
    }
}
