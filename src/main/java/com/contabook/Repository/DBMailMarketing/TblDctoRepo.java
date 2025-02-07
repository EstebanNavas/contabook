package com.contabook.Repository.DBMailMarketing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.contabook.Model.DBMailMarketing.TblDcto;

@Repository
public interface TblDctoRepo extends JpaRepository<TblDcto, Integer> {
	
	
	 @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END " +
             "FROM BDMailMarketing.dbo.tblDcto " +
             "WHERE tblDcto.idLocal = ?1 " +
             "AND tblDcto.idTipoOrden = ?2 " +
             "AND tblDcto.idDcto = ?3 ",
             nativeQuery = true)
	 Boolean ExisteDcto(int idLocal, int idTipoOrden, int idDcto);
	 
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

}
