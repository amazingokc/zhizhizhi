package com.pofeite.volley;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Runnable {


    static String httpUrl = "http://apis.baidu.com/showapi_open_bus/weixin/weixin_article_type";
    static String httpArg = "";


    /**
     * @param :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */

    public String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "d1e19200ccd340362c47275a58a317e1");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
//                Log.d("result0", "result:" + strRead);
                sbf.append(strRead);
//                Log.d("sbf", sbf.toString());
//                sbf.append("\r\n");
//                Log.d("sbf", sbf.toString());
            }
            reader.close();
            result = sbf.toString();
//            Log.d("kk", result);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            e.printStackTrace();
        }

        return result;

    }





    private FloatingActionButton button;
    Bean bean;
    public String jsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (FloatingActionButton) findViewById(R.id.fab);

        Thread t = new Thread(new MainActivity());
        t.start();
    }

    @Override
    public void run() {
        jsonResult = request(httpUrl, httpArg);
//        Log.d("okc", "okc");
        Log.d("result", "jsonResult:" + jsonResult);
        Bean bean;

        Bean.ShowapiResBodyEntity showapiResBodyEntity = new Bean.ShowapiResBodyEntity();

        Gson gson = new Gson();
        bean = gson.fromJson(jsonResult, Bean.class);


//        TypeListEntity typeListEntity = gson.fromJson(jsonResult, new TypeToken<List<TypeListEntity>>() {
//        }.getType());
//        typeListEntity.getName();


        int showapi_res_code = bean.getShowapi_res_code();
        int ret_code = showapiResBodyEntity.getRet_code();
        List<TypeListEntity> List = showapiResBodyEntity.getTypeList();
//        arrayList.get(1).getName();

        TypeListEntity typeListEntity = new TypeListEntity();
        Log.d("arrayList", "arrayList: " + typeListEntity.getName());
        Log.d("showapi_res_code", "showapi_res_code: " + showapi_res_code);
        Log.d("ret_code", "ret_code:" + ret_code);
    }

}
