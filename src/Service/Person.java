package Service;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Requests.*;
import Result.PersonResult;

import java.sql.Connection;

/**
 * Function to run the /person/[personID] API
 */
public class Person {
    /**
     * Function to run the /person/[personID] API
     * @param personID the personID to return
     */
    public PersonResult person(String personID) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDAO pDao = new PersonDAO(conn);
        Model.Person p;
        if (pDao.find(personID) != null) {
            p = pDao.find(personID);
        }
        else{
            db.closeConnection(false);
            throw new DataAccessException("Does not Exist");
        }
        PersonResult person = new PersonResult(p.getPersonID(), p.getAssociatedUsername(),p.getFirstName(),p.getLastName(), p.getGender(), p.getFatherID(),p.getMotherID(),p.getSpouseID());
        db.closeConnection(true);
        return person;

    }
}
