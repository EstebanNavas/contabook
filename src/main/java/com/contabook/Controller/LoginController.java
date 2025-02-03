package com.contabook.Controller;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contabook.Service.DBMailMarketing.TblAgendaLogVisitasService;
import com.contabook.Service.DBMailMarketing.TblDctosPeriodoService;
import com.contabook.Service.DBMailMarketing.TblOpcionesService;
import com.contabook.Service.dbaquamovil.CtrlusuariosService;
import com.contabook.Service.dbaquamovil.TblLocalesService;
import com.contabook.Utilidades.UtilidadesIP;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.DBMailMarketing.TblAgendaLogVisitas;
import com.contabook.Projection.TblOpcionesDTO;
import com.contabook.Repository.DBMailMarketing.TblAgendaLogVisitasRepo;
import com.contabook.Model.DBMailMarketing.TblDctosPeriodo;

@Controller
public class LoginController {
	
	@Autowired
	CtrlusuariosService ctrlusuariosService;
	
	@Autowired
	TblAgendaLogVisitasService tblAgendaLogVisitasService;
	
	@Autowired
	TblOpcionesService tblOpcionesService;
	
	@Autowired
	TblLocalesService tblLocalesService;
	
	@Autowired
	TblAgendaLogVisitasRepo tblAgendaLogVisitasRepo;
	
	@Autowired
	TblDctosPeriodoService tblDctosPeriodoService;
	
	Integer idLocalAutenticado = 0;
	Integer xidUsuario = 0;

