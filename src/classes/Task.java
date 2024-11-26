package classes;

import main.Status;

import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private Status status = Status.NEW;
    private int id;    // хотел сделать эту переменную final, но не понял как. Если при создании я передаю объект (т.е он уже создан кем-то),
                       // то там не может быть номера id, тк неизвестно заранее какой из объектов какое место займет, и какой номер получит.
                       // поэтому в метод createTask() пришлось добавить setId(counter), который почему-то не хочет работать если id идет как
                       // final. И вот тут не понял, final же должен реагировать на первое изменение переменной, и потом блокироваться.
                       // Почему он не хочет работать с методом, пусть и по одноразовому принципу? И как это обойти?


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
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
