package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import tasks.repository.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.ArrayList;
import java.util.List;

/**
 * Phase 2
 * */
@ExtendWith(MockitoExtension.class)
class RWithSIntegrationTest {

    private ArrayTaskList taskList;

    private TasksService tasksService;

    @Mock
    private Task mockTask1;

    @Mock
    private Task mockTask2;

    @BeforeEach
    void setUp() {
        taskList = new ArrayTaskList();

        tasksService = new TasksService(taskList);
    }

    @Test
    void testAddTaskAndRetrieveViaService() {
        tasksService.addTask(mockTask1);

        assertEquals(1, tasksService.getObservableList().size());
        assertEquals(mockTask1, tasksService.getObservableList().get(0));

        tasksService.addTask(mockTask2);

        assertEquals(2, tasksService.getObservableList().size());
        assertEquals(mockTask1, tasksService.getObservableList().get(0));
        assertEquals(mockTask2, tasksService.getObservableList().get(1));

        List<Task> allTasks = new ArrayList<>();
        tasksService.getTasks().forEach(allTasks::add);
        assertEquals(2, allTasks.size());
        assertEquals(mockTask1, allTasks.get(0));
        assertEquals(mockTask2, allTasks.get(1));
    }

    @Test
    void testTimeIntervalIntegration() {
        when(mockTask1.getRepeatInterval()).thenReturn(3600);
        when(mockTask2.getRepeatInterval()).thenReturn(7200);

        tasksService.addTask(mockTask1);

        String intervalString = tasksService.getIntervalInHours(mockTask1);

        assertEquals("01:00", intervalString);

        int parsedSeconds = tasksService.parseFromStringToSeconds(intervalString);
        assertEquals(3600, parsedSeconds);

        String interval2 = tasksService.getIntervalInHours(mockTask2);
        assertEquals("02:00", interval2);
        assertEquals(7200, tasksService.parseFromStringToSeconds(interval2));
    }
}