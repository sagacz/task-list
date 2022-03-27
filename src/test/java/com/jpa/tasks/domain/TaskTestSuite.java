package com.jpa.tasks.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskTestSuite {

    @PersistenceUnit
    private EntityManagerFactory emf;

    private List<Long> insertExampleData() {
        Person p1 = new Person(null, "FirstName2", "LastName2");
        Person p2 = new Person(null, "FirstName3", "LastName3");
        Person p3 = new Person(null, "FirstName4", "LastName4");
        Person p4 = new Person(null, "FirstName5", "LastName5");
        Task t1 = new Task(null, "Task1", "New");
        Task t2 = new Task(null, "Task2", "New");
        Task t3 = new Task(null, "Task3", "InProgress");
        Task t4 = new Task(null, "Task4", "New");
        Task t5 = new Task(null, "Task5", "InProgress");
        Task t6 = new Task(null, "Task6", "New");
        Task t7 = new Task(null, "Task7", "New");
        t1.getPersons().add(p1);
        t2.getPersons().addAll(List.of(p1,p2));
        t3.getPersons().add(p3);
        t4.getPersons().add(p1);
        t1.getSubTasks().addAll(List.of(t2,t3,t4));
        t5.getPersons().addAll(List.of(p3,p4));
        t6.getPersons().add(p1);
        t7.getPersons().add(p2);
        t5.getSubTasks().addAll(List.of(t6,t7));

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.persist(t1);
        em.persist(t2);
        em.persist(t3);
        em.persist(t4);
        em.persist(t5);
        em.persist(t6);
        em.persist(t7);
        em.flush();
        em.getTransaction().commit();
        em.close();

        return List.of(t1.getId(), t2.getId(), t3.getId(), t4.getId(), t5.getId(), t6.getId(), t7.getId());
    }

    @Test
    public void addTasksTest(){
        //Given
        List<Long> insertedTasks = insertExampleData();
        EntityManager em = emf.createEntityManager();

        //When
        System.out.println("**************** BEGIN OF FETCHING *******************");
        System.out.println("**** STEP 1 - query for task ****");

        TypedQuery<Task> query = em.createQuery(
                "from Task "
                        + "where id in (" + taskIds(insertedTasks) + ")",
                Task.class);

        List<Task> tasks = query.getResultList();
        EntityGraph<Task> eg = em.createEntityGraph(Task.class);


        eg.addSubgraph("subTasks").addAttributeNodes("persons");
        eg.addAttributeNodes("persons");
        query.setHint("javax.persistence.fetchgraph", eg);

        for(Task task: tasks){
            System.out.println("*** STEP 2 – read data from task ***");
            System.out.println(task);
            for(Person person: task.getPersons()){
                System.out.println("*** STEP 3 – read the person data ***");
                System.out.println(person);
            }
            for(Task subtask: task.getSubTasks()){
                System.out.println("*** STEP 4 – read the subtask data ***");
                System.out.println(subtask);
                for(Person person: subtask.getPersons()){
                    System.out.println("*** STEP 5 – read the person data ***");
                    System.out.println(person);
                }
            }
        }
        System.out.println("****************** END OF FETCHING *******************");

        //Then
        //Here should be some assertions and the clean up performed
    }

    private String taskIds(List<Long> insertedTasks) {
        return insertedTasks.stream()
                .map(n -> "" + n)
                .collect(Collectors.joining(","));
    }
}