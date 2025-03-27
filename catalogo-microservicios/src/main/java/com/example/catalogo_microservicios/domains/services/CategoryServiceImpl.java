package com.example.catalogo_microservicios.domains.services;

import com.example.catalogo_microservicios.domains.contracts.repositories.CategoryRepository;
import com.example.catalogo_microservicios.domains.contracts.services.CategoryService;
import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import com.example.catalogo_microservicios.exceptions.InvalidDataException;
import com.example.catalogo_microservicios.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import com.example.catalogo_microservicios.domains.entities.Category;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAllByOrderByName();
    }

    @Override
    public Optional<Category> getOne(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category add(Category item) throws DuplicatedKeyException, InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        if(item.isInvalid())
            throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
        if(item.getCategoryId() != 0 && categoryRepository.existsById(item.getCategoryId()))
            throw new DuplicatedKeyException("Ya existe");
        return categoryRepository.save(item);
    }

    @Override
    public Category modify(Category item) throws NotFoundException, InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        if(item.isInvalid())
            throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
        if(!categoryRepository.existsById(item.getCategoryId()))
            throw new NotFoundException();
        return categoryRepository.save(item);
    }

    @Override
    public void delete(Category item) throws InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        categoryRepository.delete(item);
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }


    @Override
    public List<Category> novedades(Date fecha) {
        return categoryRepository.findByLastUpdateGreaterThanEqualOrderByLastUpdate(fecha);
    }
}
