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
import org.springframework.web.bind.annotation.ResponseBody;

import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Model.dbaquamovil.TblAgendaLogVisitas;
import com.contabook.Projection.TblDctosDTO;
import com.contabook.Projection.TblPagosDTO;
import com.contabook.Repository.DBMailMarketing.TblDctoDetalleRepo;
import com.contabook.Repository.DBMailMarketing.TblDctoRepo;
import com.contabook.Service.DBMailMarketing.TblDctoService;
import com.contabook.Service.DBMailMarketing.TblDctosPeriodoService;
import com.contabook.Service.DBMailMarketing.TblTipoCpteService;
import com.contabook.Service.dbaquamovil.TblDctosService;
import com.contabook.Service.dbaquamovil.TblPagosService;
import com.contabook.Utilidades.ControlDeInactividad;
import com.contabook.Model.DBMailMarketing.TblDctosPeriodo;
import com.contabook.Model.DBMailMarketing.TblTipoCpte;

@Controller
public class ImportarController {
	
	@Autowired
	TblDctosService tblDctosService;
	
	@Autowired
	TblDctoService tblDctoService;
	
	@Autowired
	TblDctoRepo tblDctoRepo;
	
	@Autowired
	TblDctoDetalleRepo tblDctoDetalleRepo;
	
	@Autowired
	TblPagosService tblPagosService;
	
	@Autowired
	TblTipoCpteService tblTipoCpteService;
	
	@Autowired
	TblDctosPeriodoService tblDctosPeriodoService;
	
	@Autowired
	ControlDeInactividad controlDeInactividad;
	
