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
}
