package com.contabook.Model.DBMailMarketing;

import java.io.Serializable;

public class TblPucPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer idClase;
	
	
	private Integer idCuenta;
	
	public TblPucPK() {
		super();
	}
	public TblPucPK(Integer idClase,  Integer idCuenta) {
		super();
		this.idClase = idClase;
		this.idCuenta = idCuenta;

	}
	public Integer getIdClase() {
		return idClase;
	}

	public Integer getIdCuenta() {
		return idCuenta;
	}
	public void setIdClase(Integer idClase) {
		this.idClase = idClase;
	}

	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}

}
