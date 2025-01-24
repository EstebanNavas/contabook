package com.contabook.Model.DBMailMarketing;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "tblDctoDetalle")
@IdClass(TblDctoDetallePK.class)
public class TblDctoDetalle {
	
	@Id
	@Column(name= "idLocal")
	private Integer idLocal;
	
	@Id
	@Column(name= "idTipoCpte")
	private Integer idTipoCpte;
	
	@Id
	@Column(name= "idCpte")
	private Integer idCpte;
	
	@Column(name= "idCuentaAux")
	private Integer idCuentaAux;
	
	@Column(name= "idCliente")
	private String idCliente;
	
	@Column(name= "item")
	private Integer item;
	
	@Column(name= "sucursal")
	private Integer sucursal;
	
	@Column(name= "codProducto")
	private Integer codProducto;
	
	@Column(name= "codBodega")
	private Integer codBodega;
	
	@Column(name= "accion")
	private Integer accion;
	
	@Column(name= "cantProducto")
	private Integer cantProducto;
	
	@Column(name= "prefijo")
	private Integer prefijo;
	
	@Column(name= "consecutivo")
	private Integer consecutivo;
	
	@Column(name= "numeroCuota")
	private Integer numeroCuota;

	@Column(name = "fechaVencimiento")
	private Timestamp  fechaVencimiento;
	
	@Column(name = "codImpuesto")
	private Integer  codImpuesto;
	
	@Column(name = "codGrupoActivoFijo")
	private Integer  codGrupoActivoFijo;
	
	@Column(name = "codActivoFijo")
	private Integer  codActivoFijo;
	
	@Column(name = "descripcion")
	private String  descripcion;
	
	@Column(name = "codCentroSubCentro")
	private Integer  codCentroSubCentro;
	
	@Column(name = "vrDebito")
	private Double  vrDebito;
	
	@Column(name = "vrCredito")
	private Double  vrCredito;
	
	@Column(name = "observacion")
	private String  observacion;
	
	@Column(name = "baseGravable")
	private Double  baseGravable;
	
	@Column(name = "mesCierre")
	private Integer  mesCierre;

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

	public Integer getIdCuentaAux() {
		return idCuentaAux;
	}

	public void setIdCuentaAux(Integer idCuentaAux) {
		this.idCuentaAux = idCuentaAux;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Integer getSucursal() {
		return sucursal;
	}

	public void setSucursal(Integer sucursal) {
		this.sucursal = sucursal;
	}

	public Integer getCodProducto() {
		return codProducto;
	}

	public void setCodProducto(Integer codProducto) {
		this.codProducto = codProducto;
	}

	public Integer getCodBodega() {
		return codBodega;
	}

	public void setCodBodega(Integer codBodega) {
		this.codBodega = codBodega;
	}

	public Integer getAccion() {
		return accion;
	}

	public void setAccion(Integer accion) {
		this.accion = accion;
	}

	public Integer getCantProducto() {
		return cantProducto;
	}

	public void setCantProducto(Integer cantProducto) {
		this.cantProducto = cantProducto;
	}

	public Integer getPrefijo() {
		return prefijo;
	}

	public void setPrefijo(Integer prefijo) {
		this.prefijo = prefijo;
	}

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Integer getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public Timestamp getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Timestamp fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Integer getCodImpuesto() {
		return codImpuesto;
	}

	public void setCodImpuesto(Integer codImpuesto) {
		this.codImpuesto = codImpuesto;
	}

	public Integer getCodGrupoActivoFijo() {
		return codGrupoActivoFijo;
	}

	public void setCodGrupoActivoFijo(Integer codGrupoActivoFijo) {
		this.codGrupoActivoFijo = codGrupoActivoFijo;
	}

	public Integer getCodActivoFijo() {
		return codActivoFijo;
	}

	public void setCodActivoFijo(Integer codActivoFijo) {
		this.codActivoFijo = codActivoFijo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getCodCentroSubCentro() {
		return codCentroSubCentro;
	}

	public void setCodCentroSubCentro(Integer codCentroSubCentro) {
		this.codCentroSubCentro = codCentroSubCentro;
	}

	public Double getVrDebito() {
		return vrDebito;
	}

	public void setVrDebito(Double vrDebito) {
		this.vrDebito = vrDebito;
	}

	public Double getVrCredito() {
		return vrCredito;
	}

	public void setVrCredito(Double vrCredito) {
		this.vrCredito = vrCredito;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Double getBaseGravable() {
		return baseGravable;
	}

	public void setBaseGravable(Double baseGravable) {
		this.baseGravable = baseGravable;
	}

	public Integer getMesCierre() {
		return mesCierre;
	}

	public void setMesCierre(Integer mesCierre) {
		this.mesCierre = mesCierre;
	}
	
	

}
