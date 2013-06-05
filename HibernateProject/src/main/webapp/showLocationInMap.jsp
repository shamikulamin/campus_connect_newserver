<%-- 
    Document   : showLocationInMap
    Created on : Jul 10, 2012, 5:49:42 PM
    Author     : Adnan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ShowLocationInMap</title>
        
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&amp;sensor=false"></script>
        <style type="text/css">
             body {background-color:#b0c4de;}
        </style>
        
        <script type="text/javascript">
            
            function show(latLongList){
                //alert("inside show");
                var locations = latLongList.split("|");
                //alert("1");
                var firstLatLong = locations[0].split(",");
                // alert("2");
                
                var myLatlng = new google.maps.LatLng(firstLatLong[0],firstLatLong[1]);
                var myOptions = {
                    zoom: 16,
                    center: myLatlng,
                    mapTypeId: google.maps.MapTypeId.HYBRID
                }
                map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
                var marker = new google.maps.Marker({
                        position: myLatlng,
                        map: map

                    });
                 //alert("3");
                for(i = 1; i < locations.length; i++){
                    var parts = locations[i].split(",")
                    var singleLatLong = new google.maps.LatLng(parts[0],parts[1]);
                
                
                    var marker1 = new google.maps.Marker({
                        position: singleLatLong,
                        map: map

                    });
                
                }
            }
            
        </script>
    </head>
    <body bgcolor="#999966" onload="show('<%= request.getParameter("latLong")%>');" >
        <h1>Location</h1>
        <tr bgcolor="#b0c4de">
             
                  <div id="map_canvas" style="width: 800px; height: 400px"></div>
                  <li><a alighn="right" href="LogoutServlet">Logout</a></li>
        </tr>
    </body>
</html>
