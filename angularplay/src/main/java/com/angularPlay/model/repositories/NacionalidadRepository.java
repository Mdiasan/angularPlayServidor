package com.angularPlay.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.angularPlay.model.entities.Nacionalidad;


@Repository
public interface NacionalidadRepository extends CrudRepository<Nacionalidad, Integer> {

}
