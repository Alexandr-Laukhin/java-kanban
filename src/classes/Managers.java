package classes;


import main.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Managers {

    public <T extends TaskManager> void getDefault() {
        TaskManager taskManager = new TaskManager() {
            private HashMap<Integer, Task> tasks = new HashMap<>();
            private HashMap<Integer, Epic> epics = new HashMap<>();
            private HashMap<Integer, SubTask> subTasks = new HashMap<>();

            private int counter = 0;

            private List<Task> history = new ArrayList<>();

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
            public void deleteTasks() {
                tasks.clear();
            }

            @Override
            public void deleteEpics() {
                epics.clear();
                subTasks.clear();
            }

            @Override
            public void deleteSubTasks() {
                subTasks.clear();
                for (Epic epic : epics.values()) {
                    epic.getSubTasksID().clear();
                    checkEpicStatus(epic.getId());
                }
            }

            @Override
            public void deleteTaskByID(int taskID) {
                tasks.remove(taskID);
            }

            @Override
            public void deleteEpicByID(int epicID) {
                ArrayList<Integer> epicSubtasksId = new ArrayList<>(epics.get(epicID).getSubTasksID());

                for (Integer integer : epicSubtasksId) {
                    subTasks.remove(integer);
                }
                epics.remove(epicID);
            }

            @Override
            public void deleteSubTaskByID(int subTaskID) {

                int epicId = subTasks.get(subTaskID).getParentID();
                epics.get(epicId).getSubTasksID().remove(Integer.valueOf(subTaskID));

                subTasks.remove(subTaskID);
                checkEpicStatus(epicId);
            }

            @Override
            public Task getTaskByID(int taskID) {
                if (history.size() >= 10) {
                    history.removeFirst();
                }
                history.add(tasks.get(taskID));
                return tasks.get(taskID);
            }

            @Override
            public Epic getEpicByID(int epicID) {
                if (history.size() >= 10) {
                    history.removeFirst();
                }
                history.add(epics.get(epicID));
                return epics.get(epicID);
            }

            @Override
            public SubTask getSubTaskByID(int subTaskID) {
                if (history.size() >= 10) {
                    history.removeFirst();
                }
                history.add(subTasks.get(subTaskID));
                return subTasks.get(subTaskID);
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

        };
    }


    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
