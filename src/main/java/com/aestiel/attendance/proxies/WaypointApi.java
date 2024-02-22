package com.aestiel.attendance.proxies;
import com.aestiel.attendance.DTOs.Waypoint.EmailDTOWaypointApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WaypointApi {
    String BASE_URL = "https://live.waypointapi.com/v1/";

    @POST("email_messages")
    Call<ResponseBody> sendEmail(@Body EmailDTOWaypointApi emailDTO);
}
