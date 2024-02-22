package com.aestiel.attendance.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class WorkLogDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String activityName;
    private boolean isWork;
}
