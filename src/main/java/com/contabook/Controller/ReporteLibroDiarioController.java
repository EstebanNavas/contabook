package com.contabook.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contabook.Model.DBMailMarketing.TblDctosPeriodo;
import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Model.DBMailMarketing.TblTipoCpte;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.dbaquamovil.TblAgendaLogVisitas;
import com.contabook.Projection.TblDctoDTO;
import com.contabook.Service.DBMailMarketing.TblDctoService;
import com.contabook.Service.DBMailMarketing.TblDctosPeriodoService;
import com.contabook.Service.DBMailMarketing.TblLocalesReporteService;
import com.contabook.Service.DBMailMarketing.TblPucAuxService;
import com.contabook.Service.DBMailMarketing.TblPucService;
import com.contabook.Service.DBMailMarketing.TblTipoCpteService;
import com.contabook.Service.dbaquamovil.TblLocalesService;
import com.contabook.ServiceApi.ReporteSmsServiceApi;
import com.contabook.Utilidades.ControlDeInactividad;

@Controller
public class ReporteLibroDiarioController {
	
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
	TblDctoService tblDctoService;
	
	@Autowired
	TblTipoCpteService tblTipoCpteService;
	
	@Autowired
	ReporteSmsServiceApi reporteSmsServiceApi;
	
	@Autowired
	ControlDeInactividad controlDeInactividad;
	
	@GetMapping("/ReporteLibroDiario")
	public String reporteLibroDiario(HttpServletRequest request,Model model) {
		
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
	   			
	   			
	   		     // Obtener la fecha actual
		        LocalDate fechaActual = LocalDate.now();
	   			
	   		    DateTimeFormatter formatterAct = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        String FechActual = fechaActual.format(formatterAct);

		        model.addAttribute("xFechaActual", FechActual);
	           
	           model.addAttribute("xListaComprobantes", listaComprobantes);
	           model.addAttribute("xListaPeriodos", ListaPeriodos);
	           model.addAttribute("xIdPeriodo", idPeriodo);
	    

			
			return "Reportes/Contables/ReporteLibroDiario";


	}
	
	
	@PostMapping("/BuscarComprobantesPorFecha")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> BuscarComprobantesPorFecha(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();

	    System.out.println("SI ENTRÓ A  /BuscarComprobantesPorFecha");

	        // Obtenemos los datos del JSON recibido
	        @SuppressWarnings("unchecked")
		    List<String> xcomprobantesArr = (List<String>) requestBody.get("comprobantesArry");
	        
	       // Convertir List<String> a List<Integer>
	        List<Integer> xComprobantesIntList = xcomprobantesArr.stream()
	                .map(Integer::parseInt) // Convierte cada String en Integer
	                .collect(Collectors.toList());
	        
	        Integer[] xComprobantesArray = xComprobantesIntList.toArray(new Integer[0]);
	        
	        System.out.println("xComprobantesIntList es " + xComprobantesIntList);
	        System.out.println("xComprobantesArray es " + xComprobantesArray);
	        	        
	        String FechaInicial = (String) requestBody.get("FechaInicial");	 
	        String FechaFinal = (String) requestBody.get("FechaFinal");	
	        


	        List<TblDctoDTO> listaComprobantes = tblDctoService.listaComprobantesLibroDiario(idLocal, xComprobantesIntList, FechaInicial, FechaFinal);
	        System.out.println("listaComprobantesssss es " + listaComprobantes);
	        
		    
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "LOGGGGGGGGG");
		    response.put("listaComprobantes", listaComprobantes);
		    return ResponseEntity.ok(response);
	   
	    
	}

}
