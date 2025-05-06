package main;

import classes.*;

import java.io.*;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static classes.TaskConverter.fromString;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File saveFileName;

    public FileBackedTaskManager(File saveFileName) {
        this.saveFileName = saveFileName;
    }

    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try (Reader fileReader = new FileReader(file);

             BufferedReader br = new BufferedReader(fileReader)) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                Task task = fromString(line);

                switch (task.getType()) {
                    case TASK -> {
                        taskManager.tasks.put(task.getId(), task);
                        taskManager.counter++;
                    }
                    case EPIC -> {
                        taskManager.epics.put(task.getId(), (Epic) task);
                        taskManager.counter++;
                    }
                    case SUBTASK -> {
                        taskManager.subTasks.put(task.getId(), (SubTask) task);
                        taskManager.counter++;
                    }
                }
            }

            for (SubTask subTask : taskManager.getSubTasks()) {
                Epic parentEpic = taskManager.epics.get(subTask.getParentID());
                int subTaskId = subTask.getId();
                if (!parentEpic.getSubTasksID().contains(subTaskId)) {
                    parentEpic.getSubTasksID().add(subTask.getId());
                }
            }
            // Тут по заданию тоже, по идее, нужно заменить цикл на лямбду, но она получается более громоздкой,
            // чем цикл в данном случае (сам я не смог, если честно, полазил в интернете, и спросил нейронку,
            // она выдала какое-то чудовище, которое я едва понял, и решил, что  тут оставлю как есть до
            // особого распоряжения.) Если нужно, напиши, переделаю в лямбду.)))

        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка загрузки задач из файла");
        }
        return taskManager;
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(saveFileName)) {
            fileWriter.write("id,type,name,status,description,epic\n");

            Stream.of(getTasks(), getEpics(), getSubTasks())
                    .flatMap(Collection::stream)
                    .map(TaskConverter::toString)
                    .collect(Collectors.collectingAndThen(
                            Collectors.joining("\n"),
                            convertedTasks  -> {
                                try {
                                    fileWriter.write(convertedTasks);
                                } catch (IOException e) {
                                    throw new ManagerSaveException("Ошибка сохранения в потоке конвертации");
                                }
                                return convertedTasks;
                            }
                    ));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }  // тут пытался сделать красиво, но не уверен, что вышло визуально проще, чем было раньше.
    // есть еще альтернативный вариант, изначально как делал, без объединения потоков.(ниже его тоже прописал как комментарий)
    // и еще, помоги, пожалуйста, решить проблему с исключениями, у меня опять коробка в коробке получилась, а я в упор не вижу, как это решить



//    private void save() {  // альтернативный вариант. выглядит понятнее (ну для меня, по крайней мере),
//                           //но мне не нравились одинаковые действия по конвертации, и хотелось их сделать в одном потоке
//
//        try (Writer fileWriter = new FileWriter(saveFileName)) {
//
//            fileWriter.write("id,type,name,status,description,epic\n");
//
//            String convertedTasks = getTasks().stream()
//                    .map(TaskConverter::toString)
//                    .collect(Collectors.joining("\n"));
//
//            String convertedEpics = getEpics().stream()
//                    .map(TaskConverter::toString)
//                    .collect(Collectors.joining("\n"));
//
//            String convertedSubTasks = getSubTasks().stream()
//                    .map(TaskConverter::toString)
//                    .collect(Collectors.joining("\n"));
//
//            fileWriter.write(convertedTasks + convertedEpics + convertedSubTasks);
//
//        } catch (IOException e) {
//            throw new ManagerSaveException("Ошибка сохранения");
//        }
//    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteTaskByID(int taskID) {
        super.deleteTaskByID(taskID);
        save();
    }

    @Override
    public void deleteEpicByID(int epicID) {
        super.deleteEpicByID(epicID);
        save();
    }

    @Override
    public void deleteSubTaskByID(int subTaskID) {
        super.deleteSubTaskByID(subTaskID);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }
}
