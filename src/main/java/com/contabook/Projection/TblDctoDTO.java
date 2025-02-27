package com.contabook.Projection;

public interface TblDctoDTO {
	
	Integer getIdTipoCpte();
	Integer getIdCpte();
	Integer getIdTipoOrden();
	Integer getIdDcto();	
	String getFechaDcto(); 
	Integer getIdPeriodo();
	String getNombreTipoComprobante();
	String getNombreUsuario();
	Double getVrTotal();
	
	String getIdCliente();
	Integer getIdCuentaAux();
	String getNombreCuenta();
	String getNombreTercero();

}