	@PostMapping("/login-post")
	//Se obtienen los valores ingresados en el form del index
   public String login(HttpServletRequest request,  @RequestParam(value = "usuario", required = false) String idUsuario, @RequestParam(value = "password", required = false) String password, @RequestParam(value = "sistema", required = false) String sistema,
                    Model model) throws UnknownHostException { 
		
		
		System.out.println("usuario es " + idUsuario);
		System.out.println("password es " + password);
		System.out.println("sistema es " + sistema);
		
		
		 xidUsuario = (int) Long.parseLong(idUsuario);
	        
	        
	        System.out.println("Entró a /login-post");
	        
	        //------------------------------------- VALIDACION DE SISTEMA SELECCIONADO ---------------------------------------------------- 
	        
	        
	        if("Webpos".equals(sistema)) {

	        }
	        
             if("Webcomercial".equals(sistema)) {

	        }
    
    
            if("Pot".equals(sistema)) {
            	
            }
	        
	        if("Marketing".equals(sistema)) {
	        	System.out.println(" El sistema si es : " + sistema);
	        	
	        	// Se obtiene el usuario autenticado
		        boolean isAuthenticated = ctrlusuariosService.authenticate(xidUsuario, password);
		        System.out.println(" isAuthenticated es : " + isAuthenticated);
		        
		        if (isAuthenticated) {
		        	
		        	System.out.println("isAuthenticated es : " + isAuthenticated);
		        
		        	Ctrlusuarios usuarioAutenticado = ctrlusuariosService.obtenerUsuario(xidUsuario);
		            Integer xidNivel = usuarioAutenticado.getIdNivel(); // El idNivel del usuario Logueado
		            
		            // Se obtiene el Idlocal de ctrlusuarios pasanddole como argumento el idUsuario
		            idLocalAutenticado = ctrlusuariosService.consultarIdLocalPorIdUsuario(xidUsuario);
		            
		            // Obtenemos la Lista de Opciones Tipo 1
		            List<Integer> ObtenerListaIdTipoOpcion1 = tblOpcionesService.ObtenerListaIdTipoOpcion1(idLocalAutenticado);
		            System.out.println(" ObtenerListaIdTipoOpcion1 es : " + ObtenerListaIdTipoOpcion1);
		            
		            //Optenemos del idPerfil Logueado las opciones que coincidan con la lista ObtenerListaIdTipoOpcion1
		            List<Integer> ListaIdTipoOpcion1OpcionesPerfil  = tblOpcionesService.ListaIdTipoOpcion1OpcionesPerfil(idLocalAutenticado, ObtenerListaIdTipoOpcion1, xidNivel);
		            System.out.println("ListaIdTipoOpcion1OpcionesPerfil : " + ListaIdTipoOpcion1OpcionesPerfil);  
		            
		            
		            // Se obtiene la lista de las opciones Tipo 1 dependiendo de la lista ObtenerListaIdTipoOpcion1
		            List<TblOpcionesDTO>  ListaOpcionesTipo1 = tblOpcionesService.ObtenerTipoOpciones1(idLocalAutenticado, ListaIdTipoOpcion1OpcionesPerfil);
		            System.out.println("La ListaOpcionesTipo1 es : " + ListaOpcionesTipo1);
		        	
		        	// Obtenemos el idEstadoTx del usuario que se intenga loguear
		        	Integer idEstadoTx = tblAgendaLogVisitasService.ObtenerEstadoLogIdEstadoTx(idLocalAutenticado, xidUsuario);
		        	System.out.println("El idEstadoTx del usuario " + idUsuario + " es: " + idEstadoTx);
		        	
		        	
		        	if(idEstadoTx == null) {
		        		
		        		idEstadoTx = 1;
		        	}
		        	
		        	// Validamos si el idEstadoTx es = 9, si es así es proque ya el usuario está logueado, se envia mensaje de error 
		        	if(idEstadoTx == 9 ) {
		        		
		        		System.out.println("Usuario ya se encuentra logueado" );
		        		model.addAttribute("error", "Este usuario ya se encuentra logueado");
		            	model.addAttribute("url", "/");
		        		return "defaultErrorSistema";
		        	} 
		        	
		        	
		        	HttpSession session = request.getSession();
		        	session.setAttribute("xidUsuario", xidUsuario);
			
		        	// Obtenemos el ID de session 
		        	String sessionId = session.getId();	
		        	System.out.println("sessionId es : " + sessionId);
		        	

		        	
		        	// Obtenemos la fecha y hora actual
				    Date fechaActual = new Date(); 
				    
				    // Formatea la fecha en el formato deseado
				    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				    String fechaFormateada = dateFormat.format(fechaActual);
				    System.out.println("fechaFormateada en el login es: "+ fechaFormateada);
				    
				    // Obtenemos la lista de las sessiones de la fecha actual y que esten activas con el idEstadoTX = 9
				    List<String> xListaSessionId = tblAgendaLogVisitasService.ObtenerListaSessionId(fechaFormateada);
		        	System.out.println("xListaSessionId "+ xListaSessionId);
		        	
		        	// Verificamos si sessionId ya existe en la lista
		        	if (xListaSessionId.contains(sessionId)) {
		        		
		        		System.out.println("sessionId SI existe en la lista");
		        		
		        		// Obtenemos el idUsuario de la sessionId
		        		List<Integer> IdUsuarioSession = tblAgendaLogVisitasService.ObtenerIdUsuariosPorIdSession(sessionId);
		        		
		        		for(Integer xIdUsuarioSession : IdUsuarioSession) {
		        			
		        			System.out.println("xIdUsuarioSession ess " + xIdUsuarioSession);
		        			//Comparamos si el Usuario que intenta loguearse es el mismo que tiene la sessionId activa
		            		if(xIdUsuarioSession == xidUsuario) {
		            			
		            			model.addAttribute("error", "El Usuario " + xidUsuario + "ya se encuentra logueado en esta sessión, por favor ingresar desde otra página u otro navegador. ");
		                    	model.addAttribute("url", "/");
		                    	return "defaultErrorSistema";
		            		}
		        		}

		        		
		        		
		        	} else {
		        	    System.out.println("sessionId no existe en la lista");
		        	    
		        	}
		        	
		        	 // Obtenemos la IP del servidor
		        	 UtilidadesIP utilidadesIP = new UtilidadesIP();
		        	 String direccionIP = utilidadesIP.getIpServidor();
		        	 System.out.println("direccionIP es : " + direccionIP);
		        	 
		        	// Obtenemos el IDLOG Máximo y le sumamos uno
		 	        Integer maximoIDLOGSum1 = tblAgendaLogVisitasService.findMaxIDLOG() + 1;
		 	        System.out.println("maximoIDLOG en /login-post: " + maximoIDLOGSum1);
		 	        
		 	        // Actualizamos los ESTADO Que sean = 9 a 1
			        tblAgendaLogVisitasRepo.actualizarEstadoA1(idLocalAutenticado, xidUsuario);
		        	 
		        	// Ingresamos el nuevo Log con ESTADO = 9
		 	        tblAgendaLogVisitasService.ingresarLogSessionID(idLocalAutenticado, maximoIDLOGSum1, idUsuario, xidUsuario, direccionIP, sessionId);
		 	        
		 	        
		 	        //Obtenemos el registro del login guardado
		 	        List<TblAgendaLogVisitas> UsuarioLogueado = tblAgendaLogVisitasService.ObtenerRegistroDelLogin(idLocalAutenticado, maximoIDLOGSum1);
		 	       
		 	       
		 	        //------ SE GUARDA EN LA SESSION EL OBJETO registroLogin QUE CONTIENE LOS DATOS DEL USUARIO LOGUEADO
		            session.setAttribute("UsuarioLogueado", UsuarioLogueado);
		        	
		            // Se setean los valores a las variables 
		            request.getSession().setAttribute("local", tblLocalesService.consultarLocal(idLocalAutenticado));
		            request.getSession().setAttribute("usuarioAuth", usuarioAutenticado);
		            request.getSession().setAttribute("sistema", sistema);
		            request.getSession().setAttribute("ListaOpcionesTipo1", ListaOpcionesTipo1); // Guardamos la lista en una variable de Session para usarla posteriormente en Thymeleaf
		            
		            System.out.println(" request.getSession() es  " + request.getSession());
		            model.addAttribute("ListaOpcionesTipo1", ListaOpcionesTipo1);
		            
		            
		            // Obtenemos el periodo activo
					List <TblDctosPeriodo> PeriodoActivo = tblDctosPeriodoService.ObtenerPeriodoActivo(idLocalAutenticado);
					
					Integer idPeriodo = 0;
					
					for(TblDctosPeriodo P : PeriodoActivo) {						
						idPeriodo = P.getIdPeriodo();					
					}
		        	
					request.getSession().setAttribute("periodoActivo", idPeriodo);
		        	
	        	
		            return "menuPrincipal";
		        }
		        
		        else {
		            model.addAttribute("error", "Datos incorrectos o usuario inactivo, ");
		            model.addAttribute("url", "/");
		            return "defaultError";  // Mostrar página de error
		        }
	        }
	        
	        
	        
	        
     return "menuPrincipal";

	}
	
	
	@GetMapping("/menuPrincipal")
	public String MenuPrincipal(HttpServletRequest request,Model model) {
		
		return "menuPrincipal";
		
	}
	
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request,Model model) {
		
		// Actualizamos los ESTADO Que sean = 9 a 1
	    tblAgendaLogVisitasRepo.actualizarEstadoA1(idLocalAutenticado, xidUsuario);
	    
	    System.out.println("idUsuario es : " + xidUsuario);
	    
	    HttpSession session = request.getSession();
	    
	    // Obtenemos el ID de session del usuario que intenta cerrar sessión
    	String sessionId = session.getId();
    	System.out.println("sessionId en  /logout es: " + sessionId);
    	
    	
    	
    	
    	List<Integer> ListaIdLocales = tblAgendaLogVisitasService.ObtenerListaIdLocalPorSession(sessionId);
    	
    	for(Integer xIdLocal : ListaIdLocales ) {
    		
    		// Actualizamos los idEstadoTx Que sean = 9 a 1
    	    tblAgendaLogVisitasRepo.actualizarIdEstadoTxA1(xIdLocal, sessionId);
    		
    	}
    	
    	//Obtenemos el idLocal de la sessionId
    	//Integer xIdLocal = tblAgendaLogVisitasService.ObtenerIdLocalPorSession(sessionId);
    	
    	// Detenemos el contador asociado a la sesión (si existe)
       // detenerContador(sessionId);
	    
	    // Actualizamos los idEstadoTx Que sean = 9 a 1
	    //tblAgendaLogVisitasRepo.actualizarIdEstadoTxA1(xIdLocal, sessionId);
	    
		request.getSession().invalidate();
		
		return "redirect:/";
		
	}
	

	
	
}
