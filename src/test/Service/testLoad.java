package test.Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Requests.LoginRequest;
import Service.Load;
import Result.LoadResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class testLoad {
    LoadRequest f = new LoadRequest();
    @BeforeEach
    public void clearTables() throws DataAccessException {
        Person p = new Person("JG", "Hale", "Gale123A",
                "John", "Gale", "M", "JG", "efe");
        Person e = new Person("dj", "Hale", "ef",
                "Johen", "Gale", "M", "JG", "gdg");
        ArrayList<Person> listp = new ArrayList<>();
        listp.add(p);
        listp.add(e);
        User bestUser = new User("JGl", "Gale", "Gale123A",
                "John", "Gale", "M", "JG");
        User bestUser2 = new User("Jfdl", "Gagdge", "Gale123A",
                "John", "Gale", "M", "JG");
        ArrayList<User> listu = new ArrayList<>();
        listu.add(bestUser);
        listu.add(bestUser2);
        Event bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event bestEvent2 = new Event("Biking_123efeasfA", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        ArrayList<Event> liste = new ArrayList<>();
        liste.add(bestEvent);
        liste.add(bestEvent2);

        f.setUsers(listu);
        f.setPersons(listp);
        f.setEvents(liste);
        Database db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();
        UserDAO uDAO = new UserDAO(conn);
        uDAO.insert(bestUser);
        db.closeConnection(true);
    }
    @Test
    public void loadPass() throws DataAccessException{
        Load g = new Load();
        LoadResult m = g.load(f);
        assertTrue(m.getSuccess());
    }
    @Test
    public void loadFail() throws DataAccessException{
        Load g = new Load();
        f.setUsers(null);
        f.setEvents(null);
        f.setPersons(null);
        assertThrows(DataAccessException.class, ()->g.load(f));
    }
}
