package com.contabook.Repository.DBMailMarketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.contabook.Model.DBMailMarketing.TblDctoDetalle;
import com.contabook.Projection.TblDctoDetalleDTO;

@Repository
public interface TblDctoDetalleRepo extends JpaRepository<TblDctoDetalle, Integer> {
	
	@Modifying
	  @Transactional
	  @Query(value = "INSERT INTO tblDctoDetalle (idLocal,    "
           + "                      idTipoCpte,               "
           + "                      idCpte,                   "
           + "                      idCuentaAux,              "
           + "                      idCliente,                "
           + "                      item,                     "
           + "                      sucursal,                 "
           + "                      codProducto,              "
           + "                      codBodega,                "
           + "                      accion,                   "
           + "                      cantProducto,              "
           + "                      prefijo,                  "
           + "                      consecutivo,              "
           + "                      numeroCuota,              "
           + "                      fechaVencimiento,         "
           + "                      codImpuesto,              "
           + "                      codGrupoActivoFijo,       "
           + "                      codActivoFijo,            "
           + "                      descripcion,              "
           + "                      codCentroSubCentro,       "
           + "                      vrDebito,                 "
           + "                      vrCredito,                "
           + "                      observacion,              "
           + "                      baseGravable,             "
           + "                      mesCierre)                "
           + "VALUES ( ?1,"
           + "?2,"
           + "?3,"
           + "?4,"
           + "?5,"
           + "?6,"
           + "?7,"
           + "?8,"
           + "?9,"
           + "?10,"
           + "?11,"
           + "?12,"
           + "?13,"
           + "?14,"
           + "?15,"
           + "?16,"
           + "?17,"
           + "?18,"
           + "?19,"
           + "?20,"
           + "?21,"
           + "?22,"
           + "?23,"
           + "?24,"
           + "?25)", nativeQuery = true)
	  public void ingresaDctoDetalle(int idLocal, int idTipoCpte, int idCpte, int idCuentaAux, String idCliente, int item, int sucursal, int codProducto, int codBodega, 
			  int accion, int cantProducto, int prefijo, int consecutivo, int numeroCuota, String fechaVencimiento, int codImpuesto, int codGrupoActivoFijo, int codActivoFijo,
			  String descripcion, int codCentroSubCentro, Double vrDebito, Double vrCredito, String observacion, Double baseGravable, int mesCierre );
	
	
	
	
	@Modifying
	  @Transactional
	  @Query(value = "                  SELECT tblDctoDetalle.idTipoCpte                                              "                                                                                                  
		        + "            	        ,tblDctoDetalle.idCpte                                                        "                                 
		        + "            	        ,tblPucAux.idCuentaAux                                                        "                  
		        + "            	        ,tblDctoDetalle.idCliente                                                     "                     
		        + "            	        ,item                                                                         "                  
		        + "            	        ,fechaVencimiento                                                             "                                                        
		        + "            	        ,vrDebito                                                                     "                  
		        + "            	        ,vrCredito                                                                    "                  
		        + "            	        ,MAX(tercero.nombreTercero ) AS nombreUsuario                                 "          
				+ "          			,CONVERT(VARCHAR, tblDcto.fechaDcto, 23) AS fechaDcto                         "          
				+ "          			,tblDctoDetalle.observacion                                                   "          
				+ "          			,tblTipoCpte.nombreCmpte AS nombreTipoComprobante                             "          
		        + "            	  	   ,tblPucAux.nombreCuenta                                                        "
				+ "                    ,MAX(tblDcto.idPeriodo) AS idPeriodo                                           "
		        + "            	     FROM BDMailMarketing.dbo.tblDctoDetalle  								          "          
		        + "            	     INNER JOIN BDMailMarketing.dbo.tblPucAux                                         "                  
		        + "            	     ON tblDctoDetalle.idLocal = tblPucAux.idLocal                                    "                  
		        + "            	     AND tblDctoDetalle.idCuentaAux = tblPucAux.idCuentaAux  						  "          		 
		        + "            		 INNER JOIN bdaquamovil.dbo.tblTerceros tercero                                   "                        
		        + "            		 ON tblDctoDetalle.idLocal = tercero.idLocal                                      "                        
                + "                   AND tblDctoDetalle.idCliente = tercero.CC_Nit COLLATE DATABASE_DEFAULT     	  "          							 
				+ "          		 INNER JOIN BDMailMarketing.dbo.tblDcto                                           "          
				+ "          		 ON tblDcto.idLocal = tblDctoDetalle.idLocal                                      "          
				+ "          		 AND tblDcto.idCpte = tblDctoDetalle.idCpte                                       "          
				+ "          		 AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte                               "          
				+ "          		 INNER JOIN BDMailMarketing.dbo.tblTipoCpte                                       "                                          
			    + "                	 ON tblDcto.idTipoCpte = tblTipoCpte.idTipoCpte                                   "          
		        + "            	     Where tblDctoDetalle.idLocal = ?1                                               "                  
		        + "            	     AND tblDctoDetalle.idCpte = ?2                                                   "                   
		        + "            		 GROUP BY tblDctoDetalle.idTipoCpte,                                              "                  
		        + "            				  tblDctoDetalle.idCpte,                                                  "                                    
		        + "            				  tblPucAux.idCuentaAux,                                                  "                  
		        + "            				  tblDctoDetalle.idCliente,                                               "                        
		        + "            				  tblDctoDetalle.item,                                                    "                  
		        + "            				  tblDctoDetalle.fechaVencimiento,                                        "                               
		        + "            				  tblDctoDetalle.vrDebito,	                                              "                  
		        + "            				  tblDctoDetalle.vrCredito,                                               "          
				+ "          				  tblDcto.fechaDcto,                                                      "          
		        + "            				  tblDctoDetalle.observacion,                                             "          
				+ "          				  tblTipoCpte.nombreCmpte,                                                "          
		        + "            				  tblPucAux.nombreCuenta                                                  ", 
			  nativeQuery = true)
	  List<TblDctoDetalleDTO> comprobanteContableDetalle(int idLocal, int idTipoCpte);
	
	
	
	
	  @Modifying
	  @Transactional
	  @Query(value = "DELETE FROM tblDctoDetalle " +
	                 "WHERE tblDctoDetalle.idLocal = ?1 " +
	                 "AND tblDctoDetalle.idCpte =  ?2 ",
	                 nativeQuery = true)
	  public void retiraComprobanteDetalle(int idLocal, int idCpte);

}
