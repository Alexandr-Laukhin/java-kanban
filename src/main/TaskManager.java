package main;

import java.util.ArrayList;
import java.util.HashMap;
import classes.*;


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

    public void createSubTask(SubTask subTask) {
        counter++;
        subTask.setId(counter);
        subTasks.put(counter, subTask);
        if (epics.containsKey(subTask.getParentID())) {
            epics.get(subTask.getParentID()).getSubTasksID().add(counter);
            //    epics.get(subTask.getParentID()).addSubTaskToList(counter);   // тут сделал через метод, как ты сказал, но не понял в чем разница. и так, и так одинаково громоздко (хотя это мое субъективное мнение, конечно)
            checkEpicStatus(subTask.getParentID());
        }
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

        for (Integer subtaskId : epics.get(epicID).getSubTasksID()) {
            subTasksInEpic.add(subTasks.get(subtaskId));
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
        ArrayList<Integer> epicsKeys = new ArrayList<>(epics.keySet());
        subTasks.clear();
        for (int i = 0; i < epicsKeys.size(); i++) {
            epics.get(epicsKeys.get(i)).getSubTasksID().clear();
            checkEpicStatus(epics.get(epicsKeys.get(i)).getId());
        }
    }  // спасибо, что обратил внимание, я действительно не подумал об этой очевидной (как мне сейчас кажется) вещи, что стандартный цикл для хешмапы не подойдет

    public void deleteTaskByID(int taskID) {
        tasks.remove(taskID);
    }

    public void deleteEpicByID(int epicID) {
        ArrayList<Integer> epicSubtasksId = new ArrayList<>(epics.get(epicID).getSubTasksID());

        for (int i = 0; i < epicSubtasksId.size(); i++) {
            subTasks.remove(epicSubtasksId.get(i));
        }
        epics.remove(epicID);
    }

    public void deleteSubTaskByID(int subTaskID) {

        int epicId = subTasks.get(subTaskID).getParentID();
        epics.get(epicId).getSubTasksID().remove(Integer.valueOf(subTaskID));

        subTasks.remove(subTaskID);
        checkEpicStatus(epicId);
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

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        checkEpicStatus(epic.getId());
    }

    private void checkEpicStatus(int epicID) {
        if (epics.get(epicID).getSubTasksID().isEmpty()) {
            epics.get(epicID).setStatus(Status.NEW);
        } else {
            ArrayList<Integer> subTasksIdList = new ArrayList<>(epics.get(epicID).getSubTasksID());
            ArrayList<Status> subTasksStatusList = new ArrayList<>();

            for (int i = 0; i < subTasksIdList.size(); i++) {
                subTasksStatusList.add(subTasks.get(subTasksIdList.get(i)).getStatus());
            }

            if (!subTasksStatusList.contains(Status.IN_PROGRESS) && !subTasksStatusList.contains(Status.DONE)) {
                epics.get(epicID).setStatus(Status.NEW);
            } else if (!subTasksStatusList.contains(Status.IN_PROGRESS) && !subTasksStatusList.contains(Status.NEW)) {
                epics.get(epicID).setStatus(Status.DONE);
            } else {
                epics.get(epicID).setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        checkEpicStatus(subTask.getParentID());
    }

}
