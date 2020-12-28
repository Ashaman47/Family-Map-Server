package test.Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Model.Person;
import Result.PeopleResult;
import Result.PersonResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testPeople {
    Person p = new Person("JG", "Hale", "Gale123A",
            "John", "Gale", "M", "JG", "efe");
    Person e = new Person("dj", "Hale", "ef",
            "Johen", "Gale", "M", "JG", "gdg");
    Service.People g = new Service.People();
    ArrayList<Person> list = new ArrayList<>();
    @BeforeEach
    public void clearTables() throws DataAccessException {

        list.add(p);
        list.add(e);
        Database db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();
        PersonDAO pDAO = new PersonDAO(conn);
        pDAO.insert(p);
        pDAO.insert(e);
        db.closeConnection(true);
    }
    @Test
    public void testPeoplePass() throws DataAccessException {

        PeopleResult r = g.people("Hale");
        assertEquals(list,r.getPeople());
    }
    @Test
    public void testPeopleFail() throws DataAccessException{
        PeopleResult r = g.people("f");
        ArrayList<Person> l = new ArrayList<>();
        assertEquals(l,r.getPeople());
    }

}
