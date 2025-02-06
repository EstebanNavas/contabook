package com.contabook.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Model.DBMailMarketing.TblPucAux;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.dbaquamovil.TblAgendaLogVisitas;
import com.contabook.Service.DBMailMarketing.TblDctosPeriodoService;
import com.contabook.Service.DBMailMarketing.TblLocalesReporteService;
import com.contabook.Service.DBMailMarketing.TblPucAuxService;
import com.contabook.Service.DBMailMarketing.TblPucService;
import com.contabook.Service.dbaquamovil.TblLocalesService;
import com.contabook.Utilidades.ControlDeInactividad;
import com.contabook.Model.Reportes.ReportesDTO;
import com.contabook.Model.dbaquamovil.TblLocales;
import com.contabook.Projection.TblPucAuxDTO;
import com.contabook.ServiceApi.ReporteSmsServiceApi;
import com.contabook.enums.TipoReporteEnum;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class ReporteCuentasContableController {
	
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
	ReporteSmsServiceApi reporteSmsServiceApi;
	
	@Autowired
	ControlDeInactividad controlDeInactividad;
	
	@GetMapping("/ReporteCuentasContable")
	public String reporteCuentasContable(HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		
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

		    
		    
	   		List<TblPuc> listaNivel1 = tblPucService.pucNivel1();
			model.addAttribute("opcionesNivel1", listaNivel1);
	    

			
			return "Reportes/Contables/RepCuentasContable";


	}
	
	
	@PostMapping("/DescargarReporteCuentasContables")
	public ResponseEntity<Resource> DescargarReporteCuentasContables(@RequestBody Map<String, Object> requestBody,HttpServletRequest request, Model model) 
			                                                       throws JRException, IOException, SQLException {

		Class tipoObjeto = this.getClass();
		String nombreClase = tipoObjeto.getName();
		System.out.println("CONTROLLER " + nombreClase);

		// Validar si el local está logueado
		Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
		String sistema = (String) request.getSession().getAttribute("sistema");

		// Obtenemos los datos del JSON recibido
		String idClaseStr = (String) requestBody.get("idClase");
		System.out.println("idClaseStr es  : " + idClaseStr);
		Integer idClase = Integer.parseInt(idClaseStr);

		String formato = (String) requestBody.get("formato");

		int idLocal = usuario.getIdLocal();

		int xIdReporte = 1000;
		
		// Obtenemos el periodo activo
		List <TblDctosPeriodo> PeriodoActivo = tblDctosPeriodoService.ObtenerPeriodoActivo(idLocal);
		
		Integer idPeriodo = 0;
		
		for(TblDctosPeriodo P : PeriodoActivo) {						
			idPeriodo = P.getIdPeriodo();					
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
			params.put("p_idPeriodo", idPeriodo);
			params.put("p_nombreLocal", L.getNombreLocal());
			params.put("p_nit", L.getNit());
			params.put("p_titulo", xTituloReporte);
			params.put("p_direccion", L.getDireccion());
			params.put("p_idLocal", idLocal);
			params.put("p_fechaActual", strFechaActual);
			xPathReport = L.getPathReport() + "contabook" + xCharSeparator;

		}

		List<TblPucAuxDTO> lista = null;
		
		lista = tblPucAuxService.listaCuentasContables(idLocal);
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
