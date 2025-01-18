package com.contabook.Model.DBMailMarketing;

import java.io.Serializable;

public class TblTipoCptePK implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idTipoCpte;
	
	private Integer idLocal;
	
	public TblTipoCptePK() {
		super();
	}
	public TblTipoCptePK(Integer idTipoCpte,  Integer idLocal) {
		super();
		this.idTipoCpte = idTipoCpte;
		this.idLocal = idLocal;
	}
	public Integer getIdTipoCpte() {
		return idTipoCpte;
	}

	public Integer getIdLocal() {
		return idLocal;
	}
	public void setIdTipoCpte(Integer idTipoCpte) {
		this.idTipoCpte = idTipoCpte;
	}

	public void setIdLocal(Integer idLocal) {
		this.idLocal = idLocal;
	}	
}
