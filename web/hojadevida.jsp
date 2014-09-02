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
            <jsp:param name="pagina" value="Hoja de vida" />
        </jsp:include>
        <link rel="stylesheet" href="css/hojadevida.css" />
    </head>
    <body>
        <header>
            <jsp:include flush="true" page="encabezado.jsp">
                <jsp:param name="usuario" value="<%=usuario%>" />
            </jsp:include>
        </header>
        <section>
            <div class="row">
                <form id="frmFormulario" class="form-horizontal" role="form" action="SHojadevida" method="post">
                    <fieldset class="col-sm-3 col-sm-offset-1 col-lg-3 col-lg-offset-2">
                        <legend>Hoja de vida</legend>
                        <div class="row">
                            <div class="form-group">
                                <label for="idunico">ID</label>
                                <input type="number" class="form-control" id="idunico" name="idunico" placeholder="ID" readonly>
                            </div>
                            <div class="form-group">
                                <label for="idtipodedocumento">Tipo de Documento</label>
                                <select class="form-control" id="idtipodedocumento" name="idtipodedocumento" required></select>
                            </div>
                            <div class="form-group">
                                <label for="numerodocumento">Número de Documento</label>
                                <input type="text" class="form-control" id="numerodocumento" name="numerodocumento" placeholder="Número de Documento" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label for="primerapellido">Primer Apellido</label>
                                <input type="text" class="form-control" id="primerapellido" name="primerapellido" placeholder="Primer Apellido" required>
                            </div>
                            <div class="form-group">
                                <label class="segundoapellido">Segundo Apellido</label>
                                <input type="text" class="form-control" id="segundoapellido" name="segundoapellido" placeholder="Segundo Apellido" required>
                            </div>
                            <div class="form-group">
                                <label for="nombres">Nombres</label>
                                <input type="text" class="form-control" id="nombres" name="nombres" placeholder="Nombres" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label for="idgenero">Género</label>
                                <select class="form-control" id="idgenero" name="idgenero" required></select>
                            </div>
                            <div class="form-group">
                                <label for="libretamilitar">Libreta Militar</label>
                                <input type="text" class="form-control" id="libretamilitar" name="libretamilitar" placeholder="Número Libreta" required>
                            </div>
                            <div class="form-group">
                                <label for="distritolm">Distrito</label>
                                <input type="text" class="form-control" id="distritolm" name="distritolm" placeholder="Distrito Militar" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label for="iddeptonacimiento">Lugar de Nacimiento</label>
                                <select class="form-control" id="iddeptonacimiento" name="iddeptonacimiento" required></select>
                                <select class="form-control" id="idlugarnacimiento" name="idlugarnacimiento" required></select>
                            </div>
                            <div class="form-group">
                                <label for="iddeptoexpedicion">Lugar de Expedición</label>
                                <select class="form-control" id="iddeptoexpedicion" name="iddeptoexpedicion" required></select>
                                <select class="form-control" id="idlugarexpedicion" name="idlugarexpedicion" required></select>
                            </div>
                            <div class="form-group">
                                <label for="iddeptoresidencia">Lugar de Residencia</label>
                                <select class="form-control" id="iddeptoresidencia" name="iddeptoresidencia" required></select>
                                <select class="form-control" id="idlugarresidencia" name="idlugarresidencia" required></select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label for="fechanacimiento">Fecha de Nacimiento</label>
                                <input type="date" class="form-control" id="fechanacimiento" name="fechanacimiento" required>
                            </div>
                            <div class="form-group">
                                <label for="fechaexpediciond">Fecha de Expedición</label>
                                <input type="date" class="form-control" id="fechaexpediciond" name="fechaexpediciond" required>
                            </div>
                            <div class="form-group">
                                <label for="idestadocivil">Estado Civil</label>
                                <select class="form-control" id="idestadocivil" name="idestadocivil" required></select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label for="disponibilidadviaje">Disponibilidad para viajar</label>
                                <input type="checkbox" class="form-control" id="disponibilidadviaje" name="disponibilidadviaje">
                            </div>
                            <div class="form-group">
                                <label for="direccion">Dirección</label>
                                <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Dirección" required>
                            </div>
                            <div class="form-group">
                                <label for="telefono">Teléfono</label>
                                <input type="text" class="form-control" id="telefono" name="telefono" placeholder="Teléfono" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label for="correo">Correo</label>
                                <input type="text" class="form-control" id="correo" name="correo" placeholder="Correo Electrónico" required>
                            </div>
                            <div class="form-group">
                                <button data-accion="1" type="submit" class="btn btn-primary" id="btnGuardar"><span class="glyphicon glyphicon-floppy-disk">&nbsp;</span>Guardar</button>
                                <button class="btn btn-warning" id="btnCancelar"><span class="glyphicon glyphicon-remove">&nbsp;</span>Cancelar</button>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
            <form action="SCargarImagenes" method="post" enctype="multipart/form-data">
                <div class="row">
                    <div class="form-group">
                        <h3>Foto</h3>
                        <div id="preview" class="thumbnail">
                            <a href="#" id="file-select" class="btn btn-default">Seleccionar Archivo</a>
                            <img src="img/user.png" alt="Usuario" title="usuario">
                            <input class="form-control" type="file" id="foto" name="foto" required>
                        </div>
                        <button id="btnCargaFoto" class="btn btn-success">Cargar Foto</button>
                    </div>
                </div>
            </form>
            <div id="unDiv"></div>
        </section>
        <section>
            <div id="msgLista"></div>
            <div class="row">
                <div class="col-sm-10 col-sm-offset-1">
                    <form id="frmLista" class="form-horizontal" role="form" action="SHojadevida" method="post">
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
                                <option value="2">Tipo de Documento</option>
                                <option value="3">Número de Documento</option>
                                <option value="4">Primer Apellido</option>
                                <option value="5">Segundo Apellido</option>
                                <option value="6">Nombres</option>
                                <option value="7">Género</option>
                                <option value="8">Libreta Militar</option>
                                <option value="9">Distrito Militar</option>
                                <option value="10">Lugar Nacimiento</option>
                                <option value="11">Lugar Expediciond</option>
                                <option value="12">Lugar Residencia</option>
                                <option value="13">Fecha Nacimiento</option>
                                <option value="14">Fecha Expediciond</option>
                                <option value="15">Estado Civil</option>
                                <option value="16">Viaje</option>
                                <option value="17">Dirección</option>
                                <option value="18">Teléfono</option>
                                <option value="19">Correo</option>
                                <option value="20">Foto</option>
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
                    <caption><h3>Listado de Hoja de vida</h3></caption>
                    <thead>
                        <tr>
                            <th>ID</th><th>Tipo Documento</th>
                            <th>Número</th><th>Primer Apellido</th>
                            <th>Segundo Apellido</th><th>Nombres</th>
                            <th>Género</th><th>Libreta</th><th>Distrito</th>
                            <th>Nacimiento</th><th>Expedición</th>
                            <th>Residencia</th><th>Fecha Nacimiento</th>
                            <th>Fecha Expedicion</th><th>Estado Civil</th>
                            <th>Viaje</th><th>Dirección</th><th>Teléfono</th>
                            <th>e-mail</th><th colspan="2">Acciones</th>
                        </tr>
                    </thead>
                    <tbody id="cuerpoLista">
                    </tbody>
                </table>
            </div>
        </section>
        <jsp:include flush="true" page="scriptjs.jsp" />
        <script src="js/cargarCombo.jQuery.js"></script>
        <script src="js/jsHojaVida.jQuery.js"></script>
        <script>
            $(document).on('ready', function() {
                var departamentos = ['#iddeptonacimiento', '#iddeptoexpedicion', '#iddeptoresidencia'];
                $(document).cargarCombo('STiposDeDocumento', 'idtipodedocumento', 'tipodedocumento', ['#idtipodedocumento']);
                $(document).cargarCombo('SGenero', 'idgenero', 'genero', ['#idgenero']);
                $(document).cargarCombo('SDepartamentos', 'iddepartamento', 'nombre', departamentos);
                $(document).cargarCombo('SEstadocivil', 'idestadocivil', 'nombre', ['#idestadocivil']);

                var nombres = ['ID', 'idtipodedocumento', 'numerodocumento', 'primerapellido', 'segundoapellido', 'nombres', 'idgenero', 'libretamilitar', 'distritolm', 'lugarnacimiento', 'lugarexpediciond', 'lugarresidencia', 'fechanacimiento', 'fechaexpediciond', 'idestadocivil', 'disponibilidadviaje', 'direccion', 'telefono', 'correo', 'foto'];
                var campos = ['#idunico', '#idtipodedocumento', '#numerodocumento', '#primerapellido', '#segundoapellido', '#nombres', '#idgenero', '#libretamilitar', '#distritolm', '#lugarnacimiento', '#lugarexpediciond', '#lugarresidencia', '#fechanacimiento', '#fechaexpediciond', '#idestadocivil', '#disponibilidadviaje', '#direccion', '#telefono', '#correo', '#foto'];
                $('#frmFormulario').eventoAjax('#btnGuardar', '#btnCancelar', '#unDiv', '#frmLista', '#msgLista', '#cuerpoLista', nombres, campos);
                $(document).jsHojaVida();
            });
        </script>
    </body>
</html>
