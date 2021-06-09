package com.angularPlay.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.angularPlay.jwtSecurity.AutenticadorJWT;
import com.angularPlay.model.entities.Consola;
import com.angularPlay.model.entities.Juego;
import com.angularPlay.model.entities.Usuario;
import com.angularPlay.model.repositories.JuegoRepository;
import com.angularPlay.model.repositories.UsuarioRepository;
import com.angularPlay.model.repositories.ConsolaRepository;


@CrossOrigin
@RestController
public class JuegoController {
	
	public static final int DISPONIBLES = 0;
	public static final int PS4 = 1;
	public static final int PC = 2;
	public static final int NSWITCH = 3;
	
	

	@Autowired JuegoRepository juegoRepo;
	@Autowired UsuarioRepository usuRep;
	@Autowired ConsolaRepository consolRep;
	
	private DTO getDTOFromJuego(Juego j, boolean incluirImagen) {
		DTO dto = new DTO(); // Voy a devolver un dto
		if (j != null) {
			dto.put("id", j.getId());
			dto.put("idusuario", j.getUsuario());
			dto.put("nombre", j.getNombre());
			dto.put("descripcion", j.getDescripcion());
			dto.put("precio", j.getPrecio());
			dto.put("trailer", j.getTrailer());
			dto.put("stock", j.getStock());
			dto.put("consola", j.getConsola().getId());
			dto.put("imagen", incluirImagen? j.getImagen() : "");
		}
		return dto;
	}
	
	@GetMapping("/juegos/tipos")
	public DTO mensajesPorTipoParaUsuarioAutenticado (int tipo, HttpServletRequest request) {
		// Obtengo el id del usuario autenticado, mediante un JWT.
		int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request);
		
		DTO dtoResultado = new DTO(); // Creo un nuevo DTO, que voy a devolver al cliente y asigno un fallo como salida
		dtoResultado.put("result", "fail");
		
		List<DTO> listaJuegosEnDTO = new ArrayList<DTO>();		
		try {
			List<Juego> juegos = new ArrayList<Juego>();
			// Obtengo los juegos del servidor, en función del tipo de juego que se desea
			switch (tipo) {
			case DISPONIBLES:
				juegos = this.juegoRepo.getDisponibles();
				break;
			case PS4: 
				juegos = this.juegoRepo.getJuegosPS4();
				break;
			case PC:
				juegos = this.juegoRepo.getJuegosPC();
				break;
			case NSWITCH:
				juegos = this.juegoRepo.getJuegosNSwitch();
				break;
			}
			
			// Por cada juego de la lista, obtengo un DTO con los datos que realmente quiero enviar al cliente
			for (Juego j : juegos) {
				listaJuegosEnDTO.add(getDTOFromJuego(j, true));
			}
			
			// Si llegamos hasta aquí sin errores, cambio el valor del resultado de la operación, para indicar éxito
			dtoResultado.put("result", "ok");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		dtoResultado.put("juegos", listaJuegosEnDTO);
		return dtoResultado;
	}
	
	@PostMapping("/juegos/accionSobreJuegos")
	public  DTO accionSobreJuegos(@RequestBody DatosAccionesSobreJuegos datosAcciones, HttpServletRequest request) {
		DTO dto = new DTO(); // Voy a devolver un dto
		dto.put("result", "fail"); // Asumo que voy a fallar, si todo va bien se sobrescribe este valor
		int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request); // Obtengo el usuario autenticado, por su JWT

		try {
			Usuario usuarioAutenticado = usuRep.findById(idUsuAutenticado).get();
			int idJuego = datosAcciones.id;
			Juego j = juegoRepo.findById(idJuego).get();
				switch (datosAcciones.tipoAccion) {
				case 0: // marca como juego no  alquilado
			//		j.setAlquilado(false);
			//		j.setUsuario(null);
			//		this.juegoRepo.save(j);
			//		break;
		//		case 1: // marca como juego alquilado
				case 2: // borro el juego
					juegoRepo.delete(j);
					break;
				}

			dto.put("result", "ok"); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return dto;
	}
	
	@PostMapping("/juego/add")
	public DTO addJuego (@RequestBody DTO dtoRecibido, HttpServletRequest request) {
		DTO dto = new DTO(); // Voy a devolver un dto
		dto.put("result", "fail"); // Asumo que voy a fallar, si todo va bien se sobrescribe este valor

		try {
			Juego nuevojuego = new Juego(); // Localizo al usuario
			nuevojuego.setNombre((String) dtoRecibido.get("nombre"));
			nuevojuego.setDescripcion((String) dtoRecibido.get("descripcion"));
			nuevojuego.setPrecio((Integer) dtoRecibido.get("precio"));
			nuevojuego.setTrailer((String) dtoRecibido.get("trailer"));
			nuevojuego.setStock((Integer) dtoRecibido.get("stock"));
			String consola= dtoRecibido.get("consola").toString();
			nuevojuego.setConsola(this.consolRep.findById(Integer.parseInt(consola)).get());
			nuevojuego.setImagen(Base64.decodeBase64((String) dtoRecibido.get("imagen")));
			juegoRepo.save(nuevojuego);  // Guardo el usuario en la unidad de persistencia
			dto.put("result", "ok"); // Devuelvo éxito
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return dto;
	}
	
	@PostMapping("/juego/update")
	public DTO modificaDatosJuego (int id, @RequestBody DTO dtoRecibido, HttpServletRequest request) {
		DTO dto = new DTO(); // Voy a devolver un dto
		dto.put("result", "fail"); // Asumo que voy a fallar, si todo va bien se sobrescribe este valor

		try {
			Juego juegoEditado = juegoRepo.findById(id).get(); // Localizo al usuario
			// Cargo los datos recibidos en el juego localizado por su id.
			juegoEditado.setNombre((String) dtoRecibido.get("nombre"));
			juegoEditado.setDescripcion((String) dtoRecibido.get("descripcion"));
			juegoEditado.setPrecio((Integer) dtoRecibido.get("precio"));
			juegoEditado.setTrailer((String) dtoRecibido.get("trailer"));
			juegoEditado.setStock((Integer) dtoRecibido.get("stock"));
			juegoEditado.setConsola(this.consolRep.findById((int) dtoRecibido.get("consola")).get());
			juegoEditado.setImagen(Base64.decodeBase64((String) dtoRecibido.get("imagen")));
			juegoRepo.save(juegoEditado);
			dto.put("result", "ok"); // Devuelvo éxito
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return dto;
	}
	
	@GetMapping("/juego/get")
	public DTO getUsuario (int id) {

		// Intento localizar un juego a partir de su id
		Juego j = juegoRepo.findById(id).get();

		// Finalmente devuelvo el DTO
		return getDTOFromJuego(j, true);
	}
	
		

	}
class DatosAccionesSobreJuegos {
	int tipoAccion;  
	int id;
	public DatosAccionesSobreJuegos(int tipoAccion, int id) {
		super();
		this.tipoAccion = tipoAccion;
		this.id = id;
	}
}
