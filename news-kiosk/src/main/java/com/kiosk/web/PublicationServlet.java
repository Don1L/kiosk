package com.kiosk.web;

import com.kiosk.model.Book;
import com.kiosk.model.Magazine;
import com.kiosk.model.Newspaper;
import com.kiosk.model.Publication;
import com.kiosk.service.PublicationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class PublicationServlet extends HttpServlet {

    private PublicationService service;

    @Override
    public void init() {
        service = (PublicationService) getServletContext().getAttribute("service");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "list"    -> handleList(req, resp);
            case "newForm" -> handleNewForm(req, resp);
            case "edit"    -> handleEditForm(req, resp);
            default        -> resp.sendRedirect(req.getContextPath() + "/publications");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action != null ? action : "") {
                case "create" -> handleCreate(req);
                case "update" -> handleUpdate(req);
                case "sell"   -> handleSell(req);
                case "delete" -> handleDelete(req);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            handleList(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/publications");
    }

    private void handleList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("publications", service.findAll());
        req.getRequestDispatcher("/WEB-INF/views/list.jsp").forward(req, resp);
    }

    private void handleNewForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("type", req.getParameter("type"));
        req.setAttribute("formAction", "create");
        req.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(req, resp);
    }

    private void handleEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        service.findById(id).ifPresentOrElse(p -> {
            req.setAttribute("publication", p);
            req.setAttribute("type", p.getClass().getSimpleName().toUpperCase());
            req.setAttribute("formAction", "update");
            try {
                req.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            try {
                resp.sendRedirect(req.getContextPath() + "/publications");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void handleCreate(HttpServletRequest req) {
        String type = req.getParameter("type");
        String title = req.getParameter("title");
        double price = Double.parseDouble(req.getParameter("price"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        Publication publication = switch (type) {
            case "NEWSPAPER" -> new Newspaper(0, title, price, quantity,
                    Integer.parseInt(req.getParameter("issueNumber")),
                    req.getParameter("pubDate"));
            case "MAGAZINE" -> new Magazine(0, title, price, quantity,
                    Integer.parseInt(req.getParameter("issueNumber")),
                    req.getParameter("monthYear"));
            case "BOOK" -> new Book(0, title, price, quantity,
                    req.getParameter("author"),
                    req.getParameter("isbn"));
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
        service.receive(publication);
    }

    private void handleUpdate(HttpServletRequest req) {
        long id = Long.parseLong(req.getParameter("id"));
        Publication p = service.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Publication not found: " + id));

        p.setTitle(req.getParameter("title"));
        p.setPrice(Double.parseDouble(req.getParameter("price")));
        p.setQuantity(Integer.parseInt(req.getParameter("quantity")));

        if (p instanceof Newspaper n) {
            n.setIssueNumber(Integer.parseInt(req.getParameter("issueNumber")));
            n.setDate(req.getParameter("pubDate"));
        } else if (p instanceof Magazine m) {
            m.setIssueNumber(Integer.parseInt(req.getParameter("issueNumber")));
            m.setMonthYear(req.getParameter("monthYear"));
        } else if (p instanceof Book b) {
            b.setAuthor(req.getParameter("author"));
            b.setIsbn(req.getParameter("isbn"));
        }
        service.edit(p);
    }

    private void handleSell(HttpServletRequest req) {
        long id = Long.parseLong(req.getParameter("id"));
        int quantity = Integer.parseInt(req.getParameter("sellQty"));
        service.sell(id, quantity);
    }

    private void handleDelete(HttpServletRequest req) {
        long id = Long.parseLong(req.getParameter("id"));
        service.remove(id);
    }
}
