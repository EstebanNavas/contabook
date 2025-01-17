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
	
}
