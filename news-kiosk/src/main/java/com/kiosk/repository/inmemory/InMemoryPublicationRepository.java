package com.kiosk.repository.inmemory;

import com.kiosk.model.Publication;
import com.kiosk.repository.PublicationRepository;

import java.util.*;

public class InMemoryPublicationRepository implements PublicationRepository {

    private final Map<Long, Publication> storage = new HashMap<>();

    @Override
    public void save(Publication publication) {
        storage.put(publication.getId(), publication);
    }

    @Override
    public Optional<Publication> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Publication> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Publication publication) {
        if (!storage.containsKey(publication.getId())) {
            throw new NoSuchElementException("Publication not found: " + publication.getId());
        }
        storage.put(publication.getId(), publication);
    }

    @Override
    public void delete(long id) {
        if (!storage.containsKey(id)) {
            throw new NoSuchElementException("Publication not found: " + id);
        }
        storage.remove(id);
    }
}