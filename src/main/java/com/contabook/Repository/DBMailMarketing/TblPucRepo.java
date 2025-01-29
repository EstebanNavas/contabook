package com.contabook.Repository.DBMailMarketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.contabook.Model.DBMailMarketing.TblPuc;

@Repository
public interface TblPucRepo extends JpaRepository<TblPuc, Integer> {

	 @Query(value = "SELECT * " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE idCuenta < 10 " +
             "order by idCuenta  ",
             nativeQuery = true)
	 List<TblPuc> pucNivel1();
	 
	 @Query(value = "SELECT * " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE idCuenta >= ?1 " +
             "AND  idCuenta < ?2 " +
             "and idClase = ?3 " +
             "order by idCuenta ",
             nativeQuery = true)
	 List<TblPuc> pucNivel2(int idCuentaMenor, int idCuentaMayor, int idClase);
	 
	 @Query(value = "SELECT * " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE idCuenta >= ?1 " +
             "AND  idCuenta < ?2 " +
             "and idClase = ?3 ",
             nativeQuery = true)
	 List<TblPuc> pucNiveles(int idCuentaMenor, int idCuentaMayor, int idClase);
	 
	 
	 @Query(value = "SELECT nombreCuenta " +
             "FROM BDMailMarketing.dbo.tblPuc " +
             "WHERE idCuenta = ?1 " +
             "AND  idClase = ?2 ",
             nativeQuery = true)
	 String obtenerNombreCuenta(int idCuenta, int idClase);
}
