;
var NINGUNA = 0;
var INSERTAR = 1;
var MODIFICAR = 2;
var BORRAR = 3;
(function () {
    var app = angular.module('grh', []);

    app.controller('GRHController', ['$http', function($http){
        
        var grhCtrl = this;
        grhCtrl.object = {};
        grhCtrl.objetos = [];
        
        grhCtrl.tipoMensaje = NINGUNA;
        grhCtrl.mensaje = "";
        grhCtrl.tipoMensajeLista = NINGUNA;
        grhCtrl.mensajeLista = "";
        // Paginación
        grhCtrl.registros = 0;
        grhCtrl.pag = 1;
        grhCtrl.paginas = 0;
        grhCtrl.lim = 10;
        grhCtrl.cor = 1;
        grhCtrl.tor = "asc";
        grhCtrl.regInicial = 0;
        grhCtrl.regFinal = 0;
        
        grhCtrl.showConfirm = false;
        grhCtrl.confirmacion = false;
        grhCtrl.accion = NINGUNA;
        grhCtrl.servlet = '';
        
        grhCtrl.listar = function() {
            $http({
                method: 'POST',
                url: grhCtrl.servlet+'Sel',
                params: {tc: 0, pag: grhCtrl.pag, lim: grhCtrl.lim, cor: grhCtrl.cor, tor: grhCtrl.tor},
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            })
            .success(function (data) {
                grhCtrl.tipoMensajeLista = data.tipoMensaje;
                if(grhCtrl.tipoMensajeLista === 1){
                    grhCtrl.objetos = data.lista;
                    grhCtrl.registros = data.registros;
                    grhCtrl.paginas = data.paginas;
                    if(grhCtrl.pag > grhCtrl.paginas) grhCtrl.pag = grhCtrl.paginas;
                    grhCtrl.regInicial = grhCtrl.pag * grhCtrl.lim - grhCtrl.lim + 1;
                    grhCtrl.regFinal = grhCtrl.pag * grhCtrl.lim;
                    if(grhCtrl.regFinal > grhCtrl.registros) grhCtrl.regFinal = grhCtrl.registros;
                };
                grhCtrl.mensajeLista = data.mensaje;
            })
            .error(function (data, status, headers, config, statusText) {
                console.log("Entré a paila");
                console.log(data);
                console.log(status);
                console.log(headers);
                console.log(config);
                console.log(statusText);
            });
        };
        
        grhCtrl.processObject = function(){
            grhCtrl.object.accion = grhCtrl.accion;
            $http({
                method: 'post',
                url: grhCtrl.servlet,
                params: grhCtrl.object,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            })
            .success(function (data) {
                grhCtrl.tipoMensaje = data.tipoMensaje;
                grhCtrl.mensaje = data.mensaje;
                if(grhCtrl.tipoMensaje === 1){
                    grhCtrl.object = {};
                    grhCtrl.listar();
                }
            })
            .error(function (data, status, headers, config, statusText) {
                console.log("Entré a paila");
                console.log(data);
                console.log(status);
                console.log(headers);
                console.log(config);
                console.log(statusText);
            });
            grhCtrl.accion = NINGUNA;
            grhCtrl.showConfirm = false;
        };
        
        // Insertar
        grhCtrl.addObject = function(){
            grhCtrl.accion = INSERTAR;
            grhCtrl.processObject();
        };
        // Borrar
        grhCtrl.deleteObject = function(theObject){
            grhCtrl.object = theObject;
            grhCtrl.accion = BORRAR;
            grhCtrl.showConfirm = true;
        };
        // Actualizar
        grhCtrl.updateObject = function(theObject){
            grhCtrl.object = theObject;
            grhCtrl.accion = MODIFICAR;
            grhCtrl.showConfirm = true;
        };
        // Cancelar eliminar o actualizar
        grhCtrl.cancelConfirm = function(){
            grhCtrl.showConfirm = false;
            grhCtrl.accion = NINGUNA;
            grhCtrl.object = {};
        };
    }]);

    app.directive('confirmacion',function(){
        return {
            restrict:'E',
            templateUrl:'vistas/confirmacion.html'
        };
    });
    
    app.directive('paginacion',function(){
        return {
            restrict: 'E',
            templateUrl: 'vistas/paginacion.html'
        };
    });
})();