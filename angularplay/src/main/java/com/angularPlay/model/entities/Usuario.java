package com.angularPlay.model.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String email;

	@Lob
	private byte[] imagen;

	private String nombre;

	private String password;

	private String rol;

	private String usuario;

	//bi-directional many-to-one association to Compra
	@OneToMany(mappedBy="usuario")
	@JsonIgnore
	private List<Compra> compras;

	//bi-directional many-to-one association to Juego
	@OneToMany(mappedBy="usuario")
	@JsonIgnore
	private List<Juego> juegos;

	//bi-directional many-to-one association to Nacionalidad
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private Nacionalidad nacionalidad;

	//bi-directional many-to-one association to Valoracion
	@OneToMany(mappedBy="usuario")
	@JsonIgnore
	private List<Valoracion> valoracions;

	public Usuario() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getImagen() {
		return this.imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<Compra> getCompras() {
		return this.compras;
	}

	public void setCompras(List<Compra> compras) {
		this.compras = compras;
	}

	public Compra addCompra(Compra compra) {
		getCompras().add(compra);
		compra.setUsuario(this);

		return compra;
	}

	public Compra removeCompra(Compra compra) {
		getCompras().remove(compra);
		compra.setUsuario(null);

		return compra;
	}

	public List<Juego> getJuegos() {
		return this.juegos;
	}

	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}

	public Juego addJuego(Juego juego) {
		getJuegos().add(juego);
		juego.setUsuario(this);

		return juego;
	}

	public Juego removeJuego(Juego juego) {
		getJuegos().remove(juego);
		juego.setUsuario(null);

		return juego;
	}

	public Nacionalidad getNacionalidad() {
		return this.nacionalidad;
	}

	public void setNacionalidad(Nacionalidad nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public List<Valoracion> getValoracions() {
		return this.valoracions;
	}

	public void setValoracions(List<Valoracion> valoracions) {
		this.valoracions = valoracions;
	}

	public Valoracion addValoracion(Valoracion valoracion) {
		getValoracions().add(valoracion);
		valoracion.setUsuario(this);

		return valoracion;
	}

	public Valoracion removeValoracion(Valoracion valoracion) {
		getValoracions().remove(valoracion);
		valoracion.setUsuario(null);

		return valoracion;
	}

}