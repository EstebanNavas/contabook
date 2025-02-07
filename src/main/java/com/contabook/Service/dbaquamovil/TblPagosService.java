package com.contabook.Service.dbaquamovil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.contabook.Repository.dbaquamovil.TblPagosRepo;
import com.contabook.Projection.TblPagosDTO;

@Service
public class TblPagosService {
	
	@Autowired
	TblPagosRepo tblPagosRepo;
	
	
     public List<TblPagosDTO> listaRepRecaudoPeriodo(int idLocal, int idPeriodo){
		
		List<TblPagosDTO> lista = tblPagosRepo.listaRepRecaudoPeriodo(idLocal, idPeriodo);
		
		return lista;
	}

}
