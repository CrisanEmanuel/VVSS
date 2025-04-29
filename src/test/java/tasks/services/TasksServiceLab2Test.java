package tasks.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tasks.repository.ArrayTaskList;
import tasks.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TasksService Test Suite")
@TestMethodOrder(OrderAnnotation.class)
class TasksServiceLab2Test {

    private TasksService tasksService;
    private ArrayTaskList taskList;
    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
        taskList = new ArrayTaskList();
        tasksService = new TasksService(taskList);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private Date parseDate(String dateStr) throws ParseException {
        return dateFormat.parse(dateStr);
    }

    @Test
    @Order(1)
    @DisplayName("TC1: Valid task should be added successfully")
    void testAddTask_EcpValidTestCase() throws ParseException {
        // Arrange
        Date start = parseDate("2003-10-21");
        Date end = parseDate("2022-10-10");
        Task task = new Task("TestTask", start, end, 5);

        // Act
        tasksService.addTask(task);

        // Assert
        assertEquals(1, taskList.size());
        assertEquals(task, taskList.getTask(0));
    }

    @Test
    @Order(2)
    @DisplayName("TC2: Empty title should throw IllegalArgumentException")
    void testAddTask_TC1_ECP_EmptyTitle() throws ParseException {
        // Arrange
        Date start = parseDate("2024-04-03");
        Date end = parseDate("2024-04-07");

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("", start, end, 3);
            tasksService.addTask(task);
        });

        // Assert
        assertTrue(exception.getMessage().contains("Title cannot be empty"));
    }

    @Test
    @Order(3)
    @DisplayName("TC3: Invalid start date should throw IllegalArgumentException")
    void testAddTask_EcpInvalidTestCase_InvalidStartDate() throws ParseException {
        // Arrange
        Date start = parseDate("1968-10-21"); // Invalid start date
        Date end = parseDate("2022-10-10");

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("TestTask", start, end, 5);
            tasksService.addTask(task);
        });

        //Assert
        assertTrue(exception.getMessage().contains("Time cannot be negative"));
    }

    @Test
    @Order(4)
    @DisplayName("TC4: Invalid end date should throw IllegalArgumentException")
    void testAddTask_TC2_ECP_InvalidEndDate() throws ParseException {
        // Arrange
        Date start = parseDate("2024-04-03");
        Date end = parseDate("1947-09-08");

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("Alt Titlu", start, end, 4);
            tasksService.addTask(task);
        });

        //Assert
        assertTrue(exception.getMessage().contains("Time cannot be negative"));
    }

    @Test
    @Order(5)
    @DisplayName("TC5: Invalid interval should throw IllegalArgumentException")
    void testAddTask_TC3_ECP_InvalidInterval() throws ParseException {
        // Arrange
        Date start = parseDate("2003-10-21");
        Date end = parseDate("2022-10-10");

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task(" Title", start, end, -2); // Invalid interval
            tasksService.addTask(task);
        });
        //Assert
        assertTrue(exception.getMessage().contains("interval should me > 1"));
    }

    @ParameterizedTest
    @CsvSource({
            "T2, 1970-02-02, 1970-02-03, 1",        // TC7
            "T5, 1970-01-01, 1970-02-03, 2",        // TC10
            "T7, 1971-01-02, 1970-02-03, 8"         // TC12
    })
    @DisplayName("Valid tasks should be added successfully")
    void testAddValidTasks(String title, String startStr, String endStr, int interval) throws ParseException {
        // Arrange
        Date start = parseDate(startStr);
        Date end = parseDate(endStr);
        Task task = new Task(title, start, end, interval);

        // Act
        tasksService.addTask(task);

        // Assert
        assertEquals(1, taskList.size());
        assertEquals(task, taskList.getTask(0));
    }

    @Test
    @DisplayName("BVA Valid") //TC6
    void testAdd_BvaValidTasks() throws ParseException {
        // Arrange
        String title = "TestTask";
        String startStr = "2003-10-21";
        String endStr = "2022-10-10";
        int interval = 1;
        Date start = parseDate(startStr);
        Date end = parseDate(endStr);
        Task task = new Task(title, start, end, interval);

        // Act
        tasksService.addTask(task);

        // Assert
        assertEquals(1, taskList.size());
        assertEquals(task, taskList.getTask(0));
    }

    @Test
    @Order(8)
    @DisplayName("TC8: Interval of 0 should throw IllegalArgumentException")
    void testAddTask_BvaInvalidTestCase() throws ParseException {
        // Arrange3
        Date start = parseDate("2003-10-21");
        Date end = parseDate("2022-10-10");

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("TestTask", start, end, 0);
            tasksService.addTask(task);
        });
        //Assert
        assertTrue(exception.getMessage().contains("interval should me > 1"));
    }

    @Test
    @Order(9)
    @DisplayName("TC9: Negative interval should throw IllegalArgumentException")
    void testAddTask_TC9_BVA_NegativeInterval() throws ParseException {
        // Arrange
        Date start = parseDate("1970-02-02");
        Date end = parseDate("1970-02-03");

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("T4", start, end, -1);
            tasksService.addTask(task);
        });
        //Assert
        assertTrue(exception.getMessage().contains("interval should me > 1"));
    }

    @ParameterizedTest
    @CsvSource({
            "T6, 1969-12-31, 1970-02-03, 4",
            "T8, 1968-12-30, 1970-02-03, 99"
    })
    @DisplayName("TC11 & TC13: Invalid start dates should throw IllegalArgumentException")
    void testAddTask_InvalidStartDates(String title, String startStr, String endStr, int interval) throws ParseException {
        // Arrange
        Date start = parseDate(startStr);
        Date end = parseDate(endStr);

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task(title, start, end, interval);
            tasksService.addTask(task);
        });
        //Assert
        assertTrue(exception.getMessage().contains("Time cannot be negative"));
    }
}