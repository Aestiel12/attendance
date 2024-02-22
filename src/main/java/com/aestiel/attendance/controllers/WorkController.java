package com.aestiel.attendance.controllers;

import com.aestiel.attendance.DTOs.WorkLogDTO;
import com.aestiel.attendance.exceptions.ValidationAppException;
import com.aestiel.attendance.models.User;
import com.aestiel.attendance.services.UserService;
import com.aestiel.attendance.services.WorkService;
import com.aestiel.attendance.services.implementations.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/work")
public class WorkController {
    private final WorkService workService;
    private final UserService userService;

    public WorkController(WorkService workService, UserService userService) {
        this.workService = workService;
        this.userService = userService;
    }

    @GetMapping("/worklogs")
    public ResponseEntity<?> getWorkLogs(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.findById(userDetails.getId());

        List<WorkLogDTO> workLogs = workService.findAllWorkLogDTOsByUser(user);

        return ResponseEntity.status(200).body(workLogs);
    }



    @PostMapping(value = "/add-worklog", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addWorkLog(
            @RequestParam(value = "start", required = false) LocalDateTime start,
            @RequestParam(value = "end", required = false) LocalDateTime end,
            @RequestPart(value = "activity", required = false) String activity,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws ValidationAppException {
        User user = userService.findById(userDetails.getId());
        if(activity == null || activity.isBlank()) {
            throw new ValidationAppException("You need to choose your activity.");
        }
        if(start == null) {
            throw new ValidationAppException("You need to have start time for your activity.");
        }

        workService.saveWorkLog(start, end, activity, user);

        return ResponseEntity.status(204).build();
    }

    @PostMapping(value = "/add-activity", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addActivity(
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "isWork") String isWork,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws ValidationAppException {
        User user = userService.findById(userDetails.getId());
        if(name.isBlank()) {
            throw new ValidationAppException("You need to name your activity.");
        }
        boolean isWorkBoolean = "true".equals(isWork);
        workService.saveActivity(name, isWorkBoolean, user);
        return ResponseEntity.status(204).build();
    }
}
