package com.unicorn.coordinate.volley;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class VolleyHelper {

    public interface SimpleCallback {
        void copeResponse(String responseString) throws Exception;
    }

    public static void sendRequest(final String url, final SimpleCallback simpleCallback) {
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            simpleCallback.copeResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

}
