package com.contabook.Service.DBMailMarketing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Model.DBMailMarketing.TblTipoCpte;
import com.contabook.Repository.DBMailMarketing.TblTipoCpteRepo;

@Service
public class TblTipoCpteService {

	@Autowired
	TblTipoCpteRepo tblTipoCpteRepo;
	
	
	public List<TblTipoCpte> ListaComprobantes(){
		
		List<TblTipoCpte> listaComprobantes = tblTipoCpteRepo.ListaComprobantes();
		
		return listaComprobantes;
		
	}
	
	public Integer MaximoIdTipoCpte(int idLocal) {
		
		Integer MaxIdTipoCpte = tblTipoCpteRepo.MaximoIdTipoCpte(idLocal);
		
		if(MaxIdTipoCpte == null) {
			
			MaxIdTipoCpte = 0;
			return MaxIdTipoCpte;
		}
		
		return MaxIdTipoCpte;
	}
	
	
	public List<TblTipoCpte> obtenerCpteXId(int idLocal, int idTipoCpte){
		
		List<TblTipoCpte>  comprobante = tblTipoCpteRepo.obtenerCpteXId(idLocal, idTipoCpte);
		
		return comprobante;
		
	}
	
	public Integer obtenerIdTipoOrden(int idTipoCpte) {
		
		Integer idTipoOrden = tblTipoCpteRepo.obtenerIdTipoOrden(idTipoCpte);
		
		return idTipoOrden;
	}
	
	public String obtenerNombreComprobante(int idTipoCpte) {
		
		String NombreComprobante = tblTipoCpteRepo.obtenerNombreComprobante(idTipoCpte);
		
		return NombreComprobante;
		
	}
	
}
