
<nav class="navbar navbar-default" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">H.C.I. - GroupTIC</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
            <li class="active"><a href="inicio.jsp">Inicio</a></li>
            <li><a href="hojadevida.jsp">Hoja de Vida</a></li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Parámetros<b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="tiposdedocumentos.jsp">Tipos de Documentos</a></li>
                    <li><a href="departamentos.jsp">Departamentos</a></li>
                    <li><a href="municipios.jsp">Municipios</a></li>
                    <li><a href="genero.jsp">Géneros</a></li>
                    <li><a href="estadocivil.jsp">Estado Civil</a></li>
                    <li class="divider"></li>
                    <li><a href="#">Separated link</a></li>
                    <li class="divider"></li>
                    <li><a href="#">One more separated link</a></li>
                </ul>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%=request.getParameter("usuario")%><b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="cambiarclave.jsp">Cambiar Clave</a></li>
                    <li><a href="SCerrarSesion">Cerrar Sesión</a></li>
                </ul>
            </li>
        </ul>
    </div><!-- /.navbar-collapse -->
</nav>