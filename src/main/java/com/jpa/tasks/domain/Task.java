package com.jpa.tasks.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status;

    @ManyToMany
    List<Person> persons = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "parent_task_id")
    List<Task> subTasks = new ArrayList<>();

    public Task() {
    }

    public Task(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }
}
