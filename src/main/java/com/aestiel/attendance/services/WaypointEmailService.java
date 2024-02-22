package com.aestiel.attendance.services;

import com.aestiel.attendance.DTOs.Waypoint.EmailDTOWaypointApi;
import com.aestiel.attendance.models.User;

public interface WaypointEmailService {
    EmailDTOWaypointApi createWelcomeEmail(String templateId, User user);
}
