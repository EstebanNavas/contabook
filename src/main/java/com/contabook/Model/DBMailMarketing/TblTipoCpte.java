package com.contabook.Model.DBMailMarketing;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="tblTipoCpte")
@IdClass(TblTipoCptePK.class)
public class TblTipoCpte {
	
	@Id
	@Column(name = "idTipoCpte")
	private Integer idTipoCpte;
	
	@Id
	@Column(name = "idLocal")
	private Integer idLocal;
	
	@Column(name = "nombreCmpte")
	private String nombreCmpte;
	
	@Column(name = "estado")
	private Integer estado;
	
	@Column(name = "signo")
	private Integer signo;
	
	@Column(name = "idSeq")
	private Integer idSeq;
	
	@Column(name = "idAlcance")
	private Integer idAlcance;

	public Integer getIdTipoCpte() {
		return idTipoCpte;
	}

	public void setIdTipoCpte(Integer idTipoCpte) {
		this.idTipoCpte = idTipoCpte;
	}

	public String getNombreCmpte() {
		return nombreCmpte;
	}

	public void setNombreCmpte(String nombreCmpte) {
		this.nombreCmpte = nombreCmpte;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Integer getSigno() {
		return signo;
	}

	public void setSigno(Integer signo) {
		this.signo = signo;
	}

	public Integer getIdSeq() {
		return idSeq;
	}

	public void setIdSeq(Integer idSeq) {
		this.idSeq = idSeq;
	}

	public Integer getIdAlcance() {
		return idAlcance;
	}

	public void setIdAlcance(Integer idAlcance) {
		this.idAlcance = idAlcance;
	}
	
	

}
