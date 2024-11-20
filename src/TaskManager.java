import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Task> allTasks = new HashMap<>();

    private static int counter = 0;

    Task task;
    Epic epic;
    SubTask subTask;


    public void createTask(String name, String description) {           // тут видел твой комментарий, что в параметрах должен передаваться сам объект, но не понял, что имелось ввиду. если я сделал неправильно, поясни, пожалуйста.
        counter++;
        task = new Task(name, description, counter);
        tasks.put(counter, task);
    }

    public void createEpic(String name, String description) {
        counter++;
        epic = new Epic(name, description, counter);
        epics.put(counter, epic);
    }

    public void createSubTask(String name, String description, int parentID) {
        counter++;
        subTask = new SubTask(name, description, counter, parentID);
        subTasks.put(counter, subTask);
        epics.get(parentID).subTasksID.add(counter);
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Task> getAllTasks() {
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks.putAll(subTasks);
        return allTasks;
    }

    public ArrayList<Integer> getSubTasksFromEpicByID(int epicID) {
        return epics.get(epicID).subTasksID;

    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
    }

    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        allTasks.clear();
    }

    public void deleteAllSubTasksFromEpic(int epicID) {
        for (int i = 0; i < epics.get(epicID).subTasksID.size(); i++) {
            subTasks.remove(epics.get(epicID).subTasksID.get(i));
        }
    }

    public void deleteTaskByID(int taskID) {
        tasks.remove(taskID);
    }

    public void deleteEpicByID(int epicID) {
        epics.remove(epicID);
    }

    public void deleteSubTaskByID(int subTaskID) {
        subTasks.remove(subTaskID);
    }

    public Task getTaskByID(int taskID) {
        return tasks.get(taskID);
    }

    public Epic getEpicByID(int epicID) {
        return epics.get(epicID);
    }

    public SubTask getSubTaskByID(int subTaskID) {
        return subTasks.get(subTaskID);
    }

    public Task updateTask(int taskID, String name, String description, Status status) {
        tasks.get(taskID).setName(name);
        tasks.get(taskID).setDescription(description);
        tasks.get(taskID).setStatus(status);

        return tasks.get(taskID);
    }

    public Task updateEpic(int epicID, String name, String description) {
        epics.get(epicID).setName(name);
        epics.get(epicID).setDescription(description);
        checkEpicStatus(epicID);

        return epics.get(epicID);
    }

    public void checkEpicStatus(int epicID) {
        for (int i = 0; i < epics.get(epicID).subTasksID.size(); i++) {
            if (subTasks.get(epics.get(epicID).subTasksID.get(i)).getStatus().equals(Status.IN_PROGRESS)) {
                epics.get(epicID).setStatus(Status.IN_PROGRESS);
                break;
            } else if (subTasks.get(epics.get(epicID).subTasksID.get(i)).getStatus().equals(Status.DONE)) {
                epics.get(epicID).setStatus(Status.DONE);
            }
        }
    }

    public SubTask updateSubTask(int subTaskID, String name, String description, Status status) {
        subTasks.get(subTaskID).setName(name);
        subTasks.get(subTaskID).setDescription(description);
        subTasks.get(subTaskID).setStatus(status);
        checkEpicStatus(subTasks.get(subTaskID).getParentID());

        return subTasks.get(subTaskID);
    }

}