	@GetMapping("/Importar")
	public String Importar(HttpServletRequest request,Model model) {
		
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
		
		// Obtenemos el periodo activo
		List <TblDctosPeriodo> PeriodoActivo = tblDctosPeriodoService.ObtenerPeriodoActivo(idLocal);
		
		Integer idPeriodo = 0;
		
		for(TblDctosPeriodo P : PeriodoActivo) {
			
			idPeriodo = P.getIdPeriodo();
			model.addAttribute("xIdPeriodo", P.getIdPeriodo());
			System.out.println("xIdPeriodo activo es " + P.getIdPeriodo());
			model.addAttribute("xINombrePeriodo", P.getNombrePeriodo());
		
		}
		
		
		List<TblTipoCpte> listaComprobantes = tblTipoCpteService.ListaComprobantes();
	    System.out.println("La lista de ListaComprobantes es: " + listaComprobantes);
        
        model.addAttribute("xListaComprobantes", listaComprobantes);
  

			
			return "Importar/Importar";


	}
	
	
	@PostMapping("/ImportarContabilidad-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ImportarContabilidad(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
        
	    Ctrlusuarios usuario = (Ctrlusuarios) request.getSession().getAttribute("usuarioAuth");
	    Integer IdUsuario = usuario.getIdUsuario();
	    
	    Integer idLocal = usuario.getIdLocal();
	    //Integer idPeriodo = 202403;
	    
	    Map<String, Object> response = new HashMap<>();
	    System.out.println("SI ENTRÓ A  /ImportarContabilidad");
	    
	    // Obtenemos los datos del JSON recibido
        String idPeriodoStr = (String) requestBody.get("idPeriodo");
        Integer idPeriodo = Integer.parseInt(idPeriodoStr);
        System.out.println("idPeriodo en ImportarContabilidad es " + idPeriodo);
        
        String tipoComprobanteStr = (String) requestBody.get("tipoComprobante");
        Integer idTipoCpte = Integer.parseInt(tipoComprobanteStr);
        System.out.println("idTipoCpte en ImportarContabilidad es " + idTipoCpte);
        
        //Obtenemos nombre del tipoComprobante
        String NombreComprobante = tblTipoCpteService.obtenerNombreComprobante(idTipoCpte);
        
        
        //Obtenemos idTipoOrden
        Integer idTipoOrden = tblTipoCpteService.obtenerIdTipoOrden(idTipoCpte);
        System.out.println("idTipoOrden en ImportarContabilidad es " + idTipoOrden);
        

        int idTipoCpteFactura= 2;
        int idTipoCpteRecaudo= 3;
        int idTipoCpteNomina= 10;
        int idTipoCpDctoSoporte= 8;
        
        // FACTURAS
        if(idTipoCpte == idTipoCpteFactura) {
        	
        	List<TblDctosDTO> listaComprobante = tblDctosService.listaComprobanteDetallado(idLocal, idPeriodo, idTipoOrden);
    	    System.out.println("listaComprobante es " + listaComprobante);
    	    
    	   // Integer idTipoOrden = 0;
    	    Integer idDcto = 0;
    	    
    	    for (TblDctosDTO cpte : listaComprobante) {
    	       // idTipoOrden = cpte.getIdTipoOrden();
    	        idDcto = cpte.getIdDcto();

    	        // Verifica si el Dcto ya existe
    	        Boolean existe = tblDctoService.ExisteDctoCpte(idLocal, idTipoOrden, idDcto, idTipoCpte);
    	        System.out.println("existe Dcto es " + existe);

    	        if (!existe) {
    	            System.out.println("El documento no existe " + idDcto);

    	            Integer maxIdCpte = tblDctoService.MaximoiIdCpte(idLocal) + 1;

    	            tblDctoRepo.ingresaDcto(idLocal, idTipoCpte, maxIdCpte, idTipoOrden, idDcto, cpte.getFechaDcto(), cpte.getSiglaMoneda(), 0, cpte.getIdPeriodo());
    	            System.out.println("ingresaDcto " + idDcto);
    	            
    	            int item = 1;

    	            // Ciclo para ingresar detalle
    	            for (TblDctosDTO detalle : listaComprobante) {
    	                if (detalle.getIdDcto().equals(idDcto)) {
    	                	
    	                	Integer idCuentaAux = detalle.getIdSubcuenta()  != null ? detalle.getIdSubcuenta() : 0;

    	                	tblDctoDetalleRepo.ingresaDctoDetalle(idLocal, idTipoCpte, maxIdCpte, idCuentaAux, detalle.getIdCliente(), item, 0, 0, 0, 
    	                	 0, 0, 0, 0, 0, detalle.getFechaDcto(), 0, 0, 0, null, 0, detalle.getVrDebito(), detalle.getVrCredito(), detalle.getObservacion(), 0.0, detalle.getIdPeriodo());
    	                	System.out.println("ingresaDctoDetalle " + idDcto);
    	                    
    	                    item++;
    	                }
    	            }

    	        } else {
    	        	

    	        }
    	    }
        	
        	
        }
        
        // RECAUDOS
        if(idTipoCpte == idTipoCpteRecaudo) {
        	
        	
        	List<TblDctosDTO> recaudoDetallado = tblDctosService.listaComprobanteRecaudoDetallado(idLocal, idPeriodo);
        	
        	Integer idDcto = 0;
        	
        	for (TblDctosDTO recaudo : recaudoDetallado) {
     	       // idTipoOrden = cpte.getIdTipoOrden();
     	        idDcto = recaudo.getIdDctoNitCC();
     	        
     	       System.out.println("Dcto es " + idDcto);

     	        // Verifica si el Dcto ya existe
     	        Boolean existe = tblDctoService.ExisteDctoCpte(idLocal, idTipoOrden, idDcto, idTipoCpte);
     	        System.out.println("existe Dcto es " + existe);

     	       if (!existe) {
   	            System.out.println("El documento no existe " + idDcto);

   	            Integer maxIdCpte = tblDctoService.MaximoiIdCpte(idLocal) + 1;

   	            tblDctoRepo.ingresaDcto(idLocal, idTipoCpte, maxIdCpte, idTipoOrden, idDcto, recaudo.getFechaDctoSiigo(), recaudo.getSiglaMoneda(), 0, idPeriodo);
   	            System.out.println("ingresaDcto " + idDcto);
   	            
   	            int item = 1;

   	            // Ciclo para ingresar detalle
   	            for (TblDctosDTO detalle : recaudoDetallado) {
   	                if (detalle.getIdDctoNitCC().equals(idDcto)) {
   	                	
   	                	Integer idCuentaAux = detalle.getIdSubcuenta()  != null ? detalle.getIdSubcuenta() : 0;

   	                	tblDctoDetalleRepo.ingresaDctoDetalle(idLocal, idTipoCpte, maxIdCpte, idCuentaAux, detalle.getIdCliente(), item, 0, 0, 0, 
   	                	 0, 0, 0, 0, 0, detalle.getFechaDctoSiigo(), 0, 0, 0, null, 0, detalle.getVrDebito(), detalle.getVrCredito(), detalle.getObservacion(), 0.0, idPeriodo);
   	                	System.out.println("ingresaDctoDetalle " + idDcto);
   	                    
   	                    item++;
   	                }
   	            }

   	        } else {
   	        	

   	         }
     	   }
        	
        	
        }
        
        
        
      // NOMINA ELECTRONICA
      if(idTipoCpte == idTipoCpteNomina) {
        	
        	List<TblDctosDTO> nominaDetallado = tblDctosService.listaComprobanteNominaDetallado(idLocal, idPeriodo);
    	    System.out.println("nominaDetallado es " + nominaDetallado);
    	    
    	   // Integer idTipoOrden = 0;
    	    Integer idDcto = 0;
    	    
    	    for (TblDctosDTO nomina : nominaDetallado) {
    	       // idTipoOrden = cpte.getIdTipoOrden();
    	        idDcto = nomina.getIdDcto();

    	        // Verifica si el Dcto ya existe
    	        Boolean existe = tblDctoService.ExisteDctoCpte(idLocal, idTipoOrden, idDcto, idTipoCpte);
    	        System.out.println("existe Dcto es " + existe);

    	        if (!existe) {
    	            System.out.println("El documento no existe " + idDcto);

    	            Integer maxIdCpte = tblDctoService.MaximoiIdCpte(idLocal) + 1;

    	            tblDctoRepo.ingresaDcto(idLocal, idTipoCpte, maxIdCpte, idTipoOrden, idDcto, nomina.getFechaDcto(), nomina.getSiglaMoneda(), 0, nomina.getIdPeriodo());
    	            System.out.println("ingresaDcto " + idDcto);
    	            
    	            int item = 1;

    	            // Ciclo para ingresar detalle
    	            for (TblDctosDTO detalle : nominaDetallado) {
    	                if (detalle.getIdDcto().equals(idDcto)) {
    	                	
    	                	Integer idCuentaAux = detalle.getIdSubcuenta()  != null ? detalle.getIdSubcuenta() : 0;

    	                	tblDctoDetalleRepo.ingresaDctoDetalle(idLocal, idTipoCpte, maxIdCpte, idCuentaAux, detalle.getIdCliente(), item, 0, 0, 0, 
    	                	 0, 0, 0, 0, 0, detalle.getFechaDcto(), 0, 0, 0, null, 0, detalle.getVrDebito(), detalle.getVrCredito(), detalle.getObservacion(), 0.0, detalle.getIdPeriodo());
    	                	System.out.println("ingresaDctoDetalle " + idDcto);
    	                    
    	                    item++;
    	                }
    	            }

    	        } else {
    	        	

    	        }
    	    }
        	
        	
        }
      
      
      
      
      
      // DOCUMENTO SOPORTE
      if(idTipoCpte == idTipoCpDctoSoporte) {
        	
        	List<TblDctosDTO> DctoSoporteDetallado = tblDctosService.listaComprobanteDctoSoporteDetallado(idLocal, idPeriodo);
    	    System.out.println("DctoSoporteDetallado es " + DctoSoporteDetallado);
    	    
    	   // Integer idTipoOrden = 0;
    	    Integer idDcto = 0;
    	    
    	    for (TblDctosDTO dctoSoporte : DctoSoporteDetallado) {
    	       // idTipoOrden = cpte.getIdTipoOrden();
    	        idDcto = dctoSoporte.getIdDcto();

    	        // Verifica si el Dcto ya existe
    	        Boolean existe = tblDctoService.ExisteDctoCpte(idLocal, idTipoOrden, idDcto, idTipoCpte);
    	        System.out.println("existe Dcto es " + existe);

    	        if (!existe) {
    	            System.out.println("El documento no existe " + idDcto);

    	            Integer maxIdCpte = tblDctoService.MaximoiIdCpte(idLocal) + 1;

    	            tblDctoRepo.ingresaDcto(idLocal, idTipoCpte, maxIdCpte, idTipoOrden, idDcto, dctoSoporte.getFechaDcto(), dctoSoporte.getSiglaMoneda(), 0, dctoSoporte.getIdPeriodo());
    	            System.out.println("ingresaDcto " + idDcto);
    	            
    	            int item = 1;

    	            // Ciclo para ingresar detalle
    	            for (TblDctosDTO detalle : DctoSoporteDetallado) {
    	                if (detalle.getIdDcto().equals(idDcto)) {
    	                	
    	                	Integer idCuentaAux = detalle.getIdSubcuenta()  != null ? detalle.getIdSubcuenta() : 0;

    	                	tblDctoDetalleRepo.ingresaDctoDetalle(idLocal, idTipoCpte, maxIdCpte, idCuentaAux, detalle.getIdCliente(), item, 0, 0, 0, 
    	                	 0, 0, 0, 0, 0, detalle.getFechaDcto(), 0, 0, 0, null, 0, detalle.getVrDebito(), detalle.getVrCredito(), detalle.getObservacion(), 0.0, detalle.getIdPeriodo());
    	                	System.out.println("ingresaDctoDetalle " + idDcto);
    	                    
    	                    item++;
    	                }
    	            }

    	        } else {
    	        	

    	        }
    	    }
        	
        	
        }
	   	    
	    
	    	    
	    

	       response.put("message", "OK");
	       response.put("nombreComprobante", NombreComprobante);
	       
	    
		    return ResponseEntity.ok(response);
	   
	    
	}

}
