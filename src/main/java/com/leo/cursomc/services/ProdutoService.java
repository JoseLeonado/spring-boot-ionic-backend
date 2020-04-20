package com.leo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.leo.cursomc.domain.Categoria;
import com.leo.cursomc.domain.Produto;
import com.leo.cursomc.repositories.CategoriaRepository;
import com.leo.cursomc.repositories.ProdutoRepository;
import com.leo.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto buscar(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		 "Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName())); 
	}

	public Page<Produto> pesquisar(String nome, List<Integer> ids, Integer pagina, Integer linhasPorPagina, String ordenarPor, String direcao){
		PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Direction.valueOf(direcao), ordenarPor);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.pesquisar(nome, categorias, pageRequest);
	}
	
}
