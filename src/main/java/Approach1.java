import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Approach1 implements Runnable{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "ruchirthaman";
    private static final String PASS = "password";
    private int userId;
    public Approach1(int userId){
        this.userId = userId;
    }
    public void run() {
        try {

            System.out.println("Thread is running...");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "UPDATE  seats SET userId = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            Random random = new Random();
            pstmt.setInt(1, this.userId); // set userId parameter
            pstmt.setInt(2, random.nextInt(116)+1); // set userId parameter
            int affectedRows = pstmt.executeUpdate();
            System.out.println(affectedRows + " row(s) updated.");
            pstmt.close();
            conn.close();
        }
        catch(Exception e){
            System.out.println("Exception in running" +e);
        }
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            List<Thread> threads = new ArrayList<Thread>();

            for(int i=1;i<=116;i++) {
                Approach1 myRunnable = new Approach1(i);
                Thread t1 = new Thread(myRunnable);
                t1.start();  // invokes the run() method
                threads.add(t1);
            }
            // Now, wait for each thread to complete using join()
            for(Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 3. Prepare the SQL statement
            String query = "SELECT userId,name FROM seats ORDER BY id ASC";
            PreparedStatement pstmt = conn.prepareStatement(query);

            // 4. Execute the query
            ResultSet rs = pstmt.executeQuery();
            StringBuilder[] array = new StringBuilder[29];

            for (int i = 0; i < 29; i++) {
                array[i] = new StringBuilder(new String(new char[4]).replace("\0", "."));
            }
            // 5. Process the result
            int ctr = 0, tot = 0;
            while (rs.next()) {
                String name = rs.getString("name");
                int userId = rs.getInt("userId");
                System.out.println("USER IS : "+userId+" SEAT IS : "+name);
                if(userId!=0){
                    array[ctr].setCharAt(tot%4,'x');
                }
                tot++;
                if(tot%4==0) {
                    ctr++;
                }
            }
            System.out.println(Arrays.toString(array));

            // 6. Close resources
            rs.close();
            pstmt.close();
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