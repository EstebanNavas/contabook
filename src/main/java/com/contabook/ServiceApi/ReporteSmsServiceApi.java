package com.contabook.ServiceApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.contabook.Model.Reportes.ReportesDTO;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

public interface ReporteSmsServiceApi {
	
	
	ReportesDTO Reportes(Map<String, Object> params, JRDataSource dataSource, String formato, String xFileNameReporte, String xPathReport)throws JRException, IOException, SQLException;
	
	
	ReportesDTO ReporteEnCarpeta(Map<String, Object> params, JRDataSource dataSource, String formato, String xFileNameReporte, String xPathReport, String xPathPDF, String xPathXML,  int idDcto)throws JRException, IOException, SQLException;

}
