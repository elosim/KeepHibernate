package com.izv.dam.keep.gestion;

import android.content.Context;
import android.util.Log;

import com.izv.dam.keep.bd.GestorKeep;
import com.izv.dam.keep.pojo.Keep;
import com.izv.dam.keep.pojo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by izv on 10/02/2016.
 */
public class GestionKeep {
    private GestorKeep gk;
    private String urlDestino = "http://192.168.1.13:8080/Keep/go";

    public GestionKeep(Context context) {
        this.gk = new GestorKeep(context);

    }

    public GestionKeep() {
    }

    public List<Keep> getUserKeeps(Usuario u) {

        List<Keep> listakeep = new ArrayList<>();
        URL url = null;
        BufferedReader in = null;
        String r = "";
        String login;


        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            String destino = urlDestino + "?tabla=keep&op=read&login=" + login + "&origen=android&accion=";
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;
            while ((linea = in.readLine()) != null) {
                r += linea;
                Log.v("listawhile", linea);
            }
            in.close();

            JSONObject obj = new JSONObject(r);
            JSONArray jsonarray= (JSONArray) obj.get("r");

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject o = (JSONObject) jsonarray.get(i);
                Keep keep = new Keep(o.getInt("idAndroid"), o.getString("content"), true);
                System.out.println("Gestion keep getkeepsuser id = "+ keep.getId());
                listakeep.add(keep);
            }
            return listakeep;
        } catch (MalformedURLException e) {
            Log.v("Exception", e.toString());
        } catch (IOException e) {
            Log.v("Exception", e.toString());
        } catch (JSONException e) {
            Log.v("Exception", e.toString());
        }
        return null;
    }

    public long getNextAndroidId(List<Keep> l) {
        long next = -1;
        for (Keep k : l) {
            if (k.getId() > next) {
                next = k.getId();
            }
        }
        return next+1;
    }

    public List<Keep> uploadKeeps(List<Keep> l, Usuario u) {
        gk.open();

        List<Keep> d= new ArrayList<>();
        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        List<Keep> uKeep= getUserKeeps(u);

        try {

            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            for (Keep k : l) {
                if(!k.isEstado()) {
                    if(uKeep.contains(k)){
                        String destinor = urlDestino + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + k.getContenido() + "&accion=";
                        url = new URL(destinor);
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                    }
                    String destino = urlDestino + "?tabla=keep&op=create&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + k.getContenido() + "&accion=";
                    url = new URL(destino);
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String linea;
                    while ((linea = in.readLine()) != null) {
                        res += linea;
                    }
                    in.close();
                    k.setEstado(true);

                    gk.changeState(k);
                }

                d.add(k);

            }
            gk.close();
            return d;

        } catch (MalformedURLException e) {
            Log.v("Exception", e.toString());
        } catch (IOException e) {
            Log.v("Exception", e.toString());
        }

        gk.close();
        return null;

    }

    public void deleteKeep(Keep k, Usuario u){

        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        Log.v("heeeey","deletekeep id + "+ k.getId());
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            String destinor = urlDestino + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + k.getContenido() + "&accion=";

            url = new URL(destinor);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            Log.v("Exception", e.toString());
        } catch (IOException e) {
            Log.v("Exception", e.toString());
        }

    }


}
