import java.sql.*;
import java.util.ArrayList;
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
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            startMenu(conn);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
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

    static void startMenu(Connection conn) {
        Scanner input = new Scanner(System.in);
        int choice = -1;

        do {
            Statement stmt = null;
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
                input.nextLine();
                switch(choice) {
                    case 1 :
                        try {
                            stmt = conn.createStatement();
                            String sql;
                            sql = "SELECT GroupName FROM WritingGroup";
                            rs = stmt.executeQuery(sql);
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.printf("%-20s",rsmd.getColumnName(ii));
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

                            try {
                                if (stmt != null) {
                                    stmt.close();
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
                            String userInput = input.next();
                            input.nextLine();

                            statement.setString(1, userInput);
                            rs = statement.executeQuery();

                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.printf("%-20s",rsmd.getColumnName(ii));
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
                                System.out.printf("%-20s",rsmd.getColumnName(ii));
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
                            String userInput = input.next();
                            input.nextLine();
                            statement.setString(1, userInput);
                            rs = statement.executeQuery();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.printf("%-20s",rsmd.getColumnName(ii));
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
                                System.out.printf("%-20s",rsmd.getColumnName(ii));
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
                            String userInput = input.next();
                            input.nextLine(); //clearing nextLine

                            statement.setString(1, userInput);
                            rs = statement.executeQuery();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.printf("%-20s",rsmd.getColumnName(ii));
                            }

                            System.out.println("");
                            while (rs.next()) {
                                //Retrieve by column name
                                String groupName = rs.getString(rsmd.getColumnName(1));
                                String bookTitle = rs.getString(rsmd.getColumnName(2));
                                String publisherName = rs.getString(rsmd.getColumnName(3));
                                int yearPublished = rs.getInt(rsmd.getColumnName(4));
                                int numPages = rs.getInt(rsmd.getColumnName(5));

                                //Display values
                                System.out.printf("%-20s%-20s%-20s%-20s%-20s\n",
                                        dispNull(groupName), dispNull(bookTitle), dispNull(publisherName), dispNull(Integer.toString(yearPublished)), dispNull(Integer.toString(numPages)));
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
                        // have to check year is valid, have to check valid group name
                        //check publisher name
                        try {
                            String sql;
                            sql = "insert into Book (GroupName, BookTitle, PublisherName, YearPublished, NumberPages) VALUES( ?, ?, ?, ?, ?)";
                            PreparedStatement statement = conn.prepareStatement(sql);
                            System.out.print("Please enter your Book Group name: ");
                            String groupName = input.next();
                            input.nextLine();
                            //Checking to see if GroupName is within Database
                            String sqlCheck = "SELECT GroupName FROM WritingGroup";
                            stmt = conn.createStatement();
                            rs = stmt.executeQuery(sqlCheck);
                            ArrayList<String> checkList = new ArrayList<>();

                            while(rs.next()) {
                                checkList.add(rs.getString("GroupName"));
                            }

                            boolean acceptedGroup = false;

                            for(String name : checkList) {
                                if(name.equals(groupName)) {
                                    acceptedGroup = true;
                                }
                            }
                            checkList.clear();
                            if(acceptedGroup) {
                                statement.setString(1, groupName); //inserting user input into query
                                System.out.print("Please enter book Title: ");
                                String bookTitle = input.nextLine();
                                statement.setString(2, bookTitle);

                                //getting publisher name from user
                                System.out.print("Please enter Publisher name: ");
                                String publisherName = input.next();

                                //checking to see if PublisherName is within Database
                                sqlCheck = "SELECT PublisherName from Publisher";
                                rs = stmt.executeQuery(sqlCheck);

                                while(rs.next()) {
                                    checkList.add(rs.getString("PublisherName"));
                                }

                                boolean acceptedPublisher = false;

                                for(String publisher : checkList) {
                                    if(publisher.equals(publisherName)) {
                                        acceptedPublisher = true;
                                    }
                                }

                                if(acceptedPublisher) {
                                    statement.setString(3, publisherName);

                                    //getting yearPulished from user
                                    System.out.print("Please enter Published Year: ");
                                    try {
                                        int yearPublished = input.nextInt();

                                        statement.setInt(4, yearPublished);

                                        System.out.print("Please enter number of pages: ");
                                        int numPages = input.nextInt();

                                        statement.setInt(5, numPages);

                                        statement.executeUpdate();
                                        System.out.println("Book successfully added.");
                                    } catch(InputMismatchException ex) {
                                        System.out.println("Input is not an integer.");
                                    }
                                } else {
                                    System.out.println("PublisherName does not exist");
                                }
                            } else {
                                System.out.println("GroupName does not exist");
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
                    case 8 :
                        try{
                            String sql = "Update Publishers set PublisherName = ?, PublisherAddress = ?, PublisherPhone = ?, PublisherEmail = ? WHERE PublisherName = ?";
                            String sql2 = "Update Book set PublisherName = ? WHERE PublisherName = ?";
                            PreparedStatement statement = conn.prepareStatement(sql);
                            PreparedStatement statement1 = conn.prepareStatement(sql2);
                            System.out.println("Please enter the publisher to be updated");
                            String oriPub = input.next();
                            System.out.println("Finding publisher...");
                            System.out.println("Please enter the new publisher:");
                            System.out.println("Enter name: ");
                            String newPub = input.next();
                            statement1.setString(1,newPub);
                            statement1.setString(2,oriPub);
                            System.out.println("Enter Publisher Address: ");
                            String address = input.next();
                            System.out.println("Enter Phone: ");
                            String phone = input.next();
                            System.out.println("Enter Email: ");
                            String email = input.next();
                            statement.setString(1,newPub);
                            statement.setString(2,address);
                            statement.setString(3,phone);
                            statement.setString(4,email);
                            statement.setString(5,oriPub);
                            System.out.println("Finished updating publishers");
                            statement.close();
                            statement1.close();
                        }catch(SQLException ex) {
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
                    case 9 :
                        try {
                            stmt = conn.createStatement();
                            String sql = "Select BookTitle, groupname from Book";
                            rs = stmt.executeQuery(sql);

                            ResultSetMetaData rsmd = rs.getMetaData();
                            int numberOfColumns = rsmd.getColumnCount();

                            for(int ii = 1; ii <= numberOfColumns; ii++) {
                                System.out.printf("%-20s",rsmd.getColumnName(ii));
                            }
                            System.out.println("");
                            while (rs.next()) {
                                //Retrieve by column name
                                String bookTitles = rs.getString(rsmd.getColumnName(1));
                                String groupName = rs.getString(rsmd.getColumnName(2));
                                //Display values
                                System.out.printf("%-20s%-20s\n",bookTitles,groupName);
                            }
                            System.out.println("Please select the bookTitle and groupName associated to that book to remove.");
                            System.out.print("Book Title: ");
                            String bookChoice = input.next();
                            System.out.print("Group Name associated with that book: ");
                            String groupChoice = input.next();
                            input.nextLine();
                            String sql2 = "Delete from book where booktitle = ? and groupname = ?";
                            PreparedStatement statement = conn.prepareStatement(sql2);
                            statement.setString(1, bookChoice);
                            statement.setString(2, groupChoice);
                            statement.executeUpdate(); //runs command to execute
                            System.out.println("Book has been deleted");
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