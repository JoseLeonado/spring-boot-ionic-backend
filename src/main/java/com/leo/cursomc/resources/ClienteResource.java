package com.leo.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.leo.cursomc.domain.Cliente;
import com.leo.cursomc.dto.ClienteDTO;
import com.leo.cursomc.dto.ClienteNewDTO;
import com.leo.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscar(@PathVariable Integer id) {
		Cliente obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@Transactional
	@PostMapping
	public ResponseEntity<Void> inserir(@Valid @RequestBody ClienteNewDTO objDto){
		Cliente obj = service.fromDTO(objDto);
		obj = service.inserir(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> atualizar(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
		Cliente obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.atualizar(obj);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Integer id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping()
	public ResponseEntity<List<ClienteDTO>> buscarTodos() {
		List<Cliente> lista = service.buscarTodos();
		List<ClienteDTO> listaDto = lista.stream().map(obj -> new ClienteDTO(obj)) .collect(Collectors.toList());
		return ResponseEntity.ok().body(listaDto);
	}
	
	@GetMapping("/pagina")
	public ResponseEntity<Page<ClienteDTO>> buscarPagina(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "linhasPorPagina", defaultValue = "24")  Integer linhasPorPagina, 
			@RequestParam(value = "ordernarPor", defaultValue = "nome") String ordenarPor, 
			@RequestParam(value = "direcao", defaultValue = "ASC") String direcao) {
		Page<Cliente> lista = service.buscarPagina(pagina, linhasPorPagina, ordenarPor, direcao);
		Page<ClienteDTO> listaDto = lista.map(obj -> new ClienteDTO(obj));
		return ResponseEntity.ok().body(listaDto);
	}

}
