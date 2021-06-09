package com.angularPlay.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.angularPlay.model.entities.Juego;

@Repository
public interface JuegoRepository extends CrudRepository<Juego, Integer> {

	// Juegos Alquilados	
	//@Query(value = "SELECT distinct j.* FROM juego as j,"
	//		+ " usuario as u  where j.idusuario = u.id and u.id = ? and alquilado = 1 order by j.id;", nativeQuery = true)
	//public List<Juego> getJuegosAlquiladosDeUsuario(int idUsuario);

	// Juegos por consola
	@Query(value = "SELECT * from juego where consola_id = 1 ", nativeQuery = true)
	public List<Juego> getJuegosPS4();
	
	@Query(value = "SELECT * from juego where consola_id = 2 ", nativeQuery = true)
	public List<Juego> getJuegosPC();
	
	@Query(value = "SELECT * from juego where consola_id = 3 ", nativeQuery = true)
	public List<Juego> getJuegosNSwitch();
	
	@Query(value = "SELECT * from juego where stock != 0", nativeQuery = true)
	public List<Juego> getDisponibles();

	

}
