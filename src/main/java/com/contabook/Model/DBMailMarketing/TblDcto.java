package com.contabook.Model.DBMailMarketing;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "tblDcto")
@IdClass(TblDctoPK.class)
public class TblDcto {
	
	
	@Id
	@Column(name= "idLocal")
	private Integer idLocal;
	
	@Id
	@Column(name= "idTipoCpte")
	private Integer idTipoCpte;
	
	@Id
	@Column(name= "idCpte")
	private Integer idCpte;
	
	@Column(name= "idTipoOrden")
	private Integer idTipoOrden;
	
	@Column(name= "idDcto")
	private Integer idDcto;
	
	@Column(name = "fechaDcto")
	private Timestamp  fechaDcto;
	
	@Column(name = "idMoneda")
	private String  idMoneda;
	
	@Column(name = "idTasa")
	private Double  idTasa;
	
	@Column(name = "idPeriodo")
	private Integer  idPeriodo;

	public Integer getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(Integer idLocal) {
		this.idLocal = idLocal;
	}

	public Integer getIdTipoCpte() {
		return idTipoCpte;
	}

	public void setIdTipoCpte(Integer idTipoCpte) {
		this.idTipoCpte = idTipoCpte;
	}

	public Integer getIdCpte() {
		return idCpte;
	}

	public void setIdCpte(Integer idCpte) {
		this.idCpte = idCpte;
	}

	public Integer getIdTipoOrden() {
		return idTipoOrden;
	}

	public void setIdTipoOrden(Integer idTipoOrden) {
		this.idTipoOrden = idTipoOrden;
	}

	public Integer getIdDcto() {
		return idDcto;
	}

	public void setIdDcto(Integer idDcto) {
		this.idDcto = idDcto;
	}

	public Timestamp getFechaDcto() {
		return fechaDcto;
	}

	public void setFechaDcto(Timestamp fechaDcto) {
		this.fechaDcto = fechaDcto;
	}

	public String getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(String idMoneda) {
		this.idMoneda = idMoneda;
	}

	public Double getIdTasa() {
		return idTasa;
	}

	public void setIdTasa(Double idTasa) {
		this.idTasa = idTasa;
	}

	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}
	
	

}
