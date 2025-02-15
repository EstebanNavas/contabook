package com.contabook.Controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.contabook.Model.DBMailMarketing.TblPucAux;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.dbaquamovil.TblAgendaLogVisitas;
import com.contabook.Repository.DBMailMarketing.TblPucAuxRepo;
import com.contabook.Repository.DBMailMarketing.TblPucRepo;
import com.contabook.Service.DBMailMarketing.TblPucAuxService;
import com.contabook.Service.DBMailMarketing.TblPucService;
import com.contabook.Utilidades.ControlDeInactividad;


@Controller
public class PucController {
	
	@Autowired
	TblPucService tblPucService;
	
	@Autowired
	TblPucAuxRepo tblPucAuxRepo;
	
	@Autowired
	TblPucAuxService tblPucAuxService;
	
	@Autowired
	ControlDeInactividad controlDeInactividad;
	
	@Autowired
	TblPucRepo tblPucRepo;
	
	
	@GetMapping("/Puc")
	public String Puc(HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
        
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
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
	
		List<TblPuc> listaNivel1 = tblPucService.pucNivel1(idLocal);
		model.addAttribute("opcionesNivel1", listaNivel1);

			
			return "Puc/Puc";


	}
	
	
	@PostMapping("/ListaSegundoNivel")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ListaSegundoNivel(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();

		
		// Obtenemos los datos del JSON recibido
		String idCuentaStr = (String) requestBody.get("idCuenta");
		Integer idCuenta = Integer.parseInt(idCuentaStr);
		String idClaseStr = (String) requestBody.get("idClase");
		Integer idClase = Integer.parseInt(idClaseStr);
      	System.out.println("Valor de idCuenta: " + idCuenta);
      	
      	Integer idCuentaMenor = idCuenta * 10;  // Resultado será 10
      	Integer idCuentaSuma = idCuenta + 1;
      	Integer idCuentaMayor = idCuentaSuma * 10; // Resultado será 100

      	
      	System.out.println("idCuentaMenor " + idCuentaMenor);
      	System.out.println("idCuentaMayor " + idCuentaMayor);
	
      	List<TblPuc> listaNivel2 = tblPucService.pucNivel2(idLocal, idCuentaMenor, idCuentaMayor, idClase);
      	System.out.println("listaNivel2  es  " + listaNivel2);
      	
      	for(TblPuc puc : listaNivel2) {
      		
      		System.out.println("codigo es  " + puc.getIdCuenta());
      		
      	}
      	
      	response.put("xlistaNivel2", listaNivel2);     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	@PostMapping("/ListaNiveles")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ListaTercerNivel(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
        
        Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		
		Map<String, Object> response = new HashMap<>();

		
		// Obtenemos los datos del JSON recibido
		String idCuentaStr = (String) requestBody.get("idCuenta");	
		
		String idCuentaMenorStr = idCuentaStr + "00";
		String idCuentaMayorStr = idCuentaStr + "99";
		Integer idCuentaMenor = Integer.parseInt(idCuentaMenorStr);
		Integer idCuentaMayor = Integer.parseInt(idCuentaMayorStr);
		
		System.out.println("idCuentaMenor " + idCuentaMenor);
      	System.out.println("idCuentaMayor " + idCuentaMayor);
		

		String idClaseStr = (String) requestBody.get("idClase");
		Integer idClase = Integer.parseInt(idClaseStr);

      	
      
	
      	List<TblPuc> listaNiveles = tblPucService.pucNiveles(idLocal, idCuentaMenor, idCuentaMayor, idClase);
      	System.out.println("listaNiveles  es  " + listaNiveles);
      	
      	response.put("xlistaNiveles", listaNiveles);     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	@PostMapping("/ListaNivel4")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ListaNivel4(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();

		
		// Obtenemos los datos del JSON recibido
		String idCuentaStr = (String) requestBody.get("idCuenta");
		Integer idCuentaAux = Integer.parseInt(idCuentaStr);
		
		String idCuentaMenorStr = idCuentaStr + "00";
		String idCuentaMayorStr = idCuentaStr + "99";
		Integer idCuentaMenor = Integer.parseInt(idCuentaMenorStr);
		Integer idCuentaMayor = Integer.parseInt(idCuentaMayorStr);
		
		System.out.println("idCuentaMenor " + idCuentaMenor);
      	System.out.println("idCuentaMayor " + idCuentaMayor);
		

		String idClaseStr = (String) requestBody.get("idClase");
		Integer idClase = Integer.parseInt(idClaseStr);

      	
      
	
      	List<TblPuc> listaNiveles = tblPucService.pucNiveles(idLocal, idCuentaMenor, idCuentaMayor, idClase);
      	System.out.println("listaNiveles  es  " + listaNiveles);
      	
      	
      	List<TblPucAux> listaAuxiliares = tblPucAuxService.listaAuxiliares(idCuentaAux, idLocal);
      	System.out.println("listaAuxiliares  es  " + listaAuxiliares);
      	
     // Crear un array de enteros a partir del campo idCuentaAux
      	List<Integer> xListaAuxiliares = listaAuxiliares.stream()
      	        .map(TblPucAux::getIdCuentaAux)  
      	        .collect(Collectors.toList());  // Crear la lista de enteros
      	
      	response.put("xListaAuxiliares", xListaAuxiliares);       	
      	response.put("xlistaNiveles", listaNiveles);     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	@PostMapping("/ListaAuxiliares")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ListaAuxiliares(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();

		
		// Obtenemos los datos del JSON recibido
		String idCuentaStr = (String) requestBody.get("idCuenta");	
		Integer idCuentaAux = Integer.parseInt(idCuentaStr);

      	System.out.println("idCuentaAux " + idCuentaAux);
		

      	
      
		List<TblPucAux> listaAuxiliares = tblPucAuxService.listaAuxiliares(idCuentaAux, idLocal);
      	System.out.println("listaAuxiliares  es  " + listaAuxiliares);
      	
      	response.put("xListaAuxiliares", listaAuxiliares);     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	
	@PostMapping("/DetallePuc")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> DetallePuc(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClaseController = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClaseController);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();

		
		// Obtenemos los datos del JSON recibido
		String idSubCuentaStr = (String) requestBody.get("idCuenta");	
		System.out.println("idSubCuentaStr en DetallePuc es " + idSubCuentaStr);
		Integer subCuenta =  Integer.parseInt(idSubCuentaStr);
		
		
		String grupoStr = idSubCuentaStr.substring(0,2);
		System.out.println("grupoStr en DetallePuc es " + grupoStr);
		Integer grupo = Integer.parseInt(grupoStr);
		
		
		String cuentaStr = idSubCuentaStr.substring(0,4);
		System.out.println("cuentaStr en DetallePuc es " + cuentaStr);
		Integer cuenta = Integer.parseInt(cuentaStr);

		String idClaseStr = (String) requestBody.get("idClase");
		Integer idClase = Integer.parseInt(idClaseStr);
		
		
		//Clase
		String nombreClase = tblPucService.obtenerNombreCuenta(idLocal, idClase, idClase);
		response.put("xNombreClase", nombreClase);
		response.put("xIdClase", idClase);
		
		//Grupo
		String nombreGrupo = tblPucService.obtenerNombreCuenta(idLocal, grupo, idClase);
		System.out.println("nombreGrupo en DetallePuc es " + nombreGrupo);
		response.put("xNombreGrupo", nombreGrupo);
		response.put("xIdGrupo", grupo);
		
		//Cuenta
		String nombreCuenta = tblPucService.obtenerNombreCuenta(idLocal, cuenta, idClase);
		System.out.println("nombreCuenta en DetallePuc es " + nombreCuenta);
		response.put("xNombreCuenta", nombreCuenta);
		response.put("xIdCuenta", cuenta);
		
		//Subcuenta
		String nombreSubCuenta = tblPucService.obtenerNombreCuenta(idLocal, subCuenta, idClase);
		System.out.println("nombreSubCuenta en DetallePuc es " + nombreSubCuenta);
		response.put("xNombreSubCuenta", nombreSubCuenta);
		response.put("xIdSubCuenta", subCuenta);
		
		
      	List<TblPucAux> listaAuxiliares = tblPucAuxService.listaAuxiliares(subCuenta, idLocal);
      	System.out.println("listaAuxiliares  es  " + listaAuxiliares);
      	
     // Crear un array de enteros a partir del campo idCuentaAux
      	List<Integer> xListaAuxiliares = listaAuxiliares.stream()
      	        .map(TblPucAux::getIdCuentaAux)  
      	        .collect(Collectors.toList());  // Crear la lista de enteros
      	
      	response.put("xListaAuxiliares", xListaAuxiliares);    
      
	
       	
      	     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	@PostMapping("/DetalleCuenta")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> DetalleCuenta(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClaseController = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClaseController);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();

		
		// Obtenemos los datos del JSON recibido
		String idSubCuentaStr = (String) requestBody.get("idCuenta");	
		System.out.println("idSubCuentaStr en DetalleSubCuenta es " + idSubCuentaStr);
		Integer idCuenta =  Integer.parseInt(idSubCuentaStr);
		
		

		String idClaseStr = (String) requestBody.get("idClase");
		Integer idClase = Integer.parseInt(idClaseStr);
		
		
		//Clase
		String nombreClase = tblPucService.obtenerNombreCuenta(idLocal, idClase, idClase);
		response.put("xNombreClase", nombreClase);
		response.put("xIdClase", idClase);
		
		//Grupo
		String nombreGrupo = tblPucService.obtenerNombreCuenta(idLocal, idCuenta, idClase);
		System.out.println("nombreGrupo en DetalleSubCuenta es " + nombreGrupo);
		response.put("xNombreGrupo", nombreGrupo);
		response.put("xIdGrupo", idCuenta);
				

		
		
		List<TblPuc> listaCuentas = tblPucService.listaCuentas(idCuenta, idLocal);
      	System.out.println("listaCuentas  es  " + listaCuentas);
      	
     // Crear un array de enteros a partir del campo idCuentaAux
      	List<Integer> xListaCuentas = listaCuentas.stream()
      	        .map(TblPuc::getIdCuenta)  
      	        .collect(Collectors.toList());  // Crear la lista de enteros
      	
      	response.put("xListaCuentas", xListaCuentas);    
      
	
       	
      	     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	
	@PostMapping("/DetalleSubCuenta")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> DetalleSubCuenta(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClaseController = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClaseController);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();

		
		// Obtenemos los datos del JSON recibido
		String idSubCuentaStr = (String) requestBody.get("idCuenta");	
		System.out.println("idSubCuentaStr en DetalleSubCuenta es " + idSubCuentaStr);
		Integer subCuenta =  Integer.parseInt(idSubCuentaStr);
		
		
		String grupoStr = idSubCuentaStr.substring(0,2);
		System.out.println("grupoStr en DetalleSubCuenta es " + grupoStr);
		Integer grupo = Integer.parseInt(grupoStr);
		
		
		String cuentaStr = idSubCuentaStr.substring(0,4);
		System.out.println("cuentaStr en DetalleSubCuenta es " + cuentaStr);
		Integer cuenta = Integer.parseInt(cuentaStr);

		String idClaseStr = (String) requestBody.get("idClase");
		Integer idClase = Integer.parseInt(idClaseStr);
		
		
		//Clase
		String nombreClase = tblPucService.obtenerNombreCuenta(idLocal, idClase, idClase);
		response.put("xNombreClase", nombreClase);
		response.put("xIdClase", idClase);
		
		//Grupo
		String nombreGrupo = tblPucService.obtenerNombreCuenta(idLocal, grupo, idClase);
		System.out.println("nombreGrupo en DetalleSubCuenta es " + nombreGrupo);
		response.put("xNombreGrupo", nombreGrupo);
		response.put("xIdGrupo", grupo);
		
		//Cuenta
		String nombreCuenta = tblPucService.obtenerNombreCuenta(idLocal, cuenta, idClase);
		System.out.println("nombreCuenta en DetalleSubCuenta es " + nombreCuenta);
		response.put("xNombreCuenta", nombreCuenta);
		response.put("xIdCuenta", cuenta);
		
		//Subcuenta
		String nombreSubCuenta = tblPucService.obtenerNombreCuenta(idLocal, subCuenta, idClase);
		System.out.println("nombreSubCuenta en DetalleSubCuenta es " + nombreSubCuenta);
		response.put("xNombreSubCuenta", nombreSubCuenta);
		response.put("xIdSubCuenta", subCuenta);
		
		
		List<TblPuc> listaSubCuentas = tblPucService.listaSubCuentas(subCuenta, idLocal);
      	System.out.println("listaSubCuentas  es  " + listaSubCuentas);
      	
     // Crear un array de enteros a partir del campo idCuentaAux
      	List<Integer> xListaSubCuentas = listaSubCuentas.stream()
      	        .map(TblPuc::getIdCuenta)  
      	        .collect(Collectors.toList());  // Crear la lista de enteros
      	
      	response.put("xListaSubCuentas", xListaSubCuentas);    
      
	
       	
      	     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	@PostMapping("/DetalleAux")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> DetalleAux(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClaseController = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClaseController);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();

		
		// Obtenemos los datos del JSON recibido
		String idSubCuentaStr = (String) requestBody.get("idCuenta");	
		System.out.println("idSubCuentaStr en DetalleAux es " + idSubCuentaStr);
		Integer subCuenta =  Integer.parseInt(idSubCuentaStr);
		
		String idCuentaAuxStr = (String) requestBody.get("idCuentaAux");
		String coidgoAux =  idCuentaAuxStr.substring(6, 8);
		System.out.println("coidgoAux en DetalleAux es " + coidgoAux);
		System.out.println("idCuentaAuxStr en DetalleAux es " + idCuentaAuxStr);
		Integer idCuentaAux =  Integer.parseInt(idCuentaAuxStr);
		
		
		String grupoStr = idSubCuentaStr.substring(0,2);
		System.out.println("grupoStr en DetalleAux es " + grupoStr);
		Integer grupo = Integer.parseInt(grupoStr);
		
		
		String cuentaStr = idSubCuentaStr.substring(0,4);
		System.out.println("cuentaStr en DetalleAux es " + cuentaStr);
		Integer cuenta = Integer.parseInt(cuentaStr);

		String idClaseStr = (String) requestBody.get("idClase");
		Integer idClase = Integer.parseInt(idClaseStr);
		
		
		//Clase
		String nombreClase = tblPucService.obtenerNombreCuenta(idLocal, idClase, idClase);
		response.put("xNombreClase", nombreClase);
		response.put("xIdClase", idClase);
		
		//Grupo
		String nombreGrupo = tblPucService.obtenerNombreCuenta(idLocal, grupo, idClase);
		System.out.println("nombreGrupo en DetalleAux es " + nombreGrupo);
		response.put("xNombreGrupo", nombreGrupo);
		response.put("xIdGrupo", grupo);
		
		//Cuenta
		String nombreCuenta = tblPucService.obtenerNombreCuenta(idLocal, cuenta, idClase);
		System.out.println("nombreCuenta en DetalleAux es " + nombreCuenta);
		response.put("xNombreCuenta", nombreCuenta);
		response.put("xIdCuenta", cuenta);
		
		//Subcuenta
		String nombreSubCuenta = tblPucService.obtenerNombreCuenta(idLocal, subCuenta, idClase);
		System.out.println("nombreSubCuenta en DetalleAux es " + nombreSubCuenta);
		response.put("xNombreSubCuenta", nombreSubCuenta);
		response.put("xIdSubCuenta", subCuenta);
		
		
		String nombreCuentaAux = tblPucAuxService.obtenerNombreCuentaAux(idLocal, idCuentaAux);
		System.out.println("nombreCuentaAux en DetalleAux es " + nombreCuentaAux);
		response.put("xNombreCuentaAux", nombreCuentaAux);
		response.put("xCoidgoAux", coidgoAux);
      
	
       	
      	     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	
	@PostMapping("/CrearCuenta-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> CrearCuenta(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
        
        
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();


	    System.out.println("SI ENTRÓ A  /CrearCuenta");

	        // Obtenemos los datos del JSON recibido
	        String grupo = (String) requestBody.get("grupo");
	        String cuenta = (String) requestBody.get("cuenta");
	        String idCuentaStr = grupo + cuenta;
	        Integer idCuenta = Integer.parseInt(idCuentaStr);
	        
	        String idClaseStr = grupo.substring(0,1);
	        Integer idClase = Integer.parseInt(idClaseStr);
	        System.out.println("idClaseStr es " + idClaseStr);
	        
	        String nombreCuenta = (String) requestBody.get("nombreCuenta");

	        
	        System.out.println("grupo es " + grupo);
	        System.out.println("cuenta es " + cuenta);
	        System.out.println("nombreCuenta es " + nombreCuenta);

	        

	        //INGRESAR NUEVA CUENTA
	        tblPucRepo.ingresaSubCuenta(idLocal, idClase, idCuenta, nombreCuenta);

	        
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "LOGGGGGGGGG");
		    response.put("xNombreCuenta", nombreCuenta);
		    response.put("xIdCuenta", idCuenta);


		    return ResponseEntity.ok(response);
	   
	    
	}
	
	
	
	@PostMapping("/CrearSubCuenta-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> CrearSubCuenta(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
        
        
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();


	    System.out.println("SI ENTRÓ A  /CrearSubCuenta");

	        // Obtenemos los datos del JSON recibido
	        String cuenta = (String) requestBody.get("cuenta");
	        String subCuenta = (String) requestBody.get("subCuenta");
	        String idSubCuentaStr = cuenta + subCuenta;
	        Integer idSubCuenta = Integer.parseInt(idSubCuentaStr);
	        
	        String idClaseStr = cuenta.substring(0,1);
	        Integer idClase = Integer.parseInt(idClaseStr);
	        System.out.println("idClaseStr es " + idClaseStr);
	        
	        String nombreSubCuenta = (String) requestBody.get("nombreSubCuenta");

	        
	        System.out.println("cuenta es " + cuenta);
	        System.out.println("subCuenta es " + subCuenta);
	        System.out.println("idSubCuenta es " + idSubCuenta);

	        

	        //INGRESAR NUEVA SUBCUENTA
	        tblPucRepo.ingresaSubCuenta(idLocal, idClase, idSubCuenta, nombreSubCuenta);

	        
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "LOGGGGGGGGG");
		    response.put("xNombreSubCuenta", nombreSubCuenta);
		    response.put("xIdSubCuenta", idSubCuenta);


		    return ResponseEntity.ok(response);
	   
	    
	}
	
	
	
	@PostMapping("/CrearAuxiliar-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> CrearAuxiliar(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
        
        
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();


	    System.out.println("SI ENTRÓ A  /CrearAuxiliar");

	        // Obtenemos los datos del JSON recibido
	        String subCuenta = (String) requestBody.get("subCuenta");
	        String codidoAux = (String) requestBody.get("codidoAux");
	        String idCuentaAuxStr = subCuenta + codidoAux;
	        Integer idCuentaAux = Integer.parseInt(idCuentaAuxStr);
	        
	        
	        String nombreAux = (String) requestBody.get("nombreAux");

	        
	        System.out.println("subCuenta es " + subCuenta);
	        System.out.println("codidoAux es " + codidoAux);
	        System.out.println("nombreAux es " + nombreAux);

	        

	        //INGRESAR NUEVO AUXILIAR
	        tblPucAuxRepo.ingresaAuxiliar(idLocal, idCuentaAux, nombreAux, 0, 0.0, 0);

	        
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "LOGGGGGGGGG");
		    response.put("xNombreAux", nombreAux);
		    response.put("xIdCuentaAux", idCuentaAux);


		    return ResponseEntity.ok(response);
	   
	    
	}
	
	
	
	
	@PostMapping("/ActualizarAuxiliar-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ActualizarAuxiliar(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
        
        
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();


	    System.out.println("SI ENTRÓ A  /ActualizarAuxiliar");

	        // Obtenemos los datos del JSON recibido
	        String subCuenta = (String) requestBody.get("subCuenta");
	        String codidoAux = (String) requestBody.get("codidoAux");
	        String idCuentaAuxStr = subCuenta + codidoAux;
	        Integer idCuentaAux = Integer.parseInt(idCuentaAuxStr);
	        
	        
	        String nombreAux = (String) requestBody.get("nombreAux");

	        
	        System.out.println("subCuenta es " + subCuenta);
	        System.out.println("codidoAux es " + codidoAux);
	        System.out.println("nombreAux es " + nombreAux);

	        

	        //ACTUALIZAR AUXILIAR
	        tblPucAuxRepo.actualizarAuxiliar(nombreAux, idLocal, idCuentaAux);

	        
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "LOGGGGGGGGG");
		    response.put("xNombreAux", nombreAux);
		    response.put("xIdCuentaAux", idCuentaAux);


		    return ResponseEntity.ok(response);
	   
	    
	}
	
	
	@PostMapping("/EliminarAuxiliar-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> EliminarAuxiliar(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
        
        
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();


	    System.out.println("SI ENTRÓ A  /EliminarAuxiliar");

	        // Obtenemos los datos del JSON recibido
	        String subCuenta = (String) requestBody.get("subCuenta");
	        String codidoAux = (String) requestBody.get("codidoAux");
	        String idCuentaAuxStr = subCuenta + codidoAux;
	        Integer idCuentaAux = Integer.parseInt(idCuentaAuxStr);
	        
	        
	        String nombreAux = (String) requestBody.get("nombreAux");

	        
	        System.out.println("subCuenta es " + subCuenta);
	        System.out.println("codidoAux es " + codidoAux);
	        System.out.println("nombreAux es " + nombreAux);

	        

	        //ELIMINAR AUXILIAR
	        tblPucAuxRepo.eliminarAuxiliar(idLocal, idCuentaAux);

	        
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "LOGGGGGGGGG");
		    response.put("xNombreAux", nombreAux);
		    response.put("xIdCuentaAux", idCuentaAux);


		    return ResponseEntity.ok(response);
	   
	    
	}

}
