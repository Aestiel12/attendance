package com.aestiel.attendance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    boolean isWork;

    @OneToMany(mappedBy = "activity")
    Set<WorkLog> workLogs;

    @ManyToOne
    private User user;

    public Activity(String name, boolean isWork, User user) {
        this.name = name;
        this.isWork = isWork;
        this.user = user;
    }
}
