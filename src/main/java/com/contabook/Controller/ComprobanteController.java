package com.contabook.Controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.contabook.Model.DBMailMarketing.TblTipoCpte;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Repository.DBMailMarketing.TblTipoCpteRepo;
import com.contabook.Service.DBMailMarketing.TblTipoCpteService;




@Controller
public class ComprobanteController {
	
	@Autowired
	TblTipoCpteService tblTipoCpteService;
	
	@Autowired
	TblTipoCpteRepo tblTipoCpteRepo;
	
	@GetMapping("/Comprobante")
	public String Comprobante(HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		
//		// ----------------------------------------------------------- VALIDA INACTIVIDAD ------------------------------------------------------------
//	    HttpSession session = request.getSession();
//	    //Integer idUsuario = (Integer) session.getAttribute("xidUsuario");
//	    
//	    @SuppressWarnings("unchecked")
//		List<TblAgendaLogVisitas> UsuarioLogueado = (List<TblAgendaLogVisitas>) session.getAttribute("UsuarioLogueado");
//	    
//	    Integer estadoUsuario = 0;
//	    
//
//	        for (TblAgendaLogVisitas usuarioLog : UsuarioLogueado) {
//	            Integer idLocalUsuario = usuarioLog.getIdLocal();
//	            Integer idLogUsuario = usuarioLog.getIDLOG();
//	            String sessionIdUsuario = usuarioLog.getSessionId();
//	            
//	            
//	           estadoUsuario = controlDeInactividad.ingresa(idLocalUsuario, idLogUsuario, sessionIdUsuario);          
//	        }
//    
//	           if(estadoUsuario.equals(2)) {
//	        	   System.out.println("USUARIO INACTIVO");
//	        	   return "redirect:/";
//	           }
//		
//		//------------------------------------------------------------------------------------------------------------------------------------------


	    

			
			return "Comprobante/Comprobante";


	}
	
	
	
	@GetMapping("/TaertodosComprobantes")
	public String TaertodosComprobantes(HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");		
		Integer idLocal = usuario.getIdLocal();
		
		
//		 // ----------------------------------------------------------- VALIDA INACTIVIDAD ------------------------------------------------------------
//	    HttpSession session = request.getSession();
//	    //Integer idUsuario = (Integer) session.getAttribute("xidUsuario");
//	    
//	    @SuppressWarnings("unchecked")
//		List<TblAgendaLogVisitas> UsuarioLogueado = (List<TblAgendaLogVisitas>) session.getAttribute("UsuarioLogueado");
//	    
//	    Integer estadoUsuario = 0;
//	    
//
//	        for (TblAgendaLogVisitas usuarioLog : UsuarioLogueado) {
//	            Integer idLocal = usuarioLog.getIdLocal();
//	            Integer idLog = usuarioLog.getIDLOG();
//	            String sessionId = usuarioLog.getSessionId();
//	            
//	            
//	            System.out.println("idLocal: " + idLocal);
//	            System.out.println("idLog: " + idLog);
//	            System.out.println("sessionId: " + sessionId);
//	            
//	            
//	           estadoUsuario = controlDeInactividad.ingresa(idLocal, idLog, sessionId);          
//	        }
//    
//	           if(estadoUsuario.equals(2)) {
//	        	   System.out.println("USUARIO INACTIVO");
//	        	   return "redirect:/";
//	           }
		


		    
		List<TblTipoCpte> listaComprobantes = tblTipoCpteService.ListaComprobantes(idLocal);

		    System.out.println("La lista de ListaComprobantes es: " + listaComprobantes);
	        
	        model.addAttribute("listaComprobantes", listaComprobantes);
		    
			
			return "Comprobante/TodosLosComprobantes";
			

		
	}
	
	
	
	@GetMapping("/CrearComprobante")
	public String CrearComprobante(HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		System.out.println("Entró a /CrearComprobante");
		
//		 // ----------------------------------------------------------- VALIDA INACTIVIDAD ------------------------------------------------------------
//	    HttpSession session = request.getSession();
//	    //Integer idUsuario = (Integer) session.getAttribute("xidUsuario");
//	    
//	    @SuppressWarnings("unchecked")
//		List<TblAgendaLogVisitas> UsuarioLogueado = (List<TblAgendaLogVisitas>) session.getAttribute("UsuarioLogueado");
//	    
//	    Integer estadoUsuario = 0;
//	    
//
//	        for (TblAgendaLogVisitas usuarioLog : UsuarioLogueado) {
//	            Integer idLocal = usuarioLog.getIdLocal();
//	            Integer idLog = usuarioLog.getIDLOG();
//	            String sessionId = usuarioLog.getSessionId();
//	            
//	            
//	            System.out.println("idLocal: " + idLocal);
//	            System.out.println("idLog: " + idLog);
//	            System.out.println("sessionId: " + sessionId);
//	            
//	            
//	           estadoUsuario = controlDeInactividad.ingresa(idLocal, idLog, sessionId);          
//	        }
//    
//	           if(estadoUsuario.equals(2)) {
//	        	   System.out.println("USUARIO INACTIVO");
//	        	   return "redirect:/";
//	           }
//		
//		//------------------------------------------------------------------------------------------------------------------------------------------	
			

		    //Obtenemos el Maximo ID
		    Integer MaxIdTipoCpte = tblTipoCpteService.MaximoIdTipoCpte(idLocal) + 1;
		    model.addAttribute("MaxIdTipoCpte", MaxIdTipoCpte);
  
			
			return "Comprobante/CrearComprobante";

		
	}
	
	
	@PostMapping("/CrearComprobante-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> CrearComprobantePost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();
	    
	    Map<String, Object> response = new HashMap<>();


	    System.out.println("SI ENTRÓ A  /CrearComprobante");

	        // Obtenemos los datos del JSON recibido
	        String nombreComprobante = (String) requestBody.get("nombreComprobante");
	        
	        String idComprobante = (String) requestBody.get("idComprobante");  
	        Integer idCpteInt = Integer.parseInt(idComprobante);
	        
	        String estado = (String) requestBody.get("estado");  
	        Integer estadoInt = Integer.parseInt(estado);
	        
	        String signo = (String) requestBody.get("signo"); 
	        Integer signoInt = Integer.parseInt(signo);
	        
	        String idSeq = (String) requestBody.get("idSeq"); 
	        Integer idSeqInt = Integer.parseInt(idSeq);
	        
	        
	        String idAlcance = (String) requestBody.get("idAlcance");  
	        Integer idAlcanceInt = Integer.parseInt(idAlcance);
	        

	        // Ingresamos el nuevo Comprobante
	        tblTipoCpteRepo.ingresaComprobante(idLocal, idCpteInt, nombreComprobante, estadoInt, signoInt, idSeqInt, idAlcanceInt);


	        
		    
		    response.put("message", "OK");
		    response.put("nombreComprobante", nombreComprobante);
		    
		    return ResponseEntity.ok(response);
	   
	    
	}
	
	
	@PostMapping("/TraerComprobante-Post")
	public ModelAndView TraerComprobantePost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    System.out.println("Entró a /TraerComprobante");

