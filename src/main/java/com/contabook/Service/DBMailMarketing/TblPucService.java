package com.contabook.Service.DBMailMarketing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Repository.DBMailMarketing.TblPucRepo;

@Service
public class TblPucService {
	
	@Autowired
	TblPucRepo tblPucRepo;
	
	public List<TblPuc> pucNivel1(){
		
		List<TblPuc> listaNivel1 = tblPucRepo.pucNivel1();
		
		return listaNivel1;
	}
	
	public List<TblPuc> pucNivel2(int idCuentaMenor, int idCuentaMayor, int idClase){
		
		List<TblPuc> listaNivel2 = tblPucRepo.pucNivel2(idCuentaMenor, idCuentaMayor, idClase);
		
		return listaNivel2;
	}
	
	
	public List<TblPuc> pucNiveles(int idCuentaMenor, int idCuentaMayor, int idClase){
		
		List<TblPuc> listaNiveles = tblPucRepo.pucNiveles(idCuentaMenor, idCuentaMayor, idClase);
		
		return listaNiveles;
		
	}
	
	
	public String obtenerNombreCuenta(int idCuenta, int idClase) {
		
		String nombreCuenta = tblPucRepo.obtenerNombreCuenta(idCuenta, idClase);
		
		return nombreCuenta;
		
	}

}
