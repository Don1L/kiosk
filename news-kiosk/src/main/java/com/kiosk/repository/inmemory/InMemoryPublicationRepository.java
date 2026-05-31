package com.kiosk.repository.inmemory;

import com.kiosk.model.Publication;
import com.kiosk.repository.PublicationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPublicationRepository implements PublicationRepository {

    private final Map<Long, Publication> storage = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public long save(Publication publication) {
        long id = idCounter.incrementAndGet();
        publication.setId(id);
        storage.put(id, publication);
        return id;
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