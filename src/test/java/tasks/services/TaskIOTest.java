package tasks.services;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TaskDTO;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskIOTest {
    private ObservableList<Task> tasks;

    @BeforeEach
    void setUp() {
        ArrayTaskList taskList = new ArrayTaskList();
        TasksService service = new TasksService(taskList);
        tasks = service.getObservableList();
    }

    @AfterEach
    void tearDown() {
        tasks.clear();
    }

    @Test
    @DisplayName("Add Valid Task ECP")
    void testECP_1() {
        assertEquals(tasks.size(), 0);
        TaskDTO taskDTO = new TaskDTO("Meeting", new Date(2025, 3, 25, 10, 0), null, null, true);
        TaskIO.insertTask(taskDTO, tasks);
        assertEquals(tasks.size(), 1);
    }

    @Test
    @DisplayName("Add Invalid Task ECP")
    void testECP_2() {
        assertEquals(tasks.size(), 0);
        TaskDTO taskDTO = new TaskDTO("", new Date(2025, 3, 25, 10, 0), null, 0, true);
        try {
            TaskIO.insertTask(taskDTO, tasks);
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getMessage(), "Invalid combination found, either must of them must be null or none of them");
        }
        assertEquals(tasks.size(), 0);
    }

    @Test
    @DisplayName("Add Valid Task BVA")
    void testBVA_1() {
        assertEquals(tasks.size(), 0);
        TaskDTO taskDTO = new TaskDTO("Meeting", new Date(2025, 1, 1, 0, 0), new Date(2025, 1, 1, 0, 1), 1, true);
        TaskIO.insertTask(taskDTO, tasks);
        assertEquals(tasks.size(), 1);
    }

    @Test
    @DisplayName("Add Invalid Task BVA")
    void testBVA_2() {
        assertEquals(tasks.size(), 0);
        TaskDTO taskDTO = new TaskDTO("Meeting", new Date(2025, 1, 1, 0, 0), new Date(2024, 12, 31, 23, 59), -1, true);
        try {
            TaskIO.insertTask(taskDTO, tasks);
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getMessage(), "Start date should be before end");
        }
        assertEquals(tasks.size(), 0);
    }
}