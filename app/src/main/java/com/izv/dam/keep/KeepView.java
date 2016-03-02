package com.izv.dam.keep;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.izv.dam.keep.adapter.ClaseAdaptador;
import com.izv.dam.keep.gestion.GestionKeep;
import com.izv.dam.keep.pojo.Keep;
import com.izv.dam.keep.pojo.Usuario;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ivan on 2/23/2016.
 */
public class KeepView extends AppCompatActivity {
    private Usuario user;
    private String content="",mode="";
    private long id;
    private int previousPosition;
    private EditText etKeep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keep);

        previousPosition = getIntent().getExtras().getInt("previousposition");
        user = getIntent().getParcelableExtra("user");
        id = getIntent().getExtras().getLong("id");
        Log.v("despues",getIntent().getExtras().getInt("id")+"");
        content = getIntent().getExtras().getString("content");
        mode = getIntent().getExtras().getString("mode");

//        if(user != null){
//            Toast.makeText(this, user.getEmail(), Toast.LENGTH_LONG).show();
//        }

        etKeep = (EditText)findViewById(R.id.etKeep);
        etKeep.setText(content);

        init();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_keepview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(){

        content = etKeep.getText().toString();
        resultActivity();

    }

    public  void init(){


    }

    public void resultActivity(){

        Keep k;
        Intent resultIntent = new Intent();

        resultIntent.putExtra("content", content);

        if(mode.equals("old")) {
            resultIntent.putExtra("id", id);
            resultIntent.putExtra("previousposition", previousPosition);
        }

        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

}
