package pv168.hotelmasters.superhotel.backend.db;

import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public class DBCreator {
    public DataSource createBasicDB() {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:hotelDB");
        dataSource.setCreateDatabase("create");
        try {
            Utilities.executeSql(DBCreator.class.getResource("createTables.sql"), dataSource);
            Utilities.executeSql(DBCreator.class.getResource("addSampleRooms.sql"), dataSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
