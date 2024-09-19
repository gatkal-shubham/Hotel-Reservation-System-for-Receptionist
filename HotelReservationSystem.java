import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;


public class HotelReservationSystem {
    private static final String url="abc.com";  // replace  abc.com with your own url
    private static final String username="ABC"; // replace ABC with your username
    private static final String password="*********"; //replace ******** with your password

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.jdbc.Driver");

        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection= DriverManager.getConnection(url,username,password);
            Statement statement=connection.createStatement();
            while (true){
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner scanner=new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations ");
                System.out.println("3. Get a Room Number ");
                System.out.println("4. Update Reservation ");
                System.out.println("5. Delete Reservation ");
                System.out.println("0. Exit");
                System.out.print("Choose Option: ");
                int choice =scanner.nextInt();
                switch (choice){
                    case  1:
                        reserveRoom(scanner,statement);
                        break;
                    case 2:
                        viewReservations(statement);
                        break;
                    case 3:
                        getRoomNumber(scanner,statement);
                        break;
                    case 4:
                        updateReservation(scanner,statement);
                        break;
                    case 5:
                        deleteReservation(scanner,statement);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid Choice. Try Again.");

                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

    }
    private static void reserveRoom(Scanner scanner,Statement statement){
        try {
            System.out.println("Enter guest name: ");
            String guestName =scanner.next();
            System.out.println("Enter room Number: ");
            int roomNumber= scanner.nextInt();
            System.out.println("Enter contact Number: ");
            String contactNumber=scanner.next();
            String sql ="INSERT INTO reservations(guest_name,room_number,contact_number)"+
                    "VALUES('"+guestName+"',"+roomNumber+",'"+contactNumber+"')";

            int affectedRows=statement.executeUpdate(sql);
            if(affectedRows>0){
                System.out.println("Insertion Successful "+affectedRows+" rows affected");
            }else {
                System.out.println("Insertion Failed");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void viewReservations(Statement statement) throws SQLException{
        String sql="SELECT  reservation_id ,guest_name,  room_number, contact_number, reservation_date FROM reservations";
        ResultSet resultSet=statement.executeQuery(sql);
        System.out.println("Current Reservations: ");
        System.out.println("+---------------+-------------------+-------------+---------------------+-------------------------+");
        System.out.println("| Reservation ID|   Guest Name     | Room Number |     Contact Number  |    Reservation Date    |");
        System.out.println("+---------------+-------------------+-------------+---------------------+-------------------------+");
        while(resultSet.next()){
            int reservationId=resultSet.getInt("reservation_id");
            String guestName=resultSet.getString("guest_name");
            int roomNumber=resultSet.getInt("room_number");
            String contactNumber =resultSet.getString("contact_number");
            String reservationDate=resultSet.getString("reservation_date");

            System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
                    reservationId,guestName,roomNumber,contactNumber,reservationDate);
        }
        System.out.println("+---------------+-------------------+-------------+---------------------+-------------------------+");

    }
    private static void getRoomNumber(Scanner scanner,Statement statement) throws SQLException{
        try {
            System.out.println("Enter Reservation ID: ");
            int reservationId=scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter Guest Name: ");
            String guestName=scanner.nextLine();
            String sql="SELECT room_number FROM reservations " +
                    "WHERE reservation_id= "+reservationId;
            ResultSet resultSet=statement.executeQuery(sql);
            if(resultSet.next()){
                int roomNumber=resultSet.getInt("room_number");
                System.out.println("Room Number for Reservation ID "+reservationId+" and Guest "+guestName+" is: "+roomNumber);
            }else {
                System.out.println("Reservation Not found for the given ID and guest name. ");
            }
        }catch (SQLException e ){
            System.out.println(e.getMessage());
        }
    }
    private static void updateReservation(Scanner scanner,Statement statement){
        try {
            System.out.println("Enter Reservation ID:  ");
            int reservationId=scanner.nextInt();
            scanner.nextLine();
            if(!reservationExists(reservationId,statement)){
                System.out.println("Reservation Not found for the given ID.");
                return;
            }
            System.out.println("Enter new guest name: ");
            String newGuestName =scanner.nextLine();
            System.out.println("Enter new room Number: ");
            int newRoomNumber= scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter new contact Number: ");
            String newContactNumber=scanner.nextLine();
            String sql="UPDATE reservations SET " +
                    "guest_name='"+newGuestName+"',"+" room_number='"+newRoomNumber+"',"+
                    "contact_number='"+newContactNumber+"' "+"WHERE reservation_id= "+reservationId;
            int rowAffected=statement.executeUpdate(sql);
            if(rowAffected>0){
                System.out.println("Update Successful "+ rowAffected+ " rows affected");
            }else {
                System.out.println("Update Failed");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void deleteReservation(Scanner scanner,Statement statement){
        try {
            System.out.println("Enter Reservation ID:  ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();
            if (!reservationExists(reservationId, statement)) {
                System.out.println("Reservation Not found for the given ID.");
                return;
            }
            String sql="DELETE FROM reservations WHERE reservation_id= "+reservationId;
            int rowAffected=statement.executeUpdate(sql);
            if(rowAffected>0){
                System.out.println("Deletion  Successful "+ rowAffected+ " rows affected");
            }else {
                System.out.println("Deletion Failed");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static boolean reservationExists(int reservationId,Statement statement){
        try {
            String sql="SELECT reservation_id FROM reservations WHERE reservation_id ="+ reservationId;
            ResultSet resultSet=statement.executeQuery(sql);
            return resultSet.next();

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public  static void exit() throws InterruptedException{
        System.out.print("Exiting System");
        int i=5;
        while (i !=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank You For Using Hotel Reservation System !!!");
    }
}