package com.leo.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.leo.cursomc.domain.Categoria;
import com.leo.cursomc.domain.Cidade;

public class CidadeDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;

	public CidadeDTO() {

	}
	
	public CidadeDTO(Cidade obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
