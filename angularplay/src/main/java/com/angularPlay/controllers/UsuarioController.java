package com.angularPlay.controllers;


import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.angularPlay.jwtSecurity.AutenticadorJWT;
import com.angularPlay.model.entities.Juego;
import com.angularPlay.model.entities.Usuario;
import com.angularPlay.model.repositories.NacionalidadRepository;
import com.angularPlay.model.repositories.UsuarioRepository;


@CrossOrigin
@RestController
public class UsuarioController {

	@Autowired
	UsuarioRepository usuRep;
	@Autowired
	NacionalidadRepository nacionalidadRep;

	
	/**
	 * Obtiene y devuelve los datos del usuario autenticado
	 */
	@GetMapping("/usuario/getAutenticado")
	public DTO getUsuarioAutenticado (boolean imagen, HttpServletRequest request) {
		int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request); // Obtengo el usuario autenticado, por su JWT

		// Intento localizar un usuario a partir de su id
		Usuario usuAutenticado = usuRep.findById(idUsuAutenticado).get();

		// Finalmente devuelvo el JWT creado, puede estar vacío si la autenticación no ha funcionado
		return getDTOFromUsuario(usuAutenticado, imagen);
	}
	
	@PostMapping("/usuario/add")
	public DTO addUser (@RequestBody DTO dtoRecibido, HttpServletRequest request) {
		DTO dto = new DTO(); // Voy a devolver un dto
		dto.put("result", "fail"); // Asumo que voy a fallar, si todo va bien se sobrescribe este valor

		try {
			Usuario nuevousuario = new Usuario(); // Localizo al usuario
			nuevousuario.setUsuario((String) dtoRecibido.get("usuario"));
			nuevousuario.setEmail((String) dtoRecibido.get("email"));
			nuevousuario.setNombre((String) dtoRecibido.get("nombre"));
			String nacionalidad= dtoRecibido.get("nacionalidad").toString();
			nuevousuario.setNacionalidad(this.nacionalidadRep.findById(Integer.parseInt(nacionalidad)).get());
			String password = (String) dtoRecibido.get("password");  // Recibo la password que llega en el dtoRecibido
			nuevousuario.setPassword(password);
			usuRep.save(nuevousuario);  // Guardo el usuario en la unidad de persistencia
			dto.put("result", "ok"); // Devuelvo éxito
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return dto;
	}
	
	
	/**
	 * Obtiene y devuelve los datos de un usuario, a través de su id
	 */
	@GetMapping("/usuario/get")
	public DTO getUsuario (int id, boolean imagen) {

		// Intento localizar un usuario a partir de su id
		Usuario usu = usuRep.findById(id).get();

		// Finalmente devuelvo el DTO
		return getDTOFromUsuario(usu, imagen);
	}
	
	
	/**
	 * Fabrica un DTO con los datos que queremos enviar de un usuario.
	 * @param usu
	 * @param incluirImagen
	 * @return
	 */
	private DTO getDTOFromUsuario (Usuario usu, boolean incluirImagen) {
		DTO dto = new DTO(); // Voy a devolver un dto
		if (usu != null) {
			dto.put("id", usu.getId());
			dto.put("nombre", usu.getNombre());
			dto.put("usuario", usu.getUsuario());
			dto.put("password", usu.getPassword());
			dto.put("email", usu.getEmail());
			dto.put("rol", usu.getRol());
			dto.put("imagen", incluirImagen? usu.getImagen() : "");
			dto.put("nacionalidad", usu.getNacionalidad().getId());
		}
		return dto;
	}
	
	
	/**
	 * Autentica un usuario, dados su datos de acceso: nombre de usuario y contraseña
	 */
	@PostMapping("/usuario/autentica")
	public DTO autenticaUsuario (@RequestBody DatosAutenticacionUsuario datos) {
		DTO dto = new DTO(); // Voy a devolver un dto

		// Intento localizar un usuario a partir de su nombre de usuario y su password
		Usuario usuAutenticado = usuRep.findByUsuarioAndPassword(datos.usuario, datos.password);
		if (usuAutenticado != null) {
			dto.put("jwt", AutenticadorJWT.codificaJWT(usuAutenticado));
		}

		return dto;
	}
	
	@PostMapping("/usuario/autentica2")
	public DTO autenticaUsuario2 (@RequestBody DatosAutenticacionUsuario datos) {
		DTO dto = new DTO(); // Voy a devolver un dto

		// Intento localizar un usuario a partir de su nombre de usuario y su password
		Usuario usuAutenticado = usuRep.findByUsuarioAndPassword(datos.usuario, datos.password);
		if (usuAutenticado != null) {
			dto.put("jwt", AutenticadorJWT.codificaJWT(usuAutenticado));
		}

		return dto;
	}
	
	
	
	/**
	 * usado para comprobar si una contraseña es igual a la contraseña del usuario autenticado
	 */
	@PostMapping("/usuario/ratificaPassword")
	public DTO ratificaPassword (@RequestBody DTO dtoRecibido, HttpServletRequest request) {
		DTO dto = new DTO(); // Voy a devolver un dto
		dto.put("result", "fail"); // Asumo que voy a fallar, si todo va bien se sobrescribe este valor

		int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request); // Obtengo el usuario autenticado, por su JWT

		try {
			Usuario usuarioAutenticado = usuRep.findById(idUsuAutenticado).get(); // Localizo todos los datos del usuario
			String password = (String) dtoRecibido.get("password");  // Compruebo la contraseña
			if (password.equals(usuarioAutenticado.getPassword())) {
				dto.put("result", "ok"); // Devuelvo éxito, las contraseñas son iguales
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return dto;
	}
	
	
	

	/**
	 * Recibe una nueva password para el usuario autenticado y la modifica en la unidad de persistencia
	 */
	@PostMapping("/usuario/modificaPassword")
	public DTO modificaPassword (@RequestBody DTO dtoRecibido, HttpServletRequest request) {
		DTO dto = new DTO(); // Voy a devolver un dto
		dto.put("result", "fail"); // Asumo que voy a fallar, si todo va bien se sobrescribe este valor

		int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request); // Obtengo el usuario autenticado, por su JWT

		try {
			Usuario usuarioAutenticado = usuRep.findById(idUsuAutenticado).get(); // Localizo al usuario
			String password = (String) dtoRecibido.get("password");  // Recibo la password que llega en el dtoRecibido
			usuarioAutenticado.setPassword(password); // Modifico la password
			usuRep.save(usuarioAutenticado);  // Guardo el usuario, con nueva password, en la unidad de persistencia
			dto.put("result", "ok"); // Devuelvo éxito
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return dto;
	}
	
	
	

	/**
	 * Recibe los datos personales del usuario y los modifica en la unidad de persistencia
	 */
	@PostMapping("/usuario/update")
	public DTO modificaDatosUsuario (@RequestBody DTO dtoRecibido, HttpServletRequest request) {
		DTO dto = new DTO(); // Voy a devolver un dto
		dto.put("result", "fail"); // Asumo que voy a fallar, si todo va bien se sobrescribe este valor

		int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request); // Obtengo el usuario autenticado, por su JWT

		try {
			Usuario usuarioAutenticado = usuRep.findById(idUsuAutenticado).get(); // Localizo al usuario
			// Cargo los datos recibidos en el usuario localizado por su id.
			usuarioAutenticado.setUsuario((String) dtoRecibido.get("usuario"));
			usuarioAutenticado.setEmail((String) dtoRecibido.get("email"));
			usuarioAutenticado.setNombre((String) dtoRecibido.get("nombre"));
			usuarioAutenticado.setImagen(Base64.decodeBase64((String) dtoRecibido.get("imagen")));
			usuarioAutenticado.setNacionalidad(this.nacionalidadRep.findById((int) dtoRecibido.get("nacionalidad")).get());
			usuRep.save(usuarioAutenticado);  // Guardo el usuario en la unidad de persistencia
			dto.put("result", "ok"); // Devuelvo éxito
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return dto;
	}
	
	
	

}


/**
 * Clase que contiene los datos de autenticacion del usuario
 */
class DatosAutenticacionUsuario {
	String usuario;
	String password;

	/**
	 * Constructor
	 */
	public DatosAutenticacionUsuario(String usuario, String password) {
		super();
		this.usuario = usuario;
		this.password = password;
	}
}
