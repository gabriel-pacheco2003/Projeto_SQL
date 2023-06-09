package br.com.gabi_la_boutique.boutique.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabi_la_boutique.boutique.models.Category;
import br.com.gabi_la_boutique.boutique.services.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryResource {

	@Autowired
	private CategoryService service;

	@Secured({"ROLE_ADMIN"})
	@PostMapping
	public ResponseEntity<Category> insert(@RequestBody Category category) {
		return ResponseEntity.ok(service.insert(category));
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/{id}")
	public ResponseEntity<Category> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@Secured({"ROLE_USER"})
	@GetMapping
	public ResponseEntity<List<Category>> listAll() {
		return ResponseEntity.ok(service.listAll().stream().map((category) -> category).toList());
	}
	
	@Secured({"ROLE_ADMIN"})
	@PutMapping("/{id}")
	public ResponseEntity<Category> update(@PathVariable Integer id, @RequestBody Category category) {
		category.setId(id);
		return ResponseEntity.ok(service.update(category));
	}

	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/{id}")
	public ResponseEntity<Category> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/description/{description}")
	public ResponseEntity<List<Category>> findByDescriptionContainingIgnoreCase(@PathVariable String description) {
		return ResponseEntity.ok(service.findByDescriptionContainingIgnoreCase(description).stream()
				.map((category) -> category).toList());
	}
}
