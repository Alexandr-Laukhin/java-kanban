import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<SubTask> subTasks = new ArrayList<>();
    SubTask subTask;

    public Epic(String name, String description) {
        super(name, description);
    }

    public void createSubTask(String subTaskName, String subTaskDescription) {
        subTask = new SubTask(subTaskName, subTaskDescription);         // // создаем подзадачу
        subTasks.add(subTask);                                          // // добавляем ее к списку подзадач конкретного эпика

        System.out.println("Подзадача добавлена.");
    }

    public void printThisEpicSubTasks() {
        for (SubTask task : subTasks) {
            System.out.println(task.getName() + ". Номер подзадачи: " + task.getNumber() + ". Статус подзадачи: " + task.getStatus() + ".");
        }
    }

}
