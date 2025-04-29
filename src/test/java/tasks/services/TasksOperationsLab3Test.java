package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.model.Task;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class TasksOperationsLab3Test {
    @Test
    @DisplayName("F02_TC01: Empty tasks list should return empty list")
    void testF02_TC01_EmptyTasksList() {
        // Arrange
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        TasksOperations tasksOperations = new TasksOperations(tasksList);
        Date start = createDate(2000, 4, 15, 12, 30);
        Date end = createDate(2000, 4, 20, 12, 30);

        // Act
        Iterable<Task> result = tasksOperations.incoming(start, end);

        // Assert
        assertTrue(StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList()).isEmpty(),
                "Should return an empty list when no tasks exist");
    }

    @Test
    @DisplayName("F02_TC02: Null start and end dates should return empty list")
    void testF02_TC02_NullDates() {
        // Arrange
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        Task task = new Task("Test Task", createDate(2000, 4, 15, 12, 30));
        tasksList.add(task);
        TasksOperations tasksOperations = new TasksOperations(tasksList);

        // Act
        Iterable<Task> result = tasksOperations.incoming(null, null);

        // Assert
        assertTrue(StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList()).isEmpty(),
                "Should return an empty list when both start and end are null");
    }

    @Test
    @DisplayName("F02_TC03: Tasks within date range should be returned")
    void testF02_TC03_TasksWithinRange() {
        // Arrange
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        Task task1 = new Task("Task1", createDate(2000, 4, 15, 12, 30));
        Task task2 = new Task("Task2", createDate(2000, 4, 20, 12, 30));
        Task task3 = new Task("Task2", createDate(2000, 4, 25, 12, 30));
        Task task4 = new Task("Task2", createDate(2000, 4, 29, 12, 30));
        task1.setActive(true);
        task2.setActive(true);
        task3.setActive(true);
        task4.setActive(true);
        tasksList.addAll(task1, task2, task3, task4);
        TasksOperations tasksOperations = new TasksOperations(tasksList);

        Date start = createDate(2000, 4, 21, 12, 30);
        Date end = createDate(2000, 4, 30, 12, 30);

        // Act
        Iterable<Task> result = tasksOperations.incoming(start, end);
        List<Task> incomingTasks = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        assertEquals(2, incomingTasks.size(), "Should return all tasks within the date range");
        assertTrue(incomingTasks.containsAll(Arrays.asList(task3, task4)),
                "Should contain all tasks within the range");
    }

    @Test
    @DisplayName("F02_TC04: Tasks outside date range should not be returned")
    void testF02_TC04_TasksOutsideRange() {
        // Arrange
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        Task task1 = new Task("Task1", createDate(2000, 4, 15, 12, 30));
        Task task2 = new Task("Task2", createDate(2000, 4, 20, 12, 30));
        Task task3 = new Task("Task3", createDate(2000, 4, 25, 12, 30));
        Task task4 = new Task("Task4", createDate(2000, 4, 30, 12, 30));
        tasksList.addAll(task1, task2, task3, task4);
        TasksOperations tasksOperations = new TasksOperations(tasksList);

        Date start = createDate(2000, 4, 26, 12, 30);
        Date end = createDate(2000, 4, 27, 12, 30);

        // Act
        Iterable<Task> result = tasksOperations.incoming(start, end);
        List<Task> incomingTasks = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        assertTrue(incomingTasks.isEmpty(), "Should return empty list for tasks outside range");
    }

    @Test
    @DisplayName("F02_TC05: Null start date with tasks")
    void testF02_TC05_NullStartDate() {
        // Arrange
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        Task task1 = new Task("Task1", createDate(2000, 4, 15, 12, 30));
        Task task2 = new Task("Task2", createDate(2000, 4, 20, 12, 30));
        Task task3 = new Task("Task3", createDate(2000, 4, 25, 12, 30));
        Task task4 = new Task("Task4", createDate(2000, 4, 30, 12, 30));
        tasksList.addAll(task1, task2, task3, task4);
        TasksOperations tasksOperations = new TasksOperations(tasksList);

        Date end = createDate(2000, 4, 22, 12, 30);

        // Act
        Iterable<Task> result = tasksOperations.incoming(null, end);
        List<Task> incomingTasks = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        assertTrue(incomingTasks.isEmpty(), "Should return empty list when start date is null");
    }

    @Test
    @DisplayName("F02_TC06: Null end date with tasks")
    void testF02_TC06_NullEndDate() {
        // Arrange
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        Task task1 = new Task("Task1", createDate(2000, 4, 15, 12, 30));
        Task task2 = new Task("Task2", createDate(2000, 4, 20, 12, 30));
        Task task3 = new Task("Task3", createDate(2000, 4, 25, 12, 30));
        Task task4 = new Task("Task4", createDate(2000, 4, 30, 12, 30));
        tasksList.addAll(task1, task2, task3, task4);
        TasksOperations tasksOperations = new TasksOperations(tasksList);

        Date start = createDate(2000, 4, 22, 12, 30);

        // Act
        Iterable<Task> result = tasksOperations.incoming(start, null);
        List<Task> incomingTasks = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        assertTrue(incomingTasks.isEmpty(), "Should return empty list when end date is null");
    }

    @Test
    @DisplayName("F02_TC07: Tasks with specific date handling")
    void testF02_TC07_SpecificDateHandling() {
        // Arrange
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        Task task = new Task("Task1", createDate(2000, 4, 20, 12, 30));
        task.setActive(true);
        tasksList.add(task);
        TasksOperations tasksOperations = new TasksOperations(tasksList);

        Date start = createDate(2000, 4, 17, 12, 30);
        Date end = createDate(2000, 4, 26, 12, 30);

        // Act
        Iterable<Task> result = tasksOperations.incoming(start, end);
        List<Task> incomingTasks = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        assertEquals(1, incomingTasks.size(), "Should return task within the specified range");
        assertTrue(incomingTasks.contains(task), "Should contain the task within the range");
    }

    @Test
    @DisplayName("F02_TC08: Tasks with multiple scenarios")
    void testF02_TC08_MultipleScenarios() {
        // Arrange
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        Task task1 = new Task("Task1", createDate(2000, 4, 20, 12, 30));
        task1.setActive(true);
        Task task2 = new Task("Task2", createDate(2000, 4, 21, 12, 30));
        task2.setActive(true);
        tasksList.addAll(task1, task2);
        TasksOperations tasksOperations = new TasksOperations(tasksList);

        Date start = createDate(2000, 4, 17, 12, 30);
        Date end = createDate(2000, 4, 26, 12, 30);

        // Act
        Iterable<Task> result = tasksOperations.incoming(start, end);
        List<Task> incomingTasks = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        assertEquals(2, incomingTasks.size(), "Should return tasks within the specified range");
        assertTrue(incomingTasks.containsAll(Arrays.asList(task1, task2)), "Should contain both tasks within the range");
    }

    private static Date createDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, minute);
        return cal.getTime();
    }
}