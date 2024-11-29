package main;

import classes.Epic;
import classes.Status;
import classes.SubTask;
import classes.Task;

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

    public void createSubTask(SubTask subTask) {
        counter++;
        subTask.setId(counter);
        subTasks.put(counter, subTask);
        Epic epic = epics.get(subTask.getParentID());

        if (epics.containsKey(subTask.getParentID()) && epic != null) {
            epic.addSubTaskToList(counter);
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
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasksID().clear();
            checkEpicStatus(epic.getId());
        }
    }

    public void deleteTaskByID(int taskID) {
        tasks.remove(taskID);
    }

    public void deleteEpicByID(int epicID) {
        ArrayList<Integer> epicSubtasksId = new ArrayList<>(epics.get(epicID).getSubTasksID());

        for (Integer integer : epicSubtasksId) {
            subTasks.remove(integer);
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

        Epic checkEpic = epics.get(epicID);

        if (checkEpic.getSubTasksID().isEmpty()) {
            epics.get(epicID).setStatus(Status.NEW);
        } else {
            ArrayList<Integer> subTasksIdList = new ArrayList<>(checkEpic.getSubTasksID());
            ArrayList<Status> subTasksStatusList = new ArrayList<>();

            for (Integer integer : subTasksIdList) {
                subTasksStatusList.add(subTasks.get(integer).getStatus());
            }

            if (!subTasksStatusList.contains(Status.IN_PROGRESS) && !subTasksStatusList.contains(Status.DONE)) {
                checkEpic.setStatus(Status.NEW);
            } else if (!subTasksStatusList.contains(Status.IN_PROGRESS) && !subTasksStatusList.contains(Status.NEW)) {
                checkEpic.setStatus(Status.DONE);
            } else {
                checkEpic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        checkEpicStatus(subTask.getParentID());
    }

}
