package test.dao;

import DAO.DataAccessException;
import DAO.Database;
import DAO.AuthTokenDAO;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class AuthTokenDAOTest {
    private Database db;
    private AuthToken bestAuthToken;
    private AuthTokenDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new AuthToken with random data
        bestAuthToken = new AuthToken("JJ", "G23Djfe4");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the AuthTokenDAO so it can access the database
        eDao = new AuthTokenDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        eDao.insert(bestAuthToken);
        AuthToken compareTest = eDao.find(bestAuthToken.getUserName());
        assertNotNull(compareTest);
        assertEquals(bestAuthToken, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insert(bestAuthToken);
        assertThrows(DataAccessException.class, ()-> eDao.insert(bestAuthToken));
    }
    @Test
    public void delete() throws DataAccessException {
        eDao.insert(bestAuthToken);
        eDao.delete();
        assertNull(eDao.find(bestAuthToken.getUserName()));
    }
}
