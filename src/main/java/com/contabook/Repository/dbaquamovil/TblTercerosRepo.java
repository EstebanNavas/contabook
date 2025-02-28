package com.contabook.Repository.dbaquamovil;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.contabook.Model.dbaquamovil.TblTerceros;


@Repository
public interface TblTercerosRepo extends JpaRepository<TblTerceros, Integer> {
	
	@Query(value = "SELECT * " + 
	        "FROM tblTerceros " +
			"WHERE idLocal = ?1 " +
			"AND idTipoTercero IN (1,2,3, 4) " +
			"AND estado= 1 " +
			"ORDER BY nombreTercero ",
			nativeQuery = true)
	List<TblTerceros> ListaTercerosSuscriptor(int idLocal);

}
