package com.aestiel.attendance.repositories;

import com.aestiel.attendance.models.Activity;
import com.aestiel.attendance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    boolean existsByUserAndName(User user, String name);
    Activity findByUserAndName(User user, String name);
}
