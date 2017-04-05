package pv168.hotelmasters.superhotel.web;

import pv168.hotelmasters.superhotel.backend.db.DBCreator;
import pv168.hotelmasters.superhotel.backend.impl.GuestManagerImpl;
import pv168.hotelmasters.superhotel.backend.impl.RoomManagerImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Clock;
import java.util.Enumeration;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
@WebListener
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        DataSource dataSource = new DBCreator().createBasicDB();

        GuestManagerImpl guestManager = new GuestManagerImpl(Clock.systemDefaultZone());
        guestManager.setDataSource(dataSource);
        RoomManagerImpl roomManager = new RoomManagerImpl();
        roomManager.setDataSource(dataSource);

        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("guestManager", guestManager);
        servletContext.setAttribute("roomManager", roomManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
