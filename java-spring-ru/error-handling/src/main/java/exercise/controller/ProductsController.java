package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<Product> index() {
        return productRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping(path = "/{id}")
    public Product put(@RequestBody Product product, @PathVariable long id) {
        Product productToUpdate = productRepository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Product with id " + id + " not found")
            );
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setTitle(product.getTitle());
        productRepository.save(productToUpdate);
        return productToUpdate;
    }

    @GetMapping(path = "/{id}")
    public Product get(@PathVariable long id) {
        return productRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product with id " + id + " not found")
                );
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
}
