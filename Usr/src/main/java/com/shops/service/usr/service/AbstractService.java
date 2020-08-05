package com.shops.service.usr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<E, T extends JpaRepository<E, Long>> implements CrudService<E> {

    @Autowired
    protected T repository;

    @Override
    public Optional<E> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }

    @Override
    public E save(E entity) {
        return repository.save(entity);
    }

    @Override
    public List<E> saveAll(List<E> list) {
        return repository.saveAll(list);
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(E obj) {
        repository.delete(obj);
    }
}