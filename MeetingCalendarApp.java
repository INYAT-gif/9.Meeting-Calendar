import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MeetingCalendarApp {

    // Function to add a new meeting
    public static void addMeeting(Connection conn, String title, String date, String time) throws SQLException {
        String sql = "INSERT INTO meetings (title, date, time) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, title);
        pstmt.setString(2, date);
        pstmt.setString(3, time);
        pstmt.executeUpdate();
    }

    // Function to list all meetings
    public static void listMeetings(Connection conn) throws SQLException {
        String sql = "SELECT * FROM meetings";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String date = rs.getString("date");
            String time = rs.getString("time");
            System.out.println("ID: " + id + ", Title: " + title + ", Date: " + date + ", Time: " + time);
        }
    }

    public static void main(String[] args) {
        // JDBC connection parameters
        String url = "jdbc:sqlite:meetings.db";

        // SQLite connection
        try (Connection conn = DriverManager.getConnection(url)) {
            // Create a table to store meetings if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS meetings (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, date TEXT, time TEXT)";
            Statement stmt = conn.createStatement();
            stmt.execute(createTableSQL);

            // Handling command line arguments
            if (args.length < 1) {
                System.out.println("Usage: java MeetingCalendarApp <command> [options]");
                System.out.println("Commands:");
                System.out.println("  add --title <title> --date <date> --time <time> : Add a new meeting");
                System.out.println("  list : List all meetings");
                return;
            }

            // Execute command based on user input
            String command = args[0];
            switch (command) {
                case "add":
                    if (args.length < 7 || !args[1].equals("--title") || !args[3].equals("--date") || !args[5].equals("--time")) {
                        System.out.println("Usage: java MeetingCalendarApp add --title <title> --date <date> --time <time>");
                        return;
                    }
                    String title = args[2];
                    String date = args[4];
                    String time = args[6];
                    addMeeting(conn, title, date, time);
                    System.out.println("Meeting added successfully.");
                    break;
                case "list":
                    listMeetings(conn);
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
