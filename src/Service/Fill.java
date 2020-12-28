package Service;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Result.FillResult;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Class to run the /fill API
 */
public class Fill {
    /**
     * Function to run the /fill API
     * @param userName the request body for the /fill API
     */
    public FillResult fill(String userName, int numGens) throws DataAccessException, IOException {
        GenerateData g = new GenerateData();
        FillResult f = new FillResult();
        try {
            Database db = new Database();
            Connection conn = db.getConnection();
            UserDAO uDAO = new UserDAO(conn);
            User u;
            if (uDAO.find(userName) != null) {
                u = uDAO.find(userName);
            }
            else{
                db.closeConnection(false);
                throw new DataAccessException("User Does not exist");
            }
            try {
                db.clearUser(userName);
                db.closeConnection(true);
                g.Generate(userName, u.getfirstName(), u.getlastName(), u.getgender(), numGens);
                db = new Database();
                conn = db.getConnection();
                PersonDAO pDAO = new PersonDAO(conn);
                ArrayList<Person> p = pDAO.getPeople(userName);
                EventDAO eDAO = new EventDAO(conn);
                ArrayList<Event> e = eDAO.getEvents(userName);
                db.closeConnection(true);
                f.setMessage("Successfully added " + p.size() + " persons and " + e.size() + "events");
            } catch (DataAccessException e) {
                db.closeConnection(false);
                throw new DataAccessException(e.getMessage());
            }
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
        return f;
    }
}