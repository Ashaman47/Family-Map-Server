package test.Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Model.Event;
import Result.EventsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testEvents {
    Event p = new Event("JG", "Hale", "Gale123A",
            8, 5, "M", "JG", "efe", 3);
    Event e = new Event("dj", "Hale", "ef",
            5, 3, "M", "JG", "gdg", 6);
    Service.Events g = new Service.Events();
    ArrayList<Event> list = new ArrayList<>();
    @BeforeEach
    public void clearTables() throws DataAccessException {

        list.add(p);
        list.add(e);
        Database db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();
        EventDAO pDAO = new EventDAO(conn);
        pDAO.insert(p);
        pDAO.insert(e);
        db.closeConnection(true);
    }
    @Test
    public void testEventsPass() throws DataAccessException {

        EventsResult r = g.events("Hale");
        assertEquals(list,r.getEvents());
    }
    @Test
    public void testEventsFail() throws DataAccessException{
        EventsResult r = g.events("f");
        ArrayList<Event> l = new ArrayList<>();
        assertEquals(l,r.getEvents());
    }

}
