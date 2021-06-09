package com.angularPlay.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.angularPlay.model.entities.Consola;


@Repository
public interface ConsolaRepository extends CrudRepository<Consola, Integer> {

}
