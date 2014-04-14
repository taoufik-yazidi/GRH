<%-- 
    Document   : tiposdedocumento
    Created on : 9/12/2013, 05:09:49 PM
    Author     : Alexys
--%>
<%
    String usuario = "";
    if (session.getAttribute("credencial") != null) {
        objetos.ingenioti.org.OCredencial credencial = (objetos.ingenioti.org.OCredencial) session.getAttribute("credencial");
        usuario = credencial.getUsuario().getNombreCompleto();
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
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, maximum-scale=1">
        <meta name="description" content="Herramientas para el control de información">
        <meta name="author" content="Alexys Lozada">
        <title>H.C.I. Herramientas para el control de información</title>
        <script src="js/jquery-1.9.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/eventoAjax.jQuery.js"></script>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <link rel="stylesheet" href="css/estilo.css" />
    </head>
    <body>
        <header>
            <jsp:include flush="true" page="encabezado.jsp">
                <jsp:param name="usuario" value="<%=usuario%>" />
            </jsp:include>
        </header>
        <section>
            <div class="row">
            <form id="frmFormulario" class="form-horizontal" role="form" action="STiposDeDocumento" method="post">
                <fieldset class="col-sm-3 col-sm-offset-1 col-lg-3 col-lg-offset-2">
                    <legend>Tipos de documentos</legend>
                    <div class="input-group">
                        <span class="input-group-addon">ID</span>
                        <input type="number" class="form-control" id="idunico" name="idunico" placeholder="ID" readonly>
                    </div>
                    <div class="input-group">
                        <span class="input-group-addon">Abreviatura</span>
                        <input type="text" class="form-control" id="abreviatura" name="abreviatura" placeholder="Abreviatura" maxlength="2" required>
                    </div>
                    <div class="input-group">
                        <span class="input-group-addon">Tipo de Documento</span>
                        <input type="text" class="form-control" id="tipoDocumento" name="tipoDocumento" placeholder="Tipo de Documento" maxlength="25" required>
                    </div>
                    <div class="input-group">
                        <span class="input-group-addon">Activo</span>
                        <input type="checkbox" class="form-control" id="activo" name="activo" checked="checked">
                    </div>
                    <div class="form-group">
                            <button data-accion="1" type="submit" class="btn btn-primary" id="btnGuardar"><span class="glyphicon glyphicon-floppy-disk">&nbsp;</span>Guardar</button>
                            <button class="btn btn-warning" id="btnCancelar"><span class="glyphicon glyphicon-remove">&nbsp;</span>Cancelar</button>
                    </div>
                </fieldset>
            </form>
            </div>
            <div id="unDiv"></div>
        </section>
        <section>
            <div id="msgLista"></div>
            <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <form id="frmLista" class="form-horizontal" role="form" action="STiposDeDocumento" method="post">
                    <div class="input-group">
                        <span class="input-group-addon">Página</span>
                        <input class="form-control" type="number" id="pag" name="pag" value="1" required>
                        <span class="input-group-addon">de</span>
                        <span class="input-group-addon" id="totpag">1</span>
                        <span class="input-group-addon">Cantidad</span>
                        <input type="number" class="form-control" id="lim" name="lim" min="10" max="100" step="10" value="10">
                        <span class="input-group-addon">Orden:</span>
                        <select class="form-control" id="cor" name="cor">
                            <option value="1">ID</option>
                            <option value="2">Abreviatura</option>
                            <option value="3">Tipo de Documento</option>
                            <option value="4">Activo</option>
                        </select>
                        <span class="input-group-addon">/</span>
                        <select class="form-control" id="tor" name="tor">
                            <option value="asc">Ascendente</option>
                            <option value="desc">Descendente</option>
                        </select>
                        <span class="input-group-addon" id="datosListaFiltrada"></span>
                        <span class="input-group-btn">
                            <button class="btn btn-default" type="button" id="btnListar">Listar</button>
                        </span>
                    </div>
                </form>
            </div>
            </div>
        </section>
        <section>
            <div class="table-responsive">
                <table class="table table-hover table-bordered table-condensed">
                    <caption><h3>Listado de Tipos de Documentos</h3></caption>
                    <thead>
                        <tr><th>ID</th><th>Abreviatura</th><th>Tipo de Documento</th><th>Activo</th><th colspan="2">Acciones</th></tr>
                    </thead>
                    <tbody id="cuerpoLista">
                    </tbody>
                </table>
            </div>
        </section>
        <script>
            $(document).on('ready',function(){
                var nombres = ['Id Tipo','Abreviatura','Tipo de Documento','Activo'];
                var campos = ['#idunico','#abreviatura','#tipoDocumento','#activo'];
                $('#frmFormulario').eventoAjax('#btnGuardar','#btnCancelar','#unDiv','#frmLista','#msgLista','#cuerpoLista',nombres, campos);
            });
        </script>
    </body>
</html>
