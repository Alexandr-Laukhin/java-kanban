package classes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private Status status = Status.NEW;
    private int id;
    private Duration duration;
    private LocalDateTime startTime = LocalDateTime.now();

    public Task(String name, String description, Duration inMinutes) {
        this.name = name;
        this.description = description;
        this.duration = inMinutes;
    }

    public Task(String name, String description, Status status, int id, Duration inMinutes) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = inMinutes;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    public String getName() {
        return name;
    }

    public TaskTypes getType() {
        return TaskTypes.TASK;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int counter) {
        this.id = counter;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
