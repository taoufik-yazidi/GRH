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
            <jsp:param name="pagina" value="Hojadevida" />
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
                <form id="frmFormulario" class="form-horizontal" role="form" action="SHojadevida" method="post">
                    <fieldset class="col-sm-3 col-sm-offset-1 col-lg-3 col-lg-offset-2">
                        <legend>Hojadevida</legend>
                        <div class="input-group">
                            <span class="input-group-addon">ID</span>
                            <input type="number" class="form-control" id="idunico" name="idunico" placeholder="ID" readonly>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">idtipodedocumento</span>
                            <input type="number" class="form-control" id="idtipodedocumento" name="idtipodedocumento" placeholder="idtipodedocumento" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">numerodocumento</span>
                            <input type="text" class="form-control" id="numerodocumento" name="numerodocumento" placeholder="numerodocumento" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">primerapellido</span>
                            <input type="text" class="form-control" id="primerapellido" name="primerapellido" placeholder="primerapellido" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">segundoapellido</span>
                            <input type="text" class="form-control" id="segundoapellido" name="segundoapellido" placeholder="segundoapellido" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">nombres</span>
                            <input type="text" class="form-control" id="nombres" name="nombres" placeholder="nombres" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">idgenero</span>
                            <input type="number" class="form-control" id="idgenero" name="idgenero" placeholder="idgenero" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">libretamilitar</span>
                            <input type="text" class="form-control" id="libretamilitar" name="libretamilitar" placeholder="libretamilitar" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">distritolm</span>
                            <input type="text" class="form-control" id="distritolm" name="distritolm" placeholder="distritolm" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">lugarnacimiento</span>
                            <input type="number" class="form-control" id="lugarnacimiento" name="lugarnacimiento" placeholder="lugarnacimiento" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">lugarexpediciond</span>
                            <input type="number" class="form-control" id="lugarexpediciond" name="lugarexpediciond" placeholder="lugarexpediciond" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">lugarresidencia</span>
                            <input type="number" class="form-control" id="lugarresidencia" name="lugarresidencia" placeholder="lugarresidencia" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">fechanacimiento</span>
                            <input type="date" class="form-control" id="fechanacimiento" name="fechanacimiento" placeholder="fechanacimiento" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">fechaexpediciond</span>
                            <input type="date" class="form-control" id="fechaexpediciond" name="fechaexpediciond" placeholder="fechaexpediciond" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">idestadocivil</span>
                            <input type="number" class="form-control" id="idestadocivil" name="idestadocivil" placeholder="idestadocivil" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">disponibilidadviaje</span>
                            <input type="chackbox" class="form-control" id="disponibilidadviaje" name="disponibilidadviaje" placeholder="disponibilidadviaje" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">direccion</span>
                            <input type="text" class="form-control" id="direccion" name="direccion" placeholder="direccion" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">telefono</span>
                            <input type="text" class="form-control" id="telefono" name="telefono" placeholder="telefono" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">correo</span>
                            <input type="text" class="form-control" id="correo" name="correo" placeholder="correo" required>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">foto</span>
                            <input type="text" class="form-control" id="foto" name="foto" placeholder="foto" required>
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
                    <form id="frmLista" class="form-horizontal" role="form" action="SHojadevida" method="post">					<div class="input-group">
                            <span class="input-group-addon">Página</span>
                            <input class="form-control" type="number" id="pag" name="pag" value="1" required>
                            <span class="input-group-addon">de</span>
                            <span class="input-group-addon" id="totpag">1</span>
                            <span class="input-group-addon">Cantidad</span>
                            <input type="number" class="form-control" id="lim" name="lim" min="10" max="100" step="10" value="10">
                            <span class="input-group-addon">Orden:</span>
                            <select class="form-control" id="cor" name="cor">
                                <option value="1"">ID</option>
                                <option value="2"">idtipodedocumento</option>
                                <option value="3"">numerodocumento</option>
                                <option value="4"">primerapellido</option>
                                <option value="5"">segundoapellido</option>
                                <option value="6"">nombres</option>
                                <option value="7"">idgenero</option>
                                <option value="8"">libretamilitar</option>
                                <option value="9"">distritolm</option>
                                <option value="10"">lugarnacimiento</option>
                                <option value="11"">lugarexpediciond</option>
                                <option value="12"">lugarresidencia</option>
                                <option value="13"">fechanacimiento</option>
                                <option value="14"">fechaexpediciond</option>
                                <option value="15"">idestadocivil</option>
                                <option value="16"">disponibilidadviaje</option>
                                <option value="17"">direccion</option>
                                <option value="18"">telefono</option>
                                <option value="19"">correo</option>
                                <option value="20"">foto</option>
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
                    <caption><h3>Listado de Hojadevida</h3></caption>
                    <thead>
                        <tr><th>ID</th><th>idtipodedocumento</th><th>numerodocumento</th><th>primerapellido</th><th>segundoapellido</th><th>nombres</th><th>idgenero</th><th>libretamilitar</th><th>distritolm</th><th>lugarnacimiento</th><th>lugarexpediciond</th><th>lugarresidencia</th><th>fechanacimiento</th><th>fechaexpediciond</th><th>idestadocivil</th><th>disponibilidadviaje</th><th>direccion</th><th>telefono</th><th>correo</th><th>foto</th><th colspan="2">Acciones</th></tr>
                    </thead>
                    <tbody id="cuerpoLista">
                    </tbody>
                </table>
            </div>
        </section>
        <script>
            $(document).on('ready', function() {
                var nombres = ['ID', 'idtipodedocumento', 'numerodocumento', 'primerapellido', 'segundoapellido', 'nombres', 'idgenero', 'libretamilitar', 'distritolm', 'lugarnacimiento', 'lugarexpediciond', 'lugarresidencia', 'fechanacimiento', 'fechaexpediciond', 'idestadocivil', 'disponibilidadviaje', 'direccion', 'telefono', 'correo', 'foto'];
                var campos = ['#idunico', '#idtipodedocumento', '#numerodocumento', '#primerapellido', '#segundoapellido', '#nombres', '#idgenero', '#libretamilitar', '#distritolm', '#lugarnacimiento', '#lugarexpediciond', '#lugarresidencia', '#fechanacimiento', '#fechaexpediciond', '#idestadocivil', '#disponibilidadviaje', '#direccion', '#telefono', '#correo', '#foto'];
                $('#frmFormulario').eventoAjax('#btnGuardar', '#btnCancelar', '#unDiv', '#frmLista', '#msgLista', '#cuerpoLista', nombres, campos);
            });
        </script>
    </body>
</html>
