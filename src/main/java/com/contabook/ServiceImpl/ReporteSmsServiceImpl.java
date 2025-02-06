package com.contabook.ServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contabook.ServiceApi.ReporteSmsServiceApi;
import com.contabook.Model.Reportes.ReportesDTO;
import com.contabook.enums.TipoReporteEnum;
import com.contabook.commons.JasperReportManager;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

@Service
public class ReporteSmsServiceImpl implements ReporteSmsServiceApi {
	
	@Autowired
	JasperReportManager reportManager;
	
	@Override
	public ReportesDTO Reportes(Map<String, Object> params, JRDataSource dataSource, String formato, 
			String xFileNameReporte, String xPathReport) throws JRException, IOException, SQLException {
	
			int idLocal = (int) params.get("idLocal"); // Obtén el valor de idLocal del mapa params
	    
	    System.out.println("Valor de idLocal en Reportes: " + idLocal);
	
	    
		// Obtenemos el nombre base del archivo sin la extensión.
		//String fileName = "VtasRepAllVentaPeriodo"; // Se obtiene de la DB
		
		//Se determina la extensión del archivo en función del parámetro "tipo" seleccionado PDF o EXCEL
		String extension = formato.toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name()) ? ".xlsx": ".pdf";
		
		// Se crea una instancia de ReporteSmsDTO para almacenar la información del reporte
		ReportesDTO dto = new ReportesDTO();
		
		// Se le setea el nombre del archivo a la variable dto
		dto.setFileName(xFileNameReporte + extension);
		
		// Se llama al metdo export de la clase JasperReportManager para generar el archivo
		ByteArrayOutputStream stream = reportManager.exportReport(xPathReport, xFileNameReporte, formato, params, dataSource);
		
	
	    
		//Se convierte el contenido de stream en un arrya de bytes
		byte[] bs = stream.toByteArray();
		
		// Se crea un nuevo ByteArrayInputStream utilizando el arreglo de bytes bs
		dto.setStream(new ByteArrayInputStream(bs));
		
		// Se establece la longitud del archivo en bytes
		dto.setLength(bs.length);
		
		return dto;
	}

	@Override
	public ReportesDTO ReporteEnCarpeta(Map<String, Object> params, JRDataSource dataSource, String formato, 
			String xFileNameReporte, String xPathReport, String xPathPDF, String xPathXML, int idDcto) throws JRException, IOException, SQLException {
	
			int idLocal = (int) params.get("idLocal"); // Obtén el valor de idLocal del mapa params
	    
	    System.out.println("Valor de idLocal en ReporteEnCarpeta: " + idLocal);
		
		//Se determina la extensión del archivo en función del parámetro "tipo" seleccionado PDF o EXCEL
		String extension = formato.toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name()) ? ".xlsx": ".pdf";
		
		// Se crea una instancia de ReporteSmsDTO para almacenar la información del reporte
		ReportesDTO dto = new ReportesDTO();
		
		// Se le setea el nombre del archivo a la variable dto
		dto.setFileName(xFileNameReporte + extension);
		
		// Se llama al metdo export de la clase JasperReportManager para generar el archivo
		ByteArrayOutputStream stream = reportManager.exportReportCarpeta(xPathReport, xFileNameReporte, formato, params, dataSource, xPathPDF, xPathXML,  idDcto);
		
	
	    
		//Se convierte el contenido de stream en un arrya de bytes
		byte[] bs = stream.toByteArray();
		
		// Se crea un nuevo ByteArrayInputStream utilizando el arreglo de bytes bs
		dto.setStream(new ByteArrayInputStream(bs));
		
		// Se establece la longitud del archivo en bytes
		dto.setLength(bs.length);
		
		return dto;
	}
}
