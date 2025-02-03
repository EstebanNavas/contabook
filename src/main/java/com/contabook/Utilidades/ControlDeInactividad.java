package com.contabook.Utilidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.contabook.Service.dbaquamovil.TblAgendaLogVisitasService;
import com.contabook.Repository.dbaquamovil.TblAgendaLogVisitasRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class ControlDeInactividad {
	
	@Autowired
	TblAgendaLogVisitasService tblAgendaLogVisitasService;
	
	@Autowired
	TblAgendaLogVisitasRepo tblAgendaLogVisitasRepo;
	
	@Autowired
    private HttpServletRequest request;
	
	public Integer ingresa(int idLocal, int idLog, String sessionId) {
	    
		 // Obtener la última fecha y hora de la sesión con idEstadoTx = 9
	    String fechaYHoraSessionId = tblAgendaLogVisitasService.ObtenerFechaYHoraSessionId(idLocal, idLog, sessionId);
	    System.out.println("fechaYHoraSessionId es: " + fechaYHoraSessionId);
	    
	    
	    if(fechaYHoraSessionId == null) {
	    	
	    System.out.println("fechaYHoraSessionId es NULL" );   	
	    	return 2;
	    	
	    }
	    
	    

	    // Construir un formateador que acepte tanto 2 como 3 dígitos en los milisegundos
	    DateTimeFormatter formateador = new DateTimeFormatterBuilder()
	        .appendPattern("yyyy-MM-dd HH:mm:ss")
	        .optionalStart()
	        .appendFraction(java.time.temporal.ChronoField.NANO_OF_SECOND, 0, 3, true)
	        .optionalEnd()
	        .toFormatter();

	    // Convierte fechaYHoraSessionId a LocalDateTime
	    LocalDateTime fechaYHoraSessionIdFormateada = LocalDateTime.parse(fechaYHoraSessionId, formateador);
	    System.out.println("fechaYHoraSessionId formateada es: " + fechaYHoraSessionIdFormateada.format(formateador));

	    // Se obtiene la fecha y hora actual sin milisegundos
	    LocalDateTime fechaHoraActual = LocalDateTime.now().withNano(0);
	    System.out.println("fechaHoraActual es: " + fechaHoraActual.format(formateador));

	    // Compara las fechas y horas
	    long diferenciaMinutos = ChronoUnit.MINUTES.between(fechaYHoraSessionIdFormateada, fechaHoraActual);
	    System.out.println("diferenciaMinutos es : " + diferenciaMinutos);
	    
	    
	    if (diferenciaMinutos >= 10) {
	        System.out.println("Han pasado más de 10 minutos desde la última sesión.");
	        
	        //Obtenemos el idLocal de la sessionId
       	Integer xIdLocal = tblAgendaLogVisitasService.ObtenerIdLocalPorSession(sessionId);
       	
       	// Actualizamos los idEstadoTx Que sean = 9 a 1
           tblAgendaLogVisitasRepo.actualizarIdEstadoTxA1(xIdLocal, sessionId);
       	System.out.println("Sessión cerrada para el usuario con ID de sessión : " + sessionId);
       	
       	// Invalidar la sesión
       	HttpSession session = request.getSession();
       	String idSessionDelRequest = session.getId();
       	System.out.println("idSessionDelRequest es: " + idSessionDelRequest);
       	
       	request.getSession().invalidate();
       	
           
       	return 2;
       	
	        
	    } else {
	        System.out.println("No han pasado más de 10 minutos desde la última sesión.");
	        

	        // Formatear la fecha como un String
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String strFechaVisita = fechaHoraActual.format(formatter);
	        System.out.println("strFechaVisita es: " + strFechaVisita);
	        
	        //Actualizamos la fecha y hora de la session
	        tblAgendaLogVisitasRepo.actualizaActividad(strFechaVisita, idLocal, idLog, sessionId);
	        
	        
	        
	    }

	    return 0;
	}

}
