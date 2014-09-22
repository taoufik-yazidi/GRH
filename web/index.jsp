<%-- 
    Document   : index
    Created on : 24/02/2013, 07:48:44 PM
    Author     : Alexys
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include flush="true" page="head.jsp">
            <jsp:param name="pagina" value="Bienvenido" />
        </jsp:include>
        <style>
            input[type="text"]{
                text-transform: none;
            }
        </style>
    </head>
    <body>
        <header>
            <div class="jumbotron">
                <h1>HCI Ingenio T.I.</h1>
                <p>Una verdadera herramienta para controlar su información</p>
            </div>
        </header>
        <section id="ingreso">
            <div class="container">
                <form action="SAutenticar" method="post" class="form-signin">
                    <h2 class="form-signin-heading">Autenticación</h2>
                    <span class="glyphicon glyphicon-user"></span>
                    <input class="input-block-level" type="text" autofocus autocomplete="off" id="txtUsr" name="txtUsr" placeholder="Digita tu usuario" />
                    <span class="glyphicon glyphicon-open"></span>
                    <input class="input-block-level" type="password" autocomplete="off" id="txtPwd" name="txtPwd" placeholder="Clave" />
                    <input class="btn btn-large btn-primary" type="submit" name="btnEnviar" value="Ingresar"/>
                    <%
                        if (request.getAttribute("mensaje") != null) {
                    %>
                    <div class="alert alert-error">
                        <%= request.getAttribute("mensaje")%>
                    </div>
                    <%
                        }
                    %>
                </form>
            </div>
        </section>
        <section>
            <div id="estadoContexto" class="alert"></div>
        </section>
        <jsp:include flush="true" page="scriptjs.jsp" />
        <script>
            $(document).on('ready', cargaConfiguracion);
            function cargaConfiguracion() {
                $.ajax({
                    url: 'SContexto',
                    type: 'post',
                    dataType: 'xml',
                    success: function(informacion) {
                        var elTipo = 0;
                        var elMensaje = "";
                        $(informacion).find('mensaje').each(function() {
                            elTipo = parseInt($(this).find('tipo').text());
                            elMensaje = $(this).find('info').text();
                        });
                        switch (elTipo) {
                            case 0:
                                $("#estadoContexto").addClass("alert-error");
                                break;
                            case 1:
                                $("#estadoContexto").addClass("alert-info");
                                break;
                            case 2:
                                $("#estadoContexto").addClass("alert-success");
                                break;
                        }
                        $("#estadoContexto").html(elMensaje);
                    },
                    error: function(jqXHR, estado, elerror) {
                        $("#estadoContexto").addClass("alert-error");
                        $("#estadoContexto").html(elerror);
                        console.log(estado);
                        console.log(elerror);
                    }
                    //complete: function(jqXHR, estado){
                    //    console.log(estado);
                    //}
                });
            }
        </script>
    </body>
</html>
