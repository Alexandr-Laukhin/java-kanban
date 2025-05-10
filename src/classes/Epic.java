package classes;

import main.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Epic extends Task {

    private ArrayList<Integer> subTasksID = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
    }

    public ArrayList<Integer> getSubTasksID() {
        return subTasksID;
    }

    public void setSubTasksID(ArrayList<Integer> subTasksID) {
        this.subTasksID = subTasksID;
    }

    public void addSubTaskToList(int subTaskId) {
        subTasksID.add(subTaskId);
    }

    private Optional<Duration> getSubTaskDuration(Integer subTaskId, TaskManager taskManager) {

        SubTask subTask = taskManager.getSubTaskByID(subTaskId);
        Duration duration = subTask.getDuration();

        if (duration == null) {
            System.err.println("У подзадачи с id " + subTaskId + " не задана длительность");
            return Optional.empty();
        }

        return Optional.of(duration);
    }

    @Override
    public LocalDateTime getEndTime(TaskManager taskManager) {

        if (startTime == null) {
            throw new NullPointerException("Не задано время старта эпика");
        }

        Duration duration = Optional.ofNullable(subTasksID)
                .filter(subTasksID -> !subTasksID.isEmpty())
                .map(subTasksID -> subTasksID.stream()
                        .flatMap(id -> getSubTaskDuration(id, taskManager).stream())
                        .reduce(Duration.ZERO, Duration::plus))
                .orElse(Duration.ZERO);

        return startTime.plus(duration);
        // Тут не был уверен, что делать, в случае, если у всех подзадач не заполнена
        // длительность. По идее, если у всех подзадач нет длительности, то эпик, длительность которого состоит
        // как раз из длительности подзадач, не должен иметь длительности вообще, так как он не совсем
        // самостоятельная единица. Поэтому я решил вернуть startTime. Если неправильно, напиши, я переделаю.
    }

    @Override
    public TaskTypes getType() {
        return TaskTypes.EPIC;
    }
}
