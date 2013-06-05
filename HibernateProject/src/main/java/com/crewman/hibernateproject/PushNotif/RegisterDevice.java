package com.crewman.hibernateproject.PushNotif;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.crewman.hibernateproject.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

/** Simple servlet for testing. Generates HTML instead of plain
 *  text as with the HelloWorld servlet.
 */

@WebServlet("/Register")
public class RegisterDevice extends HttpServlet {
	
	/**
	 * Auto-generated SerialID used for Serialization
	 */
	private static final long serialVersionUID = 229702844546070609L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        
        String regID = request.getParameter("regID");
        if( regID == null){
        	System.out.println("Malformed register request:\nRegID: "+ regID);
        	return;
        }
        registerDevice(regID);
  	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return;	// We are currently using POST
	}
	
	private void registerDevice(String regID){
            DBHelper db = new DBHelper();
            try{
                db.insertDevice(new Devices(regID));
            }
            catch(Exception e){
                System.out.println("Probably Failed to Register Device: "+e.getMessage());
            }
            finally{
                db.destroySession();
            }
	}
}
