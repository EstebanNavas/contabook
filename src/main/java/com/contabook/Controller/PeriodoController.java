package com.contabook.Controller;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Service.DBMailMarketing.TblDctosPeriodoService;
import com.contabook.Model.DBMailMarketing.TblAgendaLogVisitas;
import com.contabook.Model.DBMailMarketing.TblDctosPeriodo;

public class PeriodoController {

	@Autowired
	TblDctosPeriodoService tblDctosPeriodoService;
	
	@GetMapping("/Periodo")
	public String Referencia(HttpServletRequest request,Model model) {
		
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

		    
		    
		    List <TblDctosPeriodo> listaPeriodos = tblDctosPeriodoService.ListaTotalPeriodos(usuario.getIdLocal());
		    model.addAttribute("listaPeriodos", listaPeriodos);
		    
		    if (!listaPeriodos.isEmpty()) {
		        // Obtenemos el primer elemento de la lista
		        TblDctosPeriodo primerPeriodo = listaPeriodos.get(0);
		        
		        int xIdPeriodo = primerPeriodo.getIdPeriodo(); //Obtenemos el idPeriodo
		        Timestamp xfechaInicioConsumo = primerPeriodo.getFechaInicial(); 
		        Timestamp xfechaFinConsumo = primerPeriodo.getFechaFinal(); 
		        Timestamp xfechaSinRecargo = primerPeriodo.getFechaSinRecargo(); 
		        Timestamp xfechaConRecargo = primerPeriodo.getFechaConRecargo(); 
	
		        model.addAttribute("xIdPeriodo", xIdPeriodo);
		        model.addAttribute("xfechaInicioConsumo", xfechaInicioConsumo);
		        model.addAttribute("xfechaFinConsumo", xfechaFinConsumo);
		        model.addAttribute("xfechaSinRecargo", xfechaSinRecargo);
		        model.addAttribute("xfechaConRecargo", xfechaConRecargo);
		    } else {
		        System.out.println("La lista de periodos está vacía.");
		    }
	    

			
			return "Periodo/Periodo";


	}
}
