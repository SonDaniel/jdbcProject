import java.sql.*;
import java.util.Scanner;
//import org.apache.derby.jdbc.ClientDriver;
import java.util.InputMismatchException;

public class Main {
    //  Database credentials
    private static String USER;
    private static String PASS;
    private static String DBNAME;

    private static final String displayFormat="%-20s%-20s%-20s%-20s\n";
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

        do {
            ResultSet rs = null;
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
            System.out.println("|            - Menu will exit on input [0] -               |");
            System.out.println(" ----------------------------------------------------------");
            try {
                choice = input.nextInt();

                switch(choice) {
                    case 1 :
                        try {
                            //STEP 4: Execute a query
                            System.out.println("Creating statement...");
                            stmt = conn.createStatement();
                            String sql;
                            sql = "SELECT * FROM WritingGroup";
                            rs = stmt.executeQuery(sql);
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.print(rsmd.getColumnName(ii) + "         ");
                            }
                            System.out.println("");
                            while (rs.next()) {
                                //Retrieve by column name
                                String groupName = rs.getString("GroupName");
                                String headWriter = rs.getString("HeadWriter");
                                String yearFormed = rs.getString("YearFormed");
                                String subject = rs.getString("Subject");

                                //Display values
                                System.out.printf(displayFormat,
                                        dispNull(groupName), dispNull(headWriter), dispNull(yearFormed), dispNull(subject));
                            }
                        } catch(SQLException ex) {
                            ex.printStackTrace();
                        } finally {
                            //finally block used to close resources
                            try {
                                if (rs != null) {
                                    rs.close();
                                }
                            } catch (SQLException se2) {
                            }// nothing we can do
                        }

                        break;
                    case 2 :

                        break;
                    case 3 :
                        try {
                            System.out.println("Creating statement...");
                            stmt = conn.createStatement();
                            String sql;
                            sql = "SELECT * FROM Publisher";
                            rs = stmt.executeQuery(sql);

                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.print(rsmd.getColumnName(ii) + "      ");
                            }
                            System.out.println("");
                            while (rs.next()) {
                                //Retrieve by column name
                                String publisherName = rs.getString(rsmd.getColumnName(1));
                                String publisherAddress = rs.getString(rsmd.getColumnName(2));
                                String publisherPhone = rs.getString(rsmd.getColumnName(3));
                                String publisherEmail = rs.getString(rsmd.getColumnName(4));

                                //Display values
                                System.out.printf(displayFormat,
                                        dispNull(publisherName), dispNull(publisherAddress), dispNull(publisherPhone), dispNull(publisherEmail));
                            }
                        } catch(SQLException ex) {
                            ex.printStackTrace();
                        } finally {
                            //finally block used to close resources
                            try {
                                if (rs != null) {
                                    rs.close();
                                }
                            } catch (SQLException se2) {
                            }// nothing we can do
                        }

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
                System.out.println("Input is invalid. Try again.");
            }
        } while(choice != 0);

    }
}