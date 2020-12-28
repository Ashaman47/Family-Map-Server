package test.Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Model.Event;
import Result.EventResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class testEvent {
    Event p = new Event("JG", "Hale", "Gale123A",
            8, 5, "M", "JG", "efe", 3);
    EventResult f = new EventResult("JG", "Hale", "Gale123A",
            8, 5, "M", "JG", "efe", 3);
    Service.Event g = new Service.Event();
    @BeforeEach
    public void clearTables() throws DataAccessException {

        Database db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();
        EventDAO pDAO = new EventDAO(conn);
        pDAO.insert(p);
        db.closeConnection(true);
    }
    @Test
    public void testEventPass() throws DataAccessException {
        EventResult r = g.event("JG");
        assertEquals(f,r);
    }
    @Test
    public void testEventFail() throws DataAccessException{
        assertThrows(DataAccessException.class, ()-> g.event(""));
    }
}
