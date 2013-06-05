package com.crewman.hibernateproject.PushNotif;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.crewman.hibernateproject.*;

@WebServlet("/Push")
public class PushTest extends HttpServlet {
	
	/**
	 * Auto-generated SerialID used for Serialization
	 */
	private static final long serialVersionUID = 4228630363034911862L;
	private static final String API_KEY = "AIzaSyCVZ_FxaGmbvCjbJ_vShvdL3g-86pJIZR0";	// API key is assigned by Google
   
    
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String alertTitle = request.getParameter("title");
            String text = request.getParameter("text");
            String location= request.getParameter("location");
            String msgType=request.getParameter("messagetype");
            if( alertTitle == null || text == null ){
                    System.out.println("Malformed push request");
                    return;
            }

            ArrayList<String> devices = getDeviceList();
            sendPushNotification(devices,alertTitle,text,location,msgType);

            /* Redirect user back to push notification page */
                    //response.sendRedirect("http://localhost:8084/Test1/userLogged.jsp");
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
        
        public void sendPush(String alertTitle, String text, String location, String msgType) {
             ArrayList<String> devices = getDeviceList();
            sendPushNotification(devices,alertTitle,text,location,msgType);
        }
	
	private void sendPushNotification(ArrayList<String> devices, String alertTitle, String text,String location,String msgType) {
            DBHelper db = new DBHelper();
            try{
                /* Send Push Notifications to all devices in list */
                Sender sender = new Sender(API_KEY);
                Message message = new Message.Builder().addData("title", alertTitle).addData("text", text).addData("location", location).addData("msgType", msgType).build();
                MulticastResult result = sender.send(message, devices, 5);
                System.out.println(result.getSuccess() + " notifications successfully sent");
                
                for (int i = 0; i < result.getTotal(); i++) {
                    Result r = result.getResults().get(i);

                    if (r.getMessageId() != null) {
                        String canonicalRegId = r.getCanonicalRegistrationId();
                        if (canonicalRegId != null) {
                            // devices.get(i) has more than one registration ID: update database
                            db.updateDevice(canonicalRegId, devices.get(i));
                        }
                    } else {
                        String error = r.getErrorCodeName();
                        if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                            // application has been removed from devices.get(i) - unregister database
                            db.deleteDevice(devices.get(i));
                        }
                    }
                }
            
            }
            catch(Exception e){
                System.out.println("Exception in PushTest.sentNotification"+
                        e.getMessage());
            }
            finally{
                db.destroySession();
            }
        }
}