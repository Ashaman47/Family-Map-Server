package Service;
import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.AuthToken;
import Model.User;
import Requests.*;
import Result.LoginResult;

import java.sql.Connection;

/**
 * Class to run the /login API
 */
public class Login {
    /**
     * Function to run the /login API
     * @param r Request body for the /login API
     */
    public LoginResult login(LoginRequest r) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        UserDAO uDAO = new UserDAO(conn);
        LoginResult f = new LoginResult();
        if (uDAO.find(r.getUserName()) != null){
            User g = uDAO.find(r.getUserName());
            if (g.getpassword().equals(r.getPassword())) {
                f.setUserName(g.getuserName());
                if (g.getpersonID() != null) {
                    f.setPersonID(g.getpersonID());
                }
                else {
                    db.closeConnection(true);
                    throw new DataAccessException("Does Not Exist");
                }
                f.setSuccess(Boolean.TRUE);
                f.setAuthToken((new UUIDGenerator()).UUIDGenerator());
                AuthToken auth = new AuthToken(g.getuserName(), f.getAuthToken());
                AuthTokenDAO aDAO = new AuthTokenDAO(conn);
                aDAO.insert(auth);
            }
            else {
                f.setMessage("Error: Invalid Password");
                f.setSuccess(Boolean.FALSE);
                db.closeConnection(false);
                throw new DataAccessException("Invalid Password");
            }
        }
        else {
            db.closeConnection(true);
            throw new DataAccessException(" Does Not Exist");
        }
        db.closeConnection(true);
        return f;
    }
}
