;
(function($) {
    $.fn.eventoAjax = function(btnGuardar, btnCancelar, msgNormal, frmLista, msgLista, tabla, nombres, campos) {
        // Variables a trabajar
        var $elemento = $(this);
        var _url = $elemento.attr('action');
        var _met = $elemento.attr('method');
        var $registro;

        // Cargar la lista
        var cargarLista = function() {
            var pagina = $(frmLista + ' #pag').val();
            var limite = $(frmLista + ' #lim').val();
            var columna = $(frmLista + ' #cor').val();
            var orden = $(frmLista + ' #tor').val();
            $.ajax({
                url: _url + 'Sel',
                type: _met,
                data: {pag: pagina, lim: limite, cor: columna, tor: orden},
                dataType: 'json',
                success: function(respuesta) {
                    var miHtml = '<div class="alert alert-';
                    switch (respuesta['tipoMensaje']) {
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
                    miHtml += respuesta['mensaje'];
                    miHtml += '</div>';
                    $(msgLista).html(miHtml);
                    var objetos = respuesta['lista'];
                    miHtml = "";
                    $.each(objetos, function(indice, valor) {
                        miHtml += "<tr>";
                        var _nombreColumna = "";
                        var _contador = 0;
                        for (var i in valor) {
                            if (_contador === 0) {
                                _nombreColumna = i;
                                _contador++;
                            }
                            miHtml += "<td>";
                            if (valor[i] === true || valor[i] === false) {
                                miHtml += "<input class=\"form-control\" type=\"checkbox\" disabled=\"disabled\"";
                                if (valor[i]) {
                                    miHtml += " checked=\"checked\"";
                                }
                                miHtml += ">";
                            } else {
                                miHtml += valor[i];
                            }
                            miHtml += "</td>";
                        }
                        miHtml += "<td>";
                        miHtml += "<button data-eliminar='1' data-idunico='" + valor[_nombreColumna] + "' class='btn btn-warning'>";
                        miHtml += "<span class='glyphicon glyphicon-remove'></span>&nbsp;Eliminar</button>";
                        miHtml += "</td>";
                        miHtml += "<td>";
                        miHtml += "<button data-actualizar='1' data-idunico='" + valor[_nombreColumna] + "' class='btn btn-info'>";
                        miHtml += "<span class='glyphicon glyphicon-edit'></span>&nbsp;Actualizar</button>";
                        miHtml += "</td>";
                        miHtml += "</tr>";
                    });
                    $(tabla).html(miHtml);
                    // Se organiza la información de la barra de paginación
                    $(frmLista + ' #totpag').html(respuesta['paginas']);
                    if (parseInt($(frmLista + ' #pag').val()) > parseInt(respuesta['paginas'])) {
                        $(frmLista + ' #pag').val(respuesta['paginas']);
                    }
                    var regInicial = parseInt($(frmLista + ' #pag').val() * $(frmLista + ' #lim').val() - $(frmLista + ' #lim').val() + 1);
                    var regFinal = parseInt($(frmLista + ' #pag').val() * $(frmLista + ' #lim').val());
                    if (regFinal > parseInt(respuesta['registros'])) {
                        regFinal = respuesta['registros'];
                    }
                    $(frmLista + ' #datosListaFiltrada').html("Mostrando " + regInicial + " al " + regFinal + " de " + respuesta['registros'] + " registros");
                }
            });
        };

        // Si se da clic en enviar al formulario
        $elemento.on('submit', function(e) {
            e.preventDefault();
            var _accion = $(btnGuardar).data("accion");
            var _datos = $(this).serialize();
            _datos += '&accion=' + _accion;
            console.log(_datos);
            enviarDatos(_datos);
            $(btnGuardar).data("accion", "1");
            $(tabla).html("");
            cargarLista();
        });

        // Si se da clic en el boton cancelar del formulario
        $(btnCancelar).on('click', function(e) {
            e.preventDefault();
            $(btnGuardar).data("accion", "1");
            limpiarFormulario();
        });

        // Funcion para borrar el formulario
        var limpiarFormulario = function() {
            $elemento.each(function() {
                this.reset();
            });
        };

        // Mostrar la ventana de confirmación de la acción
        var mostrarConfirmacion = function(acc, id) {
            var $modalBaseGris = $("<div id='modalBaseGris'>");
            var $modalVentana = $("<div id='modalVentana' class='panel panel-default'>");
            var $modalVentanaTitulo = $("<div class='panel-heading'>");
            var $modalVentanaTituloContenido = $("<h3 class='panel-title'>");
            var $modalVentanaCuerpo = $("<div class='panel-body'>");
            var $modalVentanaPie = $("<div class='panel-footer'>");

            var $modalBotonAceptar = $("<button id='btnAceptarVM' class='btn btn-warning'>");
            var $modalBotonCerrar = $("<button id='btnCerrarVM' class='btn btn-info'>");

            var _strTitulo = "Confirmación de ";
            var _strContenido = "Por favor confirma si realmente deseas ";
            var _datosRegistro = "";
            if (acc === 2) {
                _strTitulo += "ACTUALIZACIÓN";
                _strContenido += "Actualizar";
                $modalBotonAceptar.data('accion', '2');
            }
            if (acc === 3) {
                _strTitulo += "ELIMINACIÓN";
                _strContenido += "Eliminar";
                $modalBotonAceptar.data('accion', '3');
            }

            _strTitulo += " del ID: " + id;
            _strContenido += "<br>";
            // armado del elemento a mostrar en la ventana modal
            $.each($registro.children(), function(ind, ele) {
                if (ind < nombres.length) {
                    _datosRegistro += nombres[ind] + ": ";
                    if ($(ele).children().is('input[type="checkbox"]')) {
                        if ($(ele).children().is(':checked')) {
                            _datosRegistro += "SI";
                        } else {
                            _datosRegistro += "NO";
                        }
                    } else {
                        _datosRegistro += $(ele).text();
                    }
                    _datosRegistro += "<br>";
                }
            });
            _strContenido += _datosRegistro;
            $modalBotonAceptar.data('idunico', id);

            $modalVentanaTituloContenido.append(_strTitulo);
            $modalVentanaCuerpo.append(_strContenido);

            $modalBotonAceptar.append("ACEPTAR");
            $modalBotonCerrar.append("CERRAR");
            $modalVentanaPie.append($modalBotonAceptar);
            $modalVentanaPie.append($modalBotonCerrar);

            $modalVentanaTitulo.append($modalVentanaTituloContenido);
            $modalVentana.append($modalVentanaTitulo);
            $modalVentana.append($modalVentanaCuerpo);
            $modalVentana.append($modalVentanaPie);

            $modalBaseGris.append($modalVentana);

            $('body').append($modalBaseGris);
            $modalBaseGris.fadeIn();
        };

        // Cerrar la ventana de confirmación de la acción.
        $('body').on('click', '#btnCerrarVM', function(e) {
            e.preventDefault();
            $('#modalBaseGris').fadeOut(300, function() {
                $(this).remove();
            });
        });

        // Confirmación de la acción ELIMINAR O ACTUALIZAR
        $('body').on('click', '#btnAceptarVM', function(e) {
            e.preventDefault();
            var $elemento = $(this);
            var _accion = $elemento.data('accion');
            var _idunico = $elemento.data('idunico');
            switch (parseInt(_accion)) {
                case 2: // Actualizar
                    $.each($registro.children(), function(ind, ele) {
                        if (ind < campos.length) {
                            if ($(ele).children().is('input[type="checkbox"]')) {
                                if ($(ele).children().is(':checked')) {
                                    $(campos[ind]).attr("checked", true);
                                } else {
                                    $(campos[ind]).attr("checked", false);
                                }
                            } else {
                                $(campos[ind]).val($(ele).text());
                            }
                        }
                    });
                    $(btnGuardar).data("accion", "2");
                    $('html, body').animate({scrollTop: '0px'}, 500); // Para subir la pantalla
                    break;
                case 3: // Eliminar
                    var _datos = {accion: 3, idunico: _idunico};
                    console.log(_datos);
                    enviarDatos(_datos);
                    $registro.remove();
                    break;
                default:
                    console.log("No se acepta esta funcion");
            }
            ;
            $('#btnCerrarVM').click();
        });

        // Accion a realizar ELIMINAR O ACTUALIZAR
        $(tabla).on('click', 'tr td button', function(e) {
            e.preventDefault();
            var $boton = $(this);
            var _accion = 0;
            $registro = $boton.parent().parent();
            if ($boton.data('actualizar') === 1) {
                _accion = 2;
            }
            if ($boton.data('eliminar') === 1) {
                _accion = 3;
            }
            mostrarConfirmacion(_accion, $boton.data('idunico'));
        });

        // Enviar datos al servidor
        var enviarDatos = function(_datos) {
            $.ajax({
                beforeSend: function() {
                    $(msgNormal).html('procesando');
                },
                url: _url,
                type: _met,
                data: _datos,
                dataType: 'json',
                success: function(respuesta) {
                    var miHtml = '<div class="alert alert-';
                    switch (respuesta['tipoMensaje']) {
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
                    $(msgNormal).html(miHtml);
                    if (respuesta['tipoMensaje'] !== 4 && respuesta['tipoMensaje'] !== 0) {
                        limpiarFormulario();
                    }
                    ;
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.log(textStatus);
                }
            });
        };

        $(frmLista + ' #btnListar').on('click', function(e) {
            e.preventDefault();
            cargarLista();
        });
        // cargar la lista
        cargarLista();
    };
})(jQuery);