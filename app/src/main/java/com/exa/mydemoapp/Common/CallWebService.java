package com.exa.mydemoapp.Common;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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

    public synchronized static <T> void getWebservice( String url, final HashMap<String, String> param) {

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(param),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response;

                        } catch (Exception e) {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }
}
