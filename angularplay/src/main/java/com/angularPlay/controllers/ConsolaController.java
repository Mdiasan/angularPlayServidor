package com.angularPlay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angularPlay.model.entities.Consola;
import com.angularPlay.model.entities.Nacionalidad;
import com.angularPlay.model.repositories.ConsolaRepository;
import com.angularPlay.model.repositories.NacionalidadRepository;



@CrossOrigin
@RestController
public class ConsolaController {

	@Autowired
	ConsolaRepository consolaRep;
	
	@GetMapping("consola/all")
	public Iterable<Consola> getAllConsolas () {
		return this.consolaRep.findAll();
	}
}
