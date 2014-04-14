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
import negocio.ingenioti.org.NTiposDeDocumentos;
import objetos.ingenioti.org.OTiposDeDocumento;

/**
 *
 * @author Alexys
 */
@WebServlet(name = "STiposDeDocumento", urlPatterns = {"/STiposDeDocumento"})
public class STiposDeDocumento extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession sesion = request.getSession();
        if (SUtilidades.autenticado(sesion)) {
            
            // Elementos de respuesta
            String mensaje = "";
            short tipoMensaje = 0; // 0: Danger, 1: success, 2: info, 3: warning
            String mensajeLista = "";
            short tipoMensajeLista = 0; // 0: Danger, 1: success, 2: info, 3: warning
            JsonObject modelo, jsLista;
            JsonArrayBuilder jsArray;
            StringWriter sEscritor = new StringWriter();
            JsonWriter jsEscritor = Json.createWriter(sEscritor);

            OCredencial credencial = (OCredencial) sesion.getAttribute("credencial");
            String accion = request.getParameter("accion");
            String tipoConsulta = request.getParameter("tipoConsulta");
            String idTipoDocumento = request.getParameter("idunico");
            String abreviatura = request.getParameter("abreviatura");
            String tipoDeDocumento = request.getParameter("tipoDocumento");
            String activo = request.getParameter("activo");

            // Variables de paginación
            String pagina = request.getParameter("pag");
            String limite = request.getParameter("lim");
            String columnaOrden = request.getParameter("cor");
            String tipoOrden = request.getParameter("tor");
            
            if(tipoOrden == null || tipoOrden.length()==0) tipoOrden = "asc";
            
            boolean bActivo = activo != null;

            Short sAccion;
            Short sTipoConsulta;
            int iPagina, iLimite, iColumnaOrden;
            try{
                sAccion = Short.parseShort(accion);
            } catch (NumberFormatException nfe) {
                sAccion = 0;
            }
            try {
                iPagina = Integer.parseInt(pagina);
                iLimite = Integer.parseInt(limite);
                iColumnaOrden = Integer.parseInt(columnaOrden);
            } catch (NumberFormatException nfe) {
                iPagina = 1;
                iLimite = 5;
                iColumnaOrden = 1;
            }
            try {
                sTipoConsulta = Short.parseShort(tipoConsulta);
            } catch (NumberFormatException nfe) {
                sTipoConsulta = 0;
            }
            short iidTipoDocumento = 0;
            try{
                iidTipoDocumento = Short.parseShort(idTipoDocumento);
            } catch (NumberFormatException nfe){}

            // Para realizar cualquier accion de insertar, modificar o borrar
            NTiposDeDocumentos nTiposDeDocumento = new NTiposDeDocumentos();
            OTiposDeDocumento oTiposDeDocumento = new OTiposDeDocumento(iidTipoDocumento, abreviatura, tipoDeDocumento, bActivo);
            
            // Preparación de la lista de objetos a retornar
            ArrayList<OTiposDeDocumento> lista = new ArrayList<OTiposDeDocumento>();
            int totalPaginas = 0;
            int totalRegistros = 0;

            if (sAccion > 0 && sAccion < 4) {
                if(sAccion != 3 && (abreviatura==null || abreviatura.length()==0 ||
                        tipoDeDocumento==null || tipoDeDocumento.length()==0)){
                    tipoMensaje = 4;
                    mensaje = "Todos los campos son obligatorios";
                    modelo = Json.createObjectBuilder()
                        .add("tipoMensaje", tipoMensaje)
                        .add("mensaje", mensaje)
                        .build();
                    jsEscritor.writeObject(modelo);
                } else {
                    int respuesta = 0;
                    try{
                        respuesta = nTiposDeDocumento.ejecutarSQL(sAccion, oTiposDeDocumento, credencial);
                        tipoMensaje = 3; // Inicia en Warning porque es la mayor cantidad de opciones
                        if (respuesta == -2) {
                            mensaje = "Error de llave duplicada.";
                        }
                        if (respuesta == -1) {
                            mensaje = "Error de violación de llave foranea. Problema de dependencias.";
                        }
                        if (respuesta == 0 || respuesta < -2) {
                            tipoMensaje = 0;
                            mensaje = "No se encontró el registro en la BD.";
                        }
                        if (respuesta > 0){
                            tipoMensaje = 1;
                            mensaje = "Proceso realizado correctamente - ID: "+respuesta;
                        }
                    } catch (ExcepcionGeneral eg){
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
            if(sAccion == 4){
                try{
                    totalRegistros = nTiposDeDocumento.getCantidadRegistros("tiposdedocumento");
                    if(totalRegistros > 0){
                        totalPaginas = (int) Math.ceil((double)totalRegistros/(double)iLimite);
                    } else {
                        totalPaginas = 0;
                    }
                    if(iPagina > totalPaginas){
                        iPagina = totalPaginas;
                    }
                    lista = nTiposDeDocumento.consultar(sAccion, sTipoConsulta, oTiposDeDocumento, credencial, iPagina, iLimite, iColumnaOrden, tipoOrden);
                    tipoMensajeLista = 1;
                    mensajeLista = "Cargue de consulta realizado correctamente.";
                } catch (ExcepcionGeneral eg){
                    tipoMensajeLista = 3;
                    mensajeLista = eg.getMessage();
                }
                jsArray = Json.createArrayBuilder();
                for(OTiposDeDocumento td: lista){
                    JsonObject temp = Json.createObjectBuilder()
                            .add("idtipodedocumento", td.getIdtipodedocumento())
                            .add("abreviatura", td.getAbreviatura())
                            .add("tipodedocumento", td.getTipodedocumento())
                            .add("activo", td.isActivo())
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
