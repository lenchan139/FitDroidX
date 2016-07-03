package org.lenchan139.fitdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnHistory = (Button) findViewById(R.id.btnHistory);
        btnStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Code when press start button
                Intent intent = new Intent(MainActivity.this,Remark.class);
                startActivity(intent);
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Code when press history button
                Intent intent = new Intent(MainActivity.this,History.class);
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
        if (id != id) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
