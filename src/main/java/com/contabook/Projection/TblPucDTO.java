package com.contabook.Projection;

public interface TblPucDTO {
	
	Integer getIdLocal();
	Integer getIdCuentaAux();
	String getNombreAuxiliar();
	String getNombreSubCuenta();
	Integer getIdSubCuenta();
	String getNombreCuenta();
	Integer getIdCuenta();
	String getNombreGrupo();
	Integer getIdGrupo();
	String getNombreClase();
	Integer getIdClase();
	Double getVrDebito();
	Double getVrCredito();
	
	Double getTotalClaseVrDebito();
	Double getTotalClaseVrCredito();
	Double getTotalGrupoVrDebito();
	Double getTotalGrupoVrCredito();
	Double getTotalCuentaVrDebito();
	Double getTotalCuentaVrCredito();
	Double getTotalSubCuentaVrDebito();
	Double getTotalSubCuentaVrCredito();
	
	


}
