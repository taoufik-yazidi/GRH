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
        <jsp:include flush="true" page="head.jsp">
            <jsp:param name="pagina" value="Municipios" />
        </jsp:include>
    </head>
    <body>
        <header>
            <jsp:include flush="true" page="encabezado.jsp">
                <jsp:param name="usuario" value="<%=usuario%>" />
            </jsp:include>
        </header>
        <section>
            <div class="row">
                <form id="frmFormulario" class="form-horizontal" role="form" action="SMunicipios" method="post">
                    <fieldset class="col-sm-3 col-sm-offset-1 col-lg-3 col-lg-offset-2">
                        <legend>Municipios</legend>
                        <div class="input-group">
                            <span class="input-group-addon">ID</span>
                            <input type="number" class="form-control" id="idunico" name="idunico" placeholder="ID" readonly>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">DEPARTAMENTO</span>
                            <select class="form-control" id="iddepartamento" name="iddepartamento" required></select>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">CÓDIGO</span>
                            <input type="text" class="form-control" id="codigo" name="codigo" placeholder="CODIGO" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">MUNICIPIO</span>
                            <input type="text" class="form-control" id="nombre" name="nombre" placeholder="MUNICIPIO" required>
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
                    <form id="frmLista" class="form-horizontal" role="form" action="SMunicipios" method="post">					<div class="input-group">
                            <span class="input-group-addon">Página</span>
                            <input class="form-control" type="number" id="pag" name="pag" value="1" required>
                            <span class="input-group-addon">de</span>
                            <span class="input-group-addon" id="totpag">1</span>
                            <span class="input-group-addon">Cantidad</span>
                            <input type="number" class="form-control" id="lim" name="lim" min="10" max="100" step="10" value="10">
                            <span class="input-group-addon">Orden:</span>
                            <select class="form-control" id="cor" name="cor">
                                <option value="4">ID Municipio</option>
                                <option value="1">ID Departamento</option>
                                <option value="5">Código Municipio</option>
                                <option value="6">Municipio</option>
                                <option value="2">Código Departamento</option>
                                <option value="3">Departamento</option>
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
                    <caption><h3>Listado de Municipios</h3></caption>
                    <thead>
                        <tr><th>ID Mun</th><th>ID Depto</th><th>Cód Municipio</th><th>Municipio</th><th>Cód Depto</th><th>Departamento</th><th colspan="2">Acciones</th></tr>
                    </thead>
                    <tbody id="cuerpoLista">
                    </tbody>
                </table>
            </div>
        </section>
        <jsp:include flush="true" page="scriptjs.jsp" />
        <script src="js/cargarCombo.jQuery.js"></script>
        <script>
            $(document).on('ready', function() {
                $(document).cargarCombo('SDepartamentos', 'iddepartamento', 'nombre', ['#iddepartamento']);
                var nombres = ['ID Municipio', 'ID Departamento', 'Código', 'Municipio'];
                var campos = ['#idunico', '#iddepartamento', '#codigo', '#nombre'];
                $('#frmFormulario').eventoAjax('#btnGuardar', '#btnCancelar', '#unDiv', '#frmLista', '#msgLista', '#cuerpoLista', nombres, campos);
            });
        </script>
    </body>
</html>
