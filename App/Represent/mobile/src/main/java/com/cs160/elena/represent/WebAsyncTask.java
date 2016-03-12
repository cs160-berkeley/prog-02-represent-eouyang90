package com.cs160.elena.represent;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by elena on 3/11/16.
 */
public class WebAsyncTask extends AsyncTask<String, Void, JSONObject> {
    private Exception exception;

    public AsyncResponse delegate = null;

    public WebAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String serviceUrl = (String) params[0];
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(5*1000);
            urlConnection.setReadTimeout(10*1000);

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
                Log.d("T", "Http unauthorized");
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }
            Log.d("T", "Call was successful");
            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));

        } catch (MalformedURLException e) {
            // URL is invalid
            Log.d("T", "Invalid url");
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            Log.d("T", "Timeout");
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
            Log.d("T", "IOexception");
        } catch (JSONException e) {
            // response body is no valid JSON string
            Log.d("T", "Json exception");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        delegate.processFinish(result);
    }
}