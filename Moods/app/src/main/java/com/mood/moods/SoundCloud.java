package com.mood.moods;

import retrofit.RestAdapter;

/**
 * Created by Adarsh on 4/13/2016.
 */
public class SoundCloud {
    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder().setEndpoint(Config.API_URL).build();
    private static final SCService SERVICE = REST_ADAPTER.create(SCService.class);

    public static SCService getService() {
        return SERVICE;
    }
}
