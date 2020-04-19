package com.leo.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.leo.cursomc.domain.enums.TipoCliente;
import com.leo.cursomc.dto.ClienteNewDTO;
import com.leo.cursomc.resources.exception.FieldMessage;
import com.leo.cursomc.services.validation.ultis.BR;

public class ClienteInserirValidator implements ConstraintValidator<ClienteInserir, ClienteNewDTO> {
	@Override
	public void initialize(ClienteInserir ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> lista = new ArrayList<>();
		
		if (objDto.getTipo().equals(TipoCliente.PESSOA_FISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			lista.add(new FieldMessage("CpfOuCnpj", "CPF inválido"));
		}
		
		if (objDto.getTipo().equals(TipoCliente.PESSOA_JURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			lista.add(new FieldMessage("CpfOuCnpj", "CNPJ inválido"));
		}
		
		for (FieldMessage e : lista) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return lista.isEmpty();
	}
}