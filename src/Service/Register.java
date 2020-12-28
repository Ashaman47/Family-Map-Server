package Service;
import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.AuthToken;
import Model.User;
import Requests.*;
import Result.RegisterResult;

import java.io.IOException;
import java.sql.Connection;

/**
 * Class to run the /register API
 */
public class Register {
    /**
     * Function to run the /register API
     * @param r Request body for the /register API
     */
    public RegisterResult register(RegisterRequest r) throws DataAccessException, IOException {

        RegisterResult f = new RegisterResult();
        GenerateData g = new GenerateData();
        User newUser = new User(r.getuserName(),r.getPassword(),r.getEmail(),r.getFirstName(),
                r.getLastName(),r.getGender(),r.getFirstName() + r.getLastName());
        Database db = new Database();
        try {

            Connection conn = db.getConnection();
            UserDAO uDAO = new UserDAO(conn);
            if (uDAO.find(newUser.getuserName()) == null) {
                uDAO.insert(newUser);
            }
            else {
                throw new DataAccessException("User already exists");
            }
            AuthTokenDAO aDAO = new AuthTokenDAO(conn);
            f.setAuthToken((new UUIDGenerator()).UUIDGenerator());
            AuthToken auth = new AuthToken(newUser.getuserName(), f.getAuthToken());
            aDAO.insert(auth);
            db.closeConnection(true);
            g.Generate(r.getuserName(), r.getFirstName(), r.getLastName(), r.getGender(), 4);
            f.setSuccess(true);
            f.setUserName(newUser.getuserName());
            f.setPersonID(newUser.getpersonID());



        } catch (DataAccessException e){
            db.closeConnection(false);
            throw new DataAccessException(e.getMessage());
        }
        return f;
    }
}
