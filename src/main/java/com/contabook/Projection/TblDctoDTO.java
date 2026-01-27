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
	
	Integer getItem();
	Double getVrDebito();
	Double getVrCredito();
	
	Double getVrTotalAnterior();
	Integer getIdLocal();
	
	Integer getIdClase();
	String getNombreClase();
	Integer getIdGrupo();
	String getNombreGrupo();
	Integer getIdCuenta();
	Integer getIdSubCuenta();
	String getNombreSubCuenta();
	String getNombreAuxiliar();
	
	Double getVrTotalDebitoAnterior();
	Double getVrTotalCreditoAnterior();
	Double getVrTotalDebitoActual();
	Double getVrTotalCreditoActual();
	Double getvrSaldoDebito();
	Double getVrSaldoCredito();
	
	String getDetalle();
	String getTipoComprobante();
	
	Double getTotalSubCuentaVrCredito();
	Double getTotalSubCuentaVrDebito();
	Double getTotalCuentaVrCredito();
	Double getTotalCuentaVrDebito();
	Double getTotalGrupoVrCredito();
	Double getTotalGrupoVrDebito();
	Double getTotalClaseVrCredito();
	Double getTotalClaseVrDebito();
	
	Double getVrTotalDebito();
	Double getVrTotalCredito();
	Double getVrSaldoFinal();

	String getCC_Nit();

}
