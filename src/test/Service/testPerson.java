package test.Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Model.Person;
import Result.PersonResult;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class testPerson {
    Person p = new Person("JG", "Hale", "Gale123A",
            "John", "Gale", "M", "JG", "");
    PersonResult f = new PersonResult("JG", "Hale", "Gale123A","John", "Gale", "M", "JG", "");
    Service.Person g = new Service.Person();
    @BeforeEach
    public void clearTables() throws DataAccessException {

        Database db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();
        PersonDAO pDAO = new PersonDAO(conn);
        pDAO.insert(p);
        db.closeConnection(true);
    }
    @Test
    public void testPersonPass() throws DataAccessException {
        PersonResult r = g.person("JG");
        assertEquals(f,r);
    }
    @Test
    public void testPersonFail() throws DataAccessException{
        assertThrows(DataAccessException.class, ()-> g.person(""));
    }

}
