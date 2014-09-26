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
    <body ng-app="grh" ng-controller="GRHController as grhCtrl">
        <span ng-init="grhCtrl.servlet='SDepartamentos'"></span>
        <header>
            <jsp:include flush="true" page="encabezado.jsp">
                <jsp:param name="usuario" value="<%=usuario%>" />
            </jsp:include>
        </header>
        <div class="container">
        <section>
            <div class="row">
                <form id="frmFormulario" class="form-horizontal" 
                      role="form" method="post">
                    <fieldset class="col-sm-5 col-sm-offset-1">
                        <legend>Departamentos</legend>
                        <div class="form-group">
                            <label for="iddepartamento" class="col-sm-2 control-label">ID</label>
                            <div class="col-sm-4">
                                <input type="number" class="form-control" 
                                       id="iddepartamento" name="iddepartamento" 
                                       placeholder="ID" ng-model="grhCtrl.object.id" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="codigo" class="col-sm-2 control-label">Código</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" 
                                       id="codigo" name="codigo" 
                                       placeholder="Codigo" ng-model="grhCtrl.object.codigo" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="nombre" class="col-sm-2 control-label">Nombre</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" 
                                       id="nombre" name="nombre" 
                                       placeholder="Departamento" ng-model="grhCtrl.object.nombre" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <button type="button" class="btn btn-primary" id="btnGuardar" 
                                    ng-show="grhCtrl.accion===0" ng-click="grhCtrl.addObject()">
                                <span class="glyphicon glyphicon-floppy-disk">&nbsp;</span>Guardar
                            </button>
                            <button type="button" class="btn btn-primary" id="btnActualizar" 
                                    ng-show="grhCtrl.accion===2" ng-click="grhCtrl.processObject()">
                                <span class="glyphicon glyphicon-floppy-disk">&nbsp;</span>Actualizar
                            </button>
                            <button type="reset" class="btn btn-warning" id="btnCancelar">
                                <span class="glyphicon glyphicon-remove">&nbsp;</span>Cancelar
                            </button>
                        </div>
                        <div class="alert" ng-show="grhCtrl.tipoMensaje"
                             ng-class="{'alert-success':grhCtrl.tipoMensaje===1,'alert-warning':grhCtrl.tipoMensaje!=1}">
                            <span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;{{grhCtrl.mensaje}}
                        </div>
                    </fieldset>
                    
                </form>
            </div>
            
        </section>
        <section>
            <div class="alert" ng-class="{'alert-success':grhCtrl.paginacion.tipoMensajeLista===1,'alert-warning':grhCtrl.paginacion.tipoMensajeLista!=1}">{{grhCtrl.paginacion.mensajeLista}}</div>
            
            <paginacion></paginacion>
            
            <div class="table-responsive">
                <table class="table table-hover table-bordered table-condensed table-striped">
                    <caption><h3>Listado de Departamentos</h3></caption>
                    <thead>
                        <tr><th>ID</th><th>Código</th><th>Nombre</th><th class="acciones" colspan="2">Acciones</th></tr>
                    </thead>
                    <tbody id="cuerpoLista">
                        <tr ng-repeat="departamento in grhCtrl.paginacion.objetos">
                            <td>{{departamento.id}}</td>
                            <td>{{departamento.codigo}}</td>
                            <td>{{departamento.nombre}}</td>
                            <td class="acciones">
                                <button class="btn btn-default" ng-click="grhCtrl.deleteObject(departamento)"><span class="glyphicon glyphicon-remove"></span>&nbsp;Eliminar</button>
                                <button class="btn btn-default" ng-click="grhCtrl.updateObject(departamento)"><span class='glyphicon glyphicon-edit'></span>&nbsp;Actualizar</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </section>
        </div>
        <confirmacion></confirmacion>
        <jsp:include flush="true" page="scriptjs.jsp" />
    </body>
</html>
