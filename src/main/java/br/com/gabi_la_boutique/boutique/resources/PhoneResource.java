package br.com.gabi_la_boutique.boutique.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabi_la_boutique.boutique.models.Client;
import br.com.gabi_la_boutique.boutique.models.Phone;
import br.com.gabi_la_boutique.boutique.models.dto.PhoneDTO;
import br.com.gabi_la_boutique.boutique.services.ClientService;
import br.com.gabi_la_boutique.boutique.services.PhoneService;

@RestController
@RequestMapping("/phone")
public class PhoneResource {

	@Autowired
	private PhoneService service;

	@Autowired
	private ClientService clientService;

	@PostMapping
	public ResponseEntity<PhoneDTO> insert(@RequestBody PhoneDTO phoneDTO) {
		return ResponseEntity
				.ok(service.insert(new Phone(phoneDTO, clientService.findById(phoneDTO.getClientId()))).toDTO());

	}

	@GetMapping("/{id}")
	public ResponseEntity<PhoneDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());
	}

	@GetMapping
	public ResponseEntity<List<PhoneDTO>> listAll() {
		return ResponseEntity.ok(service.listAll().stream().map((phone) -> phone.toDTO()).toList());
	}

	@PutMapping("/{id}")
	public ResponseEntity<PhoneDTO> update(@PathVariable Integer id, @RequestBody PhoneDTO phoneDTO) {
		Phone phone = new Phone(phoneDTO, clientService.findById(phoneDTO.getClientId()));
		phone.setId(id);
		return ResponseEntity.ok(service.update(phone).toDTO());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Phone> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/number/{number}")
	public ResponseEntity<List<Phone>> findByNumberOrderByClient(@PathVariable String number) {
		return ResponseEntity
				.ok(service.findByNumberOrderByClient(number).stream().map((phone) -> phone).toList());
	}

	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<Phone>> findByClient(@PathVariable Client clientId) {
		return ResponseEntity.ok(service.findByClient(clientService.findById(clientId.getId())));
	}

}
