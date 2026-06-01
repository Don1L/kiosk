package com.kiosk;

import com.kiosk.repository.PublicationRepository;
import com.kiosk.repository.inmemory.InMemoryPublicationRepository;
import com.kiosk.service.PublicationService;
import com.kiosk.service.impl.PublicationServiceImpl;
import com.kiosk.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        PublicationRepository repository = new InMemoryPublicationRepository();
        PublicationService service = new PublicationServiceImpl(repository);
        ConsoleUI ui = new ConsoleUI(service);
        ui.start();
    }
}