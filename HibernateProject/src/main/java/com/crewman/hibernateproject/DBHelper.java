/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crewman.hibernateproject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
/**
 *
 * @author Peter
 */
public class DBHelper {
    Session session = null;
    public DBHelper(){
        session = NewHibernateUtil.getSessionFactory().getCurrentSession();
    }
    public Session getSession(){
        if(!session.isOpen())
            session = NewHibernateUtil.getSessionFactory().getCurrentSession();
        return this.session;
    }
    public void destroySession(){
        if(session.isOpen())
            session.close();
    }
    public void insertDevice(Devices aDevice){
        Transaction tx = this.getSession().beginTransaction();
        this.getSession().saveOrUpdate(aDevice);
        tx.commit();
    }
    public List<Devices> getDevices(){
        this.getSession().beginTransaction();
        String query = "from Devices";
        Query q = this.getSession().createQuery(query);
        
        List<Devices> avDevices = (List<Devices>) q.list();
        return avDevices;
    }
    //UPDATE devices SET regID='"+canonicalRegId+"' WHERE regID='"+devices.get(i)+"'"
    public void updateDevice(String canonicalRegId, String oldRegId){
        try{
            Transaction tx = this.getSession().beginTransaction();
            String sqlQuery = "UPDATE devices SET regID= :a WHERE regID= :b";
            Query q = this.getSession().createSQLQuery(sqlQuery);
            q.setString("a", canonicalRegId);
            q.setString("b", oldRegId);
            q.executeUpdate();
            tx.commit();
        }
        catch(Exception e){
            System.out.println("Exception Caught in UpdateDevice: "+e.getMessage());
        }
    }
    //"DELETE FROM devices WHERE regID='"+devices.get(i)+"'"
    public void deleteDevice(String regId){
        try{
            Transaction tx = this.getSession().beginTransaction();
            String sqlQuery = "DELETE FROM Devices WHERE regID = :a";
            Query q = this.getSession().createSQLQuery(sqlQuery);
            q.setString("a", regId);
            q.executeUpdate();
            tx.commit();
        }
        catch(Exception e){
            System.out.println("Exception Caught in DeleteDevice: "+e.getMessage());
        }
    }
    public void insertUser(Users aUser){
        try{
            Transaction tx = this.getSession().beginTransaction();
            String sqlQuery = "insert into users(username, pswd) VALUES(:a, :b)";
            Query q= this.getSession().createSQLQuery(sqlQuery);
            q.setString("a", aUser.getUsername());
            q.setString("b", aUser.getPswd());
            int numRows = q.executeUpdate();
            tx.commit();
            System.out.println("New user added, Num rows added: "+numRows);
        }
        catch(Exception e){
            System.out.println("Exception Caught: "+e.getMessage());
        }
    }
    public List<CommunityMsg> getCommunityMsgByID(int msgId){
        this.getSession().beginTransaction();
        List<CommunityMsg> result = null;
        List<CommunityMsg> messages = null;
        String query = "From CommunityMsg where commMsgId = :a";
        Query q = this.getSession().createQuery(query);
        q.setInteger("a", msgId);
        messages = (List<CommunityMsg>) q.list();
        result = new ArrayList<CommunityMsg>(messages.size());
        for (int i = 0; i < messages.size(); i++) {
            result.add((CommunityMsg) messages.get(i));
        }
        System.out.println("Result Size: " + result.size());
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }
        return result;
    }
    public List<CommunityMsg> getValidCommunityMsg(){
        this.getSession().beginTransaction();
        List<CommunityMsg> result = null;
        List<CommunityMsg> messages = null;
        String query = "FROM CommunityMsg where CURRENT_TIMESTAMP() < expiryTime order by reportingTime DESC";
        Query q = this.getSession().createQuery(query);
        messages = (List<CommunityMsg>) q.list();
        result = new ArrayList<CommunityMsg>(messages.size());
        for (int i = 0; i < messages.size(); i++) {
            result.add((CommunityMsg) messages.get(i));
        }
        System.out.println("Result Size: " + result.size());
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }
        return result;
    }
    public List<CommunityMsg> getValidCommunityMsgWithLocation(){
        
            this.getSession().beginTransaction();
            List<CommunityMsg> result = null;
            List<CommunityMsg> messages = null;
            String query = "From CommunityMsg where latlong <> 'none' and CURRENT_TIMESTAMP() "
                    + "< expiryTime order by reportingTime DESC";
            Query q = this.getSession().createQuery(query);
            messages = (List<CommunityMsg>) q.list();
            result = new ArrayList<CommunityMsg>(messages.size());
            for(int i=0; i < messages.size(); i++){
                result.add((CommunityMsg) messages.get(i));
            }
            System.out.println("Result Size: "+result.size());
            for(int i=0; i < result.size(); i++)
                System.out.println(result.get(i));
            return result;
        
    }
    public int insertIncidentMessage(IncidentMsg msg){
        Integer incidentId = 0;
        try{
            Transaction tx = this.getSession().beginTransaction();
            
            /*String sqlQuery = "INSERT into incident_msg(msg_title,msg_description,"
                    + "reporting_time,latlong) VALUES(:a,:b,:c, :d)";
            Query q = this.getSession().createSQLQuery(sqlQuery);
            q.setString("a", msg.getMsgTitle());
            q.setString("b", msg.getMsgDescription());
            q.setTimestamp("c", msg.getReportingTime());
            q.setString("d", msg.getLatlong());
            int createdRows = q.executeUpdate();*/
            this.getSession().saveOrUpdate(msg);
            
            
            //System.out.println("Num rows added: "+createdRows+" IncidentID: "+msg.getIncidentId());
            
            String picQuery = "INSERT into incident_picture(incident_id, picture)"
                    + "VALUES (:a, :b)";
            
            incidentId = msg.getIncidentId();
            Set<IncidentPicture> pictures = msg.getIncidentPictures();
            if(pictures != null){
                Iterator it = pictures.iterator();
                while(it.hasNext()){
                    IncidentPicture aPicture = (IncidentPicture) it.next();
                    aPicture.setIncidentMsg(msg);
                    //Query pQuery = this.getSession().createSQLQuery(picQuery);
                    //pQuery.setInteger("a", incidentId);
                    //pQuery.setString("b", aPicture.getPicture());
                    //int cEntities = pQuery.executeUpdate();
                    this.getSession().saveOrUpdate(aPicture);
                    System.out.println("PictureId: "+aPicture.getId()+ " IncidentId: "+incidentId);
                }
            }
            tx.commit();
        }
        catch(Exception e){
            System.out.println("Exception in InsertIncidentMessage "+e.getMessage());
        }
        return incidentId;
    }
    public void insertCommunityMessage(CommunityMsg msg){
        try{
            Transaction tx = this.getSession().beginTransaction();
            this.getSession().saveOrUpdate(msg);
            tx.commit(); 
           
        }
        catch(Exception e){
            System.out.println("Exception caught: "+e.getMessage());
        }
    }
    public List<CommunityMsg> getCommunityMessages(){
        this.getSession().beginTransaction();
        List<CommunityMsg> result = null;
        List<CommunityMsg> messages = null;
        String query = "from CommunityMsg";
        Query q = this.getSession().createQuery(query);
        messages = (List<CommunityMsg>) q.list();
        result = new ArrayList<CommunityMsg>(messages.size());
        for(int i=0; i < messages.size(); i++){
            result.add((CommunityMsg) messages.get(i));
        }
        System.out.println("Result Size: "+result.size());
        for(int i=0; i < 10; i++)
            System.out.println(result.get(i));
        return result;
    }
    public List<IncidentMsg> getIncidentMessages(){
        this.getSession().beginTransaction();
        String query = "from IncidentMsg";
        Query q = this.getSession().createQuery(query);
        List<IncidentMsg> messages = (List<IncidentMsg>) q.list();
        return messages;
    }
    
    public List<IncidentPicture> getPictures(Integer id){
        this.getSession().beginTransaction();
        List<IncidentPicture> result = null;
        List<IncidentPicture> pictures = null;
        String query = "from IncidentPicture where incident_id = :id";
        Query q = this.getSession().createQuery(query).setInteger("id", id);
        pictures = (List<IncidentPicture>) q.list();
        result = new ArrayList<IncidentPicture>(pictures.size());
        for(int i=0; i < pictures.size(); i++){
            result.add((IncidentPicture)pictures.get(i));
        }
        System.out.println("Result Size: "+result.size());
        for(int i=0; i < result.size(); i++)
            System.out.println(result.get(i));
        return result;
    }
    public Boolean checkLogin(Users aUser){
        String username = aUser.getUsername();
        String password = aUser.getPswd();
        List<Users> result = null;
        Boolean notEmpty=true;
        try{
            String query = "from Users where username = :username and pswd = :password";
            org.hibernate.Transaction tx = this.getSession().beginTransaction();
            Query q = this.getSession().createQuery(query);
            q.setString("username", username);
            q.setString("password", password);
            result = (List<Users>) q.list();
            if(result.isEmpty()){
                notEmpty = false;
            }
            else{
                notEmpty = true;
                for(int i=0; i < result.size(); i++){
                    System.out.println(result.get(i));
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return notEmpty;
    }
    public static void main(String args[]){
        DBHelper db = new DBHelper();
        /*CommunityMsg msg = new CommunityMsg("asswipe","go home",new Timestamp(System.currentTimeMillis()),
                "what","what",new Timestamp(System.currentTimeMillis()));
        Users u = new Users("abc", "123");
        db.insertCommunityMessage(msg);
        db.insertUser(u);*/
        //db.getIncidentMessages();
        //db.getPictures(1);
        Set<IncidentPicture> iPics = new HashSet<IncidentPicture>(0);
        List<String> pics = new ArrayList<String>();
        pics.add("C:/Apache/Go home/What");
        pics.add("C:/Users/Desktop/What");
        pics.add("C:/Users/Desktop/pictures/What");
        for(int i=0; i < pics.size(); i++){
            IncidentPicture newPic = new IncidentPicture();
            newPic.setPicture(pics.get(i));
            iPics.add(newPic);
        }
        /*IncidentMsg msg = new IncidentMsg("New Incident Message", "go home", new Timestamp(System.currentTimeMillis())
                                    , "Latitude-Longitude", iPics);
        db.insertIncidentMessage(msg);
        //db.getValidCommunityMsgWithLocation();
        //db.insertDevice(new Devices("Whats my name"));
        //db.updateDevice("Whats my fucking name", "Whats my name");
        //db.deleteDevice("Whats my fucking name");
        //db.getCommunityMessages();
        List<CommunityMsg> result = db.getValidCommunityMsgWithLocation();
        for(CommunityMsg aMessage : result){
            System.out.println(aMessage);
        }*/
        
        String name = "C:/IncidentImagesC";
        String result = name.replaceAll("\\C:", "");
        System.out.println(result);
        db.destroySession();
    }
}
