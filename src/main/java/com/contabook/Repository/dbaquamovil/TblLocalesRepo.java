package com.contabook.Repository.dbaquamovil;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.contabook.Model.dbaquamovil.TblLocales;

@Repository
public interface TblLocalesRepo extends JpaRepository<TblLocales, Integer> {

	Optional<TblLocales> findByIdLocal(Integer idLocal); // Buscamos el idLocal
	
	@Query("SELECT t.pathReport FROM TblLocales t " +
	"WHERE t.idLocal = :idLocal ")
	String rutaReporte(@Param("idLocal") int idLocal);
	
	@Query(value = "SELECT tblLocales.razonSocial " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	String ObtenerRazonSocial(int idLocal);
	
	@Query(value = "SELECT tblLocales.nit " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	String ObtenerNit(int idLocal);
	
	@Query(value = "SELECT tblLocales.direccion " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	String ObtenerDireccion(int idLocal);
	
	@Query(value = "SELECT tblLocales.telefono " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	String ObtenerTelefono(int idLocal);
	
	@Query(value = "SELECT tblLocales.ciudad " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	String ObtenerCiudad(int idLocal);
	
	@Query(value = "SELECT tblLocales.token " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	String ObtenerToken(int idLocal);
	
	@Query(value = "SELECT tblLocales.prefijo " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	String ObtenerPrefijo(int idLocal);
	
	@Query(value = "SELECT tblLocales.idResolucion " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	String ObtenerIdResolucion(int idLocal);
	
	@Query(value = "SELECT tblLocales.periodoFactura " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	Integer  ObtenerPeriodoFactura(int idLocal);
	
	@Query(value = "SELECT * " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	List<TblLocales>  ObtenerLocal(int idLocal);
	
	@Query(value = "SELECT tblLocales.idApiFinancia " + 
			"FROM bdaquamovil.dbo.tblLocales " +
			"WHERE tblLocales.idLocal = ?1 ",
			nativeQuery = true)
	Integer ObtenerIdApi(int idLocal);	
}
