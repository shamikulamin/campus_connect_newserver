<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List" %>
<jsp:useBean id="messageBean" class="com.crewman.hibernateproject.IncidentMsg" scope="request" />
<jsp:useBean id="incidentMessages" type="List<com.crewman.hibernateproject.IncidentMsg>" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Message Page</title>

<link rel="stylesheet" type="text/css" href="js/DataTables-1.9.2/media/css/jquery.dataTables.css"/>
<style type="text/css" title="currentStyle">
			@import "js/DataTables-1.9.2/media/css/demo_page.css";
			@import "js/DataTables-1.9.2/media/css/demo_table_jui.css";
			@import "js/DataTables-1.9.2/examples/examples_support/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>

<script type="text/javascript" charset="utf-8" src="js/DataTables-1.9.2/media/js/jquery.js">
</script>
<script type="text/javascript" charset="utf-8" src="js/DataTables-1.9.2/media/js/jquery.dataTables.js">
</script>
<script type="text/javascript">
    
    $(document).ready(function() {
				oTable = $('#messageTable').dataTable({
					"bJQueryUI": true,
					"sPaginationType": "full_numbers"
				});
    } );
</script>

</head>
    
    <body>
        <%
        String userName = (String) session.getAttribute("User");
        String error = (String) session.getAttribute("Error");
        if (null == userName) {
            request.setAttribute("Error", "Session has ended.  Please login.");
            RequestDispatcher rd = request.getRequestDispatcher("LoginExample.jsp");
            rd.forward(request, response);
           
        } else { %>
       
        <div class="demo_jui">
        <table id ="messageTable" >
            <thead>
                 
                  <tr>
                        <th>No</th>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Location</th>
                        <th>Pictures</th>

                  </tr>
            </thead>
            <tbody>
                <%for(int i=0; i < incidentMessages.size(); i++)
                {
                //IncidentMessageBean incidentMsg = new IncidentMessageBean();
                //incidentMsg = (IncidentMessageBean) incidentMessages.get(i);
                com.crewman.hibernateproject.IncidentMsg incidentMsg = 
                        (com.crewman.hibernateproject.IncidentMsg)incidentMessages.get(i);
                %>
                <tr>
                <td><%= incidentMsg.getIncidentId() %></td>
                <td><%= incidentMsg.getMsgTitle() %></td>
                <td><%= incidentMsg.getMsgDescription() %></td>
                <td><a href="showLocationInMap.jsp?latLong=<%= incidentMsg.getLatlong() %>">Location</a></td>
                <td><a href="showPics.jsp?noOfPics=<%= incidentMsg.getNoOfImages() %>&incidentId=<%= incidentMsg.getIncidentId()%>">images</a></td>
                </tr>
                <%}%>
            </tbody>
        </table>
        </div>
        <%}%>
    </body>
</html>
