package com.contabook.Service.dbaquamovil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Model.dbaquamovil.TblTerceros;
import com.contabook.Repository.dbaquamovil.TblTercerosRepo;

@Service
public class TblTercerosService {

	@Autowired
	TblTercerosRepo tblTercerosRepo;
	
	public List<TblTerceros> ListaTercerosSuscriptor(int idLocal){
		
		List<TblTerceros> listaTerceros = tblTercerosRepo.ListaTercerosSuscriptor(idLocal);
		
		return listaTerceros;
	}
	
	public Integer ObtenerIdTipoTercero(int idLocal, String idCliente) {
		
		Integer idTipoTercero = tblTercerosRepo.ObtenerIdTipoTercero(idLocal, idCliente);
		
		return idTipoTercero;
	}
}
