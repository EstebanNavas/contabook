package com.contabook.Repository.DBMailMarketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.contabook.Model.DBMailMarketing.TblDcto;
import com.contabook.Projection.TblDctoDTO;

@Repository
public interface TblDctoRepo extends JpaRepository<TblDcto, Integer> {
	
	
	 @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END " +
             "FROM BDMailMarketing.dbo.tblDcto " +
             "WHERE tblDcto.idLocal = ?1 " +
             "AND tblDcto.idTipoOrden = ?2 " +
             "AND tblDcto.idCpte = ?3 ",
             nativeQuery = true)
	 Boolean ExisteDcto(int idLocal, int idTipoOrden, int idCpte);
	 
	 @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END " +
             "FROM BDMailMarketing.dbo.tblDcto " +
             "WHERE tblDcto.idLocal = ?1 " +
             "AND tblDcto.idTipoOrden = ?2 " +
             "AND tblDcto.idDcto = ?3 " +
             "AND tblDcto.idTipoCpte = ?4 ",
             nativeQuery = true)
	 Boolean ExisteDctoCpte(int idLocal, int idTipoOrden, int idDcto, int idTipoCpte);
	 
	 @Query(value = "SELECT MAX(idCpte) AS idCpte " +
             "FROM BDMailMarketing.dbo.tblDcto " +
             "WHERE tblDcto.idLocal = ?1 ",
             nativeQuery = true)
	 Integer MaximoiIdCpte(int idLocal);
	 
	 
	  @Modifying
	  @Transactional
	  @Query(value = "INSERT INTO tblDcto (idLocal,             "
             + "                      idTipoCpte,               "
             + "                      idCpte,                   "
             + "                      idTipoOrden,              "
             + "                      idDcto,                   "
             + "                      fechaDcto,                "
             + "                      idMoneda,                 "
             + "                      idTasa,                   "
             + "                      idPeriodo)                "
             + "VALUES ( ?1,"
             + "?2,"
             + "?3,"
             + "?4,"
             + "?5,"
             + "?6,"
             + "?7,"
             + "?8,"
             + "?9)", nativeQuery = true)
	  public void ingresaDcto(int idLocal, int idTipoCpte, int idCpte, int idTipoOrden, int idDcto, String FechaDcto, String siglaMoneda, int idTasa, int idPeriodo);
	  
	  
	  
		 @Query(value = "         SELECT DISTINCT tblDcto.idTipoCpte,                        "   
					+ "	        tblDcto.idCpte,                                            "                           
					+ "	        CONVERT(VARCHAR, tblDcto.fechaDcto, 23) AS fechaDcto,      "
					+ "	        tblDcto.idPeriodo,                                         "
					+ "	        tblTipoCpte.nombreCmpte AS nombreTipoComprobante,          "
					+ "	 	   MAX(tblDctoDetalle.observacion) AS nombreUsuario,           "
					+ "	        SUM(tblDctoDetalle.vrDebito) AS vrTotal                    "
					+ "	 	FROM BDMailMarketing.dbo.tblDcto                               "
					+ "	 	INNER JOIN tblTipoCpte                                         "
					+ "	 	ON tblDcto.idTipoCpte = tblTipoCpte.idTipoCpte                 "
					+ "	 	INNER JOIN tblDctoDetalle                                      "
					+ "	 	ON tblDcto.idLocal = tblDctoDetalle.idLocal                    "
					+ "	 	AND tblDcto.idCpte = tblDctoDetalle.idCpte                     "
					+ "	 	AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte             "
					+ "	 	WHERE tblDcto.idLocal = ?1                                    "
					+ "	 	AND tblDcto.idTipoCpte = ?2                                     "
					+ "	 	AND tblDcto.idPeriodo = ?3   					           "
					+ "	 	GROUP BY tblDcto.idTipoCpte,                                   "
					+ "	          tblDcto.idCpte,                                          "                        
					+ "	          tblDcto.fechaDcto,                                       "
					+ "	          tblDcto.idPeriodo,                                       "            
					+ "	          tblTipoCpte.nombreCmpte					               "
					+ "			  ORDER BY idCpte desc                                     ",
	             nativeQuery = true)
		 List<TblDctoDTO> listaComprobantes(int idLocal, int idTipoCpte, int idPeriodo);
		 
		 
		 
		 @Query(value = "SELECT * " +
	             "FROM BDMailMarketing.dbo.tblDcto " +
	             "WHERE tblDcto.idLocal = ?1 " +
	             "AND tblDcto.idCpte = ?2 ",
	             nativeQuery = true)
		 List<TblDctoDTO> ObtenerIdCpte(int idLocal, int idCpte);
		 
		 
		 @Modifying
		  @Transactional
		  @Query(value = "UPDATE tblDcto SET idTipoCpte = ?1 " +
		                 "WHERE tblDcto.idLocal = ?2 " +
		                 "AND tblDcto.idCpte = ?3 ", nativeQuery = true)
		  public void actualizarDctoContable(int idTipoCpte, int idLocal, int idCpte);
		 
		 
		 
		 @Modifying
		  @Transactional
		  @Query(value = "DELETE FROM tblDcto " +
		                 "WHERE tblDcto.idLocal = ?1 " +
		                 "AND tblDcto.idCpte =  ?2 ",
		                 nativeQuery = true)
		  public void retiraDcto(int idLocal, int idCpte);
		 
		 
		 
		 
		 @Query(value = "        SELECT DISTINCT tblDcto.idTipoCpte,                                               "   
					+ "	        tblDcto.idCpte,                                                                  "     
					+ "	        CONVERT(VARCHAR, tblDcto.fechaDcto, 23) AS fechaDcto,                            "
					+ "	        tblDcto.idPeriodo,                                                               "	        
					+ "			MAX(tblDctoDetalle.idCliente) AS idCliente,                                      "
					+ "			tblDctoDetalle.idCuentaAux,                                                      "
					+ "			MAX(tblPucAux.nombreCuenta) AS nombreCuenta,                                     "
					+ "			MAX(tercero.nombreTercero ) AS nombreTercero,                                    "
					+ "			CASE                                                                             "
					+ "			 WHEN SUM(tblDctoDetalle.vrDebito) = 0                                           "
					+ "			 THEN SUM(tblDctoDetalle.vrCredito)                                              "
					+ "			 ELSE SUM(tblDctoDetalle.vrDebito)                                               "
                    + "            END AS vrTotal                                                                "
					+ "	 	FROM BDMailMarketing.dbo.tblDcto                                                     "
					+ "	 	INNER JOIN tblTipoCpte                                                               "
					+ "	 	ON tblDcto.idTipoCpte = tblTipoCpte.idTipoCpte  							         "
					+ "	 	INNER JOIN tblDctoDetalle                                                            "
					+ "	 	ON tblDcto.idLocal = tblDctoDetalle.idLocal                                          "
					+ "	 	AND tblDcto.idCpte = tblDctoDetalle.idCpte                                           "
					+ "	 	AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte    							     "
					+ "		INNER JOIN tblPucAux                                                                 "
					+ "		ON tblDctoDetalle.idLocal = tblPucAux.idLocal                                        "
					+ "		AND tblDctoDetalle.idCuentaAux = tblPucAux.idCuentaAux                               "
					+ "		INNER JOIN bdaquamovil.dbo.tblTerceros tercero                                       "
					+ "		ON tblDctoDetalle.idLocal = tercero.idLocal                                          "
                    + "        AND tblDctoDetalle.idCliente = tercero.idCliente COLLATE DATABASE_DEFAULT         "
					+ "	 	WHERE tblDcto.idLocal = ?1                                                          "            
					+ "	 	AND tblDcto.idPeriodo = ?2                                                       "
					+ "		AND tblDctoDetalle.idCuentaAux IN ?3                                         "
					+ "	 	GROUP BY tblDcto.idTipoCpte,                                                         "
					+ "	          tblDcto.idCpte,                                                                "  
					+ "	          tblDcto.fechaDcto,                                                             "
					+ "	          tblDcto.idPeriodo,                                                             "
					+ "	          tblDctoDetalle.idCuentaAux 			                                         "
					+ "			  ORDER BY idCliente desc                                                        ",
	             nativeQuery = true)
		    List<TblDctoDTO> listaMovimientoPorAuxiliar(int idLocal, int idPeriodo, List<Integer> cuentasContables);
		 
		 
		 
		 @Query(value = "        SELECT DISTINCT tblDcto.idTipoCpte,                                               "   
					+ "	        tblDcto.idCpte,                                                                  "     
					+ "	        CONVERT(VARCHAR, tblDcto.fechaDcto, 23) AS fechaDcto,                            "
					+ "	        tblDcto.idPeriodo,                                                               "		        
					+ "			MAX(tblDctoDetalle.idCliente) AS idCliente,                                      "
					+ "			tblDctoDetalle.idCuentaAux,                                  "
					+ "			MAX(tblPucAux.nombreCuenta) AS nombreCuenta,                                     "
					+ "			MAX(tercero.nombreTercero ) AS nombreTercero,                                    "
					+ "			CASE                                                                             "
					+ "			 WHEN SUM(tblDctoDetalle.vrDebito) = 0                                           "
					+ "			 THEN SUM(tblDctoDetalle.vrCredito)                                              "
					+ "			 ELSE SUM(tblDctoDetalle.vrDebito)                                               "
                    + "            END AS vrTotal                                                                "
					+ "	 	FROM BDMailMarketing.dbo.tblDcto                                                     "
					+ "	 	INNER JOIN tblTipoCpte                                                               "
					+ "	 	ON tblDcto.idTipoCpte = tblTipoCpte.idTipoCpte  							         "
					+ "	 	INNER JOIN tblDctoDetalle                                                            "
					+ "	 	ON tblDcto.idLocal = tblDctoDetalle.idLocal                                          "
					+ "	 	AND tblDcto.idCpte = tblDctoDetalle.idCpte                                           "
					+ "	 	AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte    							     "
					+ "		INNER JOIN tblPucAux                                                                 "
					+ "		ON tblDctoDetalle.idLocal = tblPucAux.idLocal                                        "
					+ "		AND tblDctoDetalle.idCuentaAux = tblPucAux.idCuentaAux                               "
					+ "		INNER JOIN bdaquamovil.dbo.tblTerceros tercero                                       "
					+ "		ON tblDctoDetalle.idLocal = tercero.idLocal                                          "
                    + "        AND tblDctoDetalle.idCliente = tercero.idCliente COLLATE DATABASE_DEFAULT         "
					+ "	 	WHERE tblDcto.idLocal = ?1                                                          "            
					+ "	 	AND tblDcto.idPeriodo = ?2                                                       "
					+ "		AND tblDctoDetalle.idCliente = ?3                                         "
					+ "	 	GROUP BY tblDcto.idTipoCpte,                                                         "
					+ "	          tblDcto.idCpte,                                                                "  
					+ "	          tblDcto.fechaDcto,                                                             "
					+ "	          tblDcto.idPeriodo,                                                             "
					+ "	          tblDctoDetalle.idCuentaAux					                                     "
					+ "			  ORDER BY idCliente desc                                                        ",
	             nativeQuery = true)
		    List<TblDctoDTO> listaMovimientoPorTercero(int idLocal, int idPeriodo, String idCliente);
		 
		 
		 
		 
		 @Query(value = "        SELECT DISTINCT tblDcto.idTipoCpte,                                               "   
					+ "	        tblDcto.idCpte,                                                                  "     
					+ "	        CONVERT(VARCHAR, tblDcto.fechaDcto, 23) AS fechaDcto,                            "
					+ "	        tblDcto.idPeriodo,                                                               "		        
					+ "			MAX(tblDctoDetalle.idCliente) AS idCliente,                                      "
					+ "			tblDctoDetalle.idCuentaAux,                                  "
					+ "			MAX(tblPucAux.nombreCuenta) AS nombreCuenta,                                     "
					+ "			MAX(tercero.nombreTercero ) AS nombreTercero,                                    "
					+ "			CASE                                                                             "
					+ "			 WHEN SUM(tblDctoDetalle.vrDebito) = 0                                           "
					+ "			 THEN SUM(tblDctoDetalle.vrCredito)                                              "
					+ "			 ELSE SUM(tblDctoDetalle.vrDebito)                                               "
                 + "            END AS vrTotal                                                                "
					+ "	 	FROM BDMailMarketing.dbo.tblDcto                                                     "
					+ "	 	INNER JOIN tblTipoCpte                                                               "
					+ "	 	ON tblDcto.idTipoCpte = tblTipoCpte.idTipoCpte  							         "
					+ "	 	INNER JOIN tblDctoDetalle                                                            "
					+ "	 	ON tblDcto.idLocal = tblDctoDetalle.idLocal                                          "
					+ "	 	AND tblDcto.idCpte = tblDctoDetalle.idCpte                                           "
					+ "	 	AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte    							     "
					+ "		INNER JOIN tblPucAux                                                                 "
					+ "		ON tblDctoDetalle.idLocal = tblPucAux.idLocal                                        "
					+ "		AND tblDctoDetalle.idCuentaAux = tblPucAux.idCuentaAux                               "
					+ "		INNER JOIN bdaquamovil.dbo.tblTerceros tercero                                       "
					+ "		ON tblDctoDetalle.idLocal = tercero.idLocal                                          "
                 + "        AND tblDctoDetalle.idCliente = tercero.idCliente COLLATE DATABASE_DEFAULT         "
					+ "	 	WHERE tblDcto.idLocal = ?1                                                          "            
					+ "	 	AND tblDcto.idPeriodo = ?2                                                       "
					+ "		AND tblDctoDetalle.idCliente = ?3                                         "
					+ "		AND tblDctoDetalle.idCuentaAux IN ?4                                         "
					+ "	 	GROUP BY tblDcto.idTipoCpte,                                                         "
					+ "	          tblDcto.idCpte,                                                                "  
					+ "	          tblDcto.fechaDcto,                                                             "
					+ "	          tblDcto.idPeriodo,                                                             "
					+ "	          tblDctoDetalle.idCuentaAux					                                     "
					+ "			  ORDER BY idCliente desc                                                        ",
	             nativeQuery = true)
		     List<TblDctoDTO> listaMovimientoPorTerceroYAuxiiar(int idLocal, int idPeriodo, String idCliente, List<Integer> cuentasContables);
		 
		 
		 
		 
		 @Query(value = "        SELECT   tblDcto.idTipoCpte,                                     "             
					+ "	        tblDcto.idCpte,                                                 "                      
					+ "	        CONVERT(VARCHAR, tblDcto.fechaDcto, 23) AS fechaDcto,           "
					+ "			MAX(tblDctoDetalle.idCuentaAux) AS idCuentaAux,                 "
					+ "			MAX(tblPucAux.nombreCuenta) AS  nombreCuenta,                   "
					+ "	        SUM(tblDctoDetalle.vrDebito) AS vrDebito,                       "
					+ "			SUM(tblDctoDetalle.vrCredito) AS vrCredito,                     "
					+ "			tblDctoDetalle.item                                             "
					+ "	 	FROM BDMailMarketing.dbo.tblDcto                                    "
					+ "	 	INNER JOIN tblTipoCpte                                              "
					+ "	 	ON tblDcto.idTipoCpte = tblTipoCpte.idTipoCpte 						"	
					+ "	 	INNER JOIN tblDctoDetalle                                           "
					+ "	 	ON tblDcto.idLocal = tblDctoDetalle.idLocal                         "
					+ "	 	AND tblDcto.idCpte = tblDctoDetalle.idCpte                          "
					+ "	 	AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte   				"			
					+ "		INNER JOIN BDMailMarketing.dbo.tblPucAux                            "
			        + "        ON tblDctoDetalle.idLocal = tblPucAux.idLocal                    "   
			        + "        AND tblDctoDetalle.idCuentaAux = tblPucAux.idCuentaAux           "
					+ "	 	WHERE tblDcto.idLocal = ?1                                         "
					+ "	 	AND tblDcto.idTipoCpte IN ?2                                          "
					+ "	    AND   CONVERT(VARCHAR(10), tblDcto.fechaDcto, 23)                   "
					+ "		BETWEEN ?3 AND  ?4                                                  "
					+ "	 	GROUP BY tblDcto.idTipoCpte,                                        "
					+ "	          tblDcto.idCpte,                                               "                   
					+ "	          tblDcto.fechaDcto,                                            "
					+ "	          tblDcto.idPeriodo,                                            "       
					+ "	          tblTipoCpte.nombreCmpte,                                      "
					+ "			  tblDctoDetalle.item                                           "
					+ "			  ORDER BY idCpte desc                                          ",
	             nativeQuery = true)
		 List<TblDctoDTO> listaComprobantesLibroDiario(int idLocal, List<Integer> idTipoCpte, String fechaIncial, String fechaFinal);
		 
		 
		 
		 
		 
		 @Query(value = "            SELECT  tblPucAux.idLocal                                                        "                                            
					+ "	          ,tblPucAux.idCuentaAux                                                            "      
					+ "	          ,tblPucAux.nombreCuenta AS nombreAuxiliar                                         "      
					+ "	    	  ,tblPuc.nombreCuenta AS nombreSubCuenta                                           "      
					+ "	    	  ,tblPuc.idCuenta AS  idSubCuenta,                                                 "      
					+ "	    	  ( SELECT TOP 1 tblPuc.nombreCuenta                                                "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,4)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) ) AS nombreCuenta,        "      
					+ "	    	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) AS idCuenta,                     "      
					+ "	        (SELECT TOP 1 tblPuc.nombreCuenta                                                   "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,2)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) ) AS nombreGrupo,         "      
					+ "	    	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) AS idGrupo,                      "      
					+ "	        (SELECT TOP 1 tblPuc.nombreCuenta                                                   "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) ) AS nombreClase,         "      
					+ "	    	 SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) AS idClase,                       "      
					+ "			 CASE                                                                               "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END AS signoNaturaleza,					                                        "      
			        + "             CASE                                                                            "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END * tmpTOT.vrDebito AS vrDebito ,                                             "      
					+ "			 CASE                                                                               "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END * tmpTOT.vrCredito AS vrCredito,                                            "
					+ "			 tmpTOT.idPeriodo,                                                                  "
					+ "			 0.0 AS vrTotalAnterior                                                             "
					+ "	      FROM tblPucAux                                                                        "      
					+ "	      INNER JOIN tblPuc                                                                     "      
					+ "	      ON SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,6) = tblPuc.idCuenta           		"				  
					+ "	      INNER JOIN (                                                                          "      
					+ "	      SELECT tblDctoDetalle.idLocal,                                                        "      
					+ "	           tblDctoDetalle.idCuentaAux,                                                      "      
					+ "	    	   SUM(tblDctoDetalle.vrDebito) AS vrDebito,                                        "      
					+ "	    	   SUM(tblDctoDetalle.vrCredito) AS vrCredito,                                      "
					+ "			   MAX(tblDcto.idPeriodo) AS idPeriodo                                              "
					+ "	     FROM    tblDcto                                                                        "      
					+ "	     INNER JOIN tblDctoDetalle                                                              "      
					+ "	     ON tblDcto.idLocal = tblDctoDetalle.idLocal                                            "      
					+ "	     AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte                                     "      
					+ "	     AND tblDcto.idCpte = tblDctoDetalle.idCpte                                             "      
					+ "	     WHERE tblDcto.idLocal = ?1                                                            "      
					+ "	     AND   tblDcto.idPeriodo = ?2	                                                    "
					+ "		 AND   tblDctoDetalle.idCuentaAux IN ?3                                                 "
					+ "	     GROUP BY tblDctoDetalle.idLocal,                                                       "      
					+ "	          tblDctoDetalle.idCuentaAux                                                        "
					+ "	      ) AS tmpTOT   						                                                "
					+ "	       ON tmpTOT.idLocal  = tblPucAux.idLocal                                               "      
					+ "	       AND tmpTOT.idCuentaAux  = tblPucAux.idCuentaAux                                      "      
					+ "	      WHERE tblPucAux.idLocal= ?1  						                                "
					+ "		 UNION                                                                                  "
					+ "		 SELECT  tblPucAux.idLocal                                                              "                                      
					+ "	          ,tblPucAux.idCuentaAux                                                            "      
					+ "	          ,tblPucAux.nombreCuenta AS nombreAuxiliar                                         "      
					+ "	    	  ,tblPuc.nombreCuenta AS nombreSubCuenta                                           "      
					+ "	    	  ,tblPuc.idCuenta AS  idSubCuenta,                                                 "      
					+ "	    	  ( SELECT TOP 1 tblPuc.nombreCuenta                                                "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,4)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) ) AS nombreCuenta,        "      
					+ "	    	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) AS idCuenta,                     "      
					+ "	        (SELECT TOP 1 tblPuc.nombreCuenta                                                   "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,2)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) ) AS nombreGrupo,         "      
					+ "	    	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) AS idGrupo,                      "      
					+ "	        (SELECT TOP 1 tblPuc.nombreCuenta                                                   "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) ) AS nombreClase,         "      
					+ "	    	 SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) AS idClase,                       "      
					+ "			 CASE                                                                               "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END AS signoNaturaleza,					                                        "      
			        + "             CASE                                                                            "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END * tmpANT.vrDebito AS vrDebito ,                                             "      
					+ "			 CASE                                                                               "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END * tmpANT.vrCredito AS vrCredito,                                            "
					+ "			 tmpANT.idPeriodo,                                                                  "
					+ "			 (tmpANT.vrDebito - tmpANT.vrCredito) AS vrTotalAnterior                            "
					+ "	      FROM tblPucAux                                                                        "      
					+ "	      INNER JOIN tblPuc                                                                     "      
					+ "	      ON SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,6) = tblPuc.idCuenta           		"				  
					+ "	      INNER JOIN (                                                                          "      
					+ "	      SELECT tblDctoDetalle.idLocal,                                                        "      
					+ "	           tblDctoDetalle.idCuentaAux,                                                      "      
					+ "	    	   SUM(tblDctoDetalle.vrDebito) AS vrDebito,                                        "      
					+ "	    	   SUM(tblDctoDetalle.vrCredito) AS vrCredito,                                      "
					+ "			   MAX(tblDcto.idPeriodo) AS idPeriodo                                              "
					+ "	     FROM    tblDcto                                                                        "      
					+ "	     INNER JOIN tblDctoDetalle                                                              "      
					+ "	     ON tblDcto.idLocal = tblDctoDetalle.idLocal                                            "      
					+ "	     AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte                                     "      
					+ "	     AND tblDcto.idCpte = tblDctoDetalle.idCpte                                             "      
					+ "	     WHERE tblDcto.idLocal = ?1                                                            "      
					+ "	     AND   tblDcto.idPeriodo < ?2                                                       "
					+ "		 AND   tblDctoDetalle.idCuentaAux IN  ?3                                        "
					+ "		 AND LEFT(CAST(tblDcto.idPeriodo AS VARCHAR), 4) = LEFT(?2, 4)                      "
					+ "	     GROUP BY tblDctoDetalle.idLocal,                                                       "      
					+ "	          tblDctoDetalle.idCuentaAux                                                        "
					+ "	      ) AS tmpANT   						                                                "
					+ "	       ON tmpANT.idLocal  = tblPucAux.idLocal                                               "      
					+ "	       AND tmpANT.idCuentaAux  = tblPucAux.idCuentaAux                                      "      
					+ "	      WHERE tblPucAux.idLocal= ?1                                                          "                                                              
					+ "	     ORDER BY 1,11,9,7,5,2                                                                  ",
	             nativeQuery = true)
		     List<TblDctoDTO> listaBalancePruebaGeneralIdPeriodoYAuxiiar(int idLocal, int idPeriodo,  List<Integer> cuentasContables);
		 
		 
		 
		 
		 
		 @Query(value = "            SELECT  tblPucAux.idLocal                                                        "                                            
					+ "	          ,tblPucAux.idCuentaAux                                                            "      
					+ "	          ,tblPucAux.nombreCuenta AS nombreAuxiliar                                         "      
					+ "	    	  ,tblPuc.nombreCuenta AS nombreSubCuenta                                           "      
					+ "	    	  ,tblPuc.idCuenta AS  idSubCuenta,                                                 "      
					+ "	    	  ( SELECT TOP 1 tblPuc.nombreCuenta                                                "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,4)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) ) AS nombreCuenta,        "      
					+ "	    	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) AS idCuenta,                     "      
					+ "	        (SELECT TOP 1 tblPuc.nombreCuenta                                                   "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,2)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) ) AS nombreGrupo,         "      
					+ "	    	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) AS idGrupo,                      "      
					+ "	        (SELECT TOP 1 tblPuc.nombreCuenta                                                   "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) ) AS nombreClase,         "      
					+ "	    	 SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) AS idClase,                       "      
					+ "			 CASE                                                                               "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END AS signoNaturaleza,					                                        "      
			        + "             CASE                                                                            "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END * tmpTOT.vrDebito AS vrDebito ,                                             "      
					+ "			 CASE                                                                               "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END * tmpTOT.vrCredito AS vrCredito,                                            "
					+ "			 tmpTOT.idPeriodo,                                                                  "
					+ "			 0.0 AS vrTotalAnterior                                                             "
					+ "	      FROM tblPucAux                                                                        "      
					+ "	      INNER JOIN tblPuc                                                                     "      
					+ "	      ON SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,6) = tblPuc.idCuenta           		"				  
					+ "	      INNER JOIN (                                                                          "      
					+ "	      SELECT tblDctoDetalle.idLocal,                                                        "      
					+ "	           tblDctoDetalle.idCuentaAux,                                                      "      
					+ "	    	   SUM(tblDctoDetalle.vrDebito) AS vrDebito,                                        "      
					+ "	    	   SUM(tblDctoDetalle.vrCredito) AS vrCredito,                                      "
					+ "			   MAX(tblDcto.idPeriodo) AS idPeriodo                                              "
					+ "	     FROM    tblDcto                                                                        "      
					+ "	     INNER JOIN tblDctoDetalle                                                              "      
					+ "	     ON tblDcto.idLocal = tblDctoDetalle.idLocal                                            "      
					+ "	     AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte                                     "      
					+ "	     AND tblDcto.idCpte = tblDctoDetalle.idCpte                                             "      
					+ "	     WHERE tblDcto.idLocal = ?1                                                            "      
					+ "	     AND   tblDcto.idPeriodo = ?2	                                                    "
					+ "	     GROUP BY tblDctoDetalle.idLocal,                                                       "      
					+ "	          tblDctoDetalle.idCuentaAux                                                        "
					+ "	      ) AS tmpTOT   						                                                "
					+ "	       ON tmpTOT.idLocal  = tblPucAux.idLocal                                               "      
					+ "	       AND tmpTOT.idCuentaAux  = tblPucAux.idCuentaAux                                      "      
					+ "	      WHERE tblPucAux.idLocal= ?1  						                                "
					+ "		 UNION                                                                                  "
					+ "		 SELECT  tblPucAux.idLocal                                                              "                                      
					+ "	          ,tblPucAux.idCuentaAux                                                            "      
					+ "	          ,tblPucAux.nombreCuenta AS nombreAuxiliar                                         "      
					+ "	    	  ,tblPuc.nombreCuenta AS nombreSubCuenta                                           "      
					+ "	    	  ,tblPuc.idCuenta AS  idSubCuenta,                                                 "      
					+ "	    	  ( SELECT TOP 1 tblPuc.nombreCuenta                                                "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,4)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) ) AS nombreCuenta,        "      
					+ "	    	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) AS idCuenta,                     "      
					+ "	        (SELECT TOP 1 tblPuc.nombreCuenta                                                   "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,2)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) ) AS nombreGrupo,         "      
					+ "	    	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) AS idGrupo,                      "      
					+ "	        (SELECT TOP 1 tblPuc.nombreCuenta                                                   "      
					+ "	    	    FROM  tblPuc                                                                    "      
					+ "	    		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)=                               "      
					+ "	    		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) ) AS nombreClase,         "      
					+ "	    	 SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) AS idClase,                       "      
					+ "			 CASE                                                                               "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END AS signoNaturaleza,					                                        "      
			        + "             CASE                                                                            "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END * tmpANT.vrDebito AS vrDebito ,                                             "      
					+ "			 CASE                                                                               "      
			        + "             WHEN SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)IN (1,5,6) THEN 1                "      
			        + "             ELSE (-1)                                                                       "      
			        + "             END * tmpANT.vrCredito AS vrCredito,                                            "
					+ "			 tmpANT.idPeriodo,                                                                  "
					+ "			 (tmpANT.vrDebito - tmpANT.vrCredito) AS vrTotalAnterior                            "
					+ "	      FROM tblPucAux                                                                        "      
					+ "	      INNER JOIN tblPuc                                                                     "      
					+ "	      ON SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,6) = tblPuc.idCuenta           		"				  
					+ "	      INNER JOIN (                                                                          "      
					+ "	      SELECT tblDctoDetalle.idLocal,                                                        "      
					+ "	           tblDctoDetalle.idCuentaAux,                                                      "      
					+ "	    	   SUM(tblDctoDetalle.vrDebito) AS vrDebito,                                        "      
					+ "	    	   SUM(tblDctoDetalle.vrCredito) AS vrCredito,                                      "
					+ "			   MAX(tblDcto.idPeriodo) AS idPeriodo                                              "
					+ "	     FROM    tblDcto                                                                        "      
					+ "	     INNER JOIN tblDctoDetalle                                                              "      
					+ "	     ON tblDcto.idLocal = tblDctoDetalle.idLocal                                            "      
					+ "	     AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte                                     "      
					+ "	     AND tblDcto.idCpte = tblDctoDetalle.idCpte                                             "      
					+ "	     WHERE tblDcto.idLocal = ?1                                                            "      
					+ "	     AND   tblDcto.idPeriodo < ?2                                                       "
					+ "		 AND LEFT(CAST(tblDcto.idPeriodo AS VARCHAR), 4) = LEFT(?2, 4)                      "
					+ "	     GROUP BY tblDctoDetalle.idLocal,                                                       "      
					+ "	          tblDctoDetalle.idCuentaAux                                                        "
					+ "	      ) AS tmpANT   						                                                "
					+ "	       ON tmpANT.idLocal  = tblPucAux.idLocal                                               "      
					+ "	       AND tmpANT.idCuentaAux  = tblPucAux.idCuentaAux                                      "      
					+ "	      WHERE tblPucAux.idLocal= ?1                                                          "                                                              
					+ "	     ORDER BY 1,11,9,7,5,2                                                                  ",
	             nativeQuery = true)
		     List<TblDctoDTO> listaBalancePruebaGeneralPorIdPeriodo(int idLocal, int idPeriodo);

}
