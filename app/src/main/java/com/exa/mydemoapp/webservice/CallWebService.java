package com.exa.mydemoapp.webservice;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.model.StudentModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Swapnil Jadhav on 13/7/16.
 */
public class CallWebService {
    public static JSONArray jsonArray1 = null;
    public static JSONObject jsonObjectResult = null;


    /* used it while u get whole data in response  not only id
     @param context
     @param method
     @param url
     @param param
     @param listener
     @param aClass
     @param <T>
*/
    static ProgressDialog progressDialog;

    public synchronized static <T> void getWebservice(Context context, int post, String url, final HashMap<String, String> param, VolleyResponseListener volleyResponseListener, Class<T[]> aClass) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response;
                            progressDialog.dismiss();

                            String key = jsonObject.getString(Constants.RESPONSE_KEY);
                            String message = jsonObject.getString(Constants.RESPONSE_MESSAGE);
//                          Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            if (key.equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                                jsonArray1 = jsonObject.getJSONArray(Constants.RESPONSE_INFO);
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();

                                Object[] object = gson.fromJson(String.valueOf(jsonArray1), aClass);
                                volleyResponseListener.onResponse(object);
                            } else if (key.equalsIgnoreCase(Constants.RESPONSE_ERROR)) {
                                progressDialog.dismiss();
                                volleyResponseListener.onError(message.toString());
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    public synchronized static <T> void getWebserviceObject(Context context, int post, String url, final HashMap<String, String> param, VolleyResponseListener volleyResponseListener, Class<T> aClass) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject jsonObject = response;
                            progressDialog.dismiss();
                            String key = jsonObject.getString(Constants.RESPONSE_KEY);
                            String message = jsonObject.getString(Constants.RESPONSE_MESSAGE);
//                          Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            if (key.equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                                jsonObjectResult = jsonObject.getJSONObject(Constants.RESPONSE_INFO);
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                Object object = gson.fromJson(String.valueOf(jsonObjectResult), aClass);
                                volleyResponseListener.onResponse(object);
                            } else if (key.equalsIgnoreCase(Constants.RESPONSE_ERROR)) {
                                progressDialog.dismiss();
                                volleyResponseListener.onError(message.toString());
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }
}
