package com.contabook.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Model.DBMailMarketing.TblTipoCpte;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.dbaquamovil.TblAgendaLogVisitas;
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
	           
	           model.addAttribute("xListaComprobantes", listaComprobantes);
	           model.addAttribute("xListaPeriodos", ListaPeriodos);

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


	        List<TblDctoDTO> listaComprobantes = tblDctoService.listaComprobantes(idLocal, tipoComprobante, idPeriodo);
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
	           model.addAttribute("xComprobanteDetalle", comprobanteDetalle);
		   

			
			return "Reportes/Contables/ComprobanteContableDetalle";

	}

}
