package com.contabook.Service.DBMailMarketing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Model.DBMailMarketing.TblPucAux;
import com.contabook.Projection.TblPucAuxDTO;
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
	
	public List<TblPucAux> listaAuxiliaresXClase(int idLocal, int idClase){
				
		List<TblPucAux> listaAux = tblPucAuxRepo.listaAuxiliaresXClase(idLocal, idClase);
		
		return listaAux;
		
	}
	
	public List<TblPucAuxDTO> listaCuentasContables(int idLocal){
		
		List<TblPucAuxDTO> cuentasContables = tblPucAuxRepo.listaCuentasContables(idLocal);
		
		return cuentasContables;
	}
	
	public List<TblPucAuxDTO> listaCuentasContablesPorClase(int idLocal, int idClase){
		
		List<TblPucAuxDTO> cuentasContables = tblPucAuxRepo.listaCuentasContablesPorClase(idLocal, idClase);
		
		return cuentasContables;
	}

}
