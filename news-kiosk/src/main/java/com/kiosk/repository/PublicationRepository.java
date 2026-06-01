package com.kiosk.repository;

import com.kiosk.model.Publication;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository {
    long save(Publication publication);
    Optional<Publication> findById(long id);
    List<Publication> findAll();
    void update(Publication publication);
    void delete(long id);
}