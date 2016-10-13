import java.sql.*;
import java.util.Scanner;
//import org.apache.derby.jdbc.ClientDriver;
import java.util.InputMismatchException;

public class Main {
    //  Database credentials
    private static String USER;
    private static String PASS;
    private static String DBNAME;

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
            System.out.println("| 'Remove a book specified by a user'                  [9] |");
            System.out.println("|                                                          |");
            System.out.println(" ----------------------------------------------------------");
            System.out.println("|            - Menu will exit on input [0] -               |");
            System.out.println(" ----------------------------------------------------------");
            try {
                System.out.print("Choice: ");
                choice = input.nextInt();

                System.out.println("Creating statement...");
                switch(choice) {
                    case 1 :
                        try {
                            //STEP 4: Execute a query
                            stmt = conn.createStatement();
                            String sql;
                            sql = "SELECT GroupName FROM WritingGroup";
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

                                //Display values
                                System.out.println(groupName);
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
                    case 2 : //view all data for a specific user
                        try {
                            //STEP 4: Execute a query
                            String sql;
                            sql = "SELECT * FROM WritingGroup where GroupName = ?";
                            PreparedStatement statement = conn.prepareStatement(sql);
                            System.out.print("Enter a group name: ");
                            input.nextLine();
                            String userInput = input.nextLine();
                            statement.setString(1, userInput);
                            rs = statement.executeQuery();
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
                                System.out.printf("%-20s%-20s%-20s%-20s\n",
                                        dispNull(groupName), dispNull(headWriter), dispNull(yearFormed), dispNull(subject) , dispNull(""));
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
                    case 3 :
                        try {
                            System.out.println("Creating statement...");
                            stmt = conn.createStatement();
                            String sql;
                            sql = "SELECT PublisherName FROM Publisher";
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

                                //Display values
                                System.out.println(publisherName);
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
                        try {
                            //STEP 4: Execute a query
                            String sql;
                            sql = "SELECT * FROM Publisher where PublisherName = ?";
                            PreparedStatement statement = conn.prepareStatement(sql);
                            System.out.print("Enter a Publisher's name: ");
                            input.nextLine();
                            String userInput = input.nextLine();
                            statement.setString(1, userInput);
                            rs = statement.executeQuery();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.print(rsmd.getColumnName(ii) + "         ");
                            }

                            System.out.println("");
                            while (rs.next()) {
                                //Retrieve by column name
                                String publisherName = rs.getString(rsmd.getColumnName(1));
                                String publisherAddress = rs.getString(rsmd.getColumnName(2));
                                String publisherPhone = rs.getString(rsmd.getColumnName(3));
                                String publisherEmail = rs.getString(rsmd.getColumnName(4));

                                //Display values
                                System.out.printf("%-20s%-20s%-20s%-20s\n",
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
                    case 5 :
                        try {
                            stmt = conn.createStatement();
                            String sql;
                            sql = "SELECT bookTitle FROM Book";
                            rs = stmt.executeQuery(sql);

                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.print(rsmd.getColumnName(ii) + "      ");
                            }
                            System.out.println("");
                            while (rs.next()) {
                                //Retrieve by column name
                                String bookTitles = rs.getString(rsmd.getColumnName(1));

                                //Display values
                                System.out.println(bookTitles);
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
                    case 6:
                        try {
                            //STEP 4: Execute a query
                            String sql;
                            sql = "SELECT * FROM Book where BookTitle = ?";
                            PreparedStatement statement = conn.prepareStatement(sql);
                            System.out.print("Enter a Book Title: ");
                            input.nextLine();
                            String userInput = input.nextLine();
                            statement.setString(1, userInput);
                            rs = statement.executeQuery();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.print(rsmd.getColumnName(ii) + "         ");
                            }

                            System.out.println("");
                            while (rs.next()) {
                                //Retrieve by column name
                                String groupName = rs.getString(rsmd.getColumnName(1));
                                String bookTitle = rs.getString(rsmd.getColumnName(2));
                                String publisherName = rs.getString(rsmd.getColumnName(3));
                                String yearPublished = rs.getString(rsmd.getColumnName(4));
                                int numPages = rs.getInt(rsmd.getColumnName(5));

                                //Display values
                                System.out.printf("%-20s%-20s%-20s%-20s%-20s\n",
                                        dispNull(groupName), dispNull(bookTitle), dispNull(publisherName), dispNull(yearPublished), dispNull(Integer.toString(numPages)));
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
                    case 7 :
                        try{
                            String sql;
                            sql = "insert into Book (GroupName, BookTitle, PublisherName, YearPublished, NumberPages) VALUES( ?, ?, ?, ?, ?);";
                            PreparedStatement statement = conn.prepareStatement(sql);
                            System.out.print("Please create a book");
                            System.out.println("Enter your Book Group Name");
                            String GroupName = input.next();
                            System.out.println("Please input the Title of the Book");
                            String BookTItle = input.next();
                            System.out.println("Please input the Publisher Name");
                            String PublisherName = input.next();
                            System.out.println("Please input the Year of Publish");
                            String YearPublished = input.next();
                            System.out.println("Please input the Number of Pages");
                            String NumberPages = input.next();
                            statement.setString(1, GroupName);
                            statement.setString(2, BookTItle);
                            statement.setString(3, PublisherName);
                            statement.setString(4, YearPublished);
                            statement.setString(5, NumberPages);
                            statement.executeUpdate();
                            statement.close();
                            System.out.println(BookTItle + " Has been inserted." );


                        }
                        catch(SQLException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 8 :

                        break;
                    case 9 :

                        break;
                    default:

                        break;

                }
            } catch(InputMismatchException ex) {
                System.out.println("Input is invalid. Try again.");
                input.nextLine(); //resetting input to get next input

            }
        } while(choice != 0);

    }
}