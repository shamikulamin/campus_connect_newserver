/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crewman.hibernateproject;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adnan
 */
@WebServlet(name = "GetMessageServlet", urlPatterns = {"/GetMessageServlet"})
public class GetMessageServlet extends HttpServlet {

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
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GetCommunityMessageServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetCommunityMessageServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        DBHelper db = new DBHelper();
        HttpSession session = request.getSession(true);
        //Connection conn = getMySqlConnection();  
        //ArrayList<IncidentMessageBean> messages  = new ArrayList<IncidentMessageBean>();
        
        String msgType = (String)request.getParameter("msgType");
        System.out.println(msgType);
        String validFlag = (String)session.getAttribute("User");
        if(validFlag == null){
            session.setAttribute("Error", "page has Expired");
            RequestDispatcher dispatcher = request.getRequestDispatcher("LoginExample.jsp");
            dispatcher.forward( request, response); 
        }
        
        List<IncidentMsg> result = null;
        List<IncidentPicture> resultPic = null;
        System.out.println("GOT HERE HOHOHO");
        if(msgType.equalsIgnoreCase("incidentMessage")){
           result = db.getIncidentMessages();
            try{
                for(int i=0; i < result.size(); i++){
                    Integer incidentId = result.get(i).getIncidentId();
                    System.out.println("Incident ID: "+incidentId);
                    resultPic = (List<IncidentPicture>) db.getPictures(incidentId);
                    int noOfPics = resultPic.size();
                    result.get(i).setNoOfImages(noOfPics);
                }
                request.setAttribute("incidentMessages", result);
                RequestDispatcher dispatcher = request.getRequestDispatcher("messages.jsp");
                dispatcher.forward(request, response);
            }
            catch(Exception e){
                Logger.getLogger(GetMessageServlet.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("Exception caught GetMessageServlet: "+e.getMessage());
            }
            finally{
                db.destroySession();
            }
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
