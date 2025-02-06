package com.contabook.Service.DBMailMarketing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Repository.DBMailMarketing.TblLocalesReporteRepo;
import com.contabook.Model.DBMailMarketing.TblLocalesReporte;

@Service
public class TblLocalesReporteService {
	
	@Autowired
	TblLocalesReporteRepo tblLocalesReporteRepo;
	
public String obtenerNombreReporte (int idLocal, int idReporte) {
		
		String nombreReporte = tblLocalesReporteRepo.nombreReporte(idLocal, idReporte);
        return nombreReporte;
	}
	
	
	public List<TblLocalesReporte> listaUnFCH(int idLocal, int idReporte){
		
		List<TblLocalesReporte> reporte = tblLocalesReporteRepo.listaUnFCH(idLocal, idReporte);
		
		return reporte;
		
	}

}
