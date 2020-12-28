package Service;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Result.EventsResult;

import java.sql.Connection;

/**
 * class to run the /event API
 */
public class Events {
    /**
     * Function to run the /event API
     */
    public EventsResult events(String userName) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        EventDAO pDao = new EventDAO(conn);
        EventsResult p = new EventsResult();
        p.setEvents(pDao.getEvents(userName));
        db.closeConnection(true);
        return p;
    }
}
