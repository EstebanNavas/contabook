package com.contabook.Projection;

public interface TblDctosDTO {
	
	Integer getIdTipoNegocio();
	Integer getIdDcto();
	String getFechaDcto();
	String getSiglaMoneda();
	Integer getIdSubcuenta();
	String getIdCliente();
	Double getVrDebito();
	Double getVrCredito();
	String getObservacion();
	String getTextoVacio();
	Integer getIdRuta();
	Integer getIdTipoOrden();
	Integer getIdPeriodo();
	String getFechaDctoSiigo();
	String getCc_nit();
	Integer getIdDctoNitCC();

}
