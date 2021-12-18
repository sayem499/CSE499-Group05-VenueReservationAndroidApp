package com.reservation.app.datasource;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reservation.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender {

    String userFcmToken;
    String title;
    String body;
    String image;
    Context mContext;
    Activity mActivity;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "AAAAndCbZlI:APA91bGM2vI9O6wbxuxhPekUwmKf5yeo026JCm7EmWQCtgcPTP79Gx3Qm2GRRpPpcH1tsHa9pBSs0ukRIwqg98noA455AqRPbg_5rzXg1GmeAoEchIYMtAGX_q845rQccwHJgKiNtsGw";


    public FcmNotificationsSender(String userFcmToken, String title, String body, String image, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.image = image;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }


    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try{
            mainObj.put("to",userFcmToken);
            JSONObject notifyObject = new JSONObject();
            notifyObject.put("title", title);
            notifyObject.put("body", body);
            notifyObject.put("image",image);
            notifyObject.put("icon", R.drawable.ic_baseline_notifications_active_24);

            mainObj.put("notification", notifyObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
               @Override
               public Map<String,String> getHeaders() throws AuthFailureError{
                   Map<String,String> header = new HashMap<>();
                   header.put("content-type","application/json");
                   header.put("authorization", "key=" + fcmServerKey);


                   return header;

               }

            };
            requestQueue.add(request);




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
