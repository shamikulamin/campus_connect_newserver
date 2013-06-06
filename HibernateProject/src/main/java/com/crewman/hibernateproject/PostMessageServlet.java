/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.crewman.hibernateproject;
import com.crewman.hibernateproject.PushNotif.UnregisterDevice;
import com.google.android.gcm.server.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Adnan
 */
@WebServlet(name = "PostMessageServlet", urlPatterns = {"/PostMessageServlet"})
public class PostMessageServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private static final String API_KEY = "AIzaSyCVZ_FxaGmbvCjbJ_vShvdL3g-86pJIZR0";
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String name = (String)request.getParameter("name");
            System.out.println(name); 
        } finally {            
            out.close();
        }
    }
    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        HttpSession session = request.getSession(true);
       
        String msgType = (String)request.getParameter("messagetype");
        System.out.println(msgType);
        
        String message = (String)request.getParameter("message");
        System.out.println(message);
        
        String messageTitle = (String)request.getParameter("msgTitle");
        System.out.println(messageTitle);
        
        String expiryHours = (String)request.getParameter("expiryhours");
        System.out.println(expiryHours);
        
        String expiryDays = (String)request.getParameter("expirydays");
        System.out.println(expiryDays);
        
        String pushCheck = (String)request.getParameter("push");
        
        long delay = Long.parseLong(expiryHours) * 60 * 60 * 1000 + Long.parseLong(expiryDays) * 24 * 60 * 60 * 1000;
        if(delay == 0)
            delay = 24 * 60 * 60 * 1000;
        
        String locationList = (String)request.getParameter("location_list");
        System.out.println(locationList);
        String validFlag = (String)session.getAttribute("User");
        if(validFlag == null){
            session.setAttribute("Error", "page has Expired");
            RequestDispatcher dispatcher = request.getRequestDispatcher("LoginExample.jsp");
            dispatcher.forward( request, response); 
        }
        DBHelper db = new DBHelper();
        try{
            String location = new String();
            if(locationList == null || locationList.equals(""))
                location = "none";
            else
                location = formatLatLong(locationList);
            CommunityMsg newMessage = new CommunityMsg(messageTitle, message, 
                                    getCurrentDate(System.currentTimeMillis()), location, msgType,
                                    getExpiryDate(System.currentTimeMillis(), delay));
            db.insertCommunityMessage(newMessage);
            
            int iMsgID = newMessage.getCommMsgId();
            if (pushCheck!=null)
                sendPushNotification(messageTitle, iMsgID, msgType);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("userLogged.jsp");
            dispatcher.forward( request, response);  
        }
        catch(Exception e){
                Logger.getLogger(PostMessageServlet.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("Exception caught PostMessageServlet: "+e.getMessage());
        }
        finally{
            db.destroySession();
        }
    }
    
   
    private ArrayList<String> getDeviceList() {
            ArrayList<String> strDevices = new ArrayList();
            List<Devices> result = null;
            DBHelper db = new DBHelper();
            try{
                result = (List<Devices>) db.getDevices();
                for(Devices aDevice : result){
                    strDevices.add(aDevice.getRegId());
                }
            }
            catch(Exception e){
                System.out.println("Exception in PushTest.getDeviceList: "
                        +e.getMessage());
            }
            finally{
                db.destroySession();
            }
            return strDevices;
	}
    private void sendPushNotification(String alertTitle, int iMsgID, String msgType) {

            ArrayList<String> devices = getDeviceList();
           
        
          

		/* Send Notifications and update our database if some error occurs */
		try {
			
			/* Send Push Notifications to all devices in list */
			Sender sender = new Sender(API_KEY);
			Message message = new Message.Builder().addData("title", alertTitle).addData("msgType",msgType).addData("msgID",Integer.toString(iMsgID)).build();
			MulticastResult result = sender.send(message, devices, 5);
			System.out.println( result.getSuccess()+" notifications successfully sent");
						
			for (int i = 0; i < result.getTotal(); i++) 
                        {
                            Result r = result.getResults().get(i);

                            if (r.getMessageId() != null) 
                            {
                                String canonicalRegId = r.getCanonicalRegistrationId();
                                if (canonicalRegId != null) {
                                // TODO-- use appropriate methods from DBHelper and test.   
                                // devices.get(i) has more than one registration ID: update database
                                   // st.executeUpdate("UPDATE devices SET regID='"+canonicalRegId+"' WHERE regID='"+devices.get(i)+"'");
                                }
                            } 
                            else 
                            {
                                String error = r.getErrorCodeName();
                                if (error.equals(Constants.ERROR_NOT_REGISTERED)) 
                                {
                                                                 
                               // TODO-- use appropriate methods from DBHelper and test.   

                                    // application has been removed from devices.get(i) - unregister database
                                    //st.executeUpdate("DELETE FROM devices WHERE regID='"+devices.get(i)+"'");
                                }
                            }
                        }
                   }
		
		 catch (IOException e) {
			e.printStackTrace();
		} 
	}
    
    private String formatLatLong(String latLongList){
        String formattedLatLong = "";
        StringBuilder locationListModified = new StringBuilder();
        for(int i = 0 ; i < latLongList.length();++i){
            if(latLongList.charAt(i) == '(')
                continue;
            if(latLongList.charAt(i) == ')'){
                locationListModified.append('|');
                i+=1;
            } else{
                locationListModified.append(latLongList.charAt(i));
            }
        }
        formattedLatLong = locationListModified.toString();
        formattedLatLong = formattedLatLong.substring(0,formattedLatLong.length()-1);
        return formattedLatLong;
    }
    
    private static java.sql.Timestamp getCurrentDate(long curTime) {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(curTime);
    }
    
     private static java.sql.Timestamp getExpiryDate(long curTime,long delay) {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(curTime + delay);
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
