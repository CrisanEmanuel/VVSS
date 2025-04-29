package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import tasks.repository.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Phase 3
 * */
class EWithRAndSIntegrationTest {

    private Task task1;
    private Task task2;

    private ArrayTaskList taskList;

    private TasksService tasksService;

    @BeforeEach
    void setUp() {
        Date currentTime = new Date();
        Date startTime = new Date(currentTime.getTime() + 3600000);
        Date endTime = new Date(currentTime.getTime() + 7200000);

        task1 = new Task("Integration Test Task 1", startTime);
        task1.setActive(true);

        task2 = new Task("Integration Test Task 2", startTime, endTime, 1800);
        task2.setActive(true);

        taskList = new ArrayTaskList();

        tasksService = new TasksService(taskList);
    }

    @Test
    void testFullTaskLifecycleIntegration() {
        tasksService.addTask(task1);
        tasksService.addTask(task2);

        assertEquals(2, tasksService.getObservableList().size());

        List<Task> retrievedTasks = new ArrayList<>();
        tasksService.getTasks().forEach(retrievedTasks::add);

        assertEquals(2, retrievedTasks.size());

        Task retrievedTask1 = retrievedTasks.get(0);
        assertEquals("Integration Test Task 1", retrievedTask1.getTitle());
        assertTrue(retrievedTask1.isActive());
        assertFalse(retrievedTask1.isRepeated());
        assertEquals(0, retrievedTask1.getRepeatInterval());

        Task retrievedTask2 = retrievedTasks.get(1);
        assertEquals("Integration Test Task 2", retrievedTask2.getTitle());
        assertTrue(retrievedTask2.isActive());
        assertTrue(retrievedTask2.isRepeated());
        assertEquals(1800, retrievedTask2.getRepeatInterval());
    }

    @Test
    void testTaskFilteringAndTimeCalculation() {
        tasksService.addTask(task1);
        tasksService.addTask(task2);

        String intervalString = tasksService.getIntervalInHours(task2);
        assertEquals("00:30", intervalString);

        assertEquals(1800, tasksService.parseFromStringToSeconds(intervalString));

        Date now = new Date();
        Date future = new Date(now.getTime() + 10000000);

        Iterable<Task> filteredTasks = tasksService.filterTasks(now, future);

        List<Task> filteredList = new ArrayList<>();
        filteredTasks.forEach(filteredList::add);

        assertEquals(2, filteredList.size());

        boolean foundTask1 = false;
        boolean foundTask2 = false;

        for (Task task : filteredList) {
            if (task.getTitle().equals(task1.getTitle())) {
                foundTask1 = true;
            }
            if (task.getTitle().equals(task2.getTitle())) {
                foundTask2 = true;
            }
        }

        assertTrue(foundTask1, "Task 1 should be in filtered results");
        assertTrue(foundTask2, "Task 2 should be in filtered results");
    }
}