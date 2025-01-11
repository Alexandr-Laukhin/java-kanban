package classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void tasksWithTheSameIdShouldBeEqual() {
        Task testTask = new Task("First task", "First Description");
        Task testTask1 = new Task("Second task", "Second description");

        assertEquals(testTask, testTask1);
    }

}