package com.contabook.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.contabook.Model.DBMailMarketing.TblDctosPeriodo;
import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Model.DBMailMarketing.TblPucAux;
import com.contabook.Model.DBMailMarketing.TblTipoCpte;
import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.dbaquamovil.TblAgendaLogVisitas;
import com.contabook.Model.dbaquamovil.TblTerceros;
import com.contabook.Projection.TblDctosDTO;
import com.contabook.Repository.DBMailMarketing.TblDctoDetalleRepo;
import com.contabook.Repository.DBMailMarketing.TblDctoRepo;
import com.contabook.Repository.DBMailMarketing.TblPucAuxRepo;
import com.contabook.Service.DBMailMarketing.TblDctoService;
import com.contabook.Service.DBMailMarketing.TblDctosPeriodoService;
import com.contabook.Service.DBMailMarketing.TblPucAuxService;
import com.contabook.Service.DBMailMarketing.TblPucService;
import com.contabook.Service.DBMailMarketing.TblTipoCpteService;
import com.contabook.Service.dbaquamovil.TblDctosService;
import com.contabook.Service.dbaquamovil.TblTercerosService;
import com.contabook.Utilidades.ControlDeInactividad;

@Controller
public class ComprobanteContableController {
	
	@Autowired
	TblPucService tblPucService;
	
	@Autowired
	TblPucAuxRepo tblPucAuxRepo;
	
	@Autowired
	TblPucAuxService tblPucAuxService;
	
	@Autowired
	TblTercerosService tblTercerosService;
	
	@Autowired
	TblDctosService tblDctosService;
	
	@Autowired
	TblDctoService tblDctoService;
	
	@Autowired
	TblDctoRepo tblDctoRepo;
	
	@Autowired
	TblDctoDetalleRepo tblDctoDetalleRepo;
	
	@Autowired
	TblTipoCpteService tblTipoCpteService;
	
	@Autowired
	TblDctosPeriodoService tblDctosPeriodoService;
	
	@Autowired
	ControlDeInactividad controlDeInactividad;
	
