
package gestion;

import hibernate.HibernateUtil;
import hibernate.Keep;
import hibernate.Usuario;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;

public class GestorKeep {
    
    public static JSONObject addKeep(Keep k, String usuario){
        System.out.println("adding...");
        System.out.println("Envia " + k.toString());
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        Usuario u = (Usuario)sesion.get(Usuario.class, usuario);
        k.setUsuario(u);
        sesion.save(k);
        Long id = ((BigInteger) sesion.createSQLQuery
            ("select last_insert_id()").uniqueResult())
            .longValue();       
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        JSONObject obj = new JSONObject();
        obj.put("r", id);
        return obj;
    }
    public static JSONObject getKeeps (String usuario){
        
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        
        Usuario u = (Usuario)sesion.get(Usuario.class, usuario);
        
        String hql = "from Keep where login = :login";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        List<Keep> keeps = q.list();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        
        JSONArray array= listToJSON(keeps);
        JSONObject obj2 = new JSONObject();
        
        obj2.put("r", array);
        System.out.println("recibe" + obj2.toString());
        return obj2;
    }
    
    public static List<Keep> getKeepsList(String usuario){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        
        Usuario u = (Usuario)sesion.get(Usuario.class, usuario);
        
        String hql = "from Keep where login = :login";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        List<Keep> keeps = q.list();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        
        return keeps;
    }
    
    public static JSONArray listToJSON(List<Keep> listaKeep){
        JSONArray arrayResponse = new JSONArray();
        for(Keep k: listaKeep){
            JSONObject objectKeep = new JSONObject();
            objectKeep.put("idAndroid",k.getIdAndroid());
            objectKeep.put("state", k.getEstado());
            objectKeep.put("content", k.getContenido());
            arrayResponse.put(objectKeep);
        }
        
        return arrayResponse;
    }
    
    
    //public static List<Keep> JSONToList(JSONArray arrayResponse){
      //  List<Keep> listaKeep = new ArrayList<>();
        
        
        
        
//    }
    
    public static JSONObject deleteKeep (Keep k, String usuario){
        System.out.println("removing...");
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        
        
        Usuario u = (Usuario)sesion.get(Usuario.class, usuario);
        k.setUsuario(u);
        
        
        String hql = "delete from Keep where login = :login and idAndroid= :id";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        q.setInteger("id", k.getIdAndroid());
        q.executeUpdate();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        
        return new JSONObject();
    }
    
    public static JSONObject deleteKeepWeb (Keep k, String usuario){
        System.out.println("removing...");
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        
        
        Usuario u = (Usuario)sesion.get(Usuario.class, usuario);
        k.setUsuario(u);
        
        
        String hql = "delete from Keep where login = :login and id= :id";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        q.setInteger("id", k.getIdAndroid());
        q.executeUpdate();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        
        return new JSONObject();
    }
    
    
    public static JSONObject updateKeep(Keep k, String usuario){
        System.out.println("updating...");
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        
        Usuario u = (Usuario)sesion.get(Usuario.class, usuario);
        k.setUsuario(u);
        
        
        String hql = "UPDATE Keep SET contenido= :content WHERE login = :login and idAndroid= :id";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        q.setString("content", k.getContenido());
        q.setInteger("id", k.getIdAndroid());
        q.executeUpdate();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        
        return null;
    }
    public static JSONObject updateKeepWeb(Keep k, String usuario){
        System.out.println("updatingWeb...");
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        
        Usuario u = (Usuario)sesion.get(Usuario.class, usuario);
        k.setUsuario(u);
        
        
        String hql = "UPDATE Keep SET contenido= :content WHERE login = :login and id= :id";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        q.setString("content", k.getContenido());
        q.setInteger("id", k.getIdAndroid());
        q.executeUpdate();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        
        return null;
    }
    public static int getNextId(String usuario){
        List<Keep> listKeeps = GestorKeep.getKeepsList(usuario);
        int max= 0;
        
        for(Keep k : listKeeps){
            if(max<k.getId()){
                max=k.getId();
            }
        }
        return max + 1;
    }

    
}
