package DAO;
import Model.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.sql.*;

/**
 * Class to handle the Person DAO.
 */
public class PersonDAO {
    private final Connection conn;

    /**
     * PersonDao constructor
     * @param conn connection to the sql database
     */
    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    }
    /**
     * Inserts a person into the database
     * @param p the person to be inserted
     */
    public void insert(Person p) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Person (personID, AssociatedUserName, firstName, lastName, gender, fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, p.getPersonID());
            stmt.setString(2, p.getAssociatedUsername());
            stmt.setString(3, p.getFirstName());
            stmt.setString(4, p.getLastName());
            stmt.setString(5, p.getGender());
            stmt.setString(6, p.getFatherID());
            stmt.setString(7, p.getMotherID());
            stmt.setString(8, p.getSpouseID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }
    /**
     * Deletes all persons in the database
     */
    public void delete() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    /**
     * Finds the Person with the given PersonID
     * @param PersonID the Id of this person
     * @return the person object
     * @throws DataAccessException
     */
    public Person find(String PersonID) throws DataAccessException{
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, PersonID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"),rs.getString("AssociatedUserName"),rs.getString("FirstName"),
                        rs.getString("LastName"),rs.getString("Gender"),
                        rs.getString("FatherID"),rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding User");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
    public ArrayList<Person> getPeople(String userName) throws DataAccessException{
        Person person;
        ArrayList<Person>People = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE AssociatedUserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("PersonID"),rs.getString("AssociatedUserName"),rs.getString("FirstName"),
                        rs.getString("LastName"),rs.getString("Gender"),
                        rs.getString("FatherID"),rs.getString("MotherID"), rs.getString("SpouseID"));
               People.add(person);
            }
            return People;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding User");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    /**
     * Returns all family members from the given user.
     */
    public ArrayList<Person> getFamily(String AssociatedUserName) throws DataAccessException{
        Person person;
        ArrayList<Person>People = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE AssociatedUserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, AssociatedUserName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("PersonID"),rs.getString("AssociatedUsername"),rs.getString("FirstName"),
                        rs.getString("LastName"),rs.getString("Gender"),
                        rs.getString("FatherID"),rs.getString("MotherID"), rs.getString("SpouseID"));
                People.add(person);
            }
            return People;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding User");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
