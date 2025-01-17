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
	  @Query(value = "    INSERT INTO dbo.tblTipoCpte (idTipoCpte   "
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
			  + "		 ?6 )                                  "
			  + "                                              ", nativeQuery = true)
	  public void ingresaComprobante(int idLocal, String nombreCmpte, int estado, int signo, int idSeq, int idAlcance);

}
