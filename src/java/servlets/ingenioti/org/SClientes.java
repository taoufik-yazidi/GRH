package servlets.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import objetos.ingenioti.org.OCredencial;
import negocio.ingenioti.org.NClientes;
import objetos.ingenioti.org.OCliente;

@WebServlet(name = "SClientes", urlPatterns = {"/SClientes"})
public class SClientes extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession sesion = request.getSession();
        if (SUtilidades.autenticado(sesion)) {
            
            // Elementos de respuesta
            String mensaje = "";
            short tipoMensaje = 0;
            String mensajeLista = "";
            short tipoMensajeLista = 0;
            JsonObject modelo, jsLista;
            JsonArrayBuilder jsArray;
            StringWriter sEscritor = new StringWriter();
            JsonWriter jsEscritor = Json.createWriter(sEscritor);

            OCredencial credencial = (OCredencial) sesion.getAttribute("credencial");
            String accion = request.getParameter("accion");
            System.err.println("Accion vale: "+accion);
            String tipoConsulta = request.getParameter("tipoConsulta");
            Short sAccion;
            Short sTipoConsulta;
            try {
                sAccion = Short.parseShort(accion);
            } catch (NumberFormatException nfe) {
                sAccion = 0;
            }
            try {
                sTipoConsulta = Short.parseShort(tipoConsulta);
            } catch (NumberFormatException nfe) {
                sTipoConsulta = 0;
            }

            // Variables de paginación
            String pagina = request.getParameter("pag");
            String limite = request.getParameter("lim");
            String columnaOrden = request.getParameter("cor");
            String tipoOrden = request.getParameter("tor");

            if (tipoOrden == null || tipoOrden.length() == 0) {
                tipoOrden = "asc";
            }

            int iPagina, iLimite, iColumnaOrden;
            try {
                iPagina = Integer.parseInt(pagina);
                iLimite = Integer.parseInt(limite);
                iColumnaOrden = Integer.parseInt(columnaOrden);
            } catch (NumberFormatException nfe) {
                iPagina = 1;
                iLimite = 5;
                iColumnaOrden = 1;
            }

            String sidcliente = request.getParameter("idunico");
            String stipodocumento = request.getParameter("tipodocumento");
            String snumerodocumento = request.getParameter("numerodocumento");
            String sdireccion = request.getParameter("direccion");
            String stelefono = request.getParameter("telefono");
            String scorreoelectronico = request.getParameter("correoelectronico");
            String sresponsable = request.getParameter("responsable");
            String sconsejo = request.getParameter("consejo");
            
            short iidcliente = 0;
            if(sAccion != 1){ // Si se va a insertar no interesa el id
                try {
                    iidcliente = Short.parseShort(sidcliente);
                } catch (NumberFormatException nfe) {
                    System.err.println("Error al convertir: idcliente en Short  en el servlet SClientes");
                }
            }
            
            short itipodocumento = 0;
            try {
                itipodocumento = Short.parseShort(stipodocumento);
            } catch (NumberFormatException nfe) {
                System.err.println("Error al convertir: tipodocumento en Short  en el servlet SClientes");
            }
            // Para realizar cualquier accion: insertar, modificar o borrar
            NClientes nClientes = new NClientes();
            OCliente oClientes = new OCliente(iidcliente, itipodocumento, snumerodocumento, sdireccion, stelefono, scorreoelectronico, sresponsable, sconsejo);

            // Preparación de la lista de objetos a retornar
            ArrayList<OCliente> lista = new ArrayList<OCliente>();
            int totalPaginas = 0;
            int totalRegistros = 0;

            // Para realizar la acción de la 1 a la 3
            if (sAccion > 0 && sAccion < 4) {
                // Validación de campos vacios
                if (sAccion != 3 && (stipodocumento == null || stipodocumento.length() == 0 || snumerodocumento == null || snumerodocumento.length() == 0 || sdireccion == null || sdireccion.length() == 0 || stelefono == null || stelefono.length() == 0 || scorreoelectronico == null || scorreoelectronico.length() == 0 || sresponsable == null || sresponsable.length() == 0 || sconsejo == null || sconsejo.length() == 0)) {
                    tipoMensaje = 4;
                    mensaje = "Todos los campos son obligatorios";
                    modelo = Json.createObjectBuilder()
                            .add("tipoMensaje", tipoMensaje)
                            .add("mensaje", mensaje)
                            .build();
                    jsEscritor.writeObject(modelo);
                } else {
                    int respuesta = 0;
                    try {
                        respuesta = nClientes.ejecutarSQL(sAccion, oClientes, credencial);
                        tipoMensaje = 3; // Inicia en warning porque es la mayor cantidad de opciones
                        if (respuesta == -2) {
                            mensaje = "Error de llave duplicada";
                        }
                        if (respuesta == -1) {
                            mensaje = "Error de violación de llave foranea. Problema de dependencias.";
                        }
                        if (respuesta == 0 || respuesta < -1) {
                            mensaje = "No se encontró el registro en la BD, o error desconocido de BD.";
                        }
                        if (respuesta > 0) {
                            mensaje = "Proceso realizado correctamente - ID: " + respuesta;
                            tipoMensaje = 1;
                        }
                    } catch (ExcepcionGeneral eg) {
                        tipoMensaje = 3;
                        mensaje = eg.getMessage();
                    }
                    modelo = Json.createObjectBuilder()
                            .add("tipoMensaje", tipoMensaje)
                            .add("mensaje", mensaje)
                            .build();
                    jsEscritor.writeObject(modelo);
                }
            }
            if (sAccion == 4) { // Consultar
                try {
                    totalRegistros = nClientes.getCantidadRegistros("clientes");
                    if (totalRegistros > 0) {
                        totalPaginas = (int) Math.ceil((double) totalRegistros / (double) iLimite);
                    } else {
                        totalPaginas = 0;
                    }
                    if (iPagina > totalPaginas) {
                        iPagina = totalPaginas;
                    }
                    lista = nClientes.consultar((short) 4, sTipoConsulta, oClientes, credencial, iPagina, iLimite, iColumnaOrden, tipoOrden);
                    tipoMensajeLista = 1;
                    mensajeLista = "Cargue de consulta realizado correctamente.";
                } catch (ExcepcionGeneral eg) {
                    tipoMensajeLista = 3;
                    mensajeLista = eg.getMessage();
                }

                jsArray = Json.createArrayBuilder();
                for (OCliente obj : lista) {
                    JsonObject temp = Json.createObjectBuilder()
                            .add("idcliente", obj.getIdcliente())
                            .add("tipodocumento", obj.getTipodocumento())
                            .add("numerodocumento", obj.getNumerodocumento())
                            .add("direccion", obj.getDireccion())
                            .add("telefono", obj.getTelefono())
                            .add("correoelectronico", obj.getCorreoelectronico())
                            .add("responsable", obj.getResponsable())
                            .add("consejo", obj.getConsejo())
                            .build();
                    jsArray.add(temp);
                }
                jsLista = Json.createObjectBuilder()
                        .add("tipoMensajeLista", tipoMensajeLista)
                        .add("mensajeLista", mensajeLista)
                        .add("registros", totalRegistros)
                        .add("paginas", totalPaginas)
                        .add("lista", jsArray)
                        .build();
                jsEscritor.writeObject(jsLista);
            }
            jsEscritor.close();

            String jsObjeto = sEscritor.toString();
            PrintWriter out = response.getWriter();
            try {
                out.println(jsObjeto);
            } finally {
                out.close();
            }
        } else {
            SUtilidades.irAPagina("/index.jsp", request, response, getServletContext());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
