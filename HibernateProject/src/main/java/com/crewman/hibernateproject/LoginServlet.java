

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crewman.hibernateproject;

//import beans.UserBean;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;*/

/**
 *
 * @author Adnan
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException 
    { 
        //RequestDispatcher rd = request.getRequestDispatcher("userLogged.jsp");
        DBHelper db = new DBHelper();
        HttpSession session = request.getSession(true);
        System.out.println("Nothing HAPPPENED");
        try { 
              /*UserBean user = new UserBean();
        
              user.setUserName(request.getParameter("un")); 
              user.setPassword(request.getParameter("pw")); 
              user = UserDAO.login(user); 
              */
              Users user = new Users(request.getParameter("un"), request.getParameter("pw"));
              System.out.println("Checking User...................................................................");
              if (db.checkLogin(user)) 
              { 
                  session.setAttribute("User",user.getUsername()); 
                  response.sendRedirect("userLogged.jsp"); //logged-in page 
              } else {
                  session.setAttribute("Error","Not registered User"); 
                  response.sendRedirect("LoginExample.jsp"); 
              }
        }catch (Throwable theException) 
        { 
            System.out.println(theException); 
        } 
    }
    
}
