import java.sql.*;
import java.util.Scanner;
import org.apache.derby.jdbc.ClientDriver;
import java.util.InputMismatchException;

public class Main {
    //  Database credentials
    private static String USER;
    private static String PASS;
    private static String DBNAME;

    private static final String displayFormat="%-5s%-15s%-15s%-15s\n";
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private static String DB_URL = "jdbc:derby://localhost:1527/";

    /**
     * Takes the input string and outputs "N/A" if the string is empty or null.
     * @param input The string to be mapped.
     * @return  Either the input string or "N/A" as appropriate.
     */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }

    public static void main(String[] args) {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.
        Scanner in = new Scanner(System.in);
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        System.out.print("Database user name: ");
        USER = in.nextLine();
        System.out.print("Database password: ");
        PASS = in.nextLine();
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME + ";user=" + USER + ";password=" + PASS;
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            startMenu(conn, stmt);
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM WritingGroup";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            System.out.printf(displayFormat, "GroupName", "HeadWriter", "YearFormed", "Subject");
            while (rs.next()) {
                System.out.println("hmm");
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }

    static void startMenu(Connection conn, Statement stmt) {
        Scanner input = new Scanner(System.in);
        int choice = -1;
        System.out.println(" ----------------------------------------------------------");
        System.out.println("| 'View all Writing Groups'                            [1] |");
        System.out.println("| 'View all data for a group specified by a user'      [2] |");
        System.out.println("| 'View all Publishers'                                [3] |");
        System.out.println("| 'View all data for a publisher specified by a user'  [4] |");
        System.out.println("| 'View all book titles'                               [5] |");
        System.out.println("| 'View all data for a book specified by a user'       [6] |");
        System.out.println("| 'Insert a new book'                                  [7] |");
        System.out.println("| 'Insert a new publisher and update all book          [8] |");
        System.out.println("|  published by one publisher to be published              |");
        System.out.println("|  by the new publisher'                                   |");
        System.out.println("|                                                          |");
        System.out.println(" ----------------------------------------------------------");
        System.out.println("|         - Menu will exit on any other input -            |");
        System.out.println(" ----------------------------------------------------------");
        try {
            choice = input.nextInt();

            switch(choice) {
                case 1 :

                    break;
                case 2 :

                    break;
                case 3 :

                    break;
                case 4 :

                    break;
                case 5 :

                    break;
                case 6 :

                    break;
                case 7 :

                    break;
                case 8 :

                    break;
                default:

                    break;

            }
        } catch(InputMismatchException ex) {
            System.out.println("Program exiting...");
        }
    }
}