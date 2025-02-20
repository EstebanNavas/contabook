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

}
