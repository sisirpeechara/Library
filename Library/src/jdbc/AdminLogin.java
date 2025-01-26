package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLogin {

    // Method to add a new admin
    public void addAdmin(String email, String password) throws Exception {
        try {
            DBConnect.getConnect();
            String query = "INSERT INTO admins (email, password) VALUES (?, ?)";
            PreparedStatement stmt = DBConnect.con.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New admin added successfully!");
            } else {
                System.out.println("Failed to add admin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to authenticate an existing admin
    public boolean login(String email, String password) throws Exception {
        boolean isAuthenticated = false;

        try {
            DBConnect.getConnect();
            String query = "SELECT * FROM admins WHERE email = ? AND password = ?";
            PreparedStatement stmt = DBConnect.con.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isAuthenticated = true;
            } else {
                System.out.println("Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }

    // Method to display all borrower details
    public void displayBorrowers() throws Exception {
        try {
            DBConnect.getConnect();
            String query = "SELECT * FROM borrowers";
            PreparedStatement stmt = DBConnect.con.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            System.out.println("Borrower Details:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("borrower_id") +
                        ", Name: " + rs.getString("name") +
                        ", Email: " + rs.getString("email_id") +
                        ", Phone: " + rs.getString("ph_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display borrowers' book details with fine period (14 days)
    public void displayBorrowersWithBookDetails() throws Exception {
        try {
            DBConnect.getConnect();
            String query = "SELECT b.borrower_id, b.name, l.book_id, bo.title, l.issue_date, "
                         + "DATE_ADD(l.issue_date, INTERVAL 14 DAY) AS last_date "
                         + "FROM borrowers b "
                         + "JOIN lending l ON b.borrower_id = l.borrower_id "
                         + "JOIN books bo ON l.book_id = bo.book_id";
            PreparedStatement stmt = DBConnect.con.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            System.out.println("Borrower and Book Details with Fine Period (14 days):");
            while (rs.next()) {
                int borrowerId = rs.getInt("borrower_id");
                String borrowerName = rs.getString("name");
                int bookId = rs.getInt("book_id");
                String bookTitle = rs.getString("title");
                String issueDate = rs.getString("issue_date");
                String lastDate = rs.getString("last_date");

                System.out.println("Borrower ID: " + borrowerId + 
                                   ", Borrower Name: " + borrowerName +
                                   ", Book ID: " + bookId +
                                   ", Book Title: " + bookTitle +
                                   ", Issue Date: " + issueDate +
                                   ", Last Date for Fine: " + lastDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
