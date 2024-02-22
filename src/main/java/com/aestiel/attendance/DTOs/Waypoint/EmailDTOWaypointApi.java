package com.aestiel.attendance.DTOs.Waypoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTOWaypointApi {
    private String templateId;
    private String to;
    private VariableDTO variables;
}
