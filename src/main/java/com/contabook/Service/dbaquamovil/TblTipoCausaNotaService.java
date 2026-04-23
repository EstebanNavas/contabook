package com.contabook.Service.dbaquamovil;

import org.springframework.stereotype.Service;

import com.contabook.Model.dbaquamovil.TblTipoCausaNota;
import com.contabook.Repository.dbaquamovil.TblTipoCausaNotaRepo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TblTipoCausaNotaService {

	@Autowired
	TblTipoCausaNotaRepo tblTipoCausaNotaRepo;
	
	public ArrayList<TblTipoCausaNota>  ObtenerTblTipoCausaNota(int idTipoTabla) {
		
		ArrayList<TblTipoCausaNota> tipoCausas = tblTipoCausaNotaRepo.ObtenerTblTipoCausaNota(idTipoTabla);

		
		return tipoCausas;
	}
	
	
	public ArrayList<TblTipoCausaNota> ObtenerTblTipoCausaNotaLecturas(int idTipoTabla){
		
		
		ArrayList<TblTipoCausaNota> CausaNotaLecturas = tblTipoCausaNotaRepo.ObtenerTblTipoCausaNotaLecturas(idTipoTabla);
		
		return CausaNotaLecturas;
		
	}
}
