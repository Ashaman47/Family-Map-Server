package test.dao;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class UserDAOTest {
    private Database db;
    private User bestUser;
    private UserDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new User with random data
        bestUser = new User("JGl", "Gale", "Gale123A",
                "John", "Gale", "M", "JG");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the UserDAO so it can access the database
        eDao = new UserDAO(conn);
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
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eDao.insert(bestUser);
        //So lets use a find method to get the User that we just put in back out
        User compareTest = eDao.find(bestUser.getuserName());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eDao.insert(bestUser);
        //but our sql table is set up so that "UserID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> eDao.insert(bestUser));
    }
    @Test
    public void findTest() throws DataAccessException {
        db.clearTables();
        eDao.insert(bestUser);
        assertEquals(bestUser, eDao.find(bestUser.getuserName()));

    }
    @Test
    public void findTestFail() throws DataAccessException {
        db.clearTables();
        eDao.insert(bestUser);
        assertNull(eDao.find("Hello"));
    }
    @Test
    public void deletePass() throws DataAccessException {
        db.clearTables();
        eDao.insert(bestUser);
        eDao.delete();
        assertNull(eDao.find(bestUser.getuserName()));
    }
    @Test
    public void getUsersPass() throws DataAccessException {
        User nextUser = new User("JJ", "Hale", "Hale123A",
                "John", "Hale", "M", "JJ");
        eDao.insert(bestUser);
        eDao.insert(nextUser);
        ArrayList<User>checkUsers = new ArrayList<>();
        checkUsers.add(bestUser);
        checkUsers.add(nextUser);
        assertEquals(checkUsers, eDao.getUsers());
    }
    @Test
    public void getUsersFail() throws DataAccessException {
        eDao.delete();
        ArrayList<User>check = new ArrayList<>();
        assertEquals(check,eDao.getUsers());
    }

}

