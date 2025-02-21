package com.contabook.Service.DBMailMarketing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Projection.TblPucDTO;
import com.contabook.Repository.DBMailMarketing.TblPucRepo;

@Service
public class TblPucService {
	
	@Autowired
	TblPucRepo tblPucRepo;
	
	public List<TblPuc> pucNivel1(int idLocal){
		
		List<TblPuc> listaNivel1 = tblPucRepo.pucNivel1(idLocal);
		
		return listaNivel1;
	}
	
	public List<TblPuc> pucNivel2(int idLocal, int idCuentaMenor, int idCuentaMayor, int idClase){
		
		List<TblPuc> listaNivel2 = tblPucRepo.pucNivel2(idLocal, idCuentaMenor, idCuentaMayor, idClase);
		
		return listaNivel2;
	}
	
	
	public List<TblPuc> pucNiveles(int idLocal, int idCuentaMenor, int idCuentaMayor, int idClase){
		
		List<TblPuc> listaNiveles = tblPucRepo.pucNiveles(idLocal, idCuentaMenor, idCuentaMayor, idClase);
		
		return listaNiveles;
		
	}
	
	
	public String obtenerNombreCuenta(int idLocal, int idCuenta, int idClase) {
		
		String nombreCuenta = tblPucRepo.obtenerNombreCuenta(idLocal, idCuenta, idClase);
		
		return nombreCuenta;
		
	}
	
	public List<TblPuc> listaSubCuentas( int idCuenta, int idLocal){
		
		List<TblPuc> listasubCuentas = tblPucRepo.listaSubCuentas(idCuenta, idLocal);
		
		return listasubCuentas;
		
	}
	
	public List<TblPuc> listaCuentas( int idCuenta, int idLocal){
		
		List<TblPuc> listaCuentas = tblPucRepo.listaCuentas(idCuenta, idLocal);
		
		return listaCuentas;
		
	}
	
	public List<TblPucDTO> RepEstadoSituacionFinanciera( int idLocal, int idPeriodo){
		
		List<TblPucDTO> reporteFinanciero = tblPucRepo.RepEstadoSituacionFinanciera(idLocal, idPeriodo);
		
		return reporteFinanciero;
	}
	
	
	public List<TblPucDTO> RepEstadoResultadoIntegral( int idLocal, int idPeriodo){
		
		List<TblPucDTO> reporteResultado = tblPucRepo.RepEstadoResultadoIntegral(idLocal, idPeriodo);
		
		return reporteResultado;
		
	}

}
