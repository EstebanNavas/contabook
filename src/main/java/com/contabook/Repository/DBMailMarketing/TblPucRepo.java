package com.contabook.Repository.DBMailMarketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Projection.TblPucDTO;

@Repository
public interface TblPucRepo extends JpaRepository<TblPuc, Integer> {

	 @Query(value = "SELECT * " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE idLocal = ?1 " +
             "AND idCuenta < 10 " +
             "order by idCuenta  ",
             nativeQuery = true)
	 List<TblPuc> pucNivel1(int idLocal);
	 
	 @Query(value = "SELECT * " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE idLocal = ?1 " +
             "AND idCuenta >= ?2 " +
             "AND  idCuenta < ?3 " +
             "and idClase = ?4 " +
             "order by idCuenta ",
             nativeQuery = true)
	 List<TblPuc> pucNivel2(int idLocal, int idCuentaMenor, int idCuentaMayor, int idClase);
	 
	 @Query(value = "SELECT * " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE idLocal = ?1 " +
             "AND idCuenta >= ?2 " +
             "AND  idCuenta < ?3 " +
             "and idClase = ?4 ",
             nativeQuery = true)
	 List<TblPuc> pucNiveles(int idLocal, int idCuentaMenor, int idCuentaMayor, int idClase);
	 
	 
	 @Query(value = "SELECT nombreCuenta " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE idLocal = ?1 " +
             "AND idCuenta = ?2 " +
             "AND  idClase = ?3 ",
             nativeQuery = true)
	 String obtenerNombreCuenta(int idLocal, int idCuenta, int idClase);
	 
	 @Query(value = "SELECT * " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE (tblPuc.idCuenta LIKE %?1%) " +
             "AND idLocal = ?2 ",
             nativeQuery = true)
	 List<TblPuc> listaSubCuentas( int idCuenta, int idLocal);
	 
	 @Query(value = "SELECT * " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE (tblPuc.idCuenta LIKE %?1%) " +
             "AND idLocal = ?2 " +
             "AND LEN(idCuenta) = 4",
             nativeQuery = true)
	 List<TblPuc> listaCuentas( int idCuenta, int idLocal);
	 
	 
	 @Modifying
	  @Transactional
	  @Query(value = "INSERT INTO tblPuc (idLocal,    "
        + "                      idClase,           "
        + "                      idCuenta,          "
        + "                      nombreCuenta)             "
        + "VALUES ( ?1,"
        + "?2,"
        + "?3,"
        + "?4)", nativeQuery = true)
	  public void ingresaSubCuenta(int idLocal, int idClase, int idCuenta, String nombreCuenta);
	 
	 
	 @Query(value = "     SELECT  tblPucAux.idLocal                                                       " 
			 + "         ,tblPucAux.idCuentaAux                                                           "
			 + "         ,tblPucAux.nombreCuenta AS nombreAuxiliar                                        "
			 + "   	  ,tblPuc.nombreCuenta AS nombreSubCuenta                                            "
			 + "   	  ,tblPuc.idCuenta AS  idSubCuenta,                                                  "
			 + "   	  ( SELECT TOP 1 tblPuc.nombreCuenta                                                 "
			 + "   	    FROM  tblPuc                                                                     "
			 + "   		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,4)=                                "
			 + "   		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) ) AS nombreCuenta,         "
			 + "   	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,4) AS idCuenta,                      "
			 + "       (SELECT TOP 1 tblPuc.nombreCuenta                                                  "
			 + "   	    FROM  tblPuc                                                                     "
			 + "   		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,2)=                                "
			 + "   		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) ) AS nombreGrupo,          "
			 + "   	  SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,2) AS idGrupo,                       "
			 + "       (SELECT TOP 1 tblPuc.nombreCuenta                                                  "
			 + "   	    FROM  tblPuc                                                                     "
			 + "   		WHERE SUBSTRING(LTRIM(STR(tblPuc.idCuenta)),1,1)=                                "
			 + "   		     SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) ) AS nombreClase,          "
			 + "   	 SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,1) AS idClase,                        "
			 + "   	  tmpTOT.vrDebito,                                                                   "
			 + "       tmpTOT.vrCredito                                                                   "
			 + "     FROM tblPucAux                                                                       "
			 + "     INNER JOIN tblPuc                                                                    "
			 + "     ON SUBSTRING(LTRIM(STR(tblPucAux.idCuentaAux)),1,6) = tblPuc.idCuenta                "
			 + "     INNER JOIN (                                                                         "
			 + "     SELECT tblDctoDetalle.idLocal,                                                       "
			 + "          tblDctoDetalle.idCuentaAux,                                                     "
			 + "   	   SUM(tblDctoDetalle.vrDebito) AS vrDebito,                                         "
			 + "   	   SUM(tblDctoDetalle.vrCredito) AS vrCredito                                        "
			 + "    FROM    tblDcto                                                                       "
			 + "    INNER JOIN tblDctoDetalle                                                             "
			 + "    ON tblDcto.idLocal = tblDctoDetalle.idLocal                                           "
			 + "    AND tblDcto.idTipoCpte = tblDctoDetalle.idTipoCpte                                    "
			 + "    AND tblDcto.idCpte = tblDctoDetalle.idCpte                                            "
			 + "    WHERE tblDcto.idLocal = ?1                                                            "
			 + "    AND   tblDcto.idPeriodo = ?2                                                      "
			 + "    GROUP BY tblDctoDetalle.idLocal,                                                      "
			 + "         tblDctoDetalle.idCuentaAux                                                       "
			 + "     ) AS tmpTOT                                                                          "
			 + "      ON tmpTOT.idLocal  = tblPucAux.idLocal                                              "
			 + "      AND tmpTOT.idCuentaAux  = tblPucAux.idCuentaAux                                     "
			 + "     WHERE tblPucAux.idLocal= ?1                                                          "
			 + "    ORDER BY 1,11,9,7,5,2                                                                 ",
             nativeQuery = true)
	     List<TblPucDTO> RepEstadoSituacionFinanciera( int idLocal, int idPeriodo);
}
