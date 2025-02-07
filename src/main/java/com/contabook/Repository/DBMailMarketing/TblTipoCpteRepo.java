package com.contabook.Repository.DBMailMarketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.contabook.Model.DBMailMarketing.TblTipoCpte;

@Repository
public interface TblTipoCpteRepo extends JpaRepository<TblTipoCpte, Integer> {
	
	
	@Query(value = "SELECT * " + 
			"FROM BDMailMarketing.dbo.tblTipoCpte ",
			nativeQuery = true)
	List<TblTipoCpte> ListaComprobantes();
	
	
	
	@Modifying
	  @Transactional
	  @Query(value = "    INSERT INTO dbo.tblTipoCpte (idLocal   "
			  + "          ,idTipoCpte                        "
			  + "          ,nombreCmpte                        "
			  + "          ,estado                             "
			  + "          ,signo                              "
			  + "          ,idSeq                              "
			  + "          ,idAlcance)                         "
			  + "    VALUES ( ?1,                              "
			  + "        ?2,                                   "
			  + "		 ?3,                                   "
			  + "        ?4,                                   "
			  + "        ?5,                                   "
			  + "		 ?6,                                   "
			  + "        ?7 )                                  ", nativeQuery = true)
	  public void ingresaComprobante(int idLocal, int idTipoCpte,  String nombreCmpte, int estado, int signo, int idSeq, int idAlcance);
	
	
	@Query(value = "SELECT MAX (idTipoCpte) AS idTipoCpte " + 
			"FROM BDMailMarketing.dbo.tblTipoCpte " +
			"where idlocal = ?1 ",
			nativeQuery = true)
	Integer MaximoIdTipoCpte(int idLocal);
	
	
	@Query(value = "SELECT * " + 
			"FROM BDMailMarketing.dbo.tblTipoCpte " +
			"Where idLocal = ?1 " +
			"and idTipoCpte = ?2 ",
			nativeQuery = true)
	List<TblTipoCpte> obtenerCpteXId(int idLocal, int idTipoCpte);
	
	
	 @Modifying
	  @Transactional
	  @Query(value = "UPDATE tblTipoCpte SET nombreCmpte = ?1, estado = ?2, signo = ?3, idSeq = ?4, idAlcance = ?5 " +
	                 "WHERE tblTipoCpte.idLocal = ?6 " +
	                 "AND tblTipoCpte.idTipoCpte = ?7 ", nativeQuery = true)
	  public void actualizarComprobante(String nombreCmpte,  int estado, int signo, int idSeq, int idAlcance, int idLocal, int idTipoCpte) ;
	 
	 
		@Query(value = "SELECT idTipoOrden " + 
				"FROM BDMailMarketing.dbo.tblTipoCpte " +
				"where idTipoCpte = ?1 ",
				nativeQuery = true)
		Integer obtenerIdTipoOrden(int idTipoCpte);
		
		@Query(value = "SELECT nombreCmpte " + 
				"FROM BDMailMarketing.dbo.tblTipoCpte " +
				"where idTipoCpte = ?1 ",
				nativeQuery = true)
		String obtenerNombreComprobante(int idTipoCpte);
	

}
