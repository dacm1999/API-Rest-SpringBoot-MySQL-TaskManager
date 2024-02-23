package com.dacm.taskManager.entity;

import com.dacm.taskManager.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Enumerated(EnumType.STRING)
    public Status status;

    @Column(name = "creation_date")
    public String creation_date;

    @Column(name = "due_date")
    public String due_date;

    @Column(name = "user_id")
    public int user_id;

    @Column(name = "priority_id")
    public int priority_id;
}
