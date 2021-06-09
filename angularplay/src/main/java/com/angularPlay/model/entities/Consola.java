package com.angularPlay.model.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the consola database table.
 * 
 */
@Entity
@NamedQuery(name="Consola.findAll", query="SELECT c FROM Consola c")
public class Consola implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String nombre;

	//bi-directional many-to-one association to Juego
	@OneToMany(mappedBy="consola")
	@JsonIgnore
	private List<Juego> juegos;

	public Consola() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Juego> getJuegos() {
		return this.juegos;
	}

	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}

	public Juego addJuego(Juego juego) {
		getJuegos().add(juego);
		juego.setConsola(this);

		return juego;
	}

	public Juego removeJuego(Juego juego) {
		getJuegos().remove(juego);
		juego.setConsola(null);

		return juego;
	}

}