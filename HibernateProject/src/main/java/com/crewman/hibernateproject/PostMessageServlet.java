/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.crewman.hibernateproject;
import java.io.IOException;
import java.io.PrintWriter;
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
        PrintWriter out = response.getWriter();
        
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
