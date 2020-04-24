package com.leo.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leo.cursomc.domain.Categoria;
import com.leo.cursomc.domain.Cliente;
import com.leo.cursomc.domain.ItemPedido;
import com.leo.cursomc.domain.PagamentoComBoleto;
import com.leo.cursomc.domain.Pedido;
import com.leo.cursomc.domain.enums.EstadoPagamento;
import com.leo.cursomc.repositories.ItemPedidoRepository;
import com.leo.cursomc.repositories.PagamentoRepository;
import com.leo.cursomc.repositories.PedidoRepository;
import com.leo.cursomc.security.UserSS;
import com.leo.cursomc.services.exception.AuthorizationException;
import com.leo.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		 "Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName())); 
	}
	
	@Transactional
	public Pedido inserir(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.buscar(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.buscar(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	public Page<Pedido> buscarPagina(Integer pagina, Integer linhasPorPagina, String ordenarPor, String direcao){
		
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Direction.valueOf(direcao), ordenarPor);
		Cliente cliente = clienteService.buscar(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
	
}
