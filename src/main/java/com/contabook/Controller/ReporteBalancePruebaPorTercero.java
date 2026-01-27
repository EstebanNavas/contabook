package com.contabook.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.contabook.Model.DBMailMarketing.TblDctosPeriodo;
import com.contabook.Model.DBMailMarketing.TblLocalesReporte;
import com.contabook.Model.DBMailMarketing.TblTipoCpte;
import com.contabook.Model.Reportes.ReportesDTO;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.dbaquamovil.TblAgendaLogVisitas;
import com.contabook.Model.dbaquamovil.TblLocales;
import com.contabook.Projection.TblDctoDTO;
import com.contabook.Projection.TblDctoDetalleDTO;
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
public class ReporteBalancePruebaPorTercero {
	
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
	
	
	@GetMapping("/ReporteBalancePruebaPorTercero")
	public String ReporteBalancePruebaPorTercero(HttpServletRequest request,Model model) {
		
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
	           
	        // Obtenemos el periodo activo
	   			List <TblDctosPeriodo> PeriodoActivo = tblDctosPeriodoService.ObtenerPeriodoActivo(idLocal);
	   			
	   			Integer idPeriodo = 0;
	   			
	   			for(TblDctosPeriodo P : PeriodoActivo) {						
	   				idPeriodo = P.getIdPeriodo();					
	   			}
	    
	           List<TblTipoCpte> listaComprobantes = tblTipoCpteService.ListaComprobantes();
	   	       System.out.println("La lista de ListaComprobantes es: " + listaComprobantes);

	   	        List <TblDctosPeriodo> ListaPeriodos = tblDctosPeriodoService.ListaTotalPeriodos(idLocal);
	           
	           model.addAttribute("xListaComprobantes", listaComprobantes);
	           model.addAttribute("xListaPeriodos", ListaPeriodos);
	           model.addAttribute("xIdPeriodo", idPeriodo);

			return "Reportes/Balances/ReporteBalancePruebaPorTercero";


	}
	
	

	
	
	
	
	@PostMapping("/DescargarBalancePruebaPorTercero")
	public ResponseEntity<Resource> DescargarBalancePruebaPorTercero(@RequestBody Map<String, Object> requestBody,HttpServletRequest request, Model model) 
			                                                       throws JRException, IOException, SQLException {

		Class tipoObjeto = this.getClass();
		String nombreClase = tipoObjeto.getName();
		System.out.println("CONTROLLER " + nombreClase);

		// Validar si el local está logueado
		Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
		String sistema = (String) request.getSession().getAttribute("sistema");
		
		
		List<Integer> listaCuentas = new ArrayList<>();

		// Obtenemos los datos del JSON recibido
		Integer Cuenta1 = Integer.parseInt((String) requestBody.get("Cuenta1"));
	    Integer Cuenta2 = Integer.parseInt((String) requestBody.get("Cuenta2"));
		Integer idPeriodo = Integer.parseInt((String) requestBody.get("idPeriodo"));
		String tercero = (String) requestBody.get("Tercero");
		    
		    System.out.println("Cuenta1 es " + Cuenta1);
		    System.out.println("Cuenta2 es " + Cuenta2);
		    System.out.println("idPeriodo es " + idPeriodo);
		    
		    
		    if (Cuenta1 != 0) {
		        listaCuentas.add(Cuenta1);
		    }
		    if (Cuenta2 != 0) {
		        listaCuentas.add(Cuenta2);
		    }
		
		
		
		

		String formato = (String) requestBody.get("formato");

		int idLocal = usuario.getIdLocal();

		int xIdReporte = 1410;
		
		

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
			//xPathReport = L.getPathReport() + "contabook" + xCharSeparator;
			xPathReport = L.getPathReportContaBook() + "contabook" + xCharSeparator;

		}
		
		
        List<TblDctoDTO> lista = null;
	    
	    //Búsqueda por cuentas y Periodo
	    if(idPeriodo != null && !listaCuentas.isEmpty()) {
	    	System.out.println("Ingresó a cuentas y Periodo"); 
	    	lista = tblDctoService.listaBalancePruebaPorTercero(idLocal, idPeriodo, Cuenta1, Cuenta2, tercero);
	    	
	    }
	    
	    


		
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
