package com.nativefetch;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

@ReactModule(name = NativeFetchModule.NAME)
public class NativeFetchModule extends ReactContextBaseJavaModule {
  public static final String NAME = "NativeFetch";

  public NativeFetchModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void fetch(String url, final ReadableMap options, Promise promise) {
    String method = options.getString("method");
    String contentType = options.getString("contentType");
    String body = options.getString("body");

    String log = "URL: " + url + ", method: " + method + ", contentType: " + contentType + ", body: " + body;

    Log.d(NAME, log);

    HashMap<String, String> hashMapBody = new HashMap<>();

    if (body != null && !body.isEmpty()) {
      try {
        // Parse the JSON string into a JSONObject
        JSONObject jsonObject = new JSONObject(body);

        // Iterate through the keys and add them to the HashMap
        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
          String key = it.next();
          String value = jsonObject.getString(key);
          hashMapBody.put(key, value);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }


    if (!url.isEmpty()) {
       makeApiCall(url, method, contentType, hashMapBody, promise);
    }

//    promise.resolve(log);
  }

  private void makeApiCall(String apiUrl, String httpMethod, String contentType, HashMap<String, String> requestBody, final Promise promise) {
    HttpApiCall.Params params = new HttpApiCall.Params(apiUrl, httpMethod, contentType, requestBody);

    HttpApiCall httpApiCall = new HttpApiCall(new HttpApiCall.ApiResponseListener() {
      @Override
      public void onResponseReceived(String response) {
        promise.resolve(response);
      }

      @Override
      public void onError(String errorMessage) {
        promise.reject(errorMessage);
      }
    });
    httpApiCall.execute(params);
  }

}
