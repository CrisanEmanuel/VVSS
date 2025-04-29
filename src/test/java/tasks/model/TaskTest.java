package tasks.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class TaskTest {
    private Task task;
    private Date time;

    @BeforeEach
    public void setUp() {
        time = new Date();
        task = new Task("Test Task", time);
    }

    @Test
    @DisplayName("Task constructor should initialize properties correctly")
    public void constructorShouldInitializePropertiesCorrectly() {
        assertEquals("Test Task", task.getTitle());
        assertEquals(time, task.getTime());
        assertEquals(time, task.getStartTime());
        assertEquals(time, task.getEndTime());
        assertEquals(0, task.getRepeatInterval());
        assertFalse(task.isActive());
        assertFalse(task.isRepeated());
    }

    @Test
    @DisplayName("Setting task title should update the title property")
    public void settingTitleShouldUpdateTitleProperty() {
        task.setTitle("Updated Task");
        assertEquals("Updated Task", task.getTitle());
    }

    @Test
    @DisplayName("Setting task active state should update active property")
    public void settingActiveShouldUpdateActiveProperty() {
        task.setActive(true);
        assertTrue(task.isActive());
    }

    @Test
    @DisplayName("Setting time with interval should make task repeated")
    public void settingTimeWithIntervalShouldMakeTaskRepeated() {
        // Test setting time with repeated interval
        Date start = new Date(time.getTime() + 3600000);
        Date end = new Date(time.getTime() + 7200000);
        int interval = 600;

        task.setTime(start, end, interval);
        assertEquals(start, task.getStartTime());
        assertEquals(end, task.getEndTime());
        assertEquals(interval, task.getRepeatInterval());
        assertTrue(task.isRepeated());
    }
}