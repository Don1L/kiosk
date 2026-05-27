package com.kiosk.service;

import com.kiosk.model.Publication;

import java.util.List;
import java.util.Optional;

public interface PublicationService {
    void receive(Publication publication);        // приёмка товара
    void sell(long id, int quantity);             // продажа
    void edit(Publication publication);           // редактирование
    void remove(long id);                         // удаление
    Optional<Publication> findById(long id);
    List<Publication> findAll();
}