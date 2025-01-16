package com.contabook.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Projection.TblOpcionesDTO;
import com.contabook.Service.DBMailMarketing.TblOpcionesService;

@Controller
public class SeguridadController {
	
	@Autowired
	TblOpcionesService tblOpcionesService;

	@PostMapping("/ListaUnNivel")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> OtenerListaUnNivel(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		
		Map<String, Object> response = new HashMap<>();
		
		Integer xIdUsuario = usuario.getIdUsuario(); //Idusuario logueado
		Integer xidLocal = usuario.getIdLocal(); // IdLocal Logueado
		Integer xidNivel = usuario.getIdNivel(); // El idNivel del usuario Logueado
		
		System.out.println("IdUsuario es  " + xIdUsuario);
		System.out.println("idLocal es  " + xidLocal);
		System.out.println("idNivel es  " + xidNivel);
		
		// Obtenemos los datos del JSON recibido
		String idOpcion = (String) requestBody.get("idOpcion");
		Integer idOpcionInt = Integer.parseInt(idOpcion);
      	System.out.println("Valor de idOpcion: " + idOpcion);
      	
      	
      	//Obtenemos la lista
      	List<TblOpcionesDTO> xListaUnnivel = tblOpcionesService.ObtenerListaUnNivel(idOpcionInt, xidLocal, xidNivel);
      	System.out.println("ListaUnnivel  es  " + xListaUnnivel);
      	
      	response.put("xListaUnnivel", xListaUnnivel);
      	
      	model.addAttribute("xListaUnnivel", xListaUnnivel);
		
      	return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/ListaSubOpcionMenu")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ObtenerListaSubOpcionMenu(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		
		Map<String, Object> response = new HashMap<>();
		
		Integer xIdUsuario = usuario.getIdUsuario(); //Idusuario logueado
		Integer xidLocal = usuario.getIdLocal(); // IdLocal Logueado
		Integer xidNivel = usuario.getIdNivel(); // El idNivel del usuario Logueado
		
		System.out.println("IdUsuario es  " + xIdUsuario);
		System.out.println("idLocal es  " + xidLocal);
		System.out.println("idNivel es  " + xidNivel);
		
		// Obtenemos los datos del JSON recibido
		String idOpcion2 = (String) requestBody.get("idOpcion2");
		Integer idOpcionInt = Integer.parseInt(idOpcion2);
      	System.out.println("Valor de idOpcion: " + idOpcion2);
      	
      	
      	//Obtenemos la lista
      	List<TblOpcionesDTO> xListaSubOpcionMenu = tblOpcionesService.ObtenerListaSubOpcionMenu(idOpcionInt, xidLocal, xidNivel);
      	System.out.println("xListaSubOpcionMenu  es  " + xListaSubOpcionMenu);
      	
      	response.put("xListaSubOpcionMenu", xListaSubOpcionMenu);
      	
      	model.addAttribute("xListaSubOpcionMenu", xListaSubOpcionMenu);
		
      	return ResponseEntity.ok(response);
		
	}
}
