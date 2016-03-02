package com.izv.dam.keep;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.izv.dam.keep.adapter.ClaseAdaptador;
import com.izv.dam.keep.gestion.GestionKeep;
import com.izv.dam.keep.pojo.Keep;
import com.izv.dam.keep.pojo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {

    private Usuario user;
    private List<Keep> listaNotas;
    private GestionKeep gk;
    private ClaseAdaptador adaptador;
    private ListView lv;
    private final int NEW = 1,OLD = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        user = getIntent().getParcelableExtra("user");

        init();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode== NEW){
            createNewKeep(data.getExtras().getString("content"), 0);
        }
        if (resultCode== RESULT_OK && requestCode == OLD){
            updateKeep(data.getExtras().getString("content"), data.getExtras().getLong("id"), data.getExtras().getInt("previousposition"));
        }
    }

    public  void init(){
        listaNotas = new ArrayList<>();
        gk = new GestionKeep(this);

        syncronice();

        lv = (ListView) findViewById(R.id.listView);

        adaptador = new ClaseAdaptador(this, R.layout.item, listaNotas);
        lv.setAdapter(adaptador);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                openKeepView(position);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            openNewKeepView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openKeepView(int position){
        Intent i = new Intent(this,KeepView.class);

        i.putExtra("user", user);
        i.putExtra("content", listaNotas.get(position).getContenido());
        i.putExtra("mode", "old");
        i.putExtra("id", listaNotas.get(position).getId());
        Log.v("antes", listaNotas.get(position).getId()+ "");

        i.putExtra("previousposition", position);

        startActivityForResult(i, OLD);
    }
    public void openNewKeepView(){
        Intent i = new Intent(this, KeepView.class);
        i.putExtra("user", user);
        i.putExtra("mode", "new");
        startActivityForResult(i, NEW);
    }

    public void createNewKeep(String content, long idd) {
        long id;
        if (idd != 0) {
            id = idd;
        }else{
            id = gk.getNextAndroidId(listaNotas);
        }
        Keep k = new Keep();

        k.setId(id);
        k.setContenido(content);
        k.setEstado(false);

        listaNotas.add(k);

        AddKeepAsync a = new AddKeepAsync();
        a.execute();

        syncronice();
    }

    public void deleteKeep(final int previousPosition){
        Keep k = new Keep();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                gk.deleteKeep( listaNotas.get(previousPosition) ,user);
            }
        };
        Thread t = new Thread(r);
        t.start();

        listaNotas.remove(listaNotas.get(previousPosition));
        adaptador.borrar(previousPosition);
    }

    public void updateKeep(String content, long id, final int previousPosition){
        deleteKeep(previousPosition);
        createNewKeep(content, id);
    }

    private class SyncAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            listaNotas = gk.getUserKeeps(user);
            List<Keep> listaNotas2= new ArrayList<>();

            listaNotas2.addAll(listaNotas);

            for(Keep k: listaNotas){
                if(!k.isEstado()){
                    listaNotas2.add(k);
                }
            }
            listaNotas= listaNotas2;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            AddKeepAsync ak= new AddKeepAsync();
            ak.execute();
        }
    }
    private class AddKeepAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            listaNotas = gk.uploadKeeps(listaNotas, user);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            super.onPostExecute(res);

            adaptador = new ClaseAdaptador(Principal.this, R.layout.item, listaNotas);
            lv.setAdapter(adaptador);

        }
    }

    private void syncronice(){
        SyncAsync syncAsync= new SyncAsync();
        syncAsync.execute();
    }


}
