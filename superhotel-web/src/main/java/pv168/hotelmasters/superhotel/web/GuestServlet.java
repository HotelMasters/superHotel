package pv168.hotelmasters.superhotel.web;

import pv168.hotelmasters.superhotel.backend.entities.Guest;
import pv168.hotelmasters.superhotel.backend.interfaces.GuestManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @author Gabriela Godiskova
 */
@WebServlet("/guest/*")
public class GuestServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("guests",getGuestManager().findAllGuests());
        request.getRequestDispatcher("/listGuests.jsp").forward(request,response);
    }

    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        String action = request.getPathInfo();
        if (action.equals("/add")) {
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String birthday = request.getParameter("birthDay");
            String crCardNum = request.getParameter("crCardNumber");
            if (name.length()==0||address.length()==0||birthday.length()==0||crCardNum.length()==0) {
                request.setAttribute("error","Všetky atribúty musia byť nastavené.");
                doGet(request, response);
                return;
            }
            try {
                Guest guest1 = guestCreator(name,address,LocalDate.parse(birthday),Long.valueOf(crCardNum));
                getGuestManager().createGuest(guest1);
                response.sendRedirect(request.getContextPath()+"/guest");
                return;
            } catch (SQLException e) {
                request.setAttribute("error","Vyskytla sa chyba pri vkladaní hosťa: "+e);
                doGet(request, response);
                return;
            }
        }
        if (action.equals("/delete")) {
                Long helpId = Long.valueOf(request.getParameter("id"));
                getGuestManager().deleteGuest(getGuestManager().findGuestById(helpId));
                response.sendRedirect(request.getContextPath()+"/guest");
        } else {
            request.setAttribute("error","Neznama akcia.");
        }
    }
    private GuestManager getGuestManager(){
        return (GuestManager) getServletContext().getAttribute("guestManager");
    }
    private Guest guestCreator(String name, String address, LocalDate birthDay,Long crCardNumber){
        Guest guest = new Guest();
        guest.setId(null);
        guest.setName(name);
        guest.setAddress(address);
        guest.setBirthDay(birthDay);
        guest.setCrCardNumber(crCardNumber);
        return guest;
    }






}
