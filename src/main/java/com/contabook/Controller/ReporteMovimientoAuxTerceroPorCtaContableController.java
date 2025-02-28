package com.contabook.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

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
public class ReporteMovimientoAuxTerceroPorCtaContableController {
	
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
	
	
	@GetMapping("/ReporteMovimientoAuxTerceroPorCtaContable")
	public String reporteMovimientoAuxTerceroPorCtaContable(HttpServletRequest request,Model model) {
		
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

			return "Reportes/Contables/ReporteMovimientoAuxTerceroPorCtaContable";


	}
	
	
	
	@PostMapping("/BuscarMovimientoAux")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> BuscarComprobantes(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    Integer idLocal = usuario.getIdLocal();
	    
	    List<Integer> listaCuentas = new ArrayList<>();

	    System.out.println("SI ENTRÓ A  /BuscarMovimientoAux");

	    // Obtenemos los datos del JSON recibido
	    Integer Cuenta1 = Integer.parseInt((String) requestBody.get("Cuenta1"));
	    Integer Cuenta2 = Integer.parseInt((String) requestBody.get("Cuenta2"));
	    String Tercero = (String) requestBody.get("Tercero");
	    Integer idPeriodo = Integer.parseInt((String) requestBody.get("idPeriodo"));
	    
	    System.out.println("Cuenta1 es " + Cuenta1);
	    System.out.println("Cuenta2 es " + Cuenta2);
	    System.out.println("Tercero es " + Tercero);
	    System.out.println("idPeriodo es " + idPeriodo);
	    
	    
	    if (Cuenta1 != 0) {
	        listaCuentas.add(Cuenta1);
	    }
	    if (Cuenta2 != 0) {
	        listaCuentas.add(Cuenta2);
	    }

	    System.out.println("listaCuentas es " + listaCuentas);
	    
	    List<TblDctoDTO> listaComprobantes = null;
	    
	    //Búsqueda por cuentas y Tercero
	    if(Tercero != null && !listaCuentas.isEmpty()) {
	    	System.out.println("Ingresó a cuentas y Tercero"); 
	    	listaComprobantes = tblDctoService.listaMovimientoPorTerceroYAuxiiar(idLocal, idPeriodo, Tercero, listaCuentas);
	    	
	    }
	    
	    // Búsqueda solo por cuentas
	    if ((Tercero == null || Tercero.trim().isEmpty()) && !listaCuentas.isEmpty()) {
	        System.out.println("Ingresó a solo cuentas");    
	        listaComprobantes = tblDctoService.listaMovimientoPorAuxiliar(idLocal, idPeriodo, listaCuentas);
	    }
	    
	    
	    //Búsqueda solo por tercero
	    if(Tercero != null && listaCuentas.isEmpty()) {
	    	System.out.println("Ingresó a solo Tercero"); 
	    	listaComprobantes = tblDctoService.listaMovimientoPorTercero(idLocal, idPeriodo, Tercero);
	    	
	    }
	    
	    

	    System.out.println("listaComprobantes es " + listaComprobantes);
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "LOGGGGGGGGG");
	     response.put("listaComprobantes", listaComprobantes);
	    return ResponseEntity.ok(response);
	}
	
	
	
	@PostMapping("/TraerMovimientoContable-Post")
	public ModelAndView TraerMovimientoContablePost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");

	    // Obtenemos los datos del JSON recibido
	    Integer idCpte = (Integer) requestBody.get("idCpte");

	    System.out.println("Entró a /TraerComprobante-Post con idCpte: " + idCpte);


	    // Redirige a la vista y le pasamos el parametro de idTercero
	    ModelAndView modelAndView = new ModelAndView("redirect:/TraerMovimientoContable?idCpte=" + idCpte);
	    return modelAndView;
	}
	
	
	@GetMapping("/TraerMovimientoContable")
	public String traerMovimientoContable(@RequestParam(name = "idCpte", required = false) Integer idCpte, HttpServletRequest request, Model model) {
		
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
	           model.addAttribute("xNumeroComprobante", idCpte);
		   

			
			return "Reportes/Contables/ReporteMovimientoContableDetalle";

	}

}
