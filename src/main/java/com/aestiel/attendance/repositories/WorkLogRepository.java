package com.aestiel.attendance.repositories;

import com.aestiel.attendance.DTOs.WorkLogDTO;
import com.aestiel.attendance.models.User;
import com.aestiel.attendance.models.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    WorkLog findTopWorkLogByStartAndUserAndEnd(LocalDateTime start, User user, LocalDateTime end);

    WorkLog findWorkLogByUserAndEndIsNull(User user);

    List<WorkLog> findAllByUserAndStartIsAfter(User user, LocalDateTime after);

    @Query(value = "SELECT new com.aestiel.attendance.DTOs.WorkLogDTO(" +
            "w.id, " +
            "w.start, " +
            "w.end," +
            "w.activity.name, " +
            "w.activity.isWork) " +
            "FROM WorkLog w " +
            "WHERE w.user.id = ?1 " +
            "ORDER BY w.start DESC")
    List<WorkLogDTO> findAllWorkLogDTOsByUserId(Long userId);

    boolean existsByUserAndStart(User user, LocalDateTime start);

    WorkLog findByUserAndStart(User user, LocalDateTime start);
}
