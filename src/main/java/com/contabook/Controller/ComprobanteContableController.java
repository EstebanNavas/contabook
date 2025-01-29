package com.contabook.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Model.DBMailMarketing.TblPucAux;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Repository.DBMailMarketing.TblPucAuxRepo;
import com.contabook.Service.DBMailMarketing.TblPucAuxService;
import com.contabook.Service.DBMailMarketing.TblPucService;

@Controller
public class ComprobanteContableController {
	
	@Autowired
	TblPucService tblPucService;
	
	@Autowired
	TblPucAuxRepo tblPucAuxRepo;
	
	@Autowired
	TblPucAuxService tblPucAuxService;
	
	@GetMapping("/ComprobanteContable")
	public String ComprobanteContable(HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		 // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Formatear la fecha como un String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String strFechaVisita = fechaActual.format(formatter);
        
        System.out.println("strFechaVisita  es" + strFechaVisita);
        

        DateTimeFormatter formatterAct = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String FechActual = fechaActual.format(formatterAct);

        model.addAttribute("xFechaActual", FechActual);
		
		

			
			return "ComprobanteContable/ComprobanteContable";


	}
	
	
	@PostMapping("/ObtenerCuentas")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ObtenerCuentas(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();
		

		List<TblPucAux> todosAuxiliares = tblPucAuxService.listaTodosAuxiliares(idLocal);
      	System.out.println("todosAuxiliares  es  " + todosAuxiliares);
      	

      	response.put("xlistaCuentas", todosAuxiliares);     	
		
      	return ResponseEntity.ok(response);
		
	}

}
