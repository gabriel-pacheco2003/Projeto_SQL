package br.com.gabi_la_boutique.boutique.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.gabi_la_boutique.boutique.BaseTests;
import br.com.gabi_la_boutique.boutique.models.Sell;
import br.com.gabi_la_boutique.boutique.services.exceptions.IntegrityViolation;
import br.com.gabi_la_boutique.boutique.services.exceptions.ObjectNotFound;
import br.com.gabi_la_boutique.boutique.utils.DateUtils;
import jakarta.transaction.Transactional;

@Transactional
public class SellServiceTest extends BaseTests {

	@Autowired
	SellService sellService;

	@Autowired
	ClientService clientService;

	@Test
	@DisplayName("Teste busca por ID")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void findByIdTest() {
		var sell = sellService.findById(1);
		assertEquals(1, sell.getId());
		assertEquals("Cliente 1", sell.getClient().getName());
		assertEquals(2023, sell.getDate().getYear());
	}

	@Test
	@DisplayName("Teste busca por ID inexistente")
	void findByIdNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> sellService.findById(1));
		assertEquals("Venda 1 não encontrada", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir venda")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void insertSellTest() {
		Sell sell = new Sell(1, clientService.findById(1), DateUtils.stringToDate("31-01-2023"));
		sellService.insert(sell);
		assertEquals(1, sell.getId());
		assertEquals("Cliente 1", sell.getClient().getName());
		assertEquals(2023, sell.getDate().getYear());
	}

	@Test
	@DisplayName("Teste inserir cliente nulo")
	void insertNullClientTest() {
		Sell sell = new Sell(null, null, DateUtils.stringToDate("01-01-2023"));
		var exception = assertThrows(IntegrityViolation.class, () -> sellService.insert(sell));
		assertEquals("Cliente inválido", exception.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void listAllTest() {
		assertEquals(3, sellService.listAll().size());
	}

	@Test
	@DisplayName("Teste listar todos sem cadastros existentes")
	void listAllNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> sellService.listAll());
		assertEquals("Nenhuma venda cadastrada", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar venda")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void updateSellTest() {
		assertEquals(2023, sellService.findById(1).getDate().getYear());
		Sell sellUpdate = new Sell(1, clientService.findById(1), DateUtils.stringToDate("01-02-2023"));
		sellService.update(sellUpdate);
		assertEquals(2023, sellService.findById(1).getDate().getYear());
	}

	@Test
	@DisplayName("Teste alterar venda com data inválida")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void updateSellWithInvalidDateTest() {
		assertEquals(2023, sellService.findById(1).getDate().getYear());
		Sell sellUpdate = new Sell(1, clientService.findById(1), DateUtils.stringToDate("01-01-2025")); 
		var exception = assertThrows(IntegrityViolation.class, () -> sellService.update(sellUpdate));
		assertEquals("Data inválida", exception.getMessage());
	} 

	@Test
	@DisplayName("Teste remover venda")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void deleteSellTest() {
		sellService.delete(1);
		assertEquals(2, sellService.listAll().size());
	}

	@Test
	@DisplayName("Teste remover venda inexistente")
	void deleteSellNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> sellService.delete(1));
		assertEquals("Venda 1 não encontrada", exception.getMessage());
	}

	@Test
	@DisplayName("Teste busca por cliente")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void findByClientTest() {
		assertEquals(2, sellService.findByClient(clientService.findById(1)).size());
	}

	@Test
	@DisplayName("Teste busca por cliente não encontrado")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void findByClientNotFoundTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> sellService.findByClient(clientService.findById(3)));
		assertEquals("Nenhuma venda encontrada", exception.getMessage());
	}

	@Test
	@DisplayName("Teste busca por data")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void findByDateTest() {
		assertEquals(1, sellService.findByDateOrderByDateDesc(DateUtils.stringToDate("01-01-2023")).size());
	}

	@Test
	@DisplayName("Teste busca por data não encontrada")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void findByDateNotFoundTest() {
		var exception = assertThrows(ObjectNotFound.class,
				() -> sellService.findByDateOrderByDateDesc(DateUtils.stringToDate("01-01-2020")));
		assertEquals("Nenhuma venda encontrada", exception.getMessage());
	}

	@Test
	@DisplayName("Teste busca por data entre")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void findByDateBetweenTest() {
		assertEquals(3, sellService
				.findByDateBetween(DateUtils.stringToDate("01-01-2022"), DateUtils.stringToDate("01-06-2023")).size());
	}

	@Test
	@DisplayName("Teste busca por data entre ( não encontrada )")
	@Sql("classpath:/resources/sqls/cidade.sql")
	@Sql("classpath:/resources/sqls/endereco.sql")
	@Sql("classpath:/resources/sqls/cliente.sql")
	@Sql("classpath:/resources/sqls/venda.sql")
	void findByDateBetweenNotFoundTest() {
		var exception = assertThrows(ObjectNotFound.class,
				() -> sellService.findByDateBetween(DateUtils.stringToDate("01-01-2022"), DateUtils.stringToDate("01-02-2022")));
		assertEquals("Nenhuma venda encontrada", exception.getMessage());
	}
	
	

}
