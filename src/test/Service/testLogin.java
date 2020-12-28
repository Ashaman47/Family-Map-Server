package test.Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.User;
import Requests.LoginRequest;
import Service.Login;
import Result.LoginResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class testLogin {
    Login l = new Login();
    @BeforeEach
    public void clearTables() throws DataAccessException {
        User bestUser = new User("JGl", "Gale", "Gale123A",
                "John", "Gale", "M", "JG");
        Database db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();
        UserDAO uDAO = new UserDAO(conn);
        uDAO.insert(bestUser);
        db.closeConnection(true);
    }
    @Test
    public void loginPass() throws DataAccessException{
        LoginRequest g = new LoginRequest();
        g.setUserName("JGl");
        g.setPassword("Gale");
        LoginResult f = l.login(g);
        assertEquals("JGl",f.getUserName());
        assertNotNull(f.getAuthToken());
        assertNotNull(f.getPersonID());
    }
    @Test
    public void loginInvalidPassword() throws DataAccessException{
        LoginRequest g = new LoginRequest();
        g.setUserName("JGl");
        g.setPassword("G");
        assertThrows(DataAccessException.class, ()-> l.login(g));

    }
    @Test
    public void loginInvalidUserName() throws DataAccessException{
        LoginRequest g = new LoginRequest();
        g.setUserName("J");
        g.setPassword("Gale");
        assertThrows(DataAccessException.class, ()-> l.login(g));

    }

}
