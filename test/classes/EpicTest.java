package classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicsWithTheSameIdShouldBeEqual() {
        Epic testEpic = new Epic ("First epic", "First Description");
        Epic testEpic1 = new Epic ("Second epic", "Second description");

        assertEquals(testEpic, testEpic1);
    }
}