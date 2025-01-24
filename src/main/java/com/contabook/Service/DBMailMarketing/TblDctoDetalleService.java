package com.contabook.Service.DBMailMarketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.Model.DBMailMarketing.TblDctoDetalle;
import com.contabook.Repository.DBMailMarketing.TblDctoDetalleRepo;

@Service
public class TblDctoDetalleService {
	
	@Autowired
	TblDctoDetalleRepo tblDctoDetalleRepo;

}
