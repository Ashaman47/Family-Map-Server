package test.Service;

import DAO.DataAccessException;
import DAO.Database;
import Requests.RegisterRequest;
import Service.Register;
import Result.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class testRegister {
    private RegisterRequest r = new RegisterRequest("susan", "gge", "gef", "ef", "geif","f", "DHFI");
    private Register reg = new Register();

    @BeforeEach
    public void clearTables() throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void registervalid() throws DataAccessException, IOException {
        RegisterResult res = reg.register(r);
        assertNotNull(res.getAuthToken());
        assertNotNull(res.getPersonID());
        assertNotNull(res.getUserName());
        assertTrue(res.getSuccess());
    }
    @Test
    public void registerfail() throws DataAccessException, IOException {
        RegisterResult res = reg.register(r);
        assertThrows(DataAccessException.class, ()-> reg.register(r));

    }
}
