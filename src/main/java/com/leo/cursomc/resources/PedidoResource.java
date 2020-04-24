package com.leo.cursomc.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.leo.cursomc.domain.Categoria;
import com.leo.cursomc.domain.Pedido;
import com.leo.cursomc.dto.CategoriaDTO;
import com.leo.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<Pedido> buscar(@PathVariable Integer id) {
		Pedido obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<Void> inserir(@Valid @RequestBody Pedido obj){
		obj = service.inserir(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping()
	public ResponseEntity<Page<Pedido>> buscarPagina(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "linhasPorPagina", defaultValue = "24")  Integer linhasPorPagina, 
			@RequestParam(value = "ordernarPor", defaultValue = "instante") String ordenarPor, 
			@RequestParam(value = "direcao", defaultValue = "DESC") String direcao) {
		Page<Pedido> lista = service.buscarPagina(pagina, linhasPorPagina, ordenarPor, direcao);
		return ResponseEntity.ok().body(lista);
	}
	
}
