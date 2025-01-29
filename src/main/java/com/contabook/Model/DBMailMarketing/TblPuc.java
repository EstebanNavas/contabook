package com.contabook.Model.DBMailMarketing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "tblPuc")
@IdClass(TblPucPK.class)
public class TblPuc {
	
	@Id
	@Column(name= "idClase")
	private Integer idClase;
	
	@Id
	@Column(name= "idCuenta")
	private Integer idCuenta;
	
	@Column(name= "nombreCuenta")
	private String nombreCuenta;

	public Integer getIdClase() {
		return idClase;
	}

	public void setIdClase(Integer idClase) {
		this.idClase = idClase;
	}

	public Integer getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getNombreCuenta() {
		return nombreCuenta;
	}

	public void setNombreCuenta(String nombreCuenta) {
		this.nombreCuenta = nombreCuenta;
	}
	
	

}
