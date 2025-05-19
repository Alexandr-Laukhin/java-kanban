package main;

import classes.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected int counter = 0;
    private HistoryManager historyManager = Managers.getDefaultHistory();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.nullsLast(
            Comparator.comparing(
                            Task::getStartTime,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(
                            Task::getDuration,
                            Comparator.nullsLast(Comparator.naturalOrder()))
    ));

    @Override
    public void createTask(Task task) {

        task.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(task))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени задачи.");
                });

        counter++;
        task.setId(counter);
        tasks.put(counter, task);
        prioritizedTasks.add(task);
    }

    @Override
    public void createEpic(Epic epic) {

        epic.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(epic))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени эпика.");
                });

        counter++;
        epic.setId(counter);
        epics.put(counter, epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {

        subTask.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(subTask))
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
        }
        prioritizedTasks.add(subTask);

        if (subTask.getStartTimeCheck().isPresent()) {
            checkEpicTime(epic);
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
        subTasks.values().forEach(prioritizedTasks::remove);
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
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        });
    }

    @Override
    public void deleteTaskByID(int taskID) {
        Task task = tasks.get(taskID);
        tasks.remove(taskID);
        historyManager.remove(taskID);
        prioritizedTasks.remove(task);

    }

    @Override
    public void deleteEpicByID(int epicID) {
        new ArrayList<>(epics.get(epicID).getSubTasksID())
                .forEach(subTaskId -> {
                    historyManager.remove(subTaskId);
                    subTasks.remove(subTaskId);
                    prioritizedTasks.remove(subTasks.get(subTaskId));
                });

        epics.remove(epicID);
        historyManager.remove(epicID);
    }

    @Override
    public void deleteSubTaskByID(int subTaskID) {

        SubTask subTask = subTasks.get(subTaskID);
        int epicId = subTask.getParentID();
        Epic parentEpic = epics.get(epicId);
        parentEpic.getSubTasksID().remove(Integer.valueOf(subTaskID));

        subTasks.remove(subTaskID);
        checkEpicStatus(epicId);
        historyManager.remove(subTaskID);
        prioritizedTasks.remove(subTask);
        checkEpicTime(parentEpic);
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

        task.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(task))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени задачи.");
                });

        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {

        epic.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(epic))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени эпика.");
                });

        epics.put(epic.getId(), epic);
        checkEpicStatus(epic.getId());
        getEndTime(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {

        Epic parentEpic = getEpicByID(subTask.getParentID());

        subTask.getStartTimeCheck()
                .filter(startTime -> intersectionCheck(subTask))
                .ifPresent(startTime -> {
                    throw new IllegalStateException("Есть пересечения по времени подзадачи.");
                });


        subTasks.put(subTask.getId(), subTask);
        checkEpicStatus(subTask.getParentID());
        prioritizedTasks.add(subTask);

        if (subTask.getStartTimeCheck().isPresent()) {
            checkEpicTime(parentEpic);
        }
    }

    private void checkEpicTime(Epic epic) {

        epic.getSubTasksID().stream()
                .map(subTasks::get)
                .filter(subTaskInStream -> subTaskInStream.getStartTime() != null)
                .min(Comparator.comparing(SubTask::getStartTime))
                .ifPresent(earliestSubTask -> epic.setStartTime(earliestSubTask.getStartTime()));

        Duration epicDuration = epic.getSubTasksID().stream()
                .map(subTasks::get)
                .filter(subTask -> subTask.getDuration() != null)
                .map(SubTask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        epic.setDuration(epicDuration);

        epic.getSubTasksID().stream()
                .map(subTasks::get)
                .filter(subTaskInStream -> subTaskInStream.getStartTime() != null && subTaskInStream.getDuration() != null)
                .max(Comparator.comparing((SubTask::getEndTime)))
                .ifPresent(latestSubTask -> epic.setEndTime(latestSubTask.getEndTime()));
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

    private Optional<Duration> getSubTaskDuration(Integer subTaskId) {

        SubTask subTask = getSubTaskByID(subTaskId);
        Duration duration = subTask.getDuration();

        if (duration == null) {
            System.err.println("У подзадачи с id " + subTaskId + " не задана длительность");
            return Optional.empty();
        }

        return Optional.of(duration);
    }

    public LocalDateTime getEndTime(Task task) {

        if (task.getType() == TaskTypes.EPIC) {

            Epic epic = (Epic) task;

            if (task.getStartTimeCheck().isEmpty()) {
                throw new NullPointerException("Не задано время старта эпика");
            }

            Duration duration = Optional.ofNullable(epic.getSubTasksID())
                    .filter(subTasksID -> !subTasksID.isEmpty())
                    .map(subTasksID -> subTasksID.stream()
                            .flatMap(id -> getSubTaskDuration(id).stream())
                            .reduce(Duration.ZERO, Duration::plus))
                    .orElse(Duration.ZERO);

            LocalDateTime endTime = epic.getStartTime().plus(duration);
            epic.setEndTime(endTime);

            return endTime;
        } else {
            LocalDateTime endTime = task.getStartTime().plus(task.getDuration());

            return endTime;
        }
    }

    public boolean notIntersection(Task task1, Task task2) {
        if (task1.getStartTimeCheck().isPresent() && task2.getStartTimeCheck().isPresent()) {
            // после написания nullsafe компаратора в prioritizedTasks появились задачи без startTime, поэтому сделал проверку на  null
            return task1.getStartTime().isAfter(getEndTime(task2)) || getEndTime(task1).isBefore(task2.getStartTime());
        } else {
            return false;
        }
    } // Если возвращает true, значит нет пересечения.

    public boolean intersectionCheck(Task task1) {
        return prioritizedTasks.stream()
                .anyMatch(task -> !notIntersection(task, task1));
    } // Если возвращает true, значит есть пересечения.
}