	@GetMapping("/ComprobanteContable")
	public String ComprobanteContable(HttpServletRequest request,Model model) {
		
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
		
		 // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Formatear la fecha como un String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String strFechaVisita = fechaActual.format(formatter);
        
        System.out.println("strFechaVisita  es" + strFechaVisita);
        

        DateTimeFormatter formatterAct = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String FechActual = fechaActual.format(formatterAct);
        
        //obtenemos el maximo idCpte
    	Integer maxIdCpte = tblDctoService.MaximoiIdCpte(idLocal) + 1;
    	System.out.println("maxIdCpte es " + maxIdCpte);
    	
    	// lista tipos de comprobante
		List<TblTipoCpte> listaComprobantes = tblTipoCpteService.ListaComprobantes();
	    System.out.println("La lista de ListaComprobantes es: " + listaComprobantes);
        
        

        model.addAttribute("xFechaActual", FechActual);
        model.addAttribute("xXaxIdCpte", maxIdCpte);
        model.addAttribute("xListaComprobantes", listaComprobantes);
		

			
			return "ComprobanteContable/ComprobanteContable";


	}
	
	
	@PostMapping("/ObtenerCuentas")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ObtenerCuentas(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();
		

		List<TblPucAux> todosAuxiliares = tblPucAuxService.listaTodosAuxiliares(idLocal);
      	System.out.println("todosAuxiliares  es  " + todosAuxiliares);
      	

      	response.put("xlistaCuentas", todosAuxiliares);     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	
	@PostMapping("/ObtenerTerceros")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ObtenerTerceros(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,  Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
		Ctrlusuarios usuario = (Ctrlusuarios)request.getSession().getAttribute("usuarioAuth");
		Integer idLocal = usuario.getIdLocal();
		
		Map<String, Object> response = new HashMap<>();
		

		List<TblTerceros> listaTerceros = tblTercerosService.ListaTercerosSuscriptor(idLocal);
      	System.out.println("listaTerceros  es  " + listaTerceros);
      	
      	for(TblTerceros tercero : listaTerceros) {
      		
      		System.out.println("nombre tercero es   " + tercero.getNombreTercero());
      		System.out.println("CCNIT tercero es   " + tercero.getCC_Nit());
      	}
      	

      	response.put("xListaTerceros", listaTerceros);     	
		
      	return ResponseEntity.ok(response);
		
	}
	
	
	
	@PostMapping("/GuardarDctoContable-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> GuardarDctoContable(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
        
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();


	    System.out.println("SI ENTRÓ A  /GuardarDctoContable");

	    @SuppressWarnings("unchecked")
		List<String> xCuentaArr = (List<String>) requestBody.get("xCuentaArr");
	    @SuppressWarnings("unchecked")
	    List<String> xTerceroArr = (List<String>) requestBody.get("xTerceroArr");
	    @SuppressWarnings("unchecked")
	    List<String> xDetalleContableArr = (List<String>) requestBody.get("xDetalleContableArr");
	    @SuppressWarnings("unchecked")
	    List<String> xDescripcionArr = (List<String>) requestBody.get("xDescripcionArr");
	    @SuppressWarnings("unchecked")
	    List<String> xDebitoArr = (List<String>) requestBody.get("xDebitoArr");
	    @SuppressWarnings("unchecked")
	    List<String> xCreditoArr = (List<String>) requestBody.get("xCreditoArr");
	    
	    
	    String tipoComprobanteStr = (String) requestBody.get("tipoComprobante");	    
	    Integer idTipoCpte = Integer.parseInt(tipoComprobanteStr);
	    
	    String fechaDcto = (String) requestBody.get("fechaComprobante");
	    
	    
	    
	    String[] xCuentaArray = xCuentaArr.toArray(new String[0]);
	    String[] xTerceroArray = xTerceroArr.toArray(new String[0]);
	    String[] xDetalleContableArray = xDetalleContableArr.toArray(new String[0]);
	    String[] xDescripcionArray = xDescripcionArr.toArray(new String[0]);
	    String[] xDebitoArray = xDebitoArr.toArray(new String[0]);
	    String[] xCreditoArray = xCreditoArr.toArray(new String[0]);
	    
	    
	    System.out.println("xCuentaArray es: " + xCuentaArray);
	    System.out.println("xTerceroArray es: " + xTerceroArray);
	    System.out.println("xDetalleContableArray es: " + xDetalleContableArray);
	    System.out.println("xDescripcionArray es: " + xDescripcionArray);
	    System.out.println("xDebitoArray es: " + xDebitoArray);
	    System.out.println("xCreditoArray es: " + xCreditoArray);
	    
	    
	    
	    Integer idTipoOrden = 999;
	    String siglaMoneda = "COP";
	    
	    // Obtenemos el periodo activo
		List <TblDctosPeriodo> PeriodoActivo = tblDctosPeriodoService.ObtenerPeriodoActivo(idLocal);
		
		Integer idPeriodo = 0;
		
		for(TblDctosPeriodo P : PeriodoActivo) {						
			idPeriodo = P.getIdPeriodo();					
		}
	    
	    //obtenemos el maximo idCpte
    	Integer maxIdCpte = tblDctoService.MaximoiIdCpte(idLocal) + 1;
    	System.out.println("maxIdCpte es " + maxIdCpte);
    	
	    
	    
	    for(int i = 0; i < xCuentaArray.length; i ++  ) {
	    	
	    	
	    	 // Verifica si el Dcto ya existe
	        Boolean existe = tblDctoService.ExisteDcto(idLocal, idTipoOrden, maxIdCpte);
	        System.out.println("existe Dcto es " + existe);

	        if (!existe) {
	            System.out.println("El documento no existe ");
	            
	            //(int idLocal, int idTipoCpte, int idCpte, int idTipoOrden, int idDcto, String FechaDcto, String siglaMoneda, int idTasa, int idPeriodo)
	            tblDctoRepo.ingresaDcto(idLocal, idTipoCpte, maxIdCpte, idTipoOrden, maxIdCpte, fechaDcto, siglaMoneda, 0, idPeriodo);
	            System.out.println("ingresaDcto ");
	            
	            int item = 1;

	            // Ciclo para ingresar detalle
	            for (int l = 0; l < xCuentaArray.length; l ++) {
	            	
	    	    	Integer idCuenta = Integer.parseInt(xCuentaArray[l]);
	    	    	System.out.println("idCuenta es " + idCuenta);
	    	    	
	    	    	String cliente = xTerceroArray[l];
	    	    	Double vrDebito = Double.parseDouble(xDebitoArray[l]);
	    	    	Double vrCredito = Double.parseDouble(xCreditoArray[l]);
	    	        String descripcion = xDescripcionArray[l];
	    	        String observación = xDetalleContableArray[l];
	            		                	
	                	                	
                        //(int idLocal, int idTipoCpte, int idCpte, int idCuentaAux, String idCliente, int item, int sucursal, int codProducto, int codBodega, 
	      			    // int accion, int cantProducto, int prefijo, int consecutivo, int numeroCuota, String fechaVencimiento, int codImpuesto, int codGrupoActivoFijo, int codActivoFijo,
	    			   //String descripcion, int codCentroSubCentro, Double vrDebito, Double vrCredito, String observacion, Double baseGravable, int mesCierre )
	                 	tblDctoDetalleRepo.ingresaDctoDetalle(idLocal, idTipoCpte, maxIdCpte, idCuenta, cliente, item, 0, 0, 0, 
	                	 0, 0, 0, 0, 0, fechaDcto, 0, 0, 0, descripcion, 0, vrDebito, vrCredito, observación, 0.0, idPeriodo);
	                 	
	                	System.out.println("ingresaDctoDetalle ");
	                    
	                    item++;
	               
	            }

	        } else {
	        	

	        }
	    	
	    	
	    }
	    

	        
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "LOGGGGGGGGG");
		    response.put("xComprobante", maxIdCpte);

		    return ResponseEntity.ok(response);
	   
	    
	}

}
