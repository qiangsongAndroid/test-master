package com.xiaoma.taobao.Utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoma.taobao.Data;
import com.xiaoma.taobao.HttpCallBackListener;
import com.xiaoma.taobao.Reposes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/6/25.
 */
public class HttpUtils {
           public static void sendOkhttpRequest(String adress, Callback callback){
               OkHttpClient okHttpClient = new OkHttpClient();
               RequestBody requestBody = new FormBody.Builder().add("name","song").add("psd","123").build();
               Request request = new Request.Builder().url(adress).post(requestBody).build();
               okHttpClient.newCall(request).enqueue(callback);
           }

          public static void sendHttpRequest(final String address, final HttpCallBackListener httpCallBackListener){
              new Thread(new Runnable() {
                  @Override
                  public void run() {
                      HttpURLConnection connection =null;
                      try {
                          URL url = new URL(address);
                          connection= (HttpURLConnection) url.openConnection();
                          connection.setRequestMethod("GET");
                          connection.setConnectTimeout(5000);
                          connection.setReadTimeout(5000);
                          connection.setDoInput(true);
                          connection.setDoOutput(true);
                          InputStream in = connection.getInputStream();
                          BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                          StringBuilder response = new StringBuilder();
                          String line;
                          while ((line=reader.readLine())!=null){
                              response.append(line);
                          }

                          if (httpCallBackListener!=null){
                              httpCallBackListener.onFinish(response.toString());
                          }
                      } catch (Exception e) {
                          e.printStackTrace();

                          if (httpCallBackListener!=null){
                              httpCallBackListener.onError(e);
                          }
                      }finally {
                          if (connection!= null){
                              connection.disconnect();
                          }
                      }
                  }
              }).start();

          }


          private void parseXMLWithPull(String xmlData){
              try {
                  XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                  XmlPullParser xmlPullParser = factory.newPullParser();
                  xmlPullParser.setInput(new StringReader(xmlData));
                  int eventType = xmlPullParser.getEventType();
                  String id ="";
                  String name ="";
                  String version ="";
                  while (eventType!=XmlPullParser.END_DOCUMENT){
                      String nodeName = xmlPullParser.getName();
                      switch (eventType){
                          case XmlPullParser.START_TAG:
                              if ("id".equals(nodeName)){
                                  id=xmlPullParser.nextText();
                              }else if ("name".equals(nodeName)){
                                  name=xmlPullParser.nextText();
                              }else if ("version".equals(nodeName)){
                                  version=xmlPullParser.nextText();
                              }
                              break;
                          case XmlPullParser.END_TAG:
                              if ("app".equals(nodeName)){

                              }
                              break;
                          default:
                              break;
                      }
                      eventType = xmlPullParser.next();

                  }

              } catch (Exception e) {
                  e.printStackTrace();
              }
          }


       private void pareJSONWithJSONObject(String jsonData){
           try {
               JSONArray jsonArray =new JSONArray(jsonData);
               for (int i=0;i<jsonArray.length();i++){
                   JSONObject object = new JSONObject();
                   object=jsonArray.getJSONObject(i);
                   String id = object.getString("id");
                   String name = object.getString("name");
                   String version = object.getString("version");
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

       }
      private void parseJsonWithGSON(String jsonData){
          Gson gson =new Gson();
          List<Data> datas = gson.fromJson(jsonData,new TypeToken<List<Data>>(){}.getType());
          for (Data data : datas){

          }
      }

      private void parseJsonWithFastJson(String jsonData){

          List<Reposes> datas = JSON.parseArray(jsonData, Reposes.class);
          for (Reposes data : datas){
              String name = data.getLists().get(1).toString();
          }


          Data data =  new Data();
          data = JSON.parseObject(jsonData,Data.class);
      }

}
