//package com.xyz.kjy.net;
//
//import android.util.Log;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.ParseError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.toolbox.HttpHeaderParser;
//import org.json.JSONException;
//import org.json.JSONObject;
//  
//
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
//import java.util.Map;
//  
//  
//
//public class JsonObjectGetRequest extends Request<JSONObject> {
//    private Response.Listener<JSONObject> mListener;
//    private Map<String, String> sendHeader=new HashMap<String, String>(1);
//    public JsonObjectGetRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        super(Request.Method.GET, url, errorListener);
//        mListener = listener;
//    }
//  
//  
//    @Override
//    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//        try {
//        	 String jsonString =
//                     new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//             JSONObject jsonObject = new JSONObject(jsonString);
//             for(String str:response.headers.keySet()){
//             	if(str.contains("Set-Cookie")){
//             		 jsonObject.put("Cookie",response.headers.get(str));
//             		 break;
//             	}
//             }
//            return Response.success(jsonObject,
//                    HttpHeaderParser.parseCacheHeaders(response));
//        } catch (UnsupportedEncodingException e) {
//            return Response.error(new ParseError(e));
//        } catch (JSONException je) {
//            return Response.error(new ParseError(je));
//        }
//    }
//  
//    @Override
//    protected void deliverResponse(JSONObject response) {
//        mListener.onResponse(response);
//    }
//  
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        return sendHeader;
//    }
//    public void setSendCookie(String cookie){
//        sendHeader.put("Cookie",cookie);
//        Log.i("TAG","设置get请求的cookie值为"+cookie);
//    }
//}
