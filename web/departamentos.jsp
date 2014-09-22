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
            <jsp:param name="pagina" value="Departamentos" />
        </jsp:include>
    </head>
    <body ng-app="grh" ng-controller="ListarController as lista">
        <header>
            <jsp:include flush="true" page="encabezado.jsp">
                <jsp:param name="usuario" value="<%=usuario%>" />
            </jsp:include>
        </header>
        <div class="container">
        <section>
            <div class="row">
                <form id="frmFormulario" class="form-horizontal" 
                      role="form"
                      ng-submit="lista.processObject(lista.tipoAccion,'SDepartamentos')" method="post">
                    <fieldset class="col-sm-5 col-sm-offset-1">
                        <legend>Departamentos</legend>
                        <div class="form-group">
                            <label for="iddepartamento" class="col-sm-2 control-label">ID</label>
                            <div class="col-sm-4">
                                <input type="number" class="form-control" 
                                       id="iddepartamento" name="iddepartamento" 
                                       placeholder="ID" ng-model="listaq.object.id" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="codigo" class="col-sm-2 control-label">Código</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" 
                                       id="codigo" name="codigo" 
                                       placeholder="Codigo" ng-model="lista.object.codigo" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="nombre" class="col-sm-2 control-label">Nombre</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" 
                                       id="nombre" name="nombre" 
                                       placeholder="Departamento" ng-model="lista.object.nombre" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <button data-accion="1" type="submit" class="btn btn-primary" id="btnGuardar"><span class="glyphicon glyphicon-floppy-disk">&nbsp;</span>Guardar</button>
                            <button class="btn btn-warning" id="btnCancelar"><span class="glyphicon glyphicon-remove">&nbsp;</span>Cancelar</button>
                        </div>
                        <div class="alert" ng-show="lista.tipoMensaje"
                             ng-class="{'alert-success':lista.tipoMensaje===1,'alert-warning':lista.tipoMensaje!=1}">
                            <span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;{{lista.mensaje}}
                        </div>
                    </fieldset>
                    
                </form>
            </div>
            
        </section>
        <section ng-init="lista.listar('SDepartamentosSel')">
            <div class="alert" ng-class="{'alert-success':lista.tipoMensajeLista===1,'alert-warning':lista.tipoMensajeLista!=1}">{{lista.mensajeLista}}</div>
            <div class="row">
                <div class="col-sm-10 col-sm-offset-1">
                    <form id="frmLista" class="form-horizontal" role="form" action="SDepartamentos" method="post">
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
                                <option value="2">Código</option>
                                <option value="3">Nombre</option>
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
            <div class="table-responsive">
                <table class="table table-hover table-bordered table-condensed table-striped">
                    <caption><h3>Listado de Departamentos</h3></caption>
                    <thead>
                        <tr><th>ID</th><th>Código</th><th>Nombre</th><th class="acciones" colspan="2">Acciones</th></tr>
                    </thead>
                    <tbody id="cuerpoLista">
                        <tr ng-repeat="departamento in lista.objetos">
                            <td>{{departamento.iddepartamento}}</td>
                            <td>{{departamento.codigo}}</td>
                            <td>{{departamento.nombre}}</td>
                            <td class="acciones">
                                <button class="btn btn-warning" ng-click="lista.processObject(3,'SDepartamentos',departamento.iddepartamento)"><span class="glyphicon glyphicon-remove"></span>&nbsp;Eliminar</button>
                                <button class="btn btn-info"><span class='glyphicon glyphicon-edit'></span>&nbsp;Actualizar</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </section>
        </div>
        <jsp:include flush="true" page="scriptjs.jsp" />
        <script>
            /*$(document).on('ready', function() {
                var nombres = ['iddepartamento', 'codigo', 'nombre'];
                var campos = ['#iddepartamento', '#codigo', '#nombre'];
                $('#frmFormulario').eventoAjax('#btnGuardar', '#btnCancelar', '#unDiv', '#frmLista', '#msgLista', '#cuerpoLista', nombres, campos);
            });*/
        </script>
    </body>
</html>
