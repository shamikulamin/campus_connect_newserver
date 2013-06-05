package com.crewman.hibernateproject.PushNotif;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.crewman.hibernateproject.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * Simple servlet for unregistering devices from  push notifications 
 * */

@WebServlet("/Unregister")
public class UnregisterDevice extends HttpServlet {
	
	/**
	 * Auto-generated SerialID used for Serialization
	 */
	private static final long serialVersionUID = -4277772430114541913L;
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String regID = request.getParameter("regID");
        if( regID == null ){
        	System.out.println("Malformed unregister request");
        	return;
        }
        unregisterDevice(regID);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return;	// We are currently using get
	}
	
	private void unregisterDevice(String regID) {
            DBHelper db = new DBHelper();
            try{
                db.deleteDevice(regID);
            }
            catch(Exception e){
                System.out.println("Probably Failed to UnRegister Device: "+e.getMessage());
            }
            finally{
                db.destroySession();
            }
	}
}
