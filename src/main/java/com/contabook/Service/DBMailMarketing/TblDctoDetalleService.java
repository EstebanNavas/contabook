package com.contabook.Service.DBMailMarketing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Model.DBMailMarketing.TblDctoDetalle;
import com.contabook.Projection.TblDctoDetalleDTO;
import com.contabook.Repository.DBMailMarketing.TblDctoDetalleRepo;

@Service
public class TblDctoDetalleService {
	
	@Autowired
	TblDctoDetalleRepo tblDctoDetalleRepo;
	
	public List<TblDctoDetalleDTO> comprobanteContableDetalle(int idLocal, int idTipoCpte){
		
		List<TblDctoDetalleDTO> comporbanteContable = tblDctoDetalleRepo.comprobanteContableDetalle(idLocal, idTipoCpte);
		
		return comporbanteContable;
		
	}

}
