package com.contabook.Service.DBMailMarketing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Projection.TblDctoDTO;
import com.contabook.Repository.DBMailMarketing.TblDctoRepo;

@Service
public class TblDctoService {
	
	@Autowired
	TblDctoRepo tblDctoRepo;
	
	public Boolean ExisteDcto(int idLocal, int idTipoOrden, int idCpte) {
		
		Boolean existe = tblDctoRepo.ExisteDcto(idLocal, idTipoOrden, idCpte);		
		if(existe == null) {			
			existe = false;
		}
		
		return existe;
	}
	
	
	public Boolean ExisteDctoCpte(int idLocal, int idTipoOrden, int idDcto, int idTipoCpte) {
		
		Boolean existe = tblDctoRepo.ExisteDctoCpte(idLocal, idTipoOrden, idDcto, idTipoCpte);		
		if(existe == null) {			
			existe = false;
		}
		
		return existe;
		
		
	}
	
	
	public Integer MaximoiIdCpte(int idLocal) {
		
		Integer MaxIdCpte = tblDctoRepo.MaximoiIdCpte(idLocal);		
		if(MaxIdCpte == null) {
			
			MaxIdCpte = 0;
			return MaxIdCpte;
		}
		
		return MaxIdCpte;
		
	}
	
	public List<TblDctoDTO> listaComprobantes(int idLocal, int idTipoCpte, int idPeriodo){
		
		List<TblDctoDTO> listaComprobantes = tblDctoRepo.listaComprobantes(idLocal, idTipoCpte, idPeriodo);
		
		return listaComprobantes;
		
	}
	
	public List<TblDctoDTO> ObtenerIdCpte(int idLocal, int idCpte) {
		
		List<TblDctoDTO> comprobante = tblDctoRepo.ObtenerIdCpte(idLocal, idCpte);
		
		return comprobante;
		
	}

}
