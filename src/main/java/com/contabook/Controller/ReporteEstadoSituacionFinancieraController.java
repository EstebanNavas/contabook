package com.contabook.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.contabook.Model.DBMailMarketing.TblDctosPeriodo;
import com.contabook.Model.DBMailMarketing.TblLocalesReporte;
import com.contabook.Model.DBMailMarketing.TblTipoCpte;
import com.contabook.Model.Reportes.ReportesDTO;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.dbaquamovil.TblAgendaLogVisitas;
import com.contabook.Model.dbaquamovil.TblLocales;
import com.contabook.Projection.TblPucAuxDTO;
import com.contabook.Projection.TblPucDTO;
import com.contabook.Service.DBMailMarketing.TblDctoDetalleService;
import com.contabook.Service.DBMailMarketing.TblDctoService;
import com.contabook.Service.DBMailMarketing.TblDctosPeriodoService;
import com.contabook.Service.DBMailMarketing.TblLocalesReporteService;
import com.contabook.Service.DBMailMarketing.TblPucAuxService;
import com.contabook.Service.DBMailMarketing.TblPucService;
import com.contabook.Service.DBMailMarketing.TblTipoCpteService;
import com.contabook.Service.dbaquamovil.TblLocalesService;
import com.contabook.ServiceApi.ReporteSmsServiceApi;
import com.contabook.Utilidades.ControlDeInactividad;
import com.contabook.enums.TipoReporteEnum;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class ReporteEstadoSituacionFinancieraController {
	
	@Autowired
	TblPucService tblPucService;
	
	@Autowired
	TblLocalesService tblLocalesService;
	
	@Autowired
	TblDctosPeriodoService tblDctosPeriodoService;
	
	@Autowired
	TblLocalesReporteService tblLocalesReporteService;
	
	@Autowired
	TblPucAuxService tblPucAuxService;
	
	@Autowired
	TblTipoCpteService tblTipoCpteService;
	
	@Autowired
	TblDctoService tblDctoService;
	
	@Autowired
	TblDctoDetalleService tblDctoDetalleService;
	
	@Autowired
	ReporteSmsServiceApi reporteSmsServiceApi;
	
	@Autowired
	ControlDeInactividad controlDeInactividad;
	
	@GetMapping("/ReporteEstadoSituacionFinanciera")
	public String reporteEstadoSituacionFinanciera(HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		// ----------------------------------------------------------- VALIDA INACTIVIDAD ------------------------------------------------------------
	    HttpSession session = request.getSession();
	    //Integer idUsuario = (Integer) session.getAttribute("xidUsuario");
	    
	    @SuppressWarnings("unchecked")
		List<TblAgendaLogVisitas> UsuarioLogueado = (List<TblAgendaLogVisitas>) session.getAttribute("UsuarioLogueado");
	    
	    Integer estadoUsuario = 0;
	    

	        for (TblAgendaLogVisitas usuarioLog : UsuarioLogueado) {
	            Integer idLocalUsuario = usuarioLog.getIdLocal();
	            Integer idLogUsuario = usuarioLog.getIDLOG();
	            String sessionIdUsuario = usuarioLog.getSessionId();
	            
	            
	           estadoUsuario = controlDeInactividad.ingresa(idLocalUsuario, idLogUsuario, sessionIdUsuario);          
	        }
    
	           if(estadoUsuario.equals(2)) {
	        	   System.out.println("USUARIO INACTIVO");
	        	   return "redirect:/";
	           }
		
		//------------------------------------------------------------------------------------------------------------------------------------------
	    
	           List<TblTipoCpte> listaComprobantes = tblTipoCpteService.ListaComprobantes();
	   	       System.out.println("La lista de ListaComprobantes es: " + listaComprobantes);

	   	        List <TblDctosPeriodo> ListaPeriodos = tblDctosPeriodoService.ListaTotalPeriodos(idLocal);
	           
	           model.addAttribute("xListaComprobantes", listaComprobantes);
	           model.addAttribute("xListaPeriodos", ListaPeriodos);

			return "Reportes/Financieros/ReporteEstadoSituacionFinanciera";


	}
	
	
	@PostMapping("/DescargarReporteEstadoSituacionFinanciera")
	public ResponseEntity<Resource> DescargarReporteEstadoSituacionFinanciera(@RequestBody Map<String, Object> requestBody,HttpServletRequest request, Model model) 
			                                                       throws JRException, IOException, SQLException {

		Class tipoObjeto = this.getClass();
		String nombreClase = tipoObjeto.getName();
		System.out.println("CONTROLLER " + nombreClase);

		// Validar si el local está logueado
		Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
		String sistema = (String) request.getSession().getAttribute("sistema");

		// Obtenemos los datos del JSON recibido
		String idPeriodo1Str = (String) requestBody.get("idPeriodo1");
		System.out.println("idPeriodo1Str es  : " + idPeriodo1Str);
		Integer idPeriodo1 = Integer.parseInt(idPeriodo1Str);
		
		String idPeriodo2Str = (String) requestBody.get("idPeriodo2");
		System.out.println("idPeriodo2Str es  : " + idPeriodo2Str);
		Integer idPeriodo2 = Integer.parseInt(idPeriodo2Str);

		String formato = (String) requestBody.get("formato");

		int idLocal = usuario.getIdLocal();

		int xIdReporte = 1200;
		
		String textoPeriodo = "";
		
		if(idPeriodo1.equals(idPeriodo2)) {
			
			textoPeriodo = idPeriodo1Str;
		}else {
			
			textoPeriodo = idPeriodo1Str + " AL " + idPeriodo2Str;
		}
		
		

		// Obtenemos el FileName del reporte y el titulo
		List<TblLocalesReporte> reporte = tblLocalesReporteService.listaUnFCH(idLocal, xIdReporte);

		String xFileNameReporte = "";
		String xTituloReporte = "";

		for (TblLocalesReporte R : reporte) {

			xFileNameReporte = R.getFileName();
			xTituloReporte = R.getReporteNombre();
		}

		// Obtenemos la información del local que usaremos para los PARAMS del
		// encabezado
		List<TblLocales> Local = tblLocalesService.ObtenerLocal(idLocal);

		Map<String, Object> params = new HashMap<>();
		params.put("tipo", formato);
		params.put("idLocal", idLocal);
		
		// Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Formatear la fecha como un String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String strFechaActual = fechaActual.format(formatter);

		String xPathReport = "";

		String xCharSeparator = File.separator;
		for (TblLocales L : Local) {

			// Parametros del encabezado
			//params.put("p_idPeriodo", idPeriodo);
			params.put("p_TextoPeriodo", textoPeriodo);
			params.put("p_nombreLocal", L.getNombreLocal());
			params.put("p_nit", L.getNit());
			params.put("p_titulo", xTituloReporte);
			params.put("p_direccion", L.getDireccion());
			params.put("p_idLocal", idLocal);
			params.put("p_fechaActual", strFechaActual);
			//xPathReport = L.getPathReport() + "contabook" + xCharSeparator;
			xPathReport = L.getPathReportContaBook() + "contabook" + xCharSeparator;

		}
		
		
		
        List<TblPucDTO> RepEstadoResultadoIntegral = tblPucService.RepEstadoResultadoIntegral(idLocal, idPeriodo1, idPeriodo2);
        
        Double totalIngreso = 0.0;
        Double totalGastos = 0.0;
        Double totalCostoVenta = 0.0;
        Double totalCostosProduccion = 0.0;
        
        
        for(TblPucDTO rep : RepEstadoResultadoIntegral ) {
        	
        	Double diferencia = rep.getVrDebito() - rep.getVrCredito();
        	
        	if (rep.getIdClase() == 4) {
                totalIngreso += diferencia;
            } else if (rep.getIdClase() == 5) {
                totalGastos += diferencia;
            } else if (rep.getIdClase() == 6) {
                totalCostoVenta += diferencia;
            } else if (rep.getIdClase() == 7) {
                totalCostosProduccion += diferencia;
            }       	
        	
        }
        
       // Double gananciaPerdida  = -515.0;
        Double gananciaPerdida = totalIngreso -  (Math.abs(totalGastos) +  Math.abs(totalCostoVenta) +   Math.abs(totalCostosProduccion));
        params.put("p_gananciaPerdida", gananciaPerdida);
        
        
        
        System.out.println("totalIngreso es : " + totalIngreso);
        System.out.println("totalGastos es : " + totalGastos);
        System.out.println("totalCostoVenta es : " + totalCostoVenta);
        System.out.println("totalCostosProduccion es : " + totalCostosProduccion);
        System.out.println("gananciaPerdida es : " + gananciaPerdida);

		List<TblPucDTO> lista = null;
		
		lista = tblPucService.RepEstadoSituacionFinanciera(idLocal, idPeriodo1, idPeriodo2);
		System.out.println("lista es : " + lista);

		System.out.println("formato es : " + formato);
		System.out.println("xFileNameReporte es : " + xFileNameReporte);
		System.out.println("xPathReport es : " + xPathReport);

		// Se crea una instancia de JRBeanCollectionDataSource con la lista
		JRDataSource dataSource = new JRBeanCollectionDataSource(lista);

		ReportesDTO dto = reporteSmsServiceApi.Reportes(params, dataSource, formato, xFileNameReporte, xPathReport); 

		// Verifica si el stream tiene datos y, si no, realiza una lectura en un búfer
		InputStream inputStream = dto.getStream();
		if (inputStream == null) {
			// Realiza una lectura en un búfer alternativo si dto.getStream() es nulo
			byte[] emptyContent = new byte[0];
			inputStream = new ByteArrayInputStream(emptyContent);
		}

		// Envuelve el flujo en un InputStreamResource
		InputStreamResource streamResource = new InputStreamResource(inputStream);

		// Configura el tipo de contenido (media type)
		MediaType mediaType;
		if (params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		} else {
			mediaType = MediaType.APPLICATION_PDF;
		}

		// Configura la respuesta HTTP
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
				.contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
	}

}
