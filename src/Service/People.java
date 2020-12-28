package Service;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Result.PeopleResult;

import java.sql.Connection;

/**
 * Class to run the /person API
 */
public class People {
    /**
     * Function to run the /person API
     */
    public PeopleResult people(String user) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDAO pDao = new PersonDAO(conn);
        PeopleResult p = new PeopleResult();
        p.setPeople(pDao.getPeople(user));
        db.closeConnection(true);
        return p;
    }
}
