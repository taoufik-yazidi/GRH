;
(function($) {
    $.fn.jsHojaVida = function() {
        var cargaSelect = function(servlet, idCampo, valorCampo, select) {
            $.ajax({
                url: servlet,
                type: 'post',
                dataType: 'json',
                data: {accion: 4, pag: 1, lim: -1, cor: 3, tor: 'asc'},
                success: function(respuesta) {
                    var miHTML = '';
                    if (respuesta['tipoMensajeLista'] !== 1) {
                        miHTML += '<div class="alert alert-warning">';
                        miHTML += '<span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;';
                        miHTML += respuesta['mensajeLista'];
                        miHTML += '</div>';
                        $('#unDiv').html(miHTML);
                    } else {
                        var objetos = respuesta['lista'];
                        $.each(objetos, function(indice, valor) {
                            miHTML += '<option value="';
                            miHTML += valor[idCampo];
                            miHTML += '">';
                            miHTML += valor[valorCampo];
                            miHTML += '</option>';
                        });
                        for (var desplegable in select) {
                            $(select[desplegable]).html(miHTML);
                        }
                    }
                }
            });
        };
        var filtraMunicipios = function(iddpto, destino) {
            $.ajax({
                url: 'SMunicipios',
                type: 'post',
                dataType: 'json',
                data: {accion: 4, tipoConsulta: 2, iddepartamento: iddpto, pag: 1, lim: -1, cor: 6, tor: 'asc'},
                success: function(respuesta) {
                    var miHTML = '';
                    if (respuesta['tipoMensajeLista'] !== 1) {
                        miHTML += '<div class="alert alert-warning">';
                        miHTML += '<span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;';
                        miHTML += respuesta['mensajeLista'];
                        miHTML += '</div>';
                        $('#unDiv').html(miHTML);
                    } else {
                        var objetos = respuesta['lista'];
                        $.each(objetos, function(indice, valor) {
                            miHTML += '<option value="';
                            miHTML += valor['idmunicipio'];
                            miHTML += '">';
                            miHTML += valor['nombremunic'];
                            miHTML += '</option>';
                        });
                        $(destino).html(miHTML);
                    }
                }
            });
        };

        var departamentos = ['#iddeptonacimiento', '#iddeptoexpedicion', '#iddeptoresidencia'];

        cargaSelect('STiposDeDocumento', 'idtipodedocumento', 'tipodedocumento', ['#idtipodedocumento']);
        cargaSelect('SGenero', 'idgenero', 'genero', ['#idgenero']);
        cargaSelect('SDepartamentos', 'iddepartamento', 'nombre', departamentos);
        cargaSelect('SEstadocivil', 'idestadocivil', 'nombre', ['#idestadocivil']);

        // Actualización de los municipios cuando cambian de departamento
        $('#iddeptonacimiento').on('change', function() {
            filtraMunicipios($(this).val(), '#idlugarnacimiento');
        });
        $('#iddeptoexpedicion').on('change', function() {
            filtraMunicipios($(this).val(), '#idlugarexpedicion');
        });
        $('#iddeptoresidencia').on('change', function() {
            filtraMunicipios($(this).val(), '#idlugarresidencia');
        });

        for (dep in departamentos) {
            $(departamentos[dep]).change();
        }

        // Para mostrar u ocultar el botón de subir archivo
        $('#preview').hover(
                function() {
                    $(this).find('a').fadeIn();
                }, function() {
            $(this).find('a').fadeOut();
        });

        $('#file-select').on('click', function(e) {
            e.preventDefault();
            $('#foto').click();
        });

        $('#foto').change(function() {
            var lector = new FileReader();
            lector.onload = function(e) {
                $('#preview img').attr('src', e.target.result);
            };
            lector.readAsDataURL(this.files[0]);
            console.log($(this).val());
        });

        // Proceso para subir la foto
        // Variable to store your files
        var files;
        // Add events
        $('#foto').on('change', function(event) {
            files = event.target.files;
        });

        $('#btnCargaFoto').on('click', function(event) {
            event.preventDefault(); 
            // Create a formdata object and add the files
            var data = new FormData();

            $.each(files, function(key, value) {
                data.append(key, value);
            });
            $.ajax({
                url: 'SCargarArchivos',
                type: 'post',
                data: data,
                cache: false,
                dataType: 'json',
                processData: false, // Don't process the files
                contentType: false, // Set content type to false as jQuery will tell the server its a query string request
                success: function(data, textStatus, jqXHR) {
                    if (typeof data.error === 'undefined') {
                        // Success so call function to process the form
                        console.log(data);
                    } else {
                        // Handle errors here
                        console.log('ERRORS: ' + data.error);
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    // Handle errors here
                    console.log('ERRORS: ' + textStatus);
                    // STOP LOADING SPINNER
                }
            });
        });

    };
})(jQuery);