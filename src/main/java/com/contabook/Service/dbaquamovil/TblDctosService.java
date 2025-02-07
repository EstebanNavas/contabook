package com.contabook.Service.dbaquamovil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Model.dbaquamovil.TblDctos;
import com.contabook.Projection.TblDctosDTO;
import com.contabook.Repository.dbaquamovil.TblDctosRepo;


@Service
public class TblDctosService {
	
	@Autowired
	TblDctosRepo tblDctosRepo;
	
	public List<TblDctosDTO> listaComprobanteDetallado(int idLocal, int idPeriodo, int idTipoOrden){
		
		List<TblDctosDTO> listaComprobante = tblDctosRepo.listaComprobanteDetallado(idLocal, idPeriodo, idTipoOrden);
		
		return listaComprobante;
		
	}
	
	
     public List<TblDctosDTO> listaComprobanteRecaudoDetallado(int idLocal, int idPeriodo){
		
		List<TblDctosDTO> recaudoDetallado = tblDctosRepo.listaComprobanteRecaudoDetallado(idLocal, idPeriodo);
		
		
		return recaudoDetallado;
		
	}

}
