package tasks.model;

import javafx.collections.ObservableList;

import java.util.*;

public class TasksOperations {
    public ArrayList<Task> tasks;

    public TasksOperations(ObservableList<Task> tasksList){
        tasks=new ArrayList<>();
        tasks.addAll(tasksList);
    }

    public Iterable<Task> incoming(Date start, Date end){
        System.out.println(start);
        System.out.println(end);
/*1*/   ArrayList<Task> incomingTasks = new ArrayList<>();
/*2*/       if (end != null && start != null) {
/*3*/           for (Task t : tasks) {
/*4*/           Date nextTime = t.nextTimeAfter(start);
/*5*/               if (nextTime != null && (nextTime.before(end) || nextTime.equals(end))) {
/*6*/               incomingTasks.add(t);
                    System.out.println(t.getTitle());
                }
            }
        }
/*7*/       if (incomingTasks.isEmpty()) {
            System.out.println("No task found in the specific range");
/*8*/       return Collections.emptyList();
        }
/*9*/       return incomingTasks;
/*10*/}

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

