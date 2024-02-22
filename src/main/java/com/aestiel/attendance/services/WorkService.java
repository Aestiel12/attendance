package com.aestiel.attendance.services;

import com.aestiel.attendance.DTOs.WorkLogDTO;
import com.aestiel.attendance.exceptions.NotFoundAppException;
import com.aestiel.attendance.exceptions.ValidationAppException;
import com.aestiel.attendance.models.User;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkService {
    void saveActivity(String name, boolean isWork, User user) throws ValidationAppException;

    boolean existsActivityByNameAndUser(User user, String name);

    List<WorkLogDTO> findAllWorkLogDTOsByUser(User user);

    boolean existsWorkLogByUserAndStart(User user, LocalDateTime start);

    void saveWorkLog(LocalDateTime start, LocalDateTime end, String activityName, User user) throws ValidationAppException, NotFoundAppException;

    void createBasicActivities(User user);
}

