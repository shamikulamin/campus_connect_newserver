
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//import javax.sql.*;
package com.crewman.hibernateproject;
import java.sql.Timestamp;
import java.util.Date;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author adnan
 */
@WebServlet(name = "campus_connect_servlet", urlPatterns = {"/campus_connect_servlet"})
public class campus_connect_servlet extends HttpServlet {
// This is just a comment for testing. 
    //static int iUserID = 0;
    Logger vLog = Logger.getLogger("com.campus");
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String sParamVal;

        sParamVal = request.getParameter("get");
 
        if(sParamVal != null)
        {
            if (sParamVal.equals("getCommunityMsg")) {
                // return all trap locations for now. We will modify it later.
                PrintWriter out = response.getWriter();
                String msg = "";
                try {
                    //response.
                    out.println(getCommunityMsg().toString());
                } finally {
                    out.close();
                }
            }
            else if(sParamVal.startsWith("getMsgById@"))
            {
                String[] sTemp = sParamVal.split("\\@");
                String sMsgID = sTemp[1];
                PrintWriter out = response.getWriter();
                out.println(getCommunityMsgByID(sMsgID).toString());
                
            }
        } 
         if(sParamVal != null)
        {
            if (sParamVal.equals("getCommunityMsgForMap")) {
                // return all trap locations for now. We will modify it later.
                PrintWriter out = response.getWriter();
                String msg = "";
                try {
                    //response.
                    out.println(getCommunityMsgForMap().toString());
                } finally {
                    out.close();
                }
            }
        }  
        
        
        sParamVal = request.getParameter("getCommMsgDesc");
        
