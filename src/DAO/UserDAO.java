package DAO;
import Model.*;

import java.sql.*;
import java.util.ArrayList;
/**
 * Class to handle the User DAO
 */
public class UserDAO {
    private final Connection conn;

    /**
     * UserDao constructor
     * @param conn connection to the sql database
     */
    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }
    /**
     * inserts a User into the database
     * @param p User to be inserted
     */
    public void insert(User p) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO User (UserName, password, email, firstName, lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, p.getuserName());
            stmt.setString(2, p.getpassword());
            stmt.setString(3, p.getemail());
            stmt.setString(4, p.getfirstName());
            stmt.setString(5, p.getlastName());
            stmt.setString(6, p.getgender());
            stmt.setString(7, p.getpersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }
    /**
     *Deletes all Users from the database
     */
    public void delete() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM User";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    /**
     * Finds the user with the given UserName
     * @param UserName UserName of given user
     * @return returns User object
     * @throws DataAccessException
     */
    public User find(String UserName) throws DataAccessException{
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE UserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, UserName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("UserName"),rs.getString("Password"),rs.getString("Email"),
                        rs.getString("FirstName"),rs.getString("LastName"),
                        rs.getString("Gender"),rs.getString("PersonID"));
                return user;
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

    /**
     * gets all users from the database
     * @return arraylist with all users
     * @throws DataAccessException
     */
    public ArrayList<User>getUsers() throws DataAccessException{
        ArrayList<User> Users = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM User";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(rs.getString("UserName"), rs.getString("Password"), rs.getString("Email"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("PersonID"));
                Users.add(user);
            }
            return Users;
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
