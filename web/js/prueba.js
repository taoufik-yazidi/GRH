                
                // Variables de url y metodo
                var _url = $("#frmFormulario").attr('action');
                var _metodo = $("#frmFormulario").attr('method');
                
                // Método ajax para cargar la lista
                var cargarLista = function(){
                    $.ajax({
                        url: _url,
                        type: _metodo,
                        data: {accion:4},
                        dataType: 'json',
                        success: function (respuesta, textStatus, jqXHR) {
                            var miHtml = '<div class="alert alert-';
                            switch(respuesta['tipoMensajeLista']){
                                case 0:
                                    miHtml += "danger";
                                    break;
                                case 1:
                                    miHtml += "success";
                                    break;
                                case 2:
                                    miHtml += "info";
                                    break;
                                case 3:
                                default:
                                    miHtml += "warning";
                                    break;                                    
                            }
                            miHtml += '">';
                            miHtml += '<span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;';
                            miHtml += respuesta['mensajeLista'];
                            miHtml += '</div>';
                            $("#msgLista").html(miHtml);
                            var objetos = respuesta['lista'];
                            miHtml = "";
                            $.each(objetos,function(indice,valor){
                                miHtml += "<tr>";
                                miHtml += "<td>";
                                miHtml += valor['idtipodedocumento'];
                                miHtml += "</td>";
                                miHtml += "<td>";
                                miHtml += valor['abreviatura'];
                                miHtml += "</td>";
                                miHtml += "<td>";
                                miHtml += valor['tipodedocumento'];
                                miHtml += "</td>";
                                miHtml += "<td>";
                                miHtml += "<input class=\"form-control\" type=\"checkbox\" disabled=\"disabled\"";
                                if(valor['activo']){
                                        miHtml += " checked=\"checked\"";
                                }
                                miHtml += ">";
                                miHtml += "</td>";
                                miHtml += "<td>";
                                miHtml += "<button data-eliminar='1' data-idunico='"+valor['idtipodedocumento']+"' class='btn btn-warning'>Eliminar</button>";
                                miHtml += "</td>";
                                miHtml += "<td>";
                                miHtml += "<button data-actualizar='1' data-idunico='"+valor['idtipodedocumento']+"' class='btn btn-info'>Actualizar</button>";
                                miHtml += "</td>";
                                miHtml += "</tr>";
                            });
                            miHtml += $("#cuerpoLista").html();
                            $("#cuerpoLista").html(miHtml);
                            recorrerBotones();
                        }
                    });
                };
                
                // Si se da clic en enviar al formulario
                $("#frmFormulario").on('submit',function(e){
                    e.preventDefault();
                    var _accion = $("#btnGuardar").data("accion");
                    var _datos = $("#frmFormulario").serialize();
                    _datos += '&accion='+_accion;
                    //console.log(_datos);
                    enviarDatos(_datos);
                    $("#btnGuardar").data("accion","1");
                });
                
                // Funcion para borrar el formulario
                var limpiarFormulario = function(){
                    $('#frmFormulario').each(function(){
                        this.reset();
                    });
                };
                
                // Funcion para los botones de las acciones de la lista
                var recorrerBotones = function(){
                    $('#cuerpoLista :button').each(function(indice, elemento){//.on('click',function(){
                        //console.log("recorriendolo: "+indice+" elemento: "+$(elemento).data('eliminar'));
                        $(elemento).on('click',function(){
                            var _idunico = $(elemento).data('idunico');
                            // Si se seleccionó el botón eliminar
                            if($(elemento).data('eliminar')===1){
                                _accion = 3;
                                if(confirm("Desea eliminar el id: "+_idunico)){
                                    var _datos = {accion: 3, idTipoDocumento:_idunico};
                                    enviarDatos(_datos);
                                    $("#cuerpoLista").html("");
                                    cargarLista();
                                }
                            } else if($(elemento).data('actualizar')===1){
                                if(confirm("Desea actualizar el id: "+_idunico)){
                                    $("#btnGuardar").data("accion","2");
                                    var _datos = {accion:4, tipoConsulta:1, idTipoDocumento:_idunico};
                                    $.ajax({
                                        url: _url,
                                        type: _metodo,
                                        data: _datos,
                                        dataType: 'json',
                                        success: function (respuesta) {
                                            var miHtml = '<div class="alert alert-';
                                            switch(respuesta['tipoMensajeLista']){
                                                case 0:
                                                    miHtml += "danger";
                                                    break;
                                                case 1:
                                                    miHtml += "success";
                                                    break;
                                                case 2:
                                                    miHtml += "info";
                                                    break;
                                                case 3:
                                                case 4:
                                                default:
                                                    miHtml += "warning";
                                                    break;                                    
                                            }
                                            miHtml += '">';
                                            miHtml += '<span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;';
                                            miHtml += respuesta['mensajeLista'];
                                            miHtml += '</div>';
                                            $('#unDiv').html(miHtml);
                                            var objetos = respuesta['lista'];
                                            $("#idTipoDocumento").val(objetos[0]['idtipodedocumento']);
                                            $("#abreviatura").val(objetos[0]['abreviatura']);
                                            $("#tipoDocumento").val(objetos[0]['tipodedocumento']);
                                            $("#activo").attr('checked',objetos[0]['activo']);
                                        },
                                        error: function(jqXHR, textStatus, errorThrown){
                                            console.log(textStatus);
                                        }
                                    });
                                }
                            }
                        });
                    });                    
                };
                
                // Enviar datos al servidor
                var enviarDatos = function(_datos){
                    $.ajax({
                        beforeSend: function(){
                            $('#unDiv').html('procesando');
                        },
                        url: _url,
                        type: _metodo,
                        data: _datos,
                        dataType: 'json',
                        success: function (respuesta) {
                            var miHtml = '<div class="alert alert-';
                            switch(respuesta['tipoMensaje']){
                                case 0:
                                    miHtml += "danger";
                                    break;
                                case 1:
                                    miHtml += "success";
                                    break;
                                case 2:
                                    miHtml += "info";
                                    break;
                                case 3:
                                case 4:
                                default:
                                    miHtml += "warning";
                                    break;                                    
                            }
                            miHtml += '">';
                            miHtml += '<span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;';
                            miHtml += respuesta['mensaje'];
                            miHtml += '</div>';
                            $('#unDiv').html(miHtml);
                            if(respuesta['tipoMensaje']!==4){
                                limpiarFormulario();
                            };
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            console.log(textStatus);
                        }
                    });
                };
                cargarLista();
