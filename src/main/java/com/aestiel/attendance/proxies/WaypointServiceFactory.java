package com.aestiel.attendance.proxies;

import com.aestiel.attendance.DTOs.Waypoint.WaypointResponseDTOApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.util.Base64;

public class WaypointServiceFactory {

    private static Converter.Factory createGsonConverter(Type type, Object typeAdapter) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(type, typeAdapter);
        Gson gson = gsonBuilder.create();

        return GsonConverterFactory.create(gson);
    }

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(WaypointApi.BASE_URL)
            .addConverterFactory(createGsonConverter(new TypeToken<WaypointResponseDTOApi>() {}.getType(), new WaypointResponseDeserializer()));
    private static Retrofit retrofit = builder.build();

    private static final OkHttpClient.Builder httpClient
            = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        String base64HTTPAuthString = Base64.getEncoder().encodeToString((username + ':' + password).getBytes());

        if ( username != null && password != null) {
            httpClient.interceptors().clear();
            httpClient.addInterceptor( chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Basic " + base64HTTPAuthString)
                        .build();
                return chain.proceed(request);
            });
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
}
