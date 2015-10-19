//package com.xyz.kjy.net;
//
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.ParseError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.toolbox.HttpHeaderParser;
//  
//import org.json.JSONException;
//import org.json.JSONObject;
//  
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
//import java.util.Map;
//  
//  
//
//public class JsonObjectPostRequest extends Request<JSONObject> {
//    private Map<String, String> mMap;
//    private Response.Listener<JSONObject> mListener;
//    private Map<String, String> sendHeader=new HashMap<String, String>(1);
//    public JsonObjectPostRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map map) {
//        super(Request.Method.POST, url, errorListener);
//        mListener = listener;
//        mMap = map;
//    }
//  
//    //当http请求是post时，则需要该使用该函数设置往里面添加的键值对
//    @Override
//    protected Map<String, String> getParams() throws AuthFailureError {
//        return mMap;
//    }
//    @Override
//    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//        try {
//            String jsonString =
//                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            JSONObject jsonObject = new JSONObject(jsonString);
//            for(String str:response.headers.keySet()){
//            	if(str.contains("Set-Cookie")){
//            		 jsonObject.put("Cookie",response.headers.get(str));
//            		 break;
//            	}
//            }
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
//    }
//}
