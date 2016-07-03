package org.lenchan139.fitdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Remark extends AppCompatActivity {
    private Spinner spinSportType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinSportType = (Spinner) findViewById(R.id.spinnSportType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sport_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSportType.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Remark.this,MainActivity.class);
                finish();
            }
        });
        Button btnRecord = (Button) findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Code here when press button
                EditText edtRemarks = (EditText) findViewById(R.id.edtRemarks);
                Button btnRecord = (Button) findViewById(R.id.btnRecord);


                Intent intent = new Intent(Remark.this,Recording.class);
                String StrSportType = longToSportType(spinSportType.getSelectedItemId());
                Date now = new Date();
                SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", java.util.Locale.US);
                String date = date1.format(now);
                String StartTime = date.toString();
                String remarks = edtRemarks.getText().toString();
                if(remarks.indexOf(" ") == -1){
                intent.putExtra("SportType", StrSportType);
                intent.putExtra("Remarks", remarks);
                intent.putExtra("StartTime",StartTime);
                Log.v("putResultLog","strRemarks: " + edtRemarks.getText());
                startActivity(intent);
                    finish();
                }
                else{
                    Snackbar.make(view, "remarks cannot include the spaces.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
    public String longToSportType(long intType){
        if(intType == 0) {
            return "Running";
        }else if(intType == 1){
            return "Tennis";
        }else if(intType == 2){
            return "Swimming";
        }else if(intType == 3){
            return "Bikecycle";
        }else if(intType == 4){
            return "Football";
        }else if(intType == 5){
            return "Hiking";
        }else if (intType == 6){
            return "Sleeping";
        }else if(intType == 7){
            return "Others";
        }else{
            return "Invaild sport type! Please try again!";
        }
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


}
