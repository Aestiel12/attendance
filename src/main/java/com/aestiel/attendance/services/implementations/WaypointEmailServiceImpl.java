package com.aestiel.attendance.services.implementations;

import com.aestiel.attendance.DTOs.Waypoint.EmailDTOWaypointApi;
import com.aestiel.attendance.DTOs.Waypoint.VariableDTO;
import com.aestiel.attendance.models.User;
import com.aestiel.attendance.services.WaypointEmailService;
import org.springframework.stereotype.Service;

@Service
public class WaypointEmailServiceImpl implements WaypointEmailService {
    @Override
    public EmailDTOWaypointApi createWelcomeEmail(String templateId, User user) {
        String email = user.getEmail();
        return new EmailDTOWaypointApi(
                templateId,
                email,
                new VariableDTO(email.substring(0, email.indexOf("@"))));
    }
}
