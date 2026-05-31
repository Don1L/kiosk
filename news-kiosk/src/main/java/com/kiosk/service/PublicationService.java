package com.kiosk.service;

import com.kiosk.model.Publication;

import java.util.List;
import java.util.Optional;

public interface PublicationService {
    long receive(Publication publication);
    void sell(long id, int quantity);
    void edit(Publication publication);
    void remove(long id);
    Optional<Publication> findById(long id);
    List<Publication> findAll();
}