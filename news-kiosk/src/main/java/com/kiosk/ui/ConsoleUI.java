package com.kiosk.ui;

import com.kiosk.model.Book;
import com.kiosk.model.Magazine;
import com.kiosk.model.Newspaper;
import com.kiosk.model.Publication;
import com.kiosk.service.PublicationService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleUI {

    private final PublicationService service;
    private final Scanner scanner;
    private final InputReader reader;

    public ConsoleUI(PublicationService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
        this.reader = new InputReader(scanner);
    }

    public void start() {
        System.out.println("=== Газетный киоск ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = reader.readInt("Выбор: ");
            switch (choice) {
                case 1 -> handleReceive();
                case 2 -> handleSell();
                case 3 -> handleEdit();
                case 4 -> handleRemove();
                case 5 -> handleList();
                case 0 -> running = false;
                default -> System.out.println("Неверный выбор.");
            }
        }
        System.out.println("До свидания!");
    }

    private void printMenu() {
        System.out.println("\n1. Приёмка товара");
        System.out.println("2. Продажа товара");
        System.out.println("3. Редактировать товар");
        System.out.println("4. Удалить товар");
        System.out.println("5. Список всех товаров");
        System.out.println("0. Выход");
    }

    // приёмка
    private void handleReceive() {
        System.out.println("\nТип товара: 1-Газета, 2-Журнал, 3-Книга");
        int type = reader.readInt("Тип: ");

        long id = reader.readLong("ID: ");
        String title = reader.readString("Название: ");
        double price = reader.readDouble("Цена: ");
        int quantity = reader.readInt("Количество: ");

        try {
            Publication publication = switch (type) {
                case 1 -> {
                    int issue = reader.readInt("Номер выпуска: ");
                    String date = reader.readString("Дата (DD.MM.YYYY): ");
                    yield new Newspaper(id, title, price, quantity, issue, date);
                }
                case 2 -> {
                    int issue = reader.readInt("Номер выпуска: ");
                    String monthYear = reader.readString("Месяц/Год (MM/YYYY): ");
                    yield new Magazine(id, title, price, quantity, issue, monthYear);
                }
                case 3 -> {
                    String author = reader.readString("Автор: ");
                    String isbn = reader.readString("ISBN: ");
                    yield new Book(id, title, price, quantity, author, isbn);
                }
                default -> throw new IllegalArgumentException("Неверный тип товара");
            };

            service.receive(publication);
            System.out.println("Товар принят.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // продажа
    private void handleSell() {
        long id = reader.readLong("ID товара: ");
        int quantity = reader.readInt("Количество: ");
        try {
            service.sell(id, quantity);
            System.out.println("Продажа выполнена.");
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // редакт
    private void handleEdit() {
        long id = reader.readLong("ID товара: ");
        service.findById(id).ifPresentOrElse(p -> {
            System.out.println("Текущие данные: " + p);
            System.out.println("Введите новые данные (Enter — оставить текущее):");

            String title = reader.readStringOrDefault("Название [" + p.getTitle() + "]: ", p.getTitle());
            String priceStr = reader.readStringOrDefault("Цена [" + p.getPrice() + "]: ", String.valueOf(p.getPrice()));
            String qtyStr = reader.readStringOrDefault("Количество [" + p.getQuantity() + "]: ", String.valueOf(p.getQuantity()));

            p.setTitle(title);
            p.setPrice(Double.parseDouble(priceStr));
            p.setQuantity(Integer.parseInt(qtyStr));

            if (p instanceof Newspaper n) {
                String issue = reader.readStringOrDefault("Номер выпуска [" + n.getIssueNumber() + "]: ", String.valueOf(n.getIssueNumber()));
                String date = reader.readStringOrDefault("Дата [" + n.getDate() + "]: ", n.getDate());
                n.setIssueNumber(Integer.parseInt(issue));
                n.setDate(date);
            } else if (p instanceof Magazine m) {
                String issue = reader.readStringOrDefault("Номер выпуска [" + m.getIssueNumber() + "]: ", String.valueOf(m.getIssueNumber()));
                String monthYear = reader.readStringOrDefault("Месяц/Год [" + m.getMonthYear() + "]: ", m.getMonthYear());
                m.setIssueNumber(Integer.parseInt(issue));
                m.setMonthYear(monthYear);
            } else if (p instanceof Book b) {
                String author = reader.readStringOrDefault("Автор [" + b.getAuthor() + "]: ", b.getAuthor());
                String isbn = reader.readStringOrDefault("ISBN [" + b.getIsbn() + "]: ", b.getIsbn());
                b.setAuthor(author);
                b.setIsbn(isbn);
            }

            try {
                service.edit(p);
                System.out.println("Товар обновлён.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }, () -> System.out.println("Товар не найден."));
    }

    // удаление
    private void handleRemove() {
        long id = reader.readLong("ID товара: ");
        try {
            service.remove(id);
            System.out.println("Товар удалён.");
        } catch (NoSuchElementException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // список
    private void handleList() {
        List<Publication> all = service.findAll();
        if (all.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            all.forEach(System.out::println);
        }
    }
}