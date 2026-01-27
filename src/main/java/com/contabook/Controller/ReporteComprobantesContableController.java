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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.contabook.Model.DBMailMarketing.TblDctosPeriodo;
import com.contabook.Model.DBMailMarketing.TblLocalesReporte;
import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Model.DBMailMarketing.TblTipoCpte;
import com.contabook.Model.Reportes.ReportesDTO;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.dbaquamovil.TblAgendaLogVisitas;
import com.contabook.Model.dbaquamovil.TblLocales;
import com.contabook.Projection.TblDctoDTO;
import com.contabook.Projection.TblDctoDetalleDTO;
import com.contabook.Projection.TblPucAuxDTO;
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
public class ReporteComprobantesContableController {
	
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
	
	@GetMapping("/ReporteCptesContable")
	public String reporteCptesContable(HttpServletRequest request,Model model) {
		
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
	   	        
	   	       // Obtenemos el periodo activo
	   			List <TblDctosPeriodo> PeriodoActivo = tblDctosPeriodoService.ObtenerPeriodoActivo(idLocal);
	   			
	   			Integer idPeriodo = 0;
	   			
	   			for(TblDctosPeriodo P : PeriodoActivo) {						
	   				idPeriodo = P.getIdPeriodo();					
	   			}
	           
	           model.addAttribute("xListaComprobantes", listaComprobantes);
	           model.addAttribute("xListaPeriodos", ListaPeriodos);
	           model.addAttribute("xIdPeriodo", idPeriodo);

			return "Reportes/Contables/RepComprobantesContable";


	}
	
	
	@PostMapping("/BuscarComprobantes")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> BuscarComprobantes(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();

	    System.out.println("SI ENTRÓ A  /BuscarComprobantes");

	        // Obtenemos los datos del JSON recibido
	        String tipoComprobanteStr = (String) requestBody.get("tipoComprobante");
	        Integer tipoComprobante = Integer.parseInt(tipoComprobanteStr);
	        	        
	        String idPeriodoStr = (String) requestBody.get("idPeriodo");	        
	        Integer idPeriodo = Integer.parseInt(idPeriodoStr);
	        
	        
	        String idPeriodo2Str = (String) requestBody.get("idPeriodo2");	        
	        Integer idPeriodo2 = Integer.parseInt(idPeriodo2Str);


	        List<TblDctoDTO> listaComprobantes = tblDctoService.listaComprobantes(idLocal, tipoComprobante, idPeriodo, idPeriodo2);
	        System.out.println("listaComprobantes es " + listaComprobantes);
	        
		    
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "LOGGGGGGGGG");
		    response.put("listaComprobantes", listaComprobantes);
		    return ResponseEntity.ok(response);
	   
	    
	}
	
	
	@PostMapping("/TraerComprobanteContable-Post")
	public ModelAndView TraerComprobanteContablePost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");

	    // Obtenemos los datos del JSON recibido
	    Integer idCpte = (Integer) requestBody.get("idCpte");

	    System.out.println("Entró a /TraerComprobante-Post con idCpte: " + idCpte);


	    // Redirige a la vista y le pasamos el parametro de idTercero
	    ModelAndView modelAndView = new ModelAndView("redirect:/TraerComprobanteContable?idCpte=" + idCpte);
	    return modelAndView;
	}
	
	
	@GetMapping("/TraerComprobanteContable")
	public String traerComprobanteContable(@RequestParam(name = "idCpte", required = false) Integer idCpte, HttpServletRequest request, Model model) {
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		System.out.println("Entró a /TraerReferencia con idCpte: " + idCpte);
		
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

	           List<TblDctoDetalleDTO> comprobanteDetalle = tblDctoDetalleService.comprobanteContableDetalle(idLocal, idCpte);
	           
	           String nombreTipoComprobante = "";
	           
	           for (TblDctoDetalleDTO detalle : comprobanteDetalle) {
	        	   
	        	   nombreTipoComprobante = detalle.getNombreTipoComprobante();
	           }
	           
	           
	           
	           model.addAttribute("xComprobanteDetalle", comprobanteDetalle);
	           model.addAttribute("xNumeroComprobante", idCpte);
	           model.addAttribute("xNombreTipoComprobante", nombreTipoComprobante);
		   

			
			return "Reportes/Contables/ComprobanteContableDetalle";

	}
	
	
	
	@PostMapping("/DescargarDetalleComprobanteContable")
	public ResponseEntity<Resource> DescargarDetalleComprobanteContable(@RequestBody Map<String, Object> requestBody,HttpServletRequest request, Model model) 
			                                                       throws JRException, IOException, SQLException {

		Class tipoObjeto = this.getClass();
		String nombreClase = tipoObjeto.getName();
		System.out.println("CONTROLLER " + nombreClase);

		// Validar si el local está logueado
		Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
		String sistema = (String) request.getSession().getAttribute("sistema");

		// Obtenemos los datos del JSON recibido
		String idCpteStr = (String) requestBody.get("idCpte");
		Integer idCpte = Integer.parseInt(idCpteStr);
		System.out.println("idCpte es  : " + idCpte);

		String formato = (String) requestBody.get("formato");

		int idLocal = usuario.getIdLocal();

		int xIdReporte = 1100;
		
		// Obtenemos el periodo activo
		List <TblDctosPeriodo> PeriodoActivo = tblDctosPeriodoService.ObtenerPeriodoActivo(idLocal);
		
//		Integer idPeriodo = 0;
//		
//		for(TblDctosPeriodo P : PeriodoActivo) {						
//			idPeriodo = P.getIdPeriodo();					
//		}
		
		

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
			
			params.put("p_nombreLocal", L.getNombreLocal());
			params.put("p_nit", L.getNit());
			params.put("p_titulo", xTituloReporte  + " No. " + idCpte);
			params.put("p_direccion", L.getDireccion());
			params.put("p_idLocal", idLocal);
			params.put("p_fechaActual", strFechaActual);
			
			//xPathReport = L.getPathReport() + "contabook" + xCharSeparator;
			xPathReport = L.getPathReportContaBook() + "contabook" + xCharSeparator;

		}

		List<TblDctoDetalleDTO> lista = null;
		
		lista = tblDctoDetalleService.comprobanteContableDetalle(idLocal, idCpte);
		System.out.println("lista es : " + lista);
		
		Integer idPeriodo = 0;
		for(TblDctoDetalleDTO L : lista) {
			
			idPeriodo = L.getIdPeriodo();
			
		}
		
		params.put("p_idPeriodo", idPeriodo);
		

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
	
	
	
	@PostMapping("/EditarComprobanteContable-Post")
	public ModelAndView EditarComprobanteContablePost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");

	    // Obtenemos los datos del JSON recibido
	    String idCpte = (String) requestBody.get("idCpte");
	    
	    // Redirige a la vista y le pasamos el parametro de idTercero
	    ModelAndView modelAndView = new ModelAndView("redirect:./EditarComprobanteContable?idCpte=" + idCpte);
	    return modelAndView;
	}
	
	
	
	
	@GetMapping("/EditarComprobanteContable")
	public String EditarComprobanteContable(@RequestParam(name = "idCpte", required = false) String idCpte, HttpServletRequest request, Model model) {
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		System.out.println("Entró a /EditarComprobanteContable con idCpte: " + idCpte);
		
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

		    
		    Integer idCpteInt = Integer.parseInt(idCpte);
		    
		     List<TblDctoDetalleDTO> comprobanteDetalle = tblDctoDetalleService.comprobanteContableDetalle(idLocal, idCpteInt);
	         model.addAttribute("xComprobanteDetalle", comprobanteDetalle);
	         
	         for(TblDctoDetalleDTO comprobante : comprobanteDetalle) {
	        	 
	        	 model.addAttribute("xIdTipoCpte", comprobante.getIdTipoCpte());
	        	 System.out.println("xIdTipoCpte es: " + comprobante.getIdTipoCpte());
	        	 
	         }
	         
	         // Obtener la fecha actual
	         LocalDate fechaActual = LocalDate.now();
	         
	         
	         DateTimeFormatter formatterAct = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	         String FechActual = fechaActual.format(formatterAct);
	         model.addAttribute("xFechaActual", FechActual);
	         
	         
	        // lista tipos de comprobante
	 		List<TblTipoCpte> listaComprobantes = tblTipoCpteService.ListaComprobantes();
	 	    System.out.println("La lista de ListaComprobantes es: " + listaComprobantes);
	 	    
	 	     model.addAttribute("xListaComprobantes", listaComprobantes);
	 	     
	 	     //Obtener idTipoOrden
	 	    List<TblDctoDTO> comprobante = tblDctoService.ObtenerIdCpte(idLocal, idCpteInt);
	 	    
	 	    for(TblDctoDTO cpte : comprobante) {
	 	    	
	 	    	model.addAttribute("xIdTipoOrden", cpte.getIdTipoOrden());
	 	    	model.addAttribute("xIdDcto", cpte.getIdDcto());
	 	    	model.addAttribute("xIPeriodo", cpte.getIdPeriodo());
	 	    	
	 	    }
	 	     
	 	     
	 	     
	 	    model.addAttribute("xNumeroComprobante", idCpteInt);
			
			return "Reportes/Contables/EditarComprobanteContable";
			
	

	}
	
	
	
	
	
	
	@PostMapping("/CopiarComprobanteContable-Post")
	public ModelAndView CopiarComprobanteContablePost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");

	    // Obtenemos los datos del JSON recibido
	    String idCpte = (String) requestBody.get("idCpte");
	    
	    // Redirige a la vista y le pasamos el parametro de idTercero
	    ModelAndView modelAndView = new ModelAndView("redirect:./CopiarComprobanteContable?idCpte=" + idCpte);
	    return modelAndView;
	}
	
	
	
	
	@GetMapping("/CopiarComprobanteContable")
	public String CopiarComprobanteContable(@RequestParam(name = "idCpte", required = false) String idCpte, HttpServletRequest request, Model model) {
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		System.out.println("Entró a /CopiarComprobanteContable con idCpte: " + idCpte);
		
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

		    
		    Integer idCpteInt = Integer.parseInt(idCpte);
		    
		     List<TblDctoDetalleDTO> comprobanteDetalle = tblDctoDetalleService.comprobanteContableDetalle(idLocal, idCpteInt);
	         model.addAttribute("xComprobanteDetalle", comprobanteDetalle);
	         
	         for(TblDctoDetalleDTO comprobante : comprobanteDetalle) {
	        	 
	        	 model.addAttribute("xIdTipoCpte", comprobante.getIdTipoCpte());
	        	 System.out.println("xIdTipoCpte es: " + comprobante.getIdTipoCpte());
	        	 
	         }
	         
	         // Obtener la fecha actual
	         LocalDate fechaActual = LocalDate.now();
	         
	         
	         DateTimeFormatter formatterAct = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	         String FechActual = fechaActual.format(formatterAct);
	         model.addAttribute("xFechaActual", FechActual);
	         
	         
	        // lista tipos de comprobante
	 		List<TblTipoCpte> listaComprobantes = tblTipoCpteService.ListaComprobantes();
	 	    System.out.println("La lista de ListaComprobantes es: " + listaComprobantes);
	 	    
	 	     model.addAttribute("xListaComprobantes", listaComprobantes);
	 	     
	 	     //Obtener idTipoOrden
	 	    List<TblDctoDTO> comprobante = tblDctoService.ObtenerIdCpte(idLocal, idCpteInt);
	 	    
	 	    for(TblDctoDTO cpte : comprobante) {
	 	    	
	 	    	model.addAttribute("xIdTipoOrden", cpte.getIdTipoOrden());
	 	    	model.addAttribute("xIdDcto", cpte.getIdDcto());
	 	    	model.addAttribute("xIPeriodo", cpte.getIdPeriodo());
	 	    	
	 	    }
	 	     
	 	     
	 	     
	 	    model.addAttribute("xNumeroComprobante", idCpteInt);
			
			return "Reportes/Contables/CopiarComprobanteContable";
			
	

	}
	
	
	
	

}
