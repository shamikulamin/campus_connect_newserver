<%-- 
    Document   : post
    Created on : Jun 28, 2012, 4:12:29 PM
    Author     : Adnan
--%>

<!DOCTYPE html>
<html>
    <head>
        
            <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
            <style type="text/css">
                html { height: 100% }
                body { height: 100%; margin: 0; padding: 0 }
                #map_canvas { height: 100% }
                body {background-color:#b0c4de;} 
            </style>
        
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&amp;sensor=false"></script>
        
        <script type="text/javascript" src="jquery.js"></script>
            <script type="text/javascript">
            var map;
            var locations = new Array();
            //var locations = new  Array();
            function initialize() {
            var myLatlng = new google.maps.LatLng(32.704111,-97.141571);
            var myOptions = {
                zoom: 16,
                center: myLatlng,
                mapTypeId: google.maps.MapTypeId.HYBRID
            }
            map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
            
            
            google.maps.event.addListener(map, 'click', function(event) {
                placeMarker(event.latLng);
                document.getElementById("location_list").value = locations.toString();
            });
            }

            function placeMarker(location) {
                var marker = new google.maps.Marker({
                    position: location,
                    map: map

                });
                locations.push(marker.getPosition().toString());
                //marker.setDraggable(true);
                //google.maps.event.addListener(marker,'drag',function(event){
                    //map.panTo(marker.getPosition());
                    
                //});
                google.maps.event.addListener(marker,'rightclick',function(event){
                    removeByValue(locations,marker.getPosition().toString());
                    document.getElementById("location_list").value = locations.toString();
                    marker.setMap(null);
                   
                });
            }
            
            function removeByValue(arr, val) {
                
                for(var i=0; i<arr.length; i++) {
                    if(arr[i] == val) {
                        arr.splice(i, 1);
                        break;
                    }
                }
            }
            
            function check(){
                //alert("Checking");
                var msgTitle=document.getElementById('msgTitle').value;
                //alert(msgTitle);
                if(msgTitle == null || msgTitle == ""){
                    alert ('Message Title cannot be empty!');
                    return false;
                }
                return true;
            }
            
        </script>
    </head>
    <body bgcolor="#999966" onload="initialize()" > 
        <%
        String userName = (String) session.getAttribute("User");
        String error = (String) session.getAttribute("Error");
        if (userName == null ) {
            request.setAttribute("Error", "Session has ended.Please login.");
            session.removeAttribute("User");
            RequestDispatcher rd = request.getRequestDispatcher("LoginExample.jsp");
            rd.forward(request, response);
           
        } else { %>
    <form action="PostMessageServlet" method=post name="msgForm" onsubmit="return check()">
        
        <table cellpadding=4 cellspacing=2 border=0>
            <th bgcolor="#b0c4de" colspan=2>
                <font size=5>POST MESSAGES</font>
                <br><font size=1><sup></sup></font><hr>
            </th>
            
            <tr bgcolor="#999966">
                
                
                <tr bgcolor="#b0c4de">
                    <td valign=top><b>Specify Location</b> <br>
                         <div id="map_canvas" style="width: 800px; height: 400px"></div>
                    </td>
                </tr>
                
                <td valign=top><b>Message Type<sup>*</sup></b> 
                <br>
                <select id="messagetype" name="messagetype">
                    <option value="parking">Parking Alert</option>
                    <option value="crimealert">Crime Alert</option>
                    <option value="weather">Weather Alert</option>
                    <option value="general">General Information</option>
                        
                </select>
                </td>
                
                <tr>
                <td valign=top><b>Message Expiry Time(hours)</b> 
                <br>
                <select id="expiryhours" name="expiryhours">
                    <option value="0" selected="0" >0</option>
                    <%
                    for(int i = 1 ;i <=24;i++ ){
                    %>
                    <option value="<%=i%>"><%=i%></option>
                    <%}%>
                    
                </select>
                </td>
                
                <tr>
                <td align=top><b>Message Expiry Time(days)</b>
                <br>
                <select id="expirydays" name="expirydays">
                     <option value="0" selected="0" >0</option>
                    <%
                    for(int i = 1 ;i <=7;i++ ){
                    %>
                    <option value="<%=i%>"><%=i%></option>
                    <%}%>
                    
                </select>
                </td>
                </tr>
                
                
               
                </td>
                </tr>
                
                
                
            </tr>
            
            <tr bgcolor="#b0c4de">
                <td valign=top><b>Message Title<sup>*(upto 10 characters)</sup></b> <br>
                    <input type="text" name="msgTitle" id ="msgTitle" maxlength="10"/>  
                </td>
            </tr>
            
            <tr bgcolor="#b0c4de">
                <td valign=top><b>Message Text<sup>*</sup></b> <br>
                    <textarea name="message" cols="40" rows="5">Enter your message
                    </textarea>
                </td>
            </tr>
            <br>
            <tr bgcolor="#b0c4de">
                <td align=center colspan=2><hr>
                   <INPUT TYPE=CHECKBOX NAME="push">Would you like to push?<P>
                </td>
            </tr>
            
           
            <br>
                </tr>
                <tr bgcolor="#b0c4de">
                    <td align=center colspan=2><hr>
                       <input type="submit" value="Submit">
                    </td>
                </tr>
                <li><a alighn="right" href="LogoutServlet">Logout</a></li>
          </table>
        
               </center>
               <input type="hidden" name="location_list" id="location_list" value="" /> 
        </form>
    <%}%>
</body>
</html>