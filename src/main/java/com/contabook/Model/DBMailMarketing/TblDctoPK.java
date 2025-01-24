package com.contabook.Model.DBMailMarketing;

import java.io.Serializable;

public class TblDctoPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer idLocal;
	
	
	private Integer idTipoCpte;
	
	private Integer idCpte;
	
	public TblDctoPK() {
		super();
	}
	public TblDctoPK(Integer idlocal,  Integer idTipoCpte, Integer idCpte) {
		super();
		this.idLocal = idlocal;
		this.idTipoCpte = idTipoCpte;
		this.idCpte = idCpte;

	}
	public Integer getIdLocal() {
		return idLocal;
	}

	public Integer getIdTipoCpte() {
		return idTipoCpte;
	}
	
	public Integer getIdCpte() {
		return idCpte;
	}
	
	public void setIdLocal(Integer idLocal) {
		this.idLocal = idLocal;
	}

	public void setIdTipoCpte(Integer idTipoCpte) {
		this.idTipoCpte = idTipoCpte;
	}
	
	public void setIdCpte(Integer idCpte) {
		this.idCpte = idCpte;
	}

}
