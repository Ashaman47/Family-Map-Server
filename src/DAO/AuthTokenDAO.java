package DAO;
import Model.*;

import java.sql.*;

/**
 * Class to handle the authToken DAO
 */
public class AuthTokenDAO {
    private final Connection conn;
    public AuthTokenDAO(Connection conn)
    {
        this.conn = conn;
    }
    /**
     * Function to insert a new AuthToken into the Database
     * @param p the AuthToken object to be submitted
     */
    public void insert(AuthToken p) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO AuthToken (UserName, AuthToken)VALUES(?,?)";
        try (
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, p.getUserName());
            stmt.setString(2, p.getAuthToken());
            stmt.executeUpdate();
        } catch (
                SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }
    /**
     * Finds the AuthToken with the given UserName
     * @param UserName AuthToken of given user
     * @return returns AuthToken object
     * @throws DataAccessException errors
     */
    public AuthToken find(String UserName) throws DataAccessException{
        AuthToken AuthToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE UserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, UserName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                AuthToken = new AuthToken(rs.getString("UserName"),rs.getString("AuthToken"));
                return AuthToken;
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
    public AuthToken findAuth(String Auth) throws DataAccessException{
        AuthToken AuthToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE AuthToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Auth);
            rs = stmt.executeQuery();
            if (rs.next()) {
                AuthToken = new AuthToken(rs.getString("UserName"),rs.getString("AuthToken"));
                return AuthToken;
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
     * Deletes all AuthTokens
     * @throws DataAccessException
     */
    public void delete() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM AuthToken";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
