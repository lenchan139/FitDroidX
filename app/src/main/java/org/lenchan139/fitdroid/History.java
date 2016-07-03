package org.lenchan139.fitdroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.lenchan139.*;


public class History extends AppCompatActivity {
    private TextView txtStartType, txtStartTime,txtEndTime,txtRemarks;
    //JSON Node Names
    private static final String TAG_sportType = "sportType";
    private static final String TAG_startTime = "startTime";
    private static final String TAG_endTime = "endTime";
    private static final String TAG_remarks = "remarks";
    private static final String TAG_OS = "TableX";

    private static final String url = "https://blog.lenchan139.org/FitDroid/show_all.php";
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    private ListView list;

    JSONArray android = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String strSportType = getIntent().getStringExtra("SportType");
        final String strRemarks = getIntent().getStringExtra("Remarks");
        final String startTime = getIntent().getStringExtra("StartTime");
        final String endTime = getIntent().getStringExtra("EndTime");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        new JSONParse().execute();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this,MainActivity.class);
                finish();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Code here when press button
                Intent intent = new Intent(History.this, MainActivity.class);
                startActivity(intent);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtStartType = (TextView)findViewById(R.id.txtSportType1);
            txtStartTime = (TextView)findViewById(R.id.txtStartType1);
            txtEndTime = (TextView)findViewById(R.id.txtEndTime1);
            txtRemarks = (TextView)findViewById(R.id.txtRemarks1);

            pDialog = new ProgressDialog(History.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                android = json.getJSONArray(TAG_OS);
                for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String sportType = "Sport Type: " + c.getString(TAG_sportType);
                    String startTime = "Start Time: " + c.getString(TAG_startTime);
                    String endTime = "End Time: " + c.getString(TAG_endTime);
                    String remarks = "Remarks: " + c.getString(TAG_remarks);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_sportType, sportType);
                    map.put(TAG_startTime, startTime);
                    map.put(TAG_endTime, endTime);
                    map.put(TAG_remarks, remarks);

                    oslist.add(map);
                    list=(ListView)findViewById(R.id.lstViewHistory);

                    ListAdapter adapter = new SimpleAdapter(History.this, oslist,
                            R.layout.list_v,
                            new String[] { TAG_sportType,TAG_startTime, TAG_endTime,TAG_remarks }, new int[] {
                            R.id.txtSportType1,R.id.txtStartType1, R.id.txtEndTime1,R.id.txtRemarks1});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(History.this, "You Clicked at "+oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
