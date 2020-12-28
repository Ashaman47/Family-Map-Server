package test.Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.User;
import Service.Fill;
import Result.FillResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testFill {
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
    public void fillPass() throws DataAccessException, IOException {
        Fill f = new Fill();
        FillResult c = f.fill("JGl", 4);
        assertTrue(c.getSuccess());
    }
    @Test
    public void fillFail() {
        Fill f = new Fill();
        assertThrows(DataAccessException.class, ()->f.fill("f", 3));
    }
}
