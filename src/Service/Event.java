package Service;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Requests.*;
import Result.EventResult;

import java.sql.Connection;

/**
*Event Class to handle the /event/[eventID] API
*/
public class Event {
    /**
     *Function to handle the /event/[eventID] API
     * @param EventID the request body
     */
    public EventResult event(String EventID) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        EventDAO pDao = new EventDAO(conn);
        Model.Event p;
        if (pDao.find(EventID) != null) {
            p = pDao.find(EventID);
        } else{
            db.closeConnection(false);
            throw new DataAccessException("Error: Event doesn't exist");
        }
        EventResult event = new EventResult(p.geteventID(), p.getassociatedUserName(), p.getPersonID(),
                p.getLatitude(), p.getLongitude(), p.getCountry(), p.getCity(), p.geteventType(), p.getYear());

        db.closeConnection(true);
        return event;
    }
}
