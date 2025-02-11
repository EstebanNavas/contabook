package com.contabook.Model.DBMailMarketing;

import java.io.Serializable;

public class TblPucPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer idLocal;
	
	
	private Integer idCuenta;
	
	public TblPucPK() {
		super();
	}
	public TblPucPK(Integer idLocal,  Integer idCuenta) {
		super();
		this.idLocal = idLocal;
		this.idCuenta = idCuenta;

	}
	public Integer getIdLocal() {
		return idLocal;
	}

	public Integer getIdCuenta() {
		return idCuenta;
	}
	public void setIdLocal(Integer idLocal) {
		this.idLocal = idLocal;
	}

	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}

}
