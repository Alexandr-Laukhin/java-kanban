import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int counter = 0;


    public void createTask(Task task) {
        counter++;
        task.setId(counter);
        tasks.put(counter, task);
    }

    public void createEpic(Epic epic) {
        counter++;
        epic.setId(counter);
        epics.put(counter, epic);
    }

    public void createSubTask(SubTask subTask, int parentId) {
        counter++;
        subTask.setId(counter);
        subTask.setParentID(parentId);
        subTasks.put(counter, subTask);
        epics.get(parentId).getSubTasksID().add(counter);
        checkEpicStatus(subTask.getParentID());
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<SubTask> getSubTasksFromEpicByID(int epicID) {

        ArrayList<SubTask> subTasksInEpic = new ArrayList<>();

        for (int i = 0; i < epics.get(epicID).getSubTasksID().size(); i++) {  // здесь мы проходимся по списку ИД подзадач в конкретном эпике
            if (subTasks.containsKey(epics.get(epicID).getSubTasksID().get(i))) {
                subTasksInEpic.add(subTasks.get(epics.get(epicID).getSubTasksID().get(i)));
            }
        }
        return subTasksInEpic;
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
        for (int i = 0; i < epics.size(); i++) {
            epics.get(i).getSubTasksID().clear();
            checkEpicStatus(epics.get(i).getId());
        }
    }

    public void deleteTaskByID(int taskID) {
        tasks.remove(taskID);
    }

    public void deleteEpicByID(int epicID) {
        epics.remove(epicID);
        for (int i = 0; i < subTasks.size(); i++) {
            if (epicID == subTasks.get(i).getParentID()) {
                subTasks.remove(i);
            }
        }
    }

    public void deleteSubTaskByID(int subTaskID) {
        subTasks.remove(subTaskID);
        for (int i = 0; i < epics.size(); i++) {
            for (int j = 0; j < epics.get(i).getSubTasksID().size(); j++) {
                if (subTaskID == epics.get(i).getSubTasksID().get(j)) {
                    epics.get(i).getSubTasksID().remove(j);
                    checkEpicStatus(epics.get(i).getId());
                }
            }
        }
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

    public void updateTask(Task task, int taskForUpdateId) {
        task.setId(taskForUpdateId);
        tasks.replace(taskForUpdateId, tasks.get(taskForUpdateId), task);
    }

    public void updateEpic(Epic epic, int epicForUdateId) {
        epic.setId(epicForUdateId);
        epic.setSubTasksID(epics.get(epicForUdateId).getSubTasksID());
        epics.replace(epicForUdateId, epics.get(epicForUdateId), epic);
    }

    private void checkEpicStatus(int epicID) {                                                     // Тут не понял ошибку, все проверил несколько раз, в упор не вижу. По умолчанию, при создании эпика, у него статус NEW.
                                                                                                   // Далее мы проверяем все его подзадачи на статусы, и если хоть одна из них IN_PROGRESS, мы останавливаем цикл, и
                                                                                                   // присваиваем эпику статус IN_PROGRESS, так как дальше идти смысла нет. Если статус всех подзадач DONE, то и эпик будет DONE.
                                                                                                   // Если же все статусы подзадач NEW, то ничего не изменится, эпик останется NEW. Покажи, пожалуйста, где косяк в логике.
        for (int i = 0; i < epics.get(epicID).getSubTasksID().size(); i++) {
            if (subTasks.get(epics.get(epicID).getSubTasksID().get(i)).getStatus().equals(Status.IN_PROGRESS)) {
                epics.get(epicID).setStatus(Status.IN_PROGRESS);
                break;
            } else if (subTasks.get(epics.get(epicID).getSubTasksID().get(i)).getStatus().equals(Status.DONE)) {
                epics.get(epicID).setStatus(Status.DONE);
            }
        }
    }

    public void updateSubTask(SubTask subTask, int subTaskForUpdateId) {
        subTask.setId(subTaskForUpdateId);
        subTask.setParentID(subTasks.get(subTaskForUpdateId).getParentID());
        subTasks.replace(subTaskForUpdateId, subTasks.get(subTaskForUpdateId), subTask);
        checkEpicStatus(subTask.getParentID());
    }

}
