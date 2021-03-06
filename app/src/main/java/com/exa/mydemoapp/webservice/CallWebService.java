package com.exa.mydemoapp.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Connectivity;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.database.DbInvoker;
import com.exa.mydemoapp.model.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


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

    public synchronized static <T> void getWebservice(Context context, int method, String url, final HashMap<String, Object> param, VolleyResponseListener volleyResponseListener, Class<T[]> aClass) {

        if (Connectivity.isConnected(context)) {

            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            Gson gson = new Gson();
            String jsonString = gson.toJson(param);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest req = new JsonObjectRequest(method, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = response;
                                progressDialog.dismiss();
                                String key = jsonObject.getString(Constants.RESPONSE_KEY);
                                String message = jsonObject.getString(Constants.RESPONSE_MESSAGE);
                                if (key.equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                                    if (!message.isEmpty()) {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    }
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
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                            String message = obj.getString(Constants.RESPONSE_MESSAGE);
                            if (message != null && !message.isEmpty()) {
                                volleyResponseListener.onError(message);
                            }
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }

                    if (error.getMessage() != null && !error.getMessage().isEmpty()) {
                        volleyResponseListener.onError(error.getMessage());
                    }
                }

            }) {

                /**
                 * Passing some request headers*
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    DbInvoker dbInvoker = new DbInvoker(context);
                    String studentId = CommonUtils.getSharedPref(Constants.STUDENT_ID, context);
                    UserModel userModel = new UserModel();
                    if (studentId != null) {
                        userModel = dbInvoker.getStudentById(studentId);
                    }
                    HashMap<String, String> headers = new HashMap();
                    if (userModel != null) {
                        headers.put("Content-Type", "application/json");
                        headers.put("X-AUTH-TOKEN", userModel.getSessionKey());

                    }
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };


            AppController.getInstance().addToRequestQueue(req);
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

    }


    public synchronized static <T> void getWebserviceObject(Context context, boolean progressFlag, boolean toastFlag, int requestMethod, String url, final HashMap<String, Object> param, VolleyResponseListener volleyResponseListener, Class<T> aClass) {
        if (Connectivity.isConnected(context)) {
            progressDialog = new ProgressDialog(context);
            if (progressFlag) {
                progressDialog.setTitle("Loading...");

                progressDialog.show();

                progressDialog.setCancelable(false);
            }
            Gson gson = new Gson();
            String jsonString = gson.toJson(param);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
             JsonObjectRequest req = new JsonObjectRequest(requestMethod, url, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                JSONObject jsonObject = response;
                                progressDialog.dismiss();
                                String key = jsonObject.getString(Constants.RESPONSE_KEY);
                                String message = jsonObject.getString(Constants.RESPONSE_MESSAGE);

                                if (key.equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                                    if (toastFlag) {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    }
                                    if (!jsonObject.isNull(Constants.RESPONSE_INFO)) {
                                        jsonObjectResult = jsonObject.getJSONObject(Constants.RESPONSE_INFO);
                                        GsonBuilder gsonBuilder = new GsonBuilder();
                                        Gson gson = gsonBuilder.create();
                                        Object object = gson.fromJson(String.valueOf(jsonObjectResult), aClass);
                                        volleyResponseListener.onResponse(object);
                                    } else {
                                        volleyResponseListener.onResponse();
                                    }
                                } else if (key.equalsIgnoreCase(Constants.RESPONSE_ERROR)) {
                                    progressDialog.dismiss();
                                    volleyResponseListener.onError(message.toString());
                                }

                            } catch (Exception e) {
                                progressDialog.dismiss();
                                volleyResponseListener.onError(e.getMessage());
                            }

                        }
                    }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                            String message = obj.getString(Constants.RESPONSE_MESSAGE);
                            if (toastFlag && message != null && !message.isEmpty()) {
                                volleyResponseListener.onError(message);
                            }
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }

                    if (error.getMessage() != null && !error.getMessage().isEmpty()) {
                        volleyResponseListener.onError(error.getMessage());
                    }
                }

            }) {

                /**
                 * Passing some request headers*
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    DbInvoker dbInvoker = new DbInvoker(context);
                    String studentId = CommonUtils.getSharedPref(Constants.STUDENT_ID, context);
                    UserModel userModel = new UserModel();
                    if (studentId != null) {
                        userModel = dbInvoker.getStudentById(studentId);
                    }
                    HashMap<String, String> headers = new HashMap();
                    if (userModel != null) {
                        headers.put("Content-Type", "application/json");
                        headers.put("X-AUTH-TOKEN", userModel.getSessionKey());
                    }
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };


            AppController.getInstance().addToRequestQueue(req);
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }



    /*{"userName":"yuu",
            "imageList":["https:\/\/s3.amazonaws.com\/kalpataru-deployments-mobilehub-1026292921\/schoolImage16032018122157.JPG","https:\/\/s3.amazonaws.com\/kalpataru-deployments-mobilehub-1026292921\/schoolImage16032018122216.JPG"]}
    */

}
