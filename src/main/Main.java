package main;

import classes.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();    // Тестирую через дебагер примерно таким образом (ниже прописано. обычно я
                                                        // удаляю это, чтобы не мусорить, но ты спрашивал, я решил оставить).
                                                        // Не знаю, как в прошлый раз проглядел ошибки, когда сейчас по твоим
                                                        // комментариям начал проходить, сразу их увидел. Постараюсь быть внимательнее в будущем.


        Epic epic1 = new Epic("Test", "Test description");
        Epic epic12= new Epic("Test 12", "Test description 12");
        SubTask subTask1 = new SubTask("test subtask 1", "test subtask description 1", 1);
        SubTask subTask2 = new SubTask("test subtask 2", "test subtask description 2", 1);
        SubTask subTask3 = new SubTask("test subtask 3", "test subtask description 3", 1);

        taskManager.createEpic(epic1);

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        // taskManager.deleteSubTasks();

        Epic epic2 = new Epic("Test 2", "Test description 2");
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic12);

        SubTask subTask12 = new SubTask("test subtask 12", "test subtask description 12", 6);
        taskManager.createSubTask(subTask12);
        // taskManager.deleteEpicByID(1);


//        taskManager.deleteSubTaskByID(2);
//        taskManager.deleteSubTaskByID(3);
//        taskManager.deleteSubTaskByID(4);


        Task task = new Task("Test task 85", "Test description 85");

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.DONE);
        subTask3.setStatus(Status.DONE);

        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        taskManager.updateSubTask(subTask3);

        subTask3.setStatus(Status.NEW);
        taskManager.updateSubTask(subTask3);
        taskManager.deleteSubTaskByID(2);
        taskManager.deleteSubTaskByID(3);




    }
}