	    // Obtenemos los datos del JSON recibido
	    String idTipoCpte = (String) requestBody.get("idTipoCpte");
	    //Integer idTercero = Integer.parseInt(idTerceroString);

	    System.out.println("idTipoCpte es " + idTipoCpte);

	    // Redirige a la vista y le pasamos el parametro de idTercero
	    ModelAndView modelAndView = new ModelAndView("redirect:/TraerComprobante?idTipoCpte=" + idTipoCpte);
	    return modelAndView;
	}
	
	
	
	@GetMapping("/TraerComprobante")
	public String TraerComprobante(@RequestParam(name = "idTipoCpte", required = false) String idTipoCpte, HttpServletRequest request, Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Integer idTipoCpteInt = Integer.parseInt(idTipoCpte);
		
//		 // ----------------------------------------------------------- VALIDA INACTIVIDAD ------------------------------------------------------------
//	    HttpSession session = request.getSession();
//	    //Integer idUsuario = (Integer) session.getAttribute("xidUsuario");
//	    
//	    @SuppressWarnings("unchecked")
//		List<TblAgendaLogVisitas> UsuarioLogueado = (List<TblAgendaLogVisitas>) session.getAttribute("UsuarioLogueado");
//	    
//	    Integer estadoUsuario = 0;
//	    
//
//	        for (TblAgendaLogVisitas usuarioLog : UsuarioLogueado) {
//	            Integer idLocal = usuarioLog.getIdLocal();
//	            Integer idLog = usuarioLog.getIDLOG();
//	            String sessionId = usuarioLog.getSessionId();
//	            
//	            
//	            System.out.println("idLocal: " + idLocal);
//	            System.out.println("idLog: " + idLog);
//	            System.out.println("sessionId: " + sessionId);
//	            
//	            
//	           estadoUsuario = controlDeInactividad.ingresa(idLocal, idLog, sessionId);          
//	        }
//    
//	           if(estadoUsuario.equals(2)) {
//	        	   System.out.println("USUARIO INACTIVO");
//	        	   return "redirect:/";
//	           }
//		
//		//------------------------------------------------------------------------------------------------------------------------------------------
	
		
		    List<TblTipoCpte>  comprobante = tblTipoCpteService.obtenerCpteXId(idLocal, idTipoCpteInt);
		    
		    for(TblTipoCpte cmpte : comprobante) {
		    	
		    	model.addAttribute("nombre", cmpte.getNombreCmpte());
		    	model.addAttribute("IdTipoCpte", cmpte.getIdTipoCpte() );
		    	model.addAttribute("estado", cmpte.getEstado());
		    	model.addAttribute("signo", cmpte.getSigno());
		    	model.addAttribute("idSeq", cmpte.getIdSeq());
		    	model.addAttribute("idAlcance", cmpte.getIdAlcance());
		    	
		    	
		    }

		    

			
			return "Comprobante/ActualizarComprobante";


	}
	
	
	@PostMapping("/ActualizarComprobante-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ActualizarComprobante(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    Integer idLocal = usuario.getIdLocal();
	    
	    Integer idTipoTercero = 1;

	    System.out.println("SI ENTRÓ A  /ActualizarComprobante-Post");

	    // Obtenemos los datos del JSON recibido
        String nombreComprobante = (String) requestBody.get("nombreComprobante");
        
        String idComprobante = (String) requestBody.get("idComprobante");  
        Integer idCpteInt = Integer.parseInt(idComprobante);
        
        String estado = (String) requestBody.get("estado");  
        Integer estadoInt = Integer.parseInt(estado);
        
        String signo = (String) requestBody.get("signo"); 
        Integer signoInt = Integer.parseInt(signo);
        
        String idSeq = (String) requestBody.get("idSeq"); 
        Integer idSeqInt = Integer.parseInt(idSeq);
        
        
        String idAlcance = (String) requestBody.get("idAlcance");  
        Integer idAlcanceInt = Integer.parseInt(idAlcance);
        
	     
		    

	        
	        // Actualizamos Comprobante
            tblTipoCpteRepo.actualizarComprobante(nombreComprobante, estadoInt, signoInt, idSeqInt, idAlcanceInt, idLocal, idCpteInt);
	       
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "LOGGGGGGGGG");
		    response.put("nombreComprobante", nombreComprobante);
		    return ResponseEntity.ok(response);
	   
	    
	}
	

}
