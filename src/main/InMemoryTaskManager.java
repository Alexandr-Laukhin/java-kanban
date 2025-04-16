package main;

import classes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected int counter = 0;
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void createTask(Task task) {
        counter++;
        task.setId(counter);
        tasks.put(counter, task);
    }

    @Override
    public void createEpic(Epic epic) {
        counter++;
        epic.setId(counter);
        epics.put(counter, epic);
    }

    @Override
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

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasksFromEpicByID(int epicID) {

        ArrayList<SubTask> subTasksInEpic = new ArrayList<>();

        for (Integer subtaskId : epics.get(epicID).getSubTasksID()) {
            subTasksInEpic.add(subTasks.get(subtaskId));
        }
        return subTasksInEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteTasks() {
        for (Integer integer : tasks.keySet()) {
            historyManager.remove(integer);
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Integer integer : epics.keySet()) {
            historyManager.remove(integer);
        }
        for (Integer integer : subTasks.keySet()) {
            historyManager.remove(integer);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        for (Integer integer : subTasks.keySet()) {
            historyManager.remove(integer);
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasksID().clear();
            checkEpicStatus(epic.getId());
        }
    }

    @Override
    public void deleteTaskByID(int taskID) {
        tasks.remove(taskID);
        historyManager.remove(taskID);
    }

    @Override
    public void deleteEpicByID(int epicID) {
        ArrayList<Integer> epicSubtasksId = new ArrayList<>(epics.get(epicID).getSubTasksID());

        for (Integer integer : epicSubtasksId) {
            historyManager.remove(integer);
            subTasks.remove(integer);
        }
        epics.remove(epicID);
        historyManager.remove(epicID);
    }

    @Override
    public void deleteSubTaskByID(int subTaskID) {

        int epicId = subTasks.get(subTaskID).getParentID();
        epics.get(epicId).getSubTasksID().remove(Integer.valueOf(subTaskID));

        subTasks.remove(subTaskID);
        checkEpicStatus(epicId);
        historyManager.remove(subTaskID);
    }

    @Override
    public Task getTaskByID(int taskID) {
        Task task = tasks.get(taskID);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicByID(int epicID) {
        Epic epic = epics.get(epicID);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskByID(int subTaskID) {
        SubTask subTask = subTasks.get(subTaskID);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        checkEpicStatus(epic.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        checkEpicStatus(subTask.getParentID());
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
}
