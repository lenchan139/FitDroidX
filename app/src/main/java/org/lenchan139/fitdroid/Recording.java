package org.lenchan139.fitdroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recording extends AppCompatActivity {


    private int[] time = new int[]{0,0,0};
    private String endTime;
    final String url1 = "http://blog.lenchan139.org/FitDroid/insert1.php";
    private String strSportType, strRemarks, startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        strSportType = getIntent().getStringExtra("SportType");
        strRemarks = getIntent().getStringExtra("Remarks");
        startTime = getIntent().getStringExtra("StartTime");
        Log.v("putResultLog", "strRemarks: " + strRemarks);
        setContentView(R.layout.activity_recording);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                time = onUpdateTime(time);
                                TextView showTime = (TextView) findViewById(R.id.tvTime);
                                String newTime = onCheckSingle(time[2]) + ":"
                                        + onCheckSingle(time[1])
                                        + ":" + onCheckSingle(time[0]);
                                showTime.setText(newTime);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Recording.this,MainActivity.class);
                finish();
            }
        });
        Button btnStart = (Button) findViewById(R.id.btnStop);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Code when press start button
                Intent intent = new Intent(Recording.this, History.class);
                Date now = new Date();
                SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", java.util.Locale.US);
                String date = date1.format(now);
                endTime = date.toString();
                new MyAsyncTask().execute(url1);
                intent.putExtra("SportType", strSportType);
                intent.putExtra("Remarks", strRemarks);
                intent.putExtra("StartTime", startTime);
                intent.putExtra("EndTime", endTime);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id != id) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public String requestSQL() {
        String result = "";
        Log.v("CheckLog:", "Start insert request");
        try {
        //HttpClient acts like a Browser (without the UI)
        HttpClient client = new DefaultHttpClient();

        // Create object to represent a POST request
        HttpPost request = new HttpPost(url1);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

        //  $_POST[ ] values to PHP
        Log.v("HyperLog,Before POSTing ","{"+strSportType+", "+startTime+", "+endTime+", "+strRemarks+"}");

        // This will store the response from the server
        HttpResponse response;

            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Actually call the server
            response = client.execute(request);

            // Extract text message from server
            result = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            result = "[ERROR] " + e.toString();

            Log.v("myLog", "result: " + result);

        }
        Log.v("ExceptionLog:",result);
        return result;



    }

    public String executeHttpPost(String url) {
        String result = "";

        //HttpClient acts like a Browser (without the UI)
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        // Create object to represent a POST request

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

        //  $_POST[ ] values to PHP
        nameValuePairs.add(new BasicNameValuePair("$sport", "DDD".toString()));
        nameValuePairs.add(new BasicNameValuePair("startTime", startTime));
        nameValuePairs.add(new BasicNameValuePair("endTime", endTime));
        nameValuePairs.add(new BasicNameValuePair("remarks", strRemarks));
        url =url + "?sportType=" + strSportType;
        url =url + "&startTime=" + startTime;
        url =url + "&endTime=" + endTime;
        url =url + "&remarks=" + strRemarks;
        try {
            request = new HttpGet(url);
        }catch (Exception e){
            Log.v("ErrorLog",e.toString());
        }

        // This will store the response from the server
        HttpResponse response;

        try {
            //request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Actually call the server
            response = client.execute(request);

            // Extract text message from server
            result = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            result = "[ERROR] " + e.toString();

        }

        Log.v("myLog", "result: " + result);
        return result;
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Connecting to server...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... url) {
            return executeHttpPost(url[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
    public String spaceToHTMLspace(String before){
        char[] after = new char[before.length()];
        for(int i = 0;i >= before.length();i++){
            after[i] = before.charAt(i);
            if(after[i] == ' '){
                after[i] = '_';
            }
        }
        String str = String.valueOf(after);
        return str;
    }

    public int[] onUpdateTime(int[] in){
        if(in[0] == 59){
            if(in[1] == 59){
                if(in[2] == 59){
                    in[0] = 0;
                    in[1] = 0;
                    in[2] = 0;
                }else{
                    in[0] = 0;
                    in[1] = 0;
                    in[2] = in[2]+1;
                }
            }else{
                in[0] = 0;
                in[1] = in[1] + 1;
            }
        }else{
            in[0] = in[0] + 1;
        }
        return in;
    }
    public String onCheckSingle(int in){
        if(in/10 >= 1){
            return "" + in;
        }else{
         return "0" + in;
        }
    }
}  // end of MainActivity




































