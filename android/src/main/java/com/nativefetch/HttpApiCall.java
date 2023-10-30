package com.nativefetch;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpApiCall extends AsyncTask<HttpApiCall.Params, Void, String> {
  private final static String TAG = "HTTP_API_CALL";

  public static class Params {
    String url;
    String method;
    String contentType;
    HashMap<String, String> requestBodyMap;

    public Params(String url, String method, String contentType, HashMap<String, String> requestBodyMap) {
      this.url = url;
      this.method = method;
      this.contentType = contentType;
      this.requestBodyMap = requestBodyMap;
    }
  }

  public interface ApiResponseListener {
    void onResponseReceived(String response);
    void onError(String errorMessage);
  }

  private final ApiResponseListener responseListener;

  public HttpApiCall(ApiResponseListener responseListener) {
    this.responseListener = responseListener;
  }

  @Override
  protected String doInBackground(Params... params) {
    if (params.length < 1) {
      if (responseListener != null) {
        responseListener.onError("Insufficient parameters provided");
      }
      return null;
    }

    Params apiParams = params[0];
    String apiUrl = apiParams.url;
    String httpMethod = apiParams.method;

    try {
      URL url = new URL(apiUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // Enable input and output streams
      connection.setDoInput(true);
      connection.setDoOutput(true);

      connection.setRequestMethod(httpMethod);

      if ("POST".equalsIgnoreCase(httpMethod) && apiParams.requestBodyMap != null) {
        Log.d(TAG, apiParams.requestBodyMap.toString());

        String postData = "";

        if (apiParams.contentType.equalsIgnoreCase("JSON")) {
           connection.setRequestProperty("Content-Type", "application/json");

          JSONObject jsonObject = new JSONObject(apiParams.requestBodyMap);
          postData = jsonObject.toString();

        } else if (apiParams.contentType.equalsIgnoreCase("MULTIPART")) {
          connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---BOUNDARY");

          // Create the request body
          String boundary = "---BOUNDARY";
          String lineEnding = "\r\n";

          for (Map.Entry<String, String> entry : apiParams.requestBodyMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            postData += "--" + boundary + lineEnding;
            postData += "Content-Disposition: form-data; name=\"" + key + "\"" + lineEnding;
            postData += lineEnding;
            postData += value + lineEnding;
          }

          postData += "--" + boundary + "--" + lineEnding;
        } else {
          connection.setRequestProperty("Content-Type", "text/plain");
        }

        Log.d(TAG, postData);

        // Get the output stream and write the request body to it
        OutputStream os = connection.getOutputStream();
        os.write(postData.getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
      }

      int responseCode = connection.getResponseCode();

      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }
        reader.close();
        return response.toString();
      } else {
        if (responseListener != null) {
          responseListener.onError("Error: " + responseCode);
        }
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
      if (responseListener != null) {
        responseListener.onError("Exception: " + e.getMessage());
      }
      return null;
    }
  }

  @Override
  protected void onPostExecute(String result) {
    if (responseListener != null) {
      if (result != null) {
        responseListener.onResponseReceived(result);
      }
    }
  }
}
