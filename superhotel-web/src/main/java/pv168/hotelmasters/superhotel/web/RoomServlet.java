package pv168.hotelmasters.superhotel.web;

import pv168.hotelmasters.superhotel.backend.entities.Room;
import pv168.hotelmasters.superhotel.backend.exceptions.DBException;
import pv168.hotelmasters.superhotel.backend.interfaces.RoomManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Kristian Lesko
 */
@WebServlet(RoomServlet.URL_MAPPING + "/*")
public class RoomServlet extends HttpServlet {
    public static final String URL_MAPPING = "/room";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        renderListPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServletException(e);
        }
        String endpoint = request.getPathInfo();
        if ("/create".equals(endpoint)) {
            System.out.println("POST /create");
            processCreate(request, response);
        } else if ("/delete".equals(endpoint)) {
            System.out.println("POST /delete");
            processDelete(request, response);
        }
    }

    private void processCreate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int capacity;
        double price;
        try {
            capacity = Integer.valueOf(request.getParameter("capacity"));
            price = Double.valueOf(request.getParameter("price"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Kapacita a cena musia byť čísla.");
            request.setAttribute("capacity", request.getParameter("capacity"));
            request.setAttribute("price", request.getParameter("price"));
            renderListPage(request, response);
            return;
        }
        try {
            Room createdRoom = new Room();
            createdRoom.setCapacity(capacity);
            createdRoom.setPrice(price);
            getRoomManager().createRoom(createdRoom);
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (DBException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    private void processDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long deletedId;
        try {
            deletedId = Long.valueOf(request.getParameter("id"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID odstraňovanej izby musí byť číslo.");
            renderListPage(request, response);
            return;
        }
        Room deletedRoom = getRoomManager().findRoomById(deletedId);
        if (deletedRoom == null) {
            request.setAttribute("error", "Izba s ID " + deletedId + " neexistuje.");
            renderListPage(request, response);
            return;
        }
        getRoomManager().deleteRoom(deletedRoom);
        response.sendRedirect(request.getContextPath() + URL_MAPPING);
    }

    private RoomManager getRoomManager() {
        return (RoomManager) getServletContext().getAttribute("roomManager");
    }

    private void renderListPage(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            request.setAttribute("rooms", getRoomManager().findAllRooms());
            request.getRequestDispatcher("/rooms.jsp").forward(request, response);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }
}
