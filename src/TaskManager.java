import java.util.ArrayList;
import java.util.Scanner;

public class TaskManager {

    Scanner scanner = new Scanner(System.in);
    ArrayList<Task> tasks = new ArrayList<>();
    Task task;
    Epic epic;

    public void createTask() {
        System.out.println("1. Создать задачу.");
        System.out.println("2. Создать эпик.");
        String command = scanner.next();


        System.out.println("Введите название: ");
        scanner.nextLine();
        String taskName = scanner.nextLine();

        System.out.println("Введите описание: ");
        String taskDescription = scanner.nextLine();

        switch (command) {
            case "1" -> {
                task = new Task(taskName, taskDescription);
                tasks.add(task);
            }
            case "2" -> {
                epic = new Epic(taskName, taskDescription);
                tasks.add(epic);
            }
            default -> System.out.println("Такой команды нет.");
        }
    }

    public void updateTask() {
        System.out.println("Введите номер или название задачи, которую хотите обновить:");
        String taskForUpdate = scanner.nextLine();
        for (int i = 0; i < tasks.size(); i++) {
            if (taskForUpdate.equals(tasks.get(i).getName()) || taskForUpdate.equals(Integer.toString(tasks.get(i).getNumber()))) {
                printUpdateMenu();
                String updateCommand = scanner.next();

                switch (updateCommand) {
                    case "1" -> updateStatus(i);
                    case "2" -> updateName(i);
                    case "3" -> updateDescription(i);
                    default ->
                            System.out.println("Такой команды нет. Пожалуйста, введите номер выбранного пункта меню.");
                }
            } else {
                System.out.println("Такой задачи нет.");
            }
        }
    }

    private void printUpdateMenu() {
        System.out.println("1. Изменить статус задачи.");
        System.out.println("2. Изменить название.");
        System.out.println("3. Изменить описание.");
    }

    private void updateStatus(int i) {
        if (tasks.get(i).getClass() == Epic.class) {
            System.out.println("Вы не можете изменить статус данной задачи. Он изменится автоматически, " +
                    "при изменении статуса подзадач.");
        } else {
            System.out.println("Выберите новый статус:");
            System.out.println("1. IN_PROGRESS");
            System.out.println("2. DONE");
            String newStatus = scanner.next();
            if (newStatus.equals("1")) {
                tasks.get(i).setStatus(Status.IN_PROGRESS);
                System.out.println("Статус изменен.");
            } else if (newStatus.equals("2")) {
                tasks.get(i).setStatus(Status.DONE);
                System.out.println("Статус изменен.");
            } else {
                System.out.println("Такой команды нет.");
            }
        }
    }

    private void updateName(int i) {
        System.out.println("Введите новое название задачи:");
        scanner.nextLine();
        String newName = scanner.nextLine();
        tasks.get(i).setName(newName);
        System.out.println("Название изменено.");
    }

    private void updateDescription(int i) {
        System.out.println("Введите новое описание задачи:");
        scanner.nextLine();
        String newDescription = scanner.nextLine();
        tasks.get(i).setDescription(newDescription);
        System.out.println("Описание изменено.");
    }

    public void printAllTasks() {
        for (Task value : tasks) {
            System.out.println(value.getName() + ". Номер задачи: " + value.getNumber() + ". Статус задачи: " + value.getStatus() + ".");
        }
        if (tasks.isEmpty()) {
            System.out.println("Список пуст.");
        }
    }

