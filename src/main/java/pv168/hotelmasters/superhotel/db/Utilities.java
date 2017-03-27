package pv168.hotelmasters.superhotel.db;

import pv168.hotelmasters.superhotel.exceptions.DBException;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Kristian Lesko
 */
public class Utilities {
    public static Long parseId(ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.getMetaData().getColumnCount() != 1) {
            throw new DBException("Invalid relation primary key setting, more than one generated key found");
        }
        if (generatedKeys.next()) {
            Long result = generatedKeys.getLong(1);
            if (generatedKeys.next()) {
                throw new DBException("Statement contains more than one row, cannot return a valid ID");
            }
            return result;
        } else {
            throw new DBException("Statement does not contain any rows, cannot return ID");
        }
    }

    public static void checkUpdateSanity(int updateCount, boolean isInsertion) {
        if (isInsertion && updateCount != 1) {
            throw new DBException("Update integrity error: " + updateCount + " rows affected by update (should be 1)");
        }
        if (updateCount == 0) {
            throw new DBException("Update integrity error: should have updated 1 row but updated 0");
        }
    }

    public static void executeSql(URL url, DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            for (String statement : readSql(url)) {
                String trimmedStatement = statement.trim();
                if (trimmedStatement.isEmpty()) {
                    continue;
                }
                connection.prepareStatement(trimmedStatement).executeUpdate();
            }
        }
    }

    private static String[] readSql(URL url) {
        try {
            char buffer[] = new char[256];
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            int count = reader.read(buffer);
            while (count > 0) {
                result.append(buffer, 0, count);
                count = reader.read(buffer);
            }
            return result.toString().split(";");
        } catch (IOException e) {
            throw new DBException("Error reading SQL script from " + url + ":", e);
        }
    }
}
