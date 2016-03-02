<%-- 
    Document   : viewKeeps
    Created on : Feb 25, 2016, 11:36:15 AM
    Author     : ivan
--%>
<%@page import="hibernate.Usuario"%>
<%@page import="java.util.List"%>
<%@page import="hibernate.Keep"%>
<%
    List<Keep> listaKeeps = (List<Keep>) request.getAttribute("listakeeps");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Keep list</h1>
        <a href="addNewKeep.jsp?login=<%= request.getParameter("login") %>">Add new Keep</a>

            <table border="2">
            <%

            for(Keep p: listaKeeps){
                %>
                <tr>
                    <td><%= p.getId()%></td>
                    <td><%= request.getParameter("login") %></td>
                    <td><%= p.getContenido()%></td>
                    <td><%= p.getIdAndroid()%></td>
                    <td><%= p.getEstado()%></td>
                    <td><a href="editKeeps.jsp?id=<%= p.getId() %>&login=<%= request.getParameter("login") %>">Edit</a></td>
                    <td><a href="go?tabla=keep&origen=web&op=delete&accion=&id=<%= p.getId() %>&login=<%= request.getParameter("login") %>" class="borrar">Delete</a></td>
                </tr>
                <%
            }
            %>               
            </table>

    </body>
</html>
