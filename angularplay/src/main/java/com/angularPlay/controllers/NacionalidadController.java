package com.angularPlay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angularPlay.model.entities.Nacionalidad;
import com.angularPlay.model.repositories.NacionalidadRepository;



@CrossOrigin
@RestController
public class NacionalidadController {

	@Autowired
	NacionalidadRepository nacionalidadRep;
	
	@GetMapping("nacionalidad/all")
	public Iterable<Nacionalidad> getAllNacionalidades () {
		return this.nacionalidadRep.findAll();
	}
}
