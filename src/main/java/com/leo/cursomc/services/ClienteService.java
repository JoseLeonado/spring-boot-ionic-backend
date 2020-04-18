package com.leo.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leo.cursomc.domain.Categoria;
import com.leo.cursomc.domain.Cliente;
import com.leo.cursomc.repositories.CategoriaRepository;
import com.leo.cursomc.repositories.ClienteRepository;
import com.leo.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente buscar(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		 "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName())); 
	}

}
