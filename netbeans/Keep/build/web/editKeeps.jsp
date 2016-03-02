<%-- 
    Document   : editKeeps
    Created on : Mar 1, 2016, 6:30:56 PM
    Author     : ivan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Keep</title>
    </head>
    <body>
        <form action="go">
            <p>Content:</p>
            <textarea name="contenido" rows="6" cols="100"></textarea>
            
            <input type="submit" value="Submit" name="Submit" />
            
            <input type="hidden" name="accion" value="" />
            <input type="hidden" name="op" value="update" />
            <input type="hidden" name="origen" value="web" />
            <input type="hidden" name="tabla" value="keep" />
            <input type="hidden" name="id" value="<%= request.getParameter("id") %>" />
            <input type="hidden" name="login" value="<%= request.getParameter("login") %>" />
        </form>
    </body>
</html>
