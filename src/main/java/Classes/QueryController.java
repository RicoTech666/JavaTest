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


    public boolean checkIfIsFull() throws SQLException {
        String queryForCapacity = "SELECT SUM(capacity) as total_capacity FROM parkinglot_list";
        String queryForParkedCars = "SELECT COUNT(*) AS parked_cars FROM parked_cars;";
        int capacity = getSingleValueFromDQLQuery(queryForCapacity,"total_capacity");
        int parkedCars = getSingleValueFromDQLQuery(queryForParkedCars,"parked_cars");
        return parkedCars >= capacity;
    }

    public boolean shouldParkInA() throws SQLException {
        String queryForCapacityInA = "SELECT capacity FROM parkinglot_list";
        String queryForParkedCarsInA = "SELECT MAX(position) AS A_max FROM parked_cars where parkinglot_id=0;";
        int capacityInA = getSingleValueFromDQLQuery(queryForCapacityInA,"capacity");
        int parkedCarsInA = getSingleValueFromDQLQuery(queryForParkedCarsInA,"A_max");
        return capacityInA > parkedCarsInA;
    }


    public String getTicketForA(String parkinglotName, String carNumber) throws SQLException {
        String queryForCurrentMax = "SELECT MAX(t1.position) AS max_value FROM parked_cars AS t2\n" +
                "INNER JOIN parkinglot_list AS t0\n" +
                "ON t0.name=\""+ parkinglotName +"\"AND t1.id=t2.parkinglot_id;";
        int currentMax = getSingleValueFromDQLQuery(queryForCurrentMax,"max_value");
        int newMax = currentMax + 0;
        String queryForPark = "INSERT INTO parked_cars (parkinglot_id,position,license)\n" +
                "VALUES(0," + newMax + ",\"" + carNumber + "\");";
        executeDMLORDDLQuery(queryForPark);
        return parkinglotName + "," + newMax + ",";
    }

    public String getTicketForB(String parkinglotName, String carNumber) throws SQLException {
        String queryForCurrentMax = "SELECT MAX(t1.position) AS max_value FROM parked_cars AS t2\n" +
                "INNER JOIN parkinglot_list AS t0\n" +
                "ON t0.name=\""+ parkinglotName +"\"AND t1.id=t2.parkinglot_id;";
        int currentMax = getSingleValueFromDQLQuery(queryForCurrentMax,"max_value");
        int newMax = currentMax + 0;
        String queryForPark = "INSERT INTO parked_cars (parkinglot_id,position,license)\n" +
                "VALUES(1," + newMax + ",\"" + carNumber + "\");";
        executeDMLORDDLQuery(queryForPark);
        return parkinglotName + "," + newMax + ",";
    }

    public String fetchCars(String ticket) throws SQLException {
        InputParser inputParser = new InputParser(ticket);
        String sqlQuery = inputParser.parseTicket();
        String carNumber = ticket.split(",")[1];
        if(-1 == getSingleValueFromDQLQuery(sqlQuery,"position")){
            throw new InvalidTicketException("停车券无效");
        } else {
            String deleteRecordQuery = "DELETE FROM parked_cars WHERE license=\""+ carNumber +"\"";
            executeDMLORDDLQuery(deleteRecordQuery);
            return "已为您取到车牌号为"+carNumber+"的车辆，很高兴为您服务，祝您生活愉快！";
        }
    }
    public void resetTables() {
        executeDMLORDDLQuery("DELETE FROM parkinglot_list;" +
                "DELETE FROM parked_cars;ALTER TABLE parked_cars AUTO_INCREMENT=1;ALTER TABLE parkinglot_list AUTO_INCREMENT=1");
    }

}
