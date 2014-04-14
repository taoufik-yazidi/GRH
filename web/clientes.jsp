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
                <form id="frmFormulario" class="form-horizontal" role="form" action="SClientes" method="post">
                    <fieldset class="col-sm-3 col-sm-offset-1 col-lg-3 col-lg-offset-2">
                        <legend>Clientes</legend>
                        <div class="input-group">
                            <span class="input-group-addon">ID</span>
                            <input type="number" class="form-control" id="idunico" name="idunico" placeholder="ID CLIENTE" readonly>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">Tipo de Documento</span>
                            <input type="number" class="form-control" id="tipodocumento" name="tipodocumento" placeholder="Tipo de Documento" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">Numero Documento</span>
                            <input type="text" class="form-control" id="numerodocumento" name="numerodocumento" placeholder="Numero de documento" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">Dirección</span>
                            <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Dirección" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">Teléfono</span>
                            <input type="text" class="form-control" id="telefono" name="telefono" placeholder="Teléfono" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">Correo</span>
                            <input type="email" class="form-control" id="correoelectronico" name="correoelectronico" placeholder="Ej: nombre@dominio.com" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">Responsable</span>
                            <input type="text" class="form-control" id="responsable" name="responsable" placeholder="Responsable" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">Consejo</span>
                            <input type="text" class="form-control" id="consejo" name="consejo" placeholder="Consejo" required>
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
                    <form id="frmLista" class="form-horizontal" role="form" action="SClientes" method="post">					<div class="input-group">
                            <span class="input-group-addon">Página</span>
                            <input class="form-control" type="number" id="pag" name="pag" value="1" required>
                            <span class="input-group-addon">de</span>
                            <span class="input-group-addon" id="totpag">1</span>
                            <span class="input-group-addon">Cantidad</span>
                            <input type="number" class="form-control" id="lim" name="lim" min="10" max="100" step="10" value="10">
                            <span class="input-group-addon">Orden:</span>
                            <select class="form-control" id="cor" name="cor">
                                <option value="1"">ID</option>
                                <option value="2"">Tipo Documento</option>
                                <option value="3"">Numero Documento</option>
                                <option value="4"">Direccion</option>
                                <option value="5"">Telefono</option>
                                <option value="6"">Correo</option>
                                <option value="7"">Responsable</option>
                                <option value="8"">Consejo</option>
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
                    <caption><h3>Listado de Clientes</h3></caption>
                    <thead>
                        <tr><th>ID</th><th>Tipo documento</th><th>Numero de Documento</th><th>Direccion</th><th>Telefono</th><th>Correo</th><th>Responsable</th><th>Consejo</th><th colspan="2">Acciones</th></tr>
                    </thead>
                    <tbody id="cuerpoLista">
                    </tbody>
                </table>
            </div>
        </section>
        <script>
            $(document).on('ready', function(){
                var nombres = ['Id Cliente', 'Tipo de documento', 'Numero de documento', 'Direccion', 'Telefono', 'Correo', 'Responsable', 'Consejo'];
                var campos = ['#idunico', '#tipodocumento', '#numerodocumento', '#direccion', '#telefono', '#correoelectronico', '#responsable', '#consejo'];
                $('#frmFormulario').eventoAjax('#btnGuardar', '#btnCancelar', '#unDiv', '#frmLista', '#msgLista', '#cuerpoLista', nombres, campos);
            });
        </script>
    </body>
</html>

