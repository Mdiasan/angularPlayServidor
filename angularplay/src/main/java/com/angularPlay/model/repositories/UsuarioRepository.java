package com.angularPlay.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.angularPlay.model.entities.Usuario;


@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
	
    public Usuario findByUsuarioAndPassword(String name,String password);

}
