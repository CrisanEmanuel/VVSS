package tasks.services;
import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import tasks.model.Task;
import tasks.model.TaskDTO;

import static org.junit.jupiter.api.Assertions.*;

class TaskIOTest {
    private ObservableList<Task> tasks;

    @BeforeEach
    void setUp() {
        tasks = FXCollections.observableArrayList();
    }

    @AfterEach
    void tearDown() {
        tasks.clear();
    }

    @Nested
    @Tag("ECP")
    @DisplayName("Equivalence Class Partitioning (ECP) Tests")
    class ECPTests {

        @ParameterizedTest
        @MethodSource("taskDataProvider")
        @DisplayName("ECP: Add Valid Task (Title: Meeting, Start: 25-03-2025)")
        void testECP_Valid1(String title, Date startDate) {
            TaskDTO taskDTO = new TaskDTO(title, startDate, null, null, true);
            assertDoesNotThrow(() -> TaskIO.insertTask(taskDTO, tasks));
            assertEquals(1, tasks.size());
        }

        static Stream<Arguments> taskDataProvider() {
            return Stream.of(
                    org.junit.jupiter.params.provider.Arguments.of("Meeting", new Date(2025 - 1900, Calendar.MARCH, 25, 10, 0))
            );
        }

        @ParameterizedTest
        @CsvSource({
                "Task XYZ, 2025-06-10 14:30, 2025-06-10 15:30, true",
                "Task ABC, 2025-06-11 08:00, 2025-06-11 09:00, false"
        })
        @DisplayName("ECP: Add Valid Task (Title, Start Date, End Date, Active)")
        void testECP_Valid2(String title, String startDate, String endDate, boolean isActive) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);

                TaskDTO taskDTO = new TaskDTO(title, start, end, 1, isActive);
                assertDoesNotThrow(() -> TaskIO.insertTask(taskDTO, tasks));
                assertEquals(1, tasks.size());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Test
        @DisplayName("ECP: Add Invalid Task (Empty Title)")
        void testECP_Invalid() {
            TaskDTO taskDTO = new TaskDTO("", new Date(2025 - 1900, Calendar.APRIL, 15, 12, 0), null, null, true);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> TaskIO.insertTask(taskDTO, tasks));
            assertEquals("Invalid input", exception.getMessage());
            assertEquals(0, tasks.size());
        }
    }

    @Nested
    @Tag("BVA")
    @DisplayName("Boundary Value Analysis (BVA) Tests")
    class BVATests {

        @ParameterizedTest
        @ValueSource(strings = {"A", "B", "C"})
        @DisplayName("BVA: Add Task with Minimum Title Length")
        void testBVA_ValidMinTitle(String title) {
            TaskDTO taskDTO = new TaskDTO(title, new Date(2025 - 1900, Calendar.JANUARY, 1, 0, 0), null, null, true);
            assertDoesNotThrow(() -> TaskIO.insertTask(taskDTO, tasks));
            assertEquals(1, tasks.size());
        }

        @Test
        @DisplayName("BVA: Add Task at Earliest Valid Start Date")
        void testBVA_ValidStartBoundary() {
            TaskDTO taskDTO = new TaskDTO("Task", new Date(2025 - 1900, Calendar.JANUARY, 1, 0, 0), new Date(2025 - 1900, Calendar.JANUARY, 1, 0, 1), 1, true);
            assertDoesNotThrow(() -> TaskIO.insertTask(taskDTO, tasks));
            assertEquals(1, tasks.size());
        }

        @Test
        @DisplayName("BVA: Add Invalid Task (Start Date After End Date)")
        void testBVA_InvalidStartAfterEnd() {
            TaskDTO taskDTO = new TaskDTO("Meeting", new Date(2025 - 1900, Calendar.JANUARY, 2, 0, 0), new Date(2025 - 1900, 1 - 1, 1, 23, 59), 3600, true);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> TaskIO.insertTask(taskDTO, tasks));
            assertEquals("Start date should be before end", exception.getMessage());
            assertEquals(0, tasks.size());
        }
    }
}
