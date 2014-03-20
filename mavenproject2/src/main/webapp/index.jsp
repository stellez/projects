


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>    
    <body>
        <center> <h1>Store Files</h1></center>
        Insert Files
        <form method="POST" enctype="multipart/form-data" action= "InsertFile"><br>
            File: <input type=file size=60 name="file1" ><br>
            <input type=submit value="Upload">
            <br>
        </form><br>
        
        Display Images
        <form method="POST" action="ViewFiles">
            <input type="submit" value="Files">
    </body>
</html>

