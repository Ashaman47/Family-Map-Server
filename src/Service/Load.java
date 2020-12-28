package Service;
import DAO.*;
import Model.Event;
import Model.Person;
import Requests.*;
import Result.LoadResult;
import Model.*;

import java.sql.Connection;

/**
 * Clear to run the /load API
 */
public class Load {
    /**
     * Function to run the /load API
     * @param r Request body for the /load API
     */
    public LoadResult load(LoadRequest r) throws DataAccessException {
        LoadResult f = new LoadResult();
        Database db = new Database();
            int x = 0,y = 0,z = 0;

            Connection conn = db.getConnection();
            db.clearTables();
            UserDAO uDAO = new UserDAO(conn);
            if (r.getUsers() != null) {
                for (User u : r.getUsers()) {
                    uDAO.insert(u);
                    x++;
                }
            }
            PersonDAO pdao = new PersonDAO(conn);
            if (r.getPersons() != null) {

                for (Person p : r.getPersons()) {
                    pdao.insert(p);
                    y++;
                }
            }
            EventDAO edao = new EventDAO(conn);
            if (r.getUsers() != null) {
                for (Event e : r.getEvents()) {
                    edao.insert(e);
                    z++;
                }
            }

            db.closeConnection(true);
            if (x == 0 && y == 0 && z == 0){
                f.setSuccess(false);
                f.setMessage("Error:Nothing to Load");
                throw new DataAccessException("Nothing to Load");
            }
            f.setMessage("Successfully added " + x + " users, " + y + " persons, and " + z + "events.");
            f.setSuccess(Boolean.TRUE);
        return f;
    }
}