        if(sParamVal != null)
        {
            int ID = Integer.parseInt(sParamVal);
            
            PrintWriter out = response.getWriter();
                String msg = "";
                try {
                    //response.
                    out.println(getCommunityMsgDesc(ID));
                } finally {
                    out.close();
                }
        }
    }   

    private String getCommunityMsgDesc(int ID)
    {   
        DBHelper db = new DBHelper();
        String sRet = "";
        try{
            List<CommunityMsg> result = db.getCommunityMsgByID(ID);
            if(result.size() == 1)
                sRet = result.get(0).getMsgDescription();
            else{
                System.out.println("We got a major problem");
                vLog.error("ERROR: Duplicate Message IDs or no ID matching passed parameter");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            vLog.error(e.getMessage());
        }
        finally{
            db.destroySession();
        }
        return sRet;
    }
    
    private JSONArray getCommunityMsgForMap()
    {
        DBHelper db = new DBHelper();
        JSONArray vReturnObjects = null;

        vReturnObjects = new JSONArray();

        try {

            List<CommunityMsg> result = db.getValidCommunityMsgWithLocation();
            for (int i = 0; i < result.size(); i++) {
                CommunityMsg msg = result.get(i);
                JSONObject object = new JSONObject();
                try {
                    object.put("comm_msg_id", msg.getCommMsgId());
                    object.put("msg_title", msg.getMsgTitle());
                    object.put("msg_description", msg.getMsgDescription());
                    object.put("reporting_time", msg.getReportingTime().toString());
                    object.put("latlong", msg.getLatlong());
                    object.put("msg_type", msg.getMsgType());
                    object.put("expiry_time", msg.getExpiryTime().toString());
                } catch (Exception e) {
                    vLog.error(e.getStackTrace());
                }
                vReturnObjects.put(object);

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            vLog.error(e.getMessage());
        } finally {
            db.destroySession();
        }
        return vReturnObjects;
    }



    private JSONObject getCommunityMsgByID(String sMsgID)
    {
        JSONObject object = new JSONObject();
        DBHelper db = new DBHelper();
        try{
            int msgId = Integer.parseInt(sMsgID);
            List<CommunityMsg> message = db.getCommunityMsgByID(msgId);
            for(int i=0; i < message.size(); i++){
                CommunityMsg msg = message.get(i);
                try{
                    object.put("comm_msg_id", msg.getCommMsgId());
                    object.put("msg_title", msg.getMsgTitle());
                    object.put("msg_description", msg.getMsgDescription());
                    object.put("reporting_time", msg.getReportingTime().toString());
                    object.put("latlong", msg.getLatlong());
                    object.put("msg_type", msg.getMsgType());
                    object.put("expiry_time", msg.getExpiryTime().toString());
                }
                catch(Exception e){
                     System.out.println("Exception in Object.put");
                     vLog.error(e.getStackTrace());
                }
            }  
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            vLog.error(e.getMessage());
        }
        finally{
            db.destroySession();
        }
        return object;
    }
    private JSONArray getCommunityMsg()
    {
        JSONArray vReturnObjects = null;
        DBHelper db = new DBHelper();
        try{
            vReturnObjects = new JSONArray();
            List<CommunityMsg> message = db.getValidCommunityMsg();
            for(int i=0; i < message.size(); i++){
                JSONObject object = new JSONObject();
                CommunityMsg msg = message.get(i);
                try{
                    object.put("comm_msg_id", msg.getCommMsgId());
                    object.put("msg_title", msg.getMsgTitle());
                    object.put("msg_description", msg.getMsgDescription());
                    object.put("reporting_time", msg.getReportingTime().toString());
                    object.put("latlong", msg.getLatlong());
                    object.put("msg_type", msg.getMsgType());
                    object.put("expiry_time", msg.getExpiryTime().toString());
                }
                catch (Exception e) {
                        System.out.println("Exception in Object.put");
                        vLog.error(e.getStackTrace());
                }
                vReturnObjects.put(object);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            vLog.error(e.getMessage());
        }
        finally{
            db.destroySession();
        }
        return vReturnObjects;
    }
   
    private int insertIncidentMsg(String msg_title,String msg_description,String time,String latLong,ArrayList<String> imagePaths){
        DBHelper db = new DBHelper();
        int incidentID=0;
        try{
            Set<IncidentPicture> iPics = new HashSet<IncidentPicture>(0);
            for(int i=0; i < imagePaths.size(); i++){
                IncidentPicture newPic = new IncidentPicture();
                newPic.setPicture(imagePaths.get(i));
                iPics.add(newPic);
            }
            IncidentMsg newMessage = new IncidentMsg(msg_title, msg_description, getCurrentDate(Long.parseLong(time)), 
                    latLong, iPics);
            incidentID = db.insertIncidentMessage(newMessage);
            
        }catch (Exception s) {
            System.out.println("Exception in campus_connect_servlet.insertIncidentMsg: "+s.getMessage());
        }finally{
            db.destroySession();
        }
        return incidentID;
    }
    
    private static java.sql.Timestamp getCurrentDate(long time) {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(time);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    
    

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException
   {
       

// get next incident id
       int maxIncidentID = 0;
       String msg_title= "";
       String msg_description= "";
       String time= "";
       String latLong = "";
       String userID="";
       String password="";
       ArrayList<String> imagePaths = new ArrayList<String>();
       List<FileItem> items = null;
        // run a query to insert a row with the incident id (already received) and latlong and message details and image paths.
       
       String currTime = new Timestamp(new Date().getTime()).toString();
       String name = currTime.replaceAll("\\:|\\-|\\s+|\\.", "");
       System.out.println(name);
        String sImagePath = "C:/incidentImages/"+name+"/";
        boolean success = ( new File(sImagePath)).mkdirs();
        if (success) {
            System.out.println("Directory: " + sImagePath + " created");
        }  
       new File(sImagePath);
       try
       {
           items = new ServletFileUpload(new
                        DiskFileItemFactory()).parseRequest(request);
           for (FileItem item : items)
           {
               if(item.getFieldName().equals("message"))
               {
//                   File dataFile = new
//                    File(sImagePath+System.currentTimeMillis());
                   msg_description = item.getString();
                   /*FileWriter v = new FileWriter(dataFile);
                   v.append(sVal);
                   v.close();*/
               }
               
                if(item.getFieldName().equals("latitude"))
                {
                   /*File dataFile = new File(sImagePath+"gps_"+System.currentTimeMillis() +".txt");
                   String sVal = item.getString();
                   FileWriter v = new FileWriter(dataFile);
                   v.append(sVal);
                   v.close();*/
                   latLong = item.getString();
                }
                if(item.getFieldName().equals("longitude"))
                {                   
                   latLong = latLong + "," + item.getString();                   
                }
                
                if(item.getFieldName().equals("message_title"))
                {                   
                   msg_title = item.getString();
                }
                
                if(item.getFieldName().equals("reporting_time"))
                {                   
                   time = item.getString();
                }
                
                if(item.getFieldName().equals("uid"))
                {
                    userID=item.getString();
                }
                
                if(item.getFieldName().equals("pass"))
                {
                    password=item.getString();
                }
                if (item.getFieldName().equals("image"))
                {
                   String fileName = item.getName();
                   //String fileContentType = item.getContentType();
                   InputStream fileContent = item.getInputStream();

                   BufferedImage bImageFromConvert = ImageIO.read(fileContent);
                   imagePaths.add(sImagePath+fileName);
                   
                   File vImageFile = new File(sImagePath+fileName);

                   ImageIO.write(bImageFromConvert, "jpg", vImageFile);
                   //Image img = ImageIO.read(new File(sImagePath+fileName)).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
                   
                    BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                    img.createGraphics().drawImage(ImageIO.read(vImageFile).getScaledInstance(100, 100, Image.SCALE_SMOOTH),0,0,null);
                    ImageIO.write(img, "jpg", new File(sImagePath+fileName+"_thumb.jpg"));

                }
                
           }
       } catch (Exception e) {
           throw new ServletException("Cannot parse multipart request.", e);
       }
       maxIncidentID = insertIncidentMsg(msg_title,msg_description,time,latLong,imagePaths);
       processRequest(request, response);

   }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

        
