package exercise.controller;

import org.hibernate.boot.xsd.MappingXsdSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<Product> index(@RequestParam(required = false) Integer min, @RequestParam(required = false) Integer max) {
        if (min != null && max != null) {
            return productRepository.findByPriceBetweenOrderByPrice(min, max).orElseThrow(
                    () -> new ResourceNotFoundException("Products with price between " + min + " and " + max + " not found")
            );
        } else if (min == null && max != null) {
            return productRepository.findByPriceBetweenOrderByPrice(0, max).orElseThrow(
                    () -> new ResourceNotFoundException("Products with price between " + min + " and " + max + " not found")
            );
        } else if (min != null) {
            return productRepository.findByPriceBetweenOrderByPrice(min, Integer.MAX_VALUE).orElseThrow(
                    () -> new ResourceNotFoundException("Products with price between " + min + " and " + max + " not found")
            );
        } else {
            return productRepository.findAll();
        }
    }

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
