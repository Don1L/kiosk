package com.kiosk.service.impl;

import com.kiosk.model.Publication;
import com.kiosk.repository.PublicationRepository;
import com.kiosk.service.PublicationService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository repository;

    public PublicationServiceImpl(PublicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void receive(Publication publication) {
        Optional<Publication> existing = repository.findById(publication.getId());
        if (existing.isPresent()) {
            // уже есть — просто увеличиваем количество
            Publication p = existing.get();
            p.setQuantity(p.getQuantity() + publication.getQuantity());
            repository.update(p);
        } else {
            repository.save(publication);
        }
    }

    @Override
    public void sell(long id, int quantity) {
        Publication p = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Publication not found: " + id));

        if (p.getQuantity() < quantity) {
            throw new IllegalStateException(
                "Not enough stock. Available: " + p.getQuantity()
            );
        }

        p.setQuantity(p.getQuantity() - quantity);
        repository.update(p);
    }

    @Override
    public void edit(Publication publication) {
        repository.update(publication);
    }

    @Override
    public void remove(long id) {
        repository.delete(id);
    }

    @Override
    public Optional<Publication> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Publication> findAll() {
        return repository.findAll();
    }
}