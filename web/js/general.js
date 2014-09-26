;
var NINGUNA = 0;
var INSERTAR = 1;
var MODIFICAR = 2;
var BORRAR = 3;
(function () {
    var app = angular.module('grh', []);
    
    app.factory('PaginacionModel',function($http){
        var servicio = {
            servlet:"",
            registros:0,
            pag:1,
            paginas:0,
            lim:10,
            cor:1,
            tor:"asc",
            regInicial:0,
            regFinal:0,
            tipoMensajeLista:"",
            mensajeLista:"",
            objetos:[],
            listar: function(){
                $http({                  
                    method: 'POST',
                    url: servicio.servlet,
                    params: {tc: 0, "pag": servicio.pag, 
                            "lim": servicio.lim, 
                            "cor": servicio.cor, 
                            "tor": servicio.tor},
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                })
                .success(function (data) {
                    servicio.tipoMensajeLista = data.tipoMensaje;
                    if(servicio.tipoMensajeLista === 1){
                        servicio.objetos = data.lista;
                        servicio.registros = data.registros;
                        servicio.paginas = data.paginas;
                        if(servicio.pag > servicio.paginas) servicio.pag = servicio.paginas;
                        servicio.regInicial = servicio.pag * servicio.lim - servicio.lim + 1;
                        servicio.regFinal = servicio.pag * servicio.lim;
                        if(servicio.regFinal > servicio.registros) servicio.regFinal = servicio.registros;
                    };
                    servicio.mensajeLista = data.mensaje;
                })
                .error(function (data, status, headers, config, statusText) {
                    console.log("Entré a paila");
                    console.log(data);
                    console.log(status);
                    console.log(headers);
                    console.log(config);
                    console.log(statusText);
            });}
        };
        return servicio;
    });

    app.controller('GRHController', function($http,PaginacionModel){
        
        var grhCtrl = this;
        grhCtrl.object = {};
        grhCtrl.objetos = [];
        
        grhCtrl.tipoMensaje = NINGUNA;
        grhCtrl.mensaje = "";
        grhCtrl.tipoMensajeLista = NINGUNA;
        grhCtrl.mensajeLista = "";
        
        grhCtrl.showConfirm = false;
        grhCtrl.confirmacion = false;
        grhCtrl.accion = NINGUNA;
        grhCtrl.servlet = '';
        grhCtrl.paginacion = PaginacionModel;
        
        grhCtrl.listar = function() {
            PaginacionModel.listar();
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
    });

    app.controller('PaginacionController', function(PaginacionModel){
        var pagCtrl = this;
        pagCtrl.paginacion = PaginacionModel;
        pagCtrl.listar = function(){
            PaginacionModel.listar();
        };
    });
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