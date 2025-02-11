package com.contabook.Repository.DBMailMarketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.contabook.Model.DBMailMarketing.TblPuc;

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
}
