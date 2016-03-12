package com.cs160.elena.represent;

import org.json.JSONObject;

public interface AsyncResponse {
    void processFinish(JSONObject output);
}