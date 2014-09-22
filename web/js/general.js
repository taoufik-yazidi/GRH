;
var INSERTAR = 1;
var MODIFICAR = 2;
var BORRAR = 3;
(function () {
    var app = angular.module('grh', []);

    app.controller('ListarController', ['$http', function($http){
        var listaCtrl = this;
        listaCtrl.object = {};
        listaCtrl.objetos = [];
        listaCtrl.tipoMensaje = 0;
        listaCtrl.mensaje = "";
        listaCtrl.tipoMensajeLista = 0;
        listaCtrl.mensajeLista = "";
        listaCtrl.tipoAccion = INSERTAR;
        listaCtrl.listar = function (servlet) {
            $http({
                method: 'POST',
                url: servlet,
                params: {tipo: 'c'},
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            })
            .success(function (data) {
                listaCtrl.tipoMensajeLista = data.tipoMensaje;
                if(listaCtrl.tipoMensajeLista === 1){
                    listaCtrl.objetos = data.lista;
                };
                listaCtrl.mensajeLista = data.mensaje;
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
        listaCtrl.processObject = function(tipo, servlet, idObject){
            listaCtrl.object.accion = tipo;
            if(listaCtrl.object.accion===BORRAR){
                listaCtrl.object.id = idObject;
            }
            $http({
                method: 'post',
                url: servlet,
                params: listaCtrl.object,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            })
            .success(function (data) {
                listaCtrl.tipoMensaje = data.tipoMensaje;
                listaCtrl.mensaje = data.mensaje;
                if(listaCtrl.tipoMensaje === 1){
                    listaCtrl.object = {};
                    listaCtrl.listar(servlet+'Sel');
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
        };
    }]);
})();