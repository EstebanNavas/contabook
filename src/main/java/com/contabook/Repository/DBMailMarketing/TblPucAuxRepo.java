package com.contabook.Repository.DBMailMarketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.contabook.Model.DBMailMarketing.TblPuc;
import com.contabook.Model.DBMailMarketing.TblPucAux;

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

}
