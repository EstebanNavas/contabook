package com.contabook.Service.dbaquamovil;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Repository.dbaquamovil.TblLocalesRepo;
import com.contabook.Model.dbaquamovil.TblLocales;

@Service
public class TblLocalesService {

	@Autowired
	TblLocalesRepo tblLocalesRepo;
	
	// EXTRAER EL ID DEL LOCAL Y SU RAZÓN SOCIAL 
	  public TblLocales consultarLocal(Integer idLocal) {
		  
		   // Buscammos el Objeto TblLocales por su id
	        Optional<TblLocales> localOptional = tblLocalesRepo.findByIdLocal(idLocal);
	        
	        if (localOptional.isPresent()) {
	            TblLocales locales = localOptional.get();
	            return locales;
	        } else {
	            System.out.println("No se encontró ningún local con el idLocal: " + idLocal);
	            return null;
	        }

	  }
	  
	  
	  // Método para obtener la ruta de pathReport por idLocal
	    public String obtenerRutaReportePorIdLocal(int idLocal) {
	        String rutaReporte = tblLocalesRepo.rutaReporte(idLocal);
	        System.out.println("La ruta de pathReport es: " + rutaReporte);
	        return rutaReporte;
	    }
	    
	    public String ObtenerRazonSocial(int idLocal) {
	    	
	    	String RazonSocial = tblLocalesRepo.ObtenerRazonSocial(idLocal);
	    	
	    	return RazonSocial;
	    }
	    
	    
	    public String ObtenerNit(int idLocal) {
	    	
	    	String nit = tblLocalesRepo.ObtenerNit(idLocal);
	    	
	    	return nit;
	    }
	    
	    
	    public String ObtenerDireccion(int idLocal) {
	    	
	    	String direccion = tblLocalesRepo.ObtenerDireccion(idLocal);
	    	
	    	return direccion;
	    }
	    
	    public String ObtenerCiudad(int idLocal) {
	    	
	    	String ciudad = tblLocalesRepo.ObtenerCiudad(idLocal);
	    	
	    	return ciudad;
	    }
	    
	    public String ObtenerToken(int idLocal) {
	    	
	    	String token = tblLocalesRepo.ObtenerToken(idLocal);
	    	
	    	return token;
	    }
	    
	    public String ObtenerPrefijo(int idLocal) {
	    	
	    	String prefijo = tblLocalesRepo.ObtenerPrefijo(idLocal);
	    	
	    	return prefijo;
	    }
	    
	    public String ObtenerIdResolucion(int idLocal) {
	    	
	    	String idResolucion = tblLocalesRepo.ObtenerIdResolucion(idLocal);
	    	
	    	return idResolucion;
	    }
	    
	    public Integer  ObtenerPeriodoFactura(int idLocal){
	    	
	    	Integer periodo = tblLocalesRepo.ObtenerPeriodoFactura(idLocal);
	    	
	    	return periodo;
	    }
	    
	    public List<TblLocales>  ObtenerLocal(int idLocal){
	    	
	    	List<TblLocales> Local = tblLocalesRepo.ObtenerLocal(idLocal);
	    	
	    	return Local;
	    }
	  
	    
	    public Integer ObtenerIdApi(int idLocal) {
	    	
	    	Integer idApi = tblLocalesRepo.ObtenerIdApi(idLocal);
	    	
	    	return idApi;
	    }	
}
