package com.aestiel.attendance.services.implementations;

import com.aestiel.attendance.DTOs.WorkLogDTO;
import com.aestiel.attendance.exceptions.NotFoundAppException;
import com.aestiel.attendance.exceptions.ValidationAppException;
import com.aestiel.attendance.models.Activity;
import com.aestiel.attendance.models.User;
import com.aestiel.attendance.models.WorkLog;
import com.aestiel.attendance.repositories.ActivityRepository;
import com.aestiel.attendance.repositories.WorkLogRepository;
import com.aestiel.attendance.services.WorkService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkServiceImpl implements WorkService {
    private final ActivityRepository activityRepository;
    private final WorkLogRepository workLogRepository;

    public WorkServiceImpl(ActivityRepository activityRepository, WorkLogRepository workLogRepository) {
        this.activityRepository = activityRepository;
        this.workLogRepository = workLogRepository;
    }

    @Override
    public void saveActivity(String name, boolean isWork, User user) throws ValidationAppException {
        if (existsActivityByNameAndUser(user, name)) {
            throw new ValidationAppException("You already have this activity.");
        }
        activityRepository.save(new Activity(name, isWork, user));
    }

    @Override
    public boolean existsActivityByNameAndUser(User user, String name){
        return activityRepository.existsByUserAndName(user, name);
    }

    @Override
    public List<WorkLogDTO> findAllWorkLogDTOsByUser(User user){
        return workLogRepository.findAllWorkLogDTOsByUserId(user.getId());
    }

    /*public List<WorkLogDTO> findAllByUserAndStartIsAfterDTO(User user, LocalDateTime after){
        return workLogRepository.findAllByUserAndStartIsAfter(user, after).stream()
                .map(this::WorkLogDTO).toList();
    }*/

    @Override
    public boolean existsWorkLogByUserAndStart(User user, LocalDateTime start){
        return workLogRepository.existsByUserAndStart(user, start);
    }

    @Override
    public void saveWorkLog(LocalDateTime start, LocalDateTime end, String activityName, User user)
            throws ValidationAppException, NotFoundAppException {
        Activity activity = activityRepository.findByUserAndName(user, activityName);
        if (activity == null) {
            throw new NotFoundAppException("This activity does not exist for your user.");
        }

        if (!existsWorkLogByUserAndStart(user, start)) {
            workLogRepository.save(new WorkLog(start, end, user, activity));
        } else if (existsWorkLogByUserAndStart(user, start) && end != null) {
            WorkLog workLog = workLogRepository.findByUserAndStart(user, start);
            if (workLog.getEnd() == null) {
                workLog.setEnd(end);
                workLogRepository.save(workLog);
            } else {
                throw new ValidationAppException("This worklog is already complete and cannot be updated via this form.");
            }
        }
    }

    @Override
    public void createBasicActivities(User user){
        activityRepository.save(new Activity("office", true, user));
        activityRepository.save(new Activity("home", true, user));
        activityRepository.save(new Activity("annual leave", false, user));
    }
}
