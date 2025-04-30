package tasks.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import tasks.repository.ArrayTaskList;
import tasks.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TasksServiceTest {
    private TasksService tasksService;

    @Mock
    private ArrayTaskList mockTaskList;

    @Mock
    private Task mockTask;

    @Spy
    private ArrayList<Task> spyTaskList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // Create service with mock repository
        tasksService = new TasksService(mockTaskList);
    }


    @Test
    void testGetIntervalInHours() {
        when(mockTask.getRepeatInterval()).thenReturn(3665);

        String result = tasksService.getIntervalInHours(mockTask);

        assertEquals("01:01", result);

        verify(mockTask).getRepeatInterval();
    }

    @Test
    void testParseFromStringToSeconds() {
        String timeString = "02:30";
        int expectedSeconds = (2 * 60 + 30) * 60;

        int result = tasksService.parseFromStringToSeconds(timeString);

        assertEquals(expectedSeconds, result);
    }

    @Test
    void testFilterTasks() {
        Date start = new Date();
        Date end = new Date(start.getTime() + 86400000);

        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);

        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.add(task2);
        when(mockTaskList.getAll()).thenReturn(allTasks);

        Iterable<Task> result = tasksService.filterTasks(start, end);

        verify(mockTaskList).getAll();

        assertNotNull(result);
    }
}