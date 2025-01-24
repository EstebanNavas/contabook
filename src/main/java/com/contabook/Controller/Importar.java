package com.contabook.Controller;

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

import com.contabook.Model.dbaquamovil.Ctrlusuarios;
import com.contabook.Projection.TblDctosDTO;
import com.contabook.Repository.DBMailMarketing.TblDctoDetalleRepo;
import com.contabook.Repository.DBMailMarketing.TblDctoRepo;
import com.contabook.Service.DBMailMarketing.TblDctoService;
import com.contabook.Service.DBMailMarketing.TblDctosPeriodoService;
import com.contabook.Service.dbaquamovil.TblDctosService;
import com.contabook.Model.DBMailMarketing.TblDctosPeriodo;

@Controller
public class Importar {
	
	@Autowired
	TblDctosService tblDctosService;
	
	@Autowired
	TblDctoService tblDctoService;
	
	@Autowired
	TblDctoRepo tblDctoRepo;
	
	@Autowired
	TblDctoDetalleRepo tblDctoDetalleRepo;
	
	@Autowired
	TblDctosPeriodoService tblDctosPeriodoService;
	
	@GetMapping("/Importar")
	public String Importar(HttpServletRequest request,Model model) {
		
		Class tipoObjeto = this.getClass();					
        String nombreClase = tipoObjeto.getName();		
        System.out.println("CONTROLLER " + nombreClase);
		
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
		
  

			
			return "Importar/Importar";


	}
	
	
	@PostMapping("/ImportarContabilidad-Post")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ImportarContabilidad(@RequestBody Map<String, Object> requestBody, HttpServletRequest request,Model model) {
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
        
	   	    
	    
	    List<TblDctosDTO> listaComprobante = tblDctosService.listaComprobanteDetallado(idLocal, idPeriodo);
	    System.out.println("listaComprobante es " + listaComprobante);
	    
	    Integer idTipoOrden = 0;
	    Integer idDcto = 0;
	    Integer idDctoBase = 0;
	    
	    for (TblDctosDTO cpte : listaComprobante) {
	        idTipoOrden = cpte.getIdTipoOrden();
	        idDcto = cpte.getIdDcto();

	        // Verifica si el Dcto ya existe
	        Boolean existe = tblDctoService.ExisteDcto(idLocal, idTipoOrden, idDcto);
	        System.out.println("existe Dcto es " + existe);

	        if (!existe) {
	            System.out.println("El documento no existe " + idDcto);

	            Integer maxIdCpte = tblDctoService.MaximoiIdCpte(idLocal) + 1;

	            tblDctoRepo.ingresaDcto(idLocal, cpte.getIdTipoNegocio(), maxIdCpte, idTipoOrden, idDcto, cpte.getFechaDcto(), cpte.getSiglaMoneda(), 0, cpte.getIdPeriodo());
	            System.out.println("ingresaDcto " + idDcto);
	            
	            int item = 1;

	            // Ciclo para ingresar detalle
	            for (TblDctosDTO detalle : listaComprobante) {
	                if (detalle.getIdDcto().equals(idDcto)) {
	                	
	                	Integer idCuentaAux = detalle.getIdSubcuenta()  != null ? detalle.getIdSubcuenta() : 0;

	                	tblDctoDetalleRepo.ingresaDctoDetalle(idLocal, detalle.getIdTipoNegocio(), maxIdCpte, idCuentaAux, detalle.getIdCliente(), item, 0, 0, 0, 
	                	 0, 0, 0, 0, 0, detalle.getFechaDcto(), 0, 0, 0, null, 0, detalle.getVrDebito(), detalle.getVrCredito(), detalle.getObservacion(), 0.0, detalle.getIdPeriodo());
	                	System.out.println("ingresaDctoDetalle " + idDcto);
	                    
	                    item++;
	                }
	            }

	        } else {
	        	

	        }
	    }
	    
	    
	    

	       response.put("message", "OK");    
	    
		    return ResponseEntity.ok(response);
	   
	    
	}

}
