import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import java.sql.*;

public class Preparation{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "ruchirthaman";
    private static final String PASS = "password";

    private static final String[] names = {
            "Alice", "Bob", "Charlie", "David", "Eve", "Frank",
            "Grace", "Hannah", "Ian", "Jill", "Kevin", "Laura",
            "Mike", "Nina", "Oscar", "Paula", "Quinn", "Rita",
            "Steve", "Tina", "Ulysses", "Vera", "Will", "Xena",
            "Yuri", "Zara", "Andy", "Belle", "Chris", "Doris",
            "Evan", "Faith", "Greg", "Heidi", "Igor", "Jane",
            "Kai", "Lynn", "Marty", "Nancy", "Olga", "Pete",
            "Queen", "Ralph", "Stacy", "Tom", "Una", "Vic",
            "Wendy", "Xander", "Yasmine", "Zane", "Amy", "Blake",
            "Celine", "Drake", "Eliza", "Floyd", "Gina", "Hank",
            "Ida", "Jack", "Kayla", "Leo", "Mia", "Ned",
            "Opal", "Phil", "Qiana", "Reese", "Sage", "Tim",
            "Ursa", "Vince", "Whitney", "Xerxes", "Yara", "Zeke",
            "Allan", "Briana", "Curt", "Dee", "Elton", "Frida",
            "Gary", "Helga", "Irvin", "Jo", "Ken", "Lila",
            "Max", "Nell", "Otto", "Polly", "Quincy", "Renee",
            "Sam", "Tori", "Upton", "Vicky", "Walt", "Xiomara",
            "Yves", "Zelda", "Arnold", "Bea", "Cal", "Dot",
            "Ernest", "Faye", "Gerald", "Hope", "Isaac", "Judy",
            "Kent", "Lola"
    };
    private static final String[] seatCharacters = {
      "A", "B", "C", "D"
    };
    public static void main(String[] args) {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String insertSQL = "INSERT INTO users (name, email) VALUES (?, ?)";
            for(int i=0;i<116;i++){
                PreparedStatement pstmt = conn.prepareStatement(insertSQL);
                pstmt.setString(1, names[i]);
                pstmt.setString(2, names[i]+"@gmail.com");

                int affectedRows = pstmt.executeUpdate();
                System.out.println(affectedRows + " row(s) inserted.");
                pstmt.close();
            }
            String insertSQLSeats = "INSERT INTO seats (name, movieId, userId) VALUES (?, ?, ?)";
            for(int i=1;i<=29;i++) {
                for(int j=0;j<4;j++) {
                    PreparedStatement pstmt = conn.prepareStatement(insertSQLSeats);
                    pstmt.setString(1, i + "-" + seatCharacters[j]);
                    pstmt.setInt(2, 1);
                    pstmt.setNull(3, Types.INTEGER);
                    int affectedRows = pstmt.executeUpdate();
                    System.out.println(affectedRows + " row(s) inserted.");
                    pstmt.close();
                }
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        try {
            // This manually deregisters JDBC driver, which prevents Tomcat from complaining about memory leaks
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            e.printStackTrace();
            // If you're logging with a framework, log this exception too
        }

    }

}