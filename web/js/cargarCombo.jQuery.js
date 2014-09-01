;
(function($) {
    $.fn.cargarCombo = function(servlet, idCampo, valorCampo, select) {
            $.ajax({
                url: servlet + 'Sel',
                type: 'post',
                dataType: 'json',
                data: {pag: 1, lim: -1, cor: 3, tor: 'asc'},
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
})(jQuery);