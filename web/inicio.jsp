<%-- 
    Document   : inicio
    Created on : 13/04/2013, 05:11:32 PM
    Author     : Alexys
--%>
<%
    String usuario = "";
    if (session.getAttribute("credencial") != null) {
        objetos.ingenioti.org.OCredencial credencial = (objetos.ingenioti.org.OCredencial) session.getAttribute("credencial");
        usuario = credencial.getUsuario().getNombre();
        if (usuario.length() <= 0) {
            response.sendRedirect("index.jsp");
        }
    } else {
        response.sendRedirect("index.jsp");
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include flush="true" page="head.jsp">
            <jsp:param name="pagina" value="H.C.I. Bienvenido" />
        </jsp:include>
    </head>
    <body>
        <header>
            <jsp:include flush="true" page="encabezado.jsp">
                <jsp:param name="usuario" value="<%=usuario%>" />
            </jsp:include>
        </header>
        <section>
            <div class="container">
                <h1>Bienvenido.</h1>
                <p>Inicie aquí su proyecto.</p>
                <p><%=usuario%></p>
                <p>
                    <%
                        if (request.getAttribute("mensaje") != null) {
                            out.println(request.getAttribute("mensaje"));
                        }
                    %>
                </p>
            </div>
        </section>
        <jsp:include flush="true" page="scriptjs.jsp" />
    </body>
</html>
