package com.contabook.Repository.DBMailMarketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Model.DBMailMarketing.TblPucAux;
import com.contabook.Projection.TblPucAuxDTO;

@Repository
public interface TblPucAuxRepo extends JpaRepository<TblPucAux, Integer> {
	
	@Modifying
	  @Transactional
	  @Query(value = "INSERT INTO tblPucAux (idLocal,    "
         + "                      idCuentaAux,           "
         + "                      nombreCuenta,          "
         + "                      ObligaTercero,         "
         + "                      Iva,                   "
         + "                      RteFuente)             "
         + "VALUES ( ?1,"
         + "?2,"
         + "?3,"
         + "?4,"
         + "?5,"
         + "?6)", nativeQuery = true)
	  public void ingresaAuxiliar(int idLocal, int idCuentaAux, String nombreCuenta, int ObligaTercero, Double Iva, int RteFuente);
	
	
	 @Query(value = "SELECT * " +
             "FROM BDMailMarketing.dbo.tblPucAux " +
             "WHERE (tblPucAux.idCuentaAux LIKE %?1%) " +
             "AND tblPucAux. idLocal = ?2",
             nativeQuery = true)
	 List<TblPucAux> listaAuxiliares(int idCuentaAux, int idLocal);
	 
	 @Query(value = "SELECT nombreCuenta " +
             "FROM BDMailMarketing.dbo.tblPucAux " +
             "WHERE idLocal = ?1 " +
             "AND idCuentaAux = ?2",
             nativeQuery = true)
	 String obtenerNombreCuentaAux(int idLocal, int idCuentaAux);
	 
	 
	  @Modifying
	  @Transactional
	  @Query(value = "UPDATE tblPucAux SET nombreCuenta = ?1 " +
	                 "WHERE tblPucAux.idLocal = ?2 " +
	                 "AND tblPucAux.idCuentaAux = ?3 ",
	                 nativeQuery = true)
	  public void actualizarAuxiliar(String nombreCuenta, int idLocal, int idCuentaAux);
	  
	  
	  @Modifying
	  @Transactional
	  @Query(value = "DELETE FROM dbo.tblPucAux " +
	                 "WHERE tblPucAux.idLocal = ?1 " +
	                 "AND tblPucAux.idCuentaAux = ?2 ",
	                 nativeQuery = true)
	  public void eliminarAuxiliar(int idLocal, int idCuentaAux);
	  
	  
		 @Query(value = "SELECT * " +
	             "FROM BDMailMarketing.dbo.tblPucAux " +
	             "WHERE tblPucAux. idLocal = ?1 ",
	             nativeQuery = true)
		 List<TblPucAux> listaTodosAuxiliares(int idLocal);
		 
		 
		 
		 @Query(value = "SELECT idCuentaAux, nombreCuenta " +
	             "FROM BDMailMarketing.dbo.tblPucAux " +
	             "WHERE tblPucAux.idLocal = ?1  " +
	             "AND (tblPucAux.idCuentaAux LIKE ?2%) " +
	             "ORDER BY idCuentaAux",
	             nativeQuery = true)
		 List<TblPucAux> listaAuxiliaresXClase(int idLocal, int idClase);
		 
		 
		 @Query(value = "   SELECT tmpPUC.idClase                                                       "
				 + "	    ,tmpPUC.nombreClase                                                         "
				 + "        ,tmpPUC.idGrupo                                                             "
				 + "	    ,tmpPUC.nombreGrupo                                                         "
				 + "        ,tmpPUC.idCuenta                                                            "
				 + "	    ,tmpPUC.nombreCuenta                                                        "
				 + "        ,tmpPUC.idSubCuenta                                                         "
				 + "	    ,tmpPUC.nombreSubCuenta                                                     "
				 + "        ,tblPucAux.idCuentaAux                                                      "
				 + "        ,tblPucAux.nombreCuenta AS nombreAuxiliar                                   "
				 + " FROM BDMailMarketing.dbo.tblPucAux                                                 "
				 + " INNER JOIN (                                                                       "
				 + " SELECT  tblPucAux.idLocal                                                          "
				 + "     ,tblPucAux.idCuentaAux                                                         "
				 + "     ,tblPucAux.nombreCuenta AS nombreAuxiliar                                      "
				 + "	  ,tblPuc.nombreCuenta AS nombreSubCuenta                                       "
				 + "	  ,tblPuc.idCuenta AS  idSubCuenta,                                             "
				 + "	  ( SELECT TOP 1 tblPuc.nombreCuenta                                            "
				 + "	    FROM  tblPuc                                                                "
				 + "		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,4)=                           "
				 + "		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) ) AS nombreCuenta,    "
				 + "	 SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) AS idCuenta,                  "
				 + " (SELECT TOP 1 tblPuc.nombreCuenta                                                  "
				 + "	    FROM  tblPuc                                                                "
				 + "		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,2)=                           "
				 + "		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) ) AS nombreGrupo,     "
				 + "	 SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) AS idGrupo,                   "
				 + " (SELECT TOP 1 tblPuc.nombreCuenta                                                  "
				 + "	    FROM  tblPuc                                                                "
				 + "		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)=                           "
				 + "		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) ) AS nombreClase,     "
				 + "	 SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) AS idClase                    "
				 + " FROM BDMailMarketing.dbo.tblPucAux                                                 "
				 + " INNER JOIN BDMailMarketing.dbo.tblPuc                                              "
				 + " ON SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,6) = tblPuc.idCuenta              "
				 + " WHERE tblPucAux.idLocal= ?1 ) AS tmpPUC                                            "
				 + " ON tblPucAux.idLocal= tmpPUC.idLocal                                               "
				 + " AND tblPucAux.idCuentaAux= tmpPUC.idCuentaAux                                      "
				 + " ORDER BY tmpPUC.idClase                                                            "
				 + "     ,tmpPUC.idGrupo                                                                "
				 + "     ,tmpPUC.idCuenta                                                               "
				 + "     ,tmpPUC.idSubCuenta                                                            "
				 + "     ,tblPucAux.idCuentaAux                                                         ",
	             nativeQuery = true)
		 List<TblPucAuxDTO> listaCuentasContables(int idLocal);

}
