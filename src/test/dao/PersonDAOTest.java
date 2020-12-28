package test.dao;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class PersonDAOTest {
    private Database db;
    private Person bestPerson;
    private PersonDAO eDao;
    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new Person with random data
        bestPerson = new Person("JG", "Hale", "Gale123A",
                "John", "Gale", "M", "JG", "");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the PersonDAO so it can access the database
        eDao = new PersonDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }
    @Test
    public void insertPass() throws DataAccessException{
        eDao.insert(bestPerson);
        Person compareTest = eDao.find(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }
    @Test
    public void insertFail() throws DataAccessException{
        eDao.insert(bestPerson);
        assertThrows(DataAccessException.class, ()-> eDao.insert(bestPerson));
    }
    @Test
    public void findTest() throws DataAccessException {
        db.clearTables();
        eDao.insert(bestPerson);
        assertEquals(bestPerson, eDao.find(bestPerson.getPersonID()));

    }
    @Test
    public void findTestFail() throws DataAccessException {
        db.clearTables();
        eDao.insert(bestPerson);
        assertNull(eDao.find("Hello"));
    }
    @Test
    public void deletePass() throws DataAccessException {
        db.clearTables();
        eDao.insert(bestPerson);
        eDao.delete();
        assertNull(eDao.find(bestPerson.getPersonID()));
    }
    @Test
    public void getPeoplePass() throws DataAccessException {
        Person nextPerson = new Person("JJ", "Hale", "Hale123A",
                "John", "Hale", "M", "JJ", "HIG");
        eDao.insert(bestPerson);
        eDao.insert(nextPerson);
        ArrayList<Person>checkPersons = new ArrayList<>();
        checkPersons.add(bestPerson);
        checkPersons.add(nextPerson);
        assertEquals(checkPersons, eDao.getPeople("Hale"));
    }
    @Test
    public void getPeopleFail() throws DataAccessException {
        eDao.delete();
        ArrayList<Person>check = new ArrayList<>();
        assertEquals(check,eDao.getPeople("HE"));
    }
    @Test
    public void getFamilyPass() throws DataAccessException {
        Person nextPerson = new Person("BN", "Hale", "Hale123A",
                "John", "Hale", "M", "JJ", "HIG");
        Person otherPerson = new Person("OGH", "Hale", "Alex",
                "John", "Hale", "M", "JJ", "HIG");
        eDao.insert(nextPerson);
        eDao.insert(otherPerson);
        eDao.insert(bestPerson);
        ArrayList<Person>checkPersons = new ArrayList<>();
        checkPersons.add(nextPerson);
        checkPersons.add(otherPerson);
        assertEquals(checkPersons, eDao.getFamily("Hale"));

    }
    @Test
    public void getFamilyFail() throws DataAccessException{
        ArrayList<Person>check = new ArrayList<>();
        assertEquals(check, eDao.getFamily("glekj"));
        assertEquals(check, eDao.getFamily(""));
    }
}
