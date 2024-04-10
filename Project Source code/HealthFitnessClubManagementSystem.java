import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

//Name: Keegan Engel
//Student Number: 101149049
//Group: 183
public class HealthFitnessClubManagementSystem {

    //Current user must configure
    private static final String dataBaseURL = "jdbc:postgresql://localhost:5432/COMP 3005 Project DB";
    private static final String dataBaseUserName = "postgres";
    private static final String dataBasePassword = "dAtaBa$3u@R!";






    // == MEMBER FUNCTIONS == //

    /**
     * User Registration Function.
     *
     * @param name;
     * @param email;
     * @param password;
     * @param fitnessGoal;
     * @param height;
     * @param weight;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * inserts passed in data to the Member table in the database.
     */
    public void registerMemberProfile(String name, String email, String password, String fitnessGoal, int height, int weight) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "INSERT INTO Member (name, email, password, fitness_goal, height, weight) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, password);
                statement.setString(4, fitnessGoal);
                statement.setInt(5, height);
                statement.setInt(6, weight);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {

                    generateBill(conn, email); // Generate bill for new member
                } else {
                    System.out.println("ERROR FAILED TO REGISTER NEW MEMBER!");
                }
            }
        } catch (SQLException e) { //catch exception
            e.printStackTrace();
        }
    }

    /**
     * User profile management function.
     *
     * @param memberId;
     * @param fitnessGoal;
     * @param height;
     * @param weight;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * updates passed in data to the Member table in the database.
     */
    public void updateMemberProfile(int memberId, String fitnessGoal, int height, int weight) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "UPDATE Member SET fitness_goal = ?, height = ?, weight = ? WHERE member_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, fitnessGoal);
                statement.setInt(2, height);
                statement.setInt(3, weight);
                statement.setInt(4, memberId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {//catch exception
            e.printStackTrace();

        }
    }

    /**
     * Dashboard Display function
     * @param memberId;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * selects the data associated with the passed in memberID.
     */
    public void displayDashboard(int memberId) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "SELECT * FROM ExerciseRoutine WHERE member_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, memberId);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int routineId = rs.getInt("routine_id");
                        Date date = rs.getDate("date");
                        String exercise = rs.getString("exercise");
                        int durationMinutes = rs.getInt("duration_minutes");
                        double caloriesBurned = rs.getDouble("calories_burned");

                        System.out.println("Routine ID: " + routineId);
                        System.out.println("Date: " + date);
                        System.out.println("Exercise: " + exercise);
                        System.out.println("Duration (minutes): " + durationMinutes);
                        System.out.println("Calories Burned: " + caloriesBurned);
                        System.out.println();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Profile management health metrics
     * @param memberId;
     * @param exercise;
     * @param durationMinutes;
     * @param caloriesBurned;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * inserts the exercise data for a passed in member.
     */

    public void logExercise(int memberId, String exercise, int durationMinutes, double caloriesBurned, Date date) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "INSERT INTO ExerciseRoutine (member_id, date, exercise, duration_minutes, calories_burned) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, memberId);
                stmt.setDate(2, new java.sql.Date(date.getTime())); // Convert java.util.Date to java.sql.Date
                stmt.setString(3, exercise);
                stmt.setInt(4, durationMinutes);
                stmt.setDouble(5, caloriesBurned);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {// do nothing print is in main

                } else {
                    System.out.println("ERROR EXERCISE WAS NOT LOGGED!");
                }
            }
        } catch (SQLException e) {// catch exception
            e.printStackTrace();
        }
    }


    /**
     * Schedule Management function.
     *
     * @param memberId;
     * @param trainerId;
     * @param startTime;
     * @param endTime;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * inserts passed in data into the PersonalTrainingSession table allowing Members to
     * sign up for training session at the gym.
     */
    public void scheduleSessionForMembers(int memberId, int trainerId, String startTime, String endTime) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "INSERT INTO PersonalTrainingSession (member_id, trainer_id, start_time, end_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, memberId);
                statement.setInt(2, trainerId);
                statement.setString(3, startTime);
                statement.setString(4, endTime);
                statement.executeUpdate();
            }
        } catch (SQLException e) {// catch exception
            e.printStackTrace();

        }
    }

    /**
     *
     * @param memberId;
     * This function connects the specified SQL database and executes a SQL statement that
     * deletes the table that signifies a scheduled session with a trainer.
     */
    public void cancelTrainingSession(int memberId) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "DELETE FROM PersonalTrainingSession WHERE member_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, memberId);
                statement.executeUpdate();
                System.out.println("Training Session for member "+memberId+ " was cancelled.");
            }
        } catch (SQLException e) {//catch exception
            e.printStackTrace();
        }
    }

    // == TRAINER FUNCTIONS == //

    /**
     * Schedule Management function.
     *
     * @param trainerId;
     * @param startTime;
     * @param endTime;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * inserts passed in data into the TrainerAvailability table which allows trainers to
     * specify the times they are available.
     */
    public void setTrainerAvailability(int trainerId, String startTime, String endTime) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "INSERT INTO TrainerAvailability (trainer_id, start_time, end_time) VALUES (?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, trainerId);
                statement.setString(2, startTime);
                statement.setString(3, endTime);
                statement.executeUpdate();
            }
        } catch (SQLException e) {// catch exception
            e.printStackTrace();

        }
    }

    /**
     * Member profile viewing function
     *
     * @param memberName;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * prints the table data that matches with the passed in memberName
     */
    public void viewMemberProfile(String memberName) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "SELECT * FROM Member WHERE name LIKE ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, "%" + memberName + "%"); // Search for member name containing the input string
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int memberId = rs.getInt("member_id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        String fitnessGoal = rs.getString("fitness_goal");
                        int height = rs.getInt("height");
                        int weight = rs.getInt("weight");

                        // Display member profile details
                        System.out.println("Member ID: " + memberId);
                        System.out.println("Name: " + name);
                        System.out.println("Email: " + email);
                        System.out.println("Fitness Goal: " + fitnessGoal);
                        System.out.println("Height: " + height);
                        System.out.println("Weight: " + weight);
                        System.out.println();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle member profile viewing failure
        }
    }


    //== ADMINISTRATIVE STAFF FUNCTIONS ==//

    // Room Booking Management

    /**
     * Room Booking Management function.
     *
     * @param classId;
     * @param roomId;
     * @param startTime;
     * @param endTime;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * inserts passed in data into the TrainerAvailability table which allows trainers to
     * specify the times they are available.
     *
     */
    public void bookClubRoom(int classId, int roomId, String startTime, String endTime) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "INSERT INTO RoomBooking (class_id, room_id, start_time, end_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, classId);
                statement.setInt(2, roomId);
                statement.setString(3, startTime);
                statement.setString(4, endTime);
                statement.executeUpdate();
            }
        } catch (SQLException e) {// catch exception
            e.printStackTrace();

        }
    }

    /**
     * Equipment Maintenance Monitoring function.
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * selects the from the tuple of the maintenance_required boolean value in the
     * Equipment table to determine if maintenance is needed.
     */
    public void monitorMaintenanceStatus() {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "SELECT * FROM Equipment";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int equipmentId = rs.getInt("equipment_id");
                        String equipmentName = rs.getString("equipment_name");
                        boolean status = rs.getBoolean("available");
                        System.out.println("Equipment ID: " + equipmentId + ", Name: " + equipmentName + ", Maintenance Status: " + status);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class Schedule Updating function
     *
     * @param classId;
     * @param startTime;
     * @param endTime;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * updates passed in data into the Class table in order to update the schedule for
     * a class.
     */
    public void updateClassSchedule(int classId, String startTime, String endTime) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "UPDATE Class SET start_time = ?, end_time = ? WHERE class_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, startTime);
                statement.setString(2, endTime);
                statement.setInt(3, classId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {// catch exception
            e.printStackTrace();

        }
    }

    /**
     * Billing and Payment Processing function
     *
     * @param memberId;
     * @param amount;
     * @param paymentMethod;
     *
     * This function connects the specified SQL database and executes a SQL statement that
     * updates passed in data into the Payment table in order to process a payment made by
     * a member.
     */
    public void processMemberPayment(int memberId, double amount, String paymentMethod) {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "UPDATE Payment SET amount = ?, payment_method = ?, payment_status = true WHERE member_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setDouble(1, amount);
                statement.setString(2, paymentMethod);
                statement.setInt(3, memberId);
                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param conn;
     * @param email;
     * This function connects the specified SQL database and executes a SQL statement that
     * inserts passed in data into the Payment table in order to generate a bill for a new member
     */
    private void generateBill(Connection conn, String email) {
        try {
            // Insert a new payment record for the newly registered member
            String insertPaymentSQL = "INSERT INTO Payment (member_id, amount, payment_method, payment_status) VALUES ((SELECT member_id FROM Member WHERE email = ?), ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(insertPaymentSQL)) {
                statement.setString(1, email); // email is used to find member
                statement.setDouble(2, 100.00); // Amount owed is $100
                statement.setNull(3, java.sql.Types.VARCHAR); // Payment method initially null
                statement.setBoolean(4, false); // Bill initially not paid
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Bill successfully generated for new member.");
                } else {
                    System.out.println("ERROR MEMBER BILL COULD NOT BE GENERATED!");
                }
            }
        } catch (SQLException e) {// catch exception
            e.printStackTrace();
        }
    }

    /**
     * This function connects the specified SQL database and executes a SQL statement that
     * sets all the available equipment to true showing that maintenance was performs for
     * all gym equipment.
     */
    public void performMaintenance() {
        String sql = "UPDATE Equipment SET available = true";

        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
            System.out.println("All equipment maintenance complete");
        } catch (SQLException e) {// catch exception
            e.printStackTrace();
        }
    }

    /**
     * This function connects the specified SQL database and executes a SQL statement that
     *  selects passing data to show all currents payments made to health club
     */
    public void getPayments() {
        try (Connection conn = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword)) {
            String sql = "SELECT * FROM Payment";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int paymentId = rs.getInt("payment_id");
                        int memberId = rs.getInt("member_id");
                        double amount = rs.getDouble("amount");
                        String paymentMethod = rs.getString("payment_method");
                        boolean paymentStatus = rs.getBoolean("payment_status");

                        System.out.println("Payment ID: " + paymentId);
                        System.out.println("Member ID: " + memberId);
                        System.out.println("Amount: " + amount);
                        System.out.println("Payment Method: " + paymentMethod);
                        System.out.println("Payment Status: " + paymentStatus);
                        System.out.println();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        HealthFitnessClubManagementSystem system = new HealthFitnessClubManagementSystem();
        Scanner scanner = new Scanner(System.in);

        //TEST CONNECTION TO DATABASE
        try {
            Connection connection = DriverManager.getConnection(dataBaseURL, dataBaseUserName, dataBasePassword);
            System.out.println("CONNECTION TO DATABASE WAS ESTABLISHED");
            connection.close(); // exit test connection if successful and continue
        } catch (SQLException e) {
            System.err.println("ERROR CANNOT CONNECT TO DATABASE " + e.getMessage());
        }



        while (true) {
            System.out.println();
            System.out.println("Welcome to Fitness Club Management System");
            System.out.println("Select an option:");
            System.out.println("-----Member functions-----");
            System.out.println("1: Register a new member"); //registerMemberProfile && generateBill
            System.out.println("2: Update member data"); //updateMemberProfile
            System.out.println("3: Log an exercise for a member"); //logExercise
            System.out.println("4: Display member dashboard"); //displayDashboard
            System.out.println("5: Schedule a training session"); //scheduleSessionForMembers
            System.out.println("6: Make a payment"); //processMemberPayment
            System.out.println("7: Cancel Training Session"); //cancel session
            System.out.println("-----Trainer functions-----");
            System.out.println("8: View member profile"); //viewMemberProfile
            System.out.println("9: Set schedule"); //setTrainerAvailability
            System.out.println("-----Administrative Staff functions-----");
            System.out.println("10: Book room"); //bookClubRoom
            System.out.println("11: Check equipment maintenance"); //monitorMaintenanceStatus
            System.out.println("12: Update class schedule");//updateClassSchedule
            System.out.println("13: Perform maintenance");//perform maintenance
            System.out.println("14: Get payments");// get payments
            System.out.println("-----EXIT PROGRAM-----");
            System.out.println("15: EXIT");




            int input = scanner.nextInt();
            scanner.nextLine();

            switch (input) {

                case 1: //Register a new member
                    System.out.println("Enter member name (only string type accepted):");
                    String name1 = scanner.nextLine();

                    System.out.println("Enter an email (only string type accepted):");
                    String email1 = scanner.nextLine();

                    System.out.println("Enter a password (only string type accepted):");
                    String password1 = scanner.nextLine();

                    System.out.println("Enter a fitness goal (only string type accepted):");
                    String fitnessGoal1 = scanner.nextLine();

                    System.out.println("Enter member height in cm (only int type accepted):");
                    int height1 = scanner.nextInt();

                    System.out.println("Enter weight in lbs (only int type accepted): ");
                    int weight1 = scanner.nextInt();

                    //registerMemberProfile(String name, String email, String password, String fitnessGoal, int height, int weight)
                    system.registerMemberProfile(name1, email1, password1, fitnessGoal1, height1, weight1);
                    System.out.println("New member registration successful. Welcome"+" "+ name1);

                    break;

                case 2: //Update member data
                    System.out.println("Enter member ID (only int type accepted):");
                    int id2 = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter a new fitness goal (only string type accepted):");
                    String fitnessGoal2 = scanner.nextLine();

                    System.out.println("Enter a new height (only int type accepted):");
                    int height2 = scanner.nextInt();

                    System.out.println("Enter a new weight (only int type accepted):");
                    int weight2 = scanner.nextInt();


                    //updateMemberProfile(int memberId, String fitnessGoal, int height, int weight)
                    system.updateMemberProfile(id2,fitnessGoal2, height2, weight2);
                    System.out.println("Member data for member "+id2+" "+"successfully updated. Welcome back");
                    break;

                case 3: //Log exercise
                    System.out.println("Enter member ID (only int type accepted):");
                    int id3 = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter completed exercise (only string type accepted):");
                    String exercise3 = scanner.nextLine();

                    System.out.println("Enter exercise duration (only int type accepted):");
                    int duration3 = scanner.nextInt();

                    System.out.println("Enter calories burned (only float type accepted):");
                    double cals3 = scanner.nextDouble();

                    //public void logExercise(int memberId, String exercise, int durationMinutes, double caloriesBurned)
                    system.logExercise(id3, exercise3, duration3, cals3, new Date());

                    System.out.println("Exercise for member "+id3 +" "+ "logged successfully.");

                    break;

                case 4: //Display dashboard
                    System.out.println("Enter member ID (only int type accepted):");
                    int id4 = scanner.nextInt();

                    //displayDashboard(int memberId)
                    system.displayDashboard(id4);
                    break;
                case 5: //Schedule training session
                    System.out.println("Enter member ID (only int type accepted):");
                    int id5 = scanner.nextInt();
                    System.out.println("Enter trainer ID (only int type accepted):");
                    int tid5 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter start time (only string type accepted):");
                    String starttime5 = scanner.nextLine();
                    System.out.println("Enter end time (only string type accepted):");
                    String endtime5 = scanner.nextLine();

                    system.scheduleSessionForMembers(id5, tid5, starttime5, endtime5);
                    System.out.println("Exercise class for member "+id5 +" "+ "was successfully scheduled.");
                    //scheduleSessionForMembers(int memberId, int trainerId, String startTime, String endTime)
                    break;

                case 6: //Make payment
                    System.out.println("Enter member ID (only int type accepted):");
                    int id6 = scanner.nextInt();
                    System.out.println("Enter payment amount (only int float accepted):");
                    double pay6 = scanner.nextDouble();
                    System.out.println("Enter a payment method (only string type accepted):");
                    scanner.nextLine();
                    String paymethod6 = scanner.nextLine();

                    system.processMemberPayment(id6, pay6, paymethod6);
                    System.out.println("Payment for member "+id6 +" "+ "was received successfully.");
                    //processMemberPayment(int memberId, double amount, String paymentMethod)
                    break;

                case 7:
                    System.out.println("Enter member ID (only int type accepted):");
                    int id = scanner.nextInt();
                    system.cancelTrainingSession(id);
                    break;

                case 8:// View member profile
                    System.out.println("Enter member name (only string type accepted):");
                    String name7 = scanner.nextLine();

                    System.out.println("----USER DASHBOARD----");
                    system.viewMemberProfile(name7);
                    //viewMemberProfile(String memberName)
                    break;

                case 9:// Set trainer schedule
                    System.out.println("Enter trainer ID (only int type accepted):");
                    int tid8 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter start time (only string type accepted):");
                    String starttime7 = scanner.nextLine();
                    System.out.println("Enter end time (only string type accepted):");
                    String endtime7 = scanner.nextLine();

                    system.setTrainerAvailability(tid8, starttime7, endtime7);
                    System.out.println("Schedule for trainer "+tid8 +" "+ "was set successfully.");
                    //setTrainerAvailability(int trainerId, String startTime, String endTime)
                    break;

                case 10:// Book room
                    System.out.println("Enter class ID (only int type accepted):");
                    int classid9 = scanner.nextInt();
                    System.out.println("Enter room ID (only int type accepted):");
                    int roomid9 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter start time (only string type accepted):");
                    String starttime9 = scanner.nextLine();
                    System.out.println("Enter end time (only string type accepted):");
                    String endtime9 = scanner.nextLine();

                    system.bookClubRoom(classid9, roomid9, starttime9, endtime9);
                    System.out.println("Room "+roomid9+" "+ "was successfully booked.");
                    //bookClubRoom(int classId, int roomId, String startTime, String endTime)
                    break;

                case 11://Check equipment maintenance
                    system.monitorMaintenanceStatus();
                    break;

                case 12://Update class schedule
                    System.out.println("Enter class ID (only int type accepted):");
                    int classid11 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter new start time (only string type accepted):");
                    String starttime11 = scanner.nextLine();
                    System.out.println("Enter new end time: (only string type accepted)");
                    String endtime11 = scanner.nextLine();

                    system.updateClassSchedule(classid11, starttime11, endtime11);
                    System.out.println("Schedule for class "+classid11+" "+ "was successfully updated.");
                    //updateClassSchedule(int classId, String startTime, String endTime)
                    break;

                    case 13://perform maintenance
                    system.performMaintenance();
                    break;

                    case 14://get payments
                    system.getPayments();
                    break;

                case 15://Exit
                    System.out.println("Exiting. Goodbye.");
                    return;

                default:
                    System.out.println("Invalid entry! Please try again.");

            }


        }
    }

}