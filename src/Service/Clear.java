package Service;

import DAO.*;
import Model.AuthToken;
import Result.ClearResult;

import java.sql.Connection;

/**
 *Class to run the /clear API
 */
public class Clear {
    /**
     * Function to run the /clear API
     */
    public ClearResult clear() throws DataAccessException {
        ClearResult f = new ClearResult();
        Database db = new Database();
        Connection conn = db.getConnection();
        EventDAO eDao = new EventDAO(conn);
        eDao.delete();
        UserDAO uDao = new UserDAO(conn);
        uDao.delete();
        PersonDAO pDao = new PersonDAO(conn);
        pDao.delete();
        AuthTokenDAO aDao = new AuthTokenDAO(conn);
        aDao.delete();
        f.setMessage("Clear succeeded");
        f.setSuccess(true);
        db.closeConnection(true);
        return f;
    }
}
