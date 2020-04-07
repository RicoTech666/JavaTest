package Classes;

public class InputParser {
    private String input;

    public InputParser(String input) {
        this.input = input;
    }

    public String parseInit() {
        String parkinglotA = input.split(",")[0];
        String parkinglotB = input.split(",")[1];

        return "INSERT INTO parkinglot_list (name,capacity)\n" +
                "VALUES(\"" + parkinglotA.split(":")[0] + "\"," + parkinglotA.split(":")[1]+"),\n" +
                "(\"" + parkinglotB.split(":")[0] + "\","+parkinglotB.split(":")[1]+");\n";
    }

    public String parseTicket() {
        String parkinglotName = input.split(",")[0];
        String position = input.split(",")[1];
        String carNumber = input.split(",")[2];

        return "SELECT position from parked_cars AS t2 \n" +
                "INNER JOIN parkinglot_list AS t1\n" +
                "ON t1.name=\"" + parkinglotName + "\" AND t1.id=t2.parkinglot_id AND t2.position=" + position + " AND t2.license=\"" + carNumber + "\";";
    }
}
