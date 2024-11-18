import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String command;
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        while (true) {

            printMenu();
            command = scanner.next();

            switch (command) {
                case "1" -> taskManager.printAllTasks();
                case "2" -> taskManager.createTask();
                case "3" -> taskManager.updateTask();
                case "4" -> taskManager.findTaskByNumber();
                case "5" -> taskManager.deleteTaskByNumber();
                case "6" -> taskManager.tasks.clear();
                case "7" -> taskManager.epicMenu();
                case "8" -> System.exit(0);
                default -> System.out.println("Такой команды нет.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("1. Показать список всех задач (и эпиков, без подзадач).");
        System.out.println("2. Создать задачу. (или эпик, подзадачи создаются внутри эпика");
        System.out.println("3. Обновить задачу. (или эпик, создать подменю)");
        System.out.println("4. Найти задачу по номеру.");
        System.out.println("5. Удалить задачу по номеру.");
        System.out.println("6. Удалить все задачи.");
        System.out.println("7. Меню эпиков.");
        System.out.println("8. Выход.");
    }

}


