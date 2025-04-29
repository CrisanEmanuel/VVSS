package tasks.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tasks.model.Task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ArrayTaskListTest {
    private ArrayTaskList taskList;

    @Mock
    private Task mockTask1;

    @Mock
    private Task mockTask2;

    @BeforeEach
    public void setUp() {
        taskList = new ArrayTaskList();
    }

    @Test
    public void testAddAndSize() {
        when(mockTask1.getTitle()).thenReturn("Task 1");
        when(mockTask2.getTitle()).thenReturn("Task 2");

        assertEquals(0, taskList.size());

        taskList.add(mockTask1);
        assertEquals(1, taskList.size());

        taskList.add(mockTask2);
        assertEquals(2, taskList.size());

        assertEquals(mockTask1, taskList.getTask(0));
        assertEquals(mockTask2, taskList.getTask(1));

        assertEquals("Task 1", taskList.getTask(0).getTitle());
        assertEquals("Task 2", taskList.getTask(1).getTitle());
    }

    @Test
    public void testRemoveAndGetAll() {
        taskList.add(mockTask1);
        taskList.add(mockTask2);

        List<Task> allTasks = taskList.getAll();
        assertEquals(2, allTasks.size());
        assertEquals(mockTask1, allTasks.get(0));
        assertEquals(mockTask2, allTasks.get(1));

        boolean result = taskList.remove(mockTask1);
        assertTrue(result);
        assertEquals(1, taskList.size());

        allTasks = taskList.getAll();
        assertEquals(1, allTasks.size());
        assertEquals(mockTask2, allTasks.get(0));
    }

    @Test
    public void testIterator() {
        taskList.add(mockTask1);
        taskList.add(mockTask2);

        Iterator<Task> iterator = taskList.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(mockTask1, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(mockTask2, iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCopyConstructor() {
        taskList.add(mockTask1);
        taskList.add(mockTask2);

        ArrayTaskList copy = new ArrayTaskList(taskList);

        assertEquals(taskList.size(), copy.size());
        assertEquals(mockTask1, copy.getTask(0));
        assertEquals(mockTask2, copy.getTask(1));

        taskList.remove(mockTask1);
        assertEquals(1, taskList.size());
        assertEquals(2, copy.size());
    }
}