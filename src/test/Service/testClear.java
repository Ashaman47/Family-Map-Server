package test.Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Model.Person;
import Service.Clear;
import Result.ClearResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class testClear {

    @BeforeEach
    public void clearTables() throws DataAccessException {
        Person p = new Person("JG", "Hale", "Gale123A",
                "John", "Gale", "M", "JG", "");
        Database db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();
        PersonDAO pDAO = new PersonDAO(conn);
        pDAO.insert(p);
        db.closeConnection(true);
    }
    @Test
    public void clearPass() throws DataAccessException {
        Clear c = new Clear();
        ClearResult r = c.clear();
        assertTrue(r.getSuccess());

    }
    public void clearTwice() throws DataAccessException {
        Clear c = new Clear();
        ClearResult r = c.clear();
        r = c.clear();
        assertTrue(r.getSuccess());
    }

}
