package com.aestiel.attendance.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class WorkLog {

    @Id
    @GeneratedValue
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end;

    @ManyToOne
    private User user;

    @ManyToOne
    private Activity activity;

    public WorkLog(LocalDateTime start, LocalDateTime end, User user, Activity activity) {
        this.start = start;
        this.end = end;
        this.user = user;
        this.activity = activity;
    }
}
