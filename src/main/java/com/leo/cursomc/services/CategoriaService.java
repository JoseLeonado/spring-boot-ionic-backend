package com.leo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.leo.cursomc.domain.Categoria;
import com.leo.cursomc.repositories.CategoriaRepository;
import com.leo.cursomc.services.exception.DataIntegrityException;
import com.leo.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria inserir(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Categoria atualizar(Categoria obj) {
		buscar(obj.getId());
		return repo.save(obj);
	}

	public void deletar(Integer id) {
		buscar(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produto");
		}
	}
	
	public List<Categoria> buscarTodos(){
		return repo.findAll();
	}
	
	public Page<Categoria> buscarPagina(Integer pagina, Integer linhasPorPagina, String ordenarPor, String direcao){
		PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Direction.valueOf(direcao),
				ordenarPor);
		return repo.findAll(pageRequest);
	}

}
