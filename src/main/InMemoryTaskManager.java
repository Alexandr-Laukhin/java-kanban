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
    public void createTask(Task task, TaskManager taskManager) {

        task.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(task, taskManager))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени задачи.");
                });

        counter++;
        task.setId(counter);
        tasks.put(counter, task);

        if (task.getStartTimeCheck().isPresent() && task.getDuration() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createEpic(Epic epic, TaskManager taskManager) {

        epic.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(epic, taskManager))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени эпика.");
                });

        counter++;
        epic.setId(counter);
        epics.put(counter, epic);
    }

    @Override
    public void createSubTask(SubTask subTask, TaskManager taskManager) {

        subTask.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(subTask, taskManager))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени подзадачи.");
                });

        counter++;
        subTask.setId(counter);
        subTasks.put(counter, subTask);
        Epic epic = epics.get(subTask.getParentID());

        if (epics.containsKey(subTask.getParentID()) && epic != null) {
            epic.addSubTaskToList(counter);
            checkEpicStatus(subTask.getParentID());
            // Тут был комментарий про расчет времени эпика. Я его реализовал в самом эпике, поэтому тут в нем нет
            // необходимости. Мне показалось, что в эпике он будет более уместен, на случай если поля эпика или
            // сабтасок будут изменены вручную. Чтобы не было привязки именно к методу создания подзадачи.
        }

        if (subTask.getStartTimeCheck().isPresent() && subTask.getDurationCheck().isPresent()) {
            prioritizedTasks.add(subTask);
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
        tasks.values().forEach(prioritizedTasks::remove);
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
        subTasks.values().forEach(prioritizedTasks::remove);
        subTasks.clear();
        epics.values().forEach(epic -> {
            epic.getSubTasksID().clear();
            checkEpicStatus(epic.getId());
        });
    }

    @Override
    public void deleteTaskByID(int taskID) {
        Task task = tasks.get(taskID);
        tasks.remove(taskID);
        historyManager.remove(taskID);

        if (task.getStartTimeCheck().isPresent() && task.getDurationCheck().isPresent()) {
            prioritizedTasks.remove(task);
        }
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

        SubTask subTask = subTasks.get(subTaskID);
        int epicId = subTask.getParentID();
        epics.get(epicId).getSubTasksID().remove(Integer.valueOf(subTaskID));

        subTasks.remove(subTaskID);
        checkEpicStatus(epicId);
        historyManager.remove(subTaskID);

        if (subTask.getStartTimeCheck().isPresent() && subTask.getDurationCheck().isPresent()) {
            prioritizedTasks.remove(subTask);
        }
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
    public void updateTask(Task task, TaskManager taskManager) {

        task.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(task, taskManager))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени задачи.");
                });

        tasks.put(task.getId(), task);

        if (task.getStartTimeCheck().isPresent() && task.getDuration() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic, TaskManager taskManager) {

        epic.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(epic, taskManager))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени эпика.");
                });

        epics.put(epic.getId(), epic);
        checkEpicStatus(epic.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask, TaskManager taskManager) {

        subTask.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(subTask, taskManager))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени подзадачи.");
                });

        subTasks.put(subTask.getId(), subTask);
        checkEpicStatus(subTask.getParentID());

        if (subTask.getStartTimeCheck().isPresent() && subTask.getDurationCheck().isPresent()) {
            prioritizedTasks.add(subTask);
        }
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

    public boolean notIntersection(Task task1, Task task2, TaskManager taskManager) {
        return task1.getStartTime().isAfter(task2.getEndTime(taskManager)) || task1.getEndTime(taskManager).isBefore(task2.getStartTime());
    } // Если возвращает true, значит нет пересечения.

    public boolean intersectionCheck(Task task1, TaskManager taskManager) {
        return prioritizedTasks.stream()
                .anyMatch(task -> !notIntersection(task, task1, taskManager));
    } // Если возвращает true, значит есть пересечения.
}
