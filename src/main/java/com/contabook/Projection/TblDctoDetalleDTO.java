package com.contabook.Projection;

public interface TblDctoDetalleDTO {
	
	Integer getIdTipoCpte();
	Integer getIdCpte();
	Integer getIdCuentaAux();
	String getIdCliente();
	Integer getItem();
	String getFechaVencimiento();
	String getDescripcion();
	Double getVrDebito();
	Double getVrCredito();
	String getObservacion();
	String getNombreCuenta();
	
	String getNombreUsuario();
}
