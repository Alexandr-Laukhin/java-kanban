package classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    void managersLoadObjectTest() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void managersLoadHistoryTest() {
        assertNotNull(Managers.getDefaultHistory());
    }
}