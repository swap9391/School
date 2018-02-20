package com.exa.mydemoapp.webservice;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.exa.mydemoapp.Common.AppController;
import com.exa.mydemoapp.model.StudentModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Swapnil Jadhav on 13/7/16.
 */
public class CallWebService {
    public static JSONArray jsonArray1 = null;


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

    public synchronized static <T> void getWebservice(Context context, int post, String url, final HashMap<String, String> param, VolleyResponseListener<StudentModel> volleyResponseListener, Class<StudentModel[]> aClass) {

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
