package tasks.services;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import tasks.model.Task;

import java.util.*;

public class TasksOperations {
    public final List<Task> tasks;
    private static final Logger log = Logger.getLogger( TasksOperations.class.getName( ) );


    public TasksOperations(ObservableList<Task> tasksList){
        tasks=new ArrayList<>();
        tasks.addAll(tasksList);
    }
    public Iterable<Task> incoming(Date start, Date end){
        log.info(start);
        log.info(end);
        ArrayList<Task> incomingTasks = new ArrayList<>();
        if (end != null && start != null) {
            for (Task t : tasks) {
                Date nextTime = t.nextTimeAfter(start);
                if (nextTime != null && (nextTime.before(end) || nextTime.equals(end))) {
                    incomingTasks.add(t);
                    log.info(t.getTitle());
                }
            }
        }
        if (incomingTasks.isEmpty()) {
            log.info("No tasks found in the specified range.");
            return Collections.emptyList();
        }
        return incomingTasks;
    }
    public SortedMap<Date, Set<Task>> calendar( Date start, Date end){
        Iterable<Task> incomingTasks = incoming(start, end);
        TreeMap<Date, Set<Task>> calendar = new TreeMap<>();

        for (Task t : incomingTasks){
            Date nextTimeAfter = t.nextTimeAfter(start);
            while (nextTimeAfter!= null && (nextTimeAfter.before(end) || nextTimeAfter.equals(end))){
                if (calendar.containsKey(nextTimeAfter)){
                    calendar.get(nextTimeAfter).add(t);
                }
                else {
                    HashSet<Task> oneDateTasks = new HashSet<>();
                    oneDateTasks.add(t);
                    calendar.put(nextTimeAfter,oneDateTasks);
                }
                nextTimeAfter = t.nextTimeAfter(nextTimeAfter);
            }
        }
        return calendar;
    }
}