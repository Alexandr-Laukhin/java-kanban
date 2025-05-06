package main;

import classes.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected int counter = 0;
    private HistoryManager historyManager = Managers.getDefaultHistory();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public void createTask(Task task) {
        if (intersectionCheck(task)) {
            System.out.println("Есть пересечения по времени задачи. Я не знаю, что дальше делать с найденными пересечениями, поэтому просто вывел это сообщение");
            // return;
        }
        counter++;
        task.setId(counter);
        tasks.put(counter, task);
    }

    @Override
    public void createEpic(Epic epic) {
        if (intersectionCheck(epic)) {
            System.out.println("Есть пересечения по времени эпика");
            // return;
        }
        counter++;
        epic.setId(counter);
        epics.put(counter, epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (intersectionCheck(subTask)) {
            System.out.println("Есть пересечения по времени подзадачи");
            // return;
        }
        counter++;
        subTask.setId(counter);
        subTasks.put(counter, subTask);
        Epic epic = epics.get(subTask.getParentID());

        if (epics.containsKey(subTask.getParentID()) && epic != null) {
            epic.addSubTaskToList(counter);
            epic.setDuration(epic.getDuration().plus(subTask.getDuration()));
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
        return epics.get(epicID).getSubTasksID().stream()
                .map(subTasks::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.keySet().forEach(historyManager::remove);
        subTasks.keySet().forEach(historyManager::remove);
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTasks.keySet().forEach(historyManager::remove);
        subTasks.clear();
        epics.values().forEach(epic -> {
            epic.getSubTasksID().clear();
            checkEpicStatus(epic.getId());
        });

    }

    @Override
    public void deleteTaskByID(int taskID) {
        tasks.remove(taskID);
        historyManager.remove(taskID);
    }

    @Override
    public void deleteEpicByID(int epicID) {
        new ArrayList<>(epics.get(epicID).getSubTasksID())
                .forEach(subTaskId -> {
                    historyManager.remove(subTaskId);
                    subTasks.remove(subTaskId);
                });

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
        if (intersectionCheck(task)) {
            System.out.println("Есть пересечения по времени задачи");
            //return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (intersectionCheck(epic)) {
            System.out.println("Есть пересечения по времени эпика");
            //return;
        }
        epics.put(epic.getId(), epic);
        checkEpicStatus(epic.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (intersectionCheck(subTask)) {
            System.out.println("Есть пересечения по времени подзадачи");
            //return;
        }
        subTasks.put(subTask.getId(), subTask);
        checkEpicStatus(subTask.getParentID());
    }

    private void checkEpicStatus(int epicID) {

        Epic checkEpic = epics.get(epicID);

        if (checkEpic.getSubTasksID().isEmpty()) {
            epics.get(epicID).setStatus(Status.NEW);
            return;
        }

        List<Status> subTasksStatusList = checkEpic.getSubTasksID().stream()
                .map(subTasks::get)
                .map(Task::getStatus)
                .toList();

        boolean isNew = subTasksStatusList.contains(Status.NEW);
        boolean isInProgress = subTasksStatusList.contains(Status.IN_PROGRESS);
        boolean isDone = subTasksStatusList.contains(Status.DONE);

        checkEpic.setStatus(
                !isInProgress && !isDone ? Status.NEW :
                        !isInProgress && !isNew ? Status.DONE :
                                Status.IN_PROGRESS
        );
    }

    public TreeSet<Task> getPrioritizedTasks() {
        prioritizedTasks.clear();
        Stream.of(tasks.values(), epics.values(), subTasks.values())
                .forEach(prioritizedTasks::addAll);
        return prioritizedTasks;
    }

    public boolean segmentIntersection(Task task1, Task task2) {
        boolean ifT1isBeforeT2 = task1.getStartTime().isBefore(task2.getStartTime()) && task1.getEndTime().isAfter(task2.getStartTime());
        boolean ifT1isAfterT2 = task2.getStartTime().isBefore(task1.getStartTime()) && task2.getEndTime().isAfter(task1.getStartTime());
        boolean ifT1equalsT2 = task1.equals(task2);

        return ifT1isBeforeT2 || ifT1isAfterT2 || ifT1equalsT2;
    } // Если возвращает true, значит есть пересечения.

    public boolean intersectionCheck(Task task1) {

        ArrayList<Task> prioritizedTasksArray = new ArrayList<>(getPrioritizedTasks());

//        if (prioritizedTasksArray.size() < 2) {
//            return false;
//        }

        return IntStream.range(0, prioritizedTasksArray.size())
                .anyMatch(i -> segmentIntersection(prioritizedTasksArray.get(i), task1));
    } // Если возвращает true, значит есть пересечения.
}