    public void findTaskByNumber() {
        System.out.println("Введите номер задачи: ");
        boolean isFound = false;
        int taskNumber = scanner.nextInt();

        for (int i = 0; i < tasks.size(); i++) {
            if (taskNumber == tasks.get(i).getNumber()) {
                System.out.println(tasks.get(i).getName() + " Описание задачи: " + tasks.get(i).getDescription());
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("Задачи с таким номером не найдено.");
        }
    }

    public void deleteTaskByNumber() {
        System.out.println("Введите номер задачи: ");
        boolean isFound = false;
        int taskNumber = scanner.nextInt();

        for (int i = 0; i < tasks.size(); i++) {
            if (taskNumber == tasks.get(i).getNumber()) {
                System.out.println(tasks.get(i).getName() + " с номером: " + tasks.get(i).getNumber() + " удалена.");
                tasks.remove(i);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("Задачи с таким номером не найдено.");
        }
    }

    public void epicMenu() {
        printEpicMenu();
        String epicMenuCommand = scanner.next();

        switch (epicMenuCommand) {
            case "1" -> printAllEpics();
            case "2" -> findEpicByNumber();
            case "3" -> printAllTasksInEpic();
            case "4" -> createSubTaskInEpic();
            case "5" -> updateSubTaskInEpic();
            case "6" -> deleteSubTaskInEpic();
            case "7" -> deleteAllSubTasksInEpic();
            default -> System.out.println("Такой команды нет. Пожалуйста, введите номер выбранного пункта меню.");
        }

    }

    public void printEpicMenu() {
        System.out.println("1. Показать список всех эпиков.");
        System.out.println("2. Найти эпик по номеру.");
        System.out.println("3. Показать список всех подзадач эпика (по номеру эпика).");
        System.out.println("4. Создать подзадачу в эпике (по номеру эпика)");
        System.out.println("5. Обновить подзадачу в эпике (по номеру эпика)");
        System.out.println("6. Удалить подзадачу в эпике (по номеру эпика)");
        System.out.println("7. Удалить все подзадачи в эпике (по номеру эпика).");

    }

    public void printAllEpics() {
        boolean isFound = false;

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getClass() == Epic.class) {
                System.out.println(tasks.get(i).getName() + ". Номер эпика: " + tasks.get(i).getNumber() + ". Статус эпика: " + tasks.get(i).getStatus() + ".");
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("Эпиков в списке не обнаружено.");
        }
    }

    public void findEpicByNumber() {
        System.out.println("Введите номер эпика: ");
        boolean isFound = false;
        int taskNumber = scanner.nextInt();

        for (int i = 0; i < tasks.size(); i++) {
            if (taskNumber == tasks.get(i).getNumber()) {
                System.out.println(tasks.get(i).getName() + " Описание эпика: " + tasks.get(i).getDescription());
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("Эпиков с таким номером не найдено.");
        }
    }

    public void printAllTasksInEpic() {
        System.out.println("Введите номер эпика: ");
        int taskNumber = scanner.nextInt();
        boolean isFound = false;

        for (int i = 0; i < tasks.size(); i++) {
            if (taskNumber == tasks.get(i).getNumber()) {
                ((Epic) tasks.get(i)).printThisEpicSubTasks();
                isFound = true;
                if (((Epic) tasks.get(i)).subTasks.isEmpty()) {
                    System.out.println("Список подзадач пуст.");
                }
            }
        }

        if (!isFound) {
            System.out.println("Эпиков с таким номером не найдено.");
        }
    }

    public void createSubTaskInEpic() {
        System.out.println("Введите номер эпика: ");
        int taskNumber = scanner.nextInt();
        System.out.println("Введите название задачи: ");
        scanner.nextLine();
        String subTaskName = scanner.nextLine();
        System.out.println("Введите описание задачи: ");
        String subTaskDescription = scanner.nextLine();
        boolean isFound = false;

        for (int i = 0; i < tasks.size(); i++) {
            if (taskNumber == tasks.get(i).getNumber()) {
                ((Epic) tasks.get(i)).createSubTask(subTaskName, subTaskDescription);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("Эпиков с таким номером не найдено.");
        }
    }

    public void deleteSubTaskInEpic() {
        System.out.println("Введите номер эпика: ");
        scanner.nextLine();
        int taskNumber = scanner.nextInt();
        boolean isFound = false;

        for (int i = 0; i < tasks.size(); i++) {
            if (taskNumber == tasks.get(i).getNumber()) {
                System.out.println("Введите номер подзадачи: ");
                int subTaskNumber = scanner.nextInt();
                for (int j = 0; j < ((Epic) tasks.get(i)).subTasks.size(); j++) {
                    if (((Epic) tasks.get(i)).subTasks.get(j).getNumber() == subTaskNumber) {
                        ((Epic) tasks.get(i)).subTasks.remove(j);
                        isFound = true;
                        System.out.println("Подзадача удалена.");
                    }
                }
                if (!isFound) {
                    System.out.println("Подзадач с таким номером не найдено.");
                }
            }
        }
        if (!isFound) {
            System.out.println("Эпиков с таким номером не найдено.");
        }
    }

    public void deleteAllSubTasksInEpic() {
        System.out.println("Введите номер эпика: ");
        int taskNumber = scanner.nextInt();
        boolean isFound = false;

        for (int i = 0; i < tasks.size(); i++) {
            if (taskNumber == tasks.get(i).getNumber()) {
                ((Epic) tasks.get(i)).subTasks.clear();
                isFound = true;
                System.out.println("Список подзадач в данном эпике очищен.");
            }
        }
        if (!isFound) {
            System.out.println("Эпиков с таким номером не найдено.");
        }
    }

    public void updateSubTaskInEpic() {


        System.out.println("Введите номер или название эпика, подзадачи которого хотите обновить:");
        scanner.nextLine();                                       // помоги, пожалуйста, не понимаю, как тут сделать так, чтобы после ввода номера или названия не
        String taskForUpdate = scanner.nextLine();                // нужно было 2 раза нажимать enter. везде получилось, а здесь никак, не понимаю в чем дело

        boolean isFound = false;

        for (int i = 0; i < tasks.size(); i++) {
            if (taskForUpdate.equals(tasks.get(i).getName()) || taskForUpdate.equals(Integer.toString(tasks.get(i).getNumber()))) {
                scanner.nextLine();
                System.out.println("Введите номер подзадачи, которую хотите обновить: ");
                int subTaskForUpdate = scanner.nextInt();

                for (int j = 0; j < ((Epic) tasks.get(i)).subTasks.size(); j++) {
                    if (subTaskForUpdate == ((Epic) tasks.get(i)).subTasks.get(j).getNumber()) {
                        isFound = true;
                        printUpdateMenuForSubTasks();
                        String updateCommand = scanner.next();

                        switch (updateCommand) {
                            case "1" -> updateSubTaskName(i, j);
                            case "2" -> updateSubTaskDescription(i, j);
                            case "3" -> updateSubTaskStatus(i, j);
                            default ->
                                    System.out.println("Такой команды нет. Пожалуйста, введите номер выбранного пункта меню.");
                        }
                    }

                }

            }
        }
        if (!isFound) {
            System.out.println("Задач с таким номером не найдено.");
        }
    }

    public void printUpdateMenuForSubTasks() {
        System.out.println("1. Изменить название.");
        System.out.println("2. Изменить описание.");
        System.out.println("3. Изменить статус.");
    }

    public void updateSubTaskName(int i, int j) {
        System.out.println("Введите новое название подзадачи:");
        scanner.nextLine();
        String newName = scanner.nextLine();
        ((Epic) tasks.get(i)).subTasks.get(j).setName(newName);
        System.out.println("Название изменено.");
    }

    public void updateSubTaskDescription(int i, int j) {
        System.out.println("Введите новое описание подзадачи:");
        scanner.nextLine();
        String newDescription = scanner.nextLine();
        ((Epic) tasks.get(i)).subTasks.get(j).setDescription(newDescription);
        System.out.println("Описание изменено.");
    }

    public void updateSubTaskStatus(int i, int j) {
        System.out.println("Выберите новый статус:");
        System.out.println("1. IN_PROGRESS");
        System.out.println("2. DONE");
        String newStatus = scanner.next();
        if (newStatus.equals("1")) {
            ((Epic) tasks.get(i)).subTasks.get(j).setStatus(Status.IN_PROGRESS);
            System.out.println("Статус изменен.");
        } else if (newStatus.equals("2")) {
            ((Epic) tasks.get(i)).subTasks.get(j).setStatus(Status.DONE);
            System.out.println("Статус изменен.");
        } else {
            System.out.println("Такой команды нет.");
        }

        updateEpicStatus(i);
    }

    private void updateEpicStatus(int i) {
        boolean isInProgress = false;
        for (int k = 0; k < ((Epic) tasks.get(i)).subTasks.size(); k++) {
            if (((Epic) tasks.get(i)).subTasks.get(k).getStatus() != Status.NEW) {
                isInProgress = true;
            }
        }
        if (isInProgress) {
            tasks.get(i).setStatus(Status.IN_PROGRESS);
        }

        boolean isDone = true;
        for (int z = 0; z < ((Epic) tasks.get(i)).subTasks.size(); z++) {
            if (((Epic) tasks.get(i)).subTasks.get(z).getStatus() != Status.DONE) {
                isDone = false;
            }
        }
        if (isDone) {
            tasks.get(i).setStatus(Status.DONE);
        }
    }
}
