package Classes;

import exception.InvalidTicketException;
import utils.ConnectionTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryController {
    public QueryController() {
    }

    public void executeDMLORDDLQuery(String queryScript) {
        ConnectionTools connectionTools = new ConnectionTools();
        Connection connection = connectionTools.getConnect();
        Statement statement = connectionTools.getStatement(connection);
        connectionTools.executeDMLORDDL(statement, queryScript);
        ConnectionTools.closeConnect(statement,connection);
    }

    private int getSingleValueFromDQLQuery(String queryScript, String columnName) throws SQLException {
        ConnectionTools connectionTools = new ConnectionTools();
        Connection connection = connectionTools.getConnect();
        Statement statement = connectionTools.getStatement(connection);
        ResultSet rs = connectionTools.executeDQL(statement, queryScript);
        if(rs == null) {
            return 0;
        }
        int result = 0;
        while (rs.next()) {
            result = rs.getInt(columnName);
        }
        ConnectionTools.closeConnect(rs, statement, connection);
        return result;
    }


    public void resetTables() {
        executeDMLORDDLQuery("DELETE FROM parkinglot_list;" +
                "DELETE FROM parked_cars;ALTER TABLE parked_cars AUTO_INCREMENT=1;ALTER TABLE parkinglot_list AUTO_INCREMENT=1");
    }

}
