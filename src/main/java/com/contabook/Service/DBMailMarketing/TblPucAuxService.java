package com.contabook.Service.DBMailMarketing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Model.DBMailMarketing.TblPucAux;
import com.contabook.Repository.DBMailMarketing.TblPucAuxRepo;

@Service
public class TblPucAuxService {
	
	@Autowired
	TblPucAuxRepo tblPucAuxRepo;
	
	public List<TblPucAux> listaAuxiliares(int idCuentaAux, int idLocal){
		
		List<TblPucAux> auxiliares = tblPucAuxRepo.listaAuxiliares(idCuentaAux, idLocal);
		
		return auxiliares;
	}
	
	public String obtenerNombreCuentaAux(int idLocal, int idCuentaAux) {
		
		String nombreCuenta = tblPucAuxRepo.obtenerNombreCuentaAux(idLocal, idCuentaAux);
		
		return nombreCuenta;
	}
	
	public List<TblPucAux> listaTodosAuxiliares(int idLocal){
		
		List<TblPucAux> todosAuxiliares = tblPucAuxRepo.listaTodosAuxiliares(idLocal);
		
		return todosAuxiliares;
	}

}
