package com.aestiel.attendance.proxies;

import com.aestiel.attendance.DTOs.Waypoint.WaypointResponseDTOApi;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class WaypointResponseDeserializer implements JsonDeserializer<WaypointResponseDTOApi> {
    @Override
    public WaypointResponseDTOApi deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
