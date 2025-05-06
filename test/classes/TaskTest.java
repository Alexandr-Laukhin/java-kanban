package classes;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void tasksWithTheSameIdShouldBeEqual() {
        Task testTask = new Task("First task", "First Description", Duration.ofMinutes(10));
        Task testTask1 = new Task("Second task", "Second description", Duration.ofMinutes(10));

        assertEquals(testTask, testTask1);
    }

}