<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>User Login</title>

<link href="login-box.css" rel="stylesheet" type="text/css" />
<style type="text/css">
body {background-color:#b0c4de;} 
</style>

<script type="text/javascript">
    
    function submit(){
        document.forms["userLogin"].submit();
    }
    
</script>
</head>



<body>
    <form name ="userLogin" action="LoginServlet">
<% String error = (String) session.getAttribute("Error");
    if(error == null)
        error = "";
%>

        <div style="padding: 100px 0 0 250px;">


            <div id="login-box">

                <H2>Campus Connect</H2>
                <br />
                <br />
                    <div id="login-box-name" style="margin-top:20px;">UserId:</div>
                    <div id="login-box-field" style="margin-top:20px;">
                        <input type="text" name="un" class="form-login" title="Username" value="" size="30" maxlength="2048" />
                    </div>
                    <div id="login-box-name">Password:</div>
                    <div id="login-box-field"><input type="text" name="pw" type="password" class="form-login" title="Password" value="" size="30" maxlength="2048" /></div>
                    <div id="login-box-name" style="margin-top:20px;" bgcolor="#b0c4de"><%=error%></div>
                <br />
                    
                <br />
                <br />
                <img src="images/login-btn.png" width="103" height="42" style="margin-left:90px;" onclick ="submit()"/>

            </div>

        </div>

    </form>
</body>
</html>
