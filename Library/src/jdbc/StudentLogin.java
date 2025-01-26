package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentLogin {

    // Register a new student
    public void registerStudent(int borrowerId, String email, String password, String name, String phoneno1) throws Exception {
        try {
            DBConnect.getConnect();

            // Insert into borrowers table
            String borrowerQuery = "INSERT INTO borrowers (borrower_id, name, email_id, ph_no) VALUES (?, ?, ?, ?)";
            PreparedStatement borrowerStmt = DBConnect.con.prepareStatement(borrowerQuery);
            borrowerStmt.setInt(1, borrowerId);
            borrowerStmt.setString(2, name);
            borrowerStmt.setString(3, email);
            borrowerStmt.setString(4, phoneno1); // Add phone number here
            int borrowerRowsInserted = borrowerStmt.executeUpdate();

            if (borrowerRowsInserted > 0) {
                // Insert into students table
                String studentQuery = "INSERT INTO students (borrower_id, email_id, password) VALUES (?, ?, ?)";
                PreparedStatement studentStmt = DBConnect.con.prepareStatement(studentQuery);
                studentStmt.setInt(1, borrowerId);
                studentStmt.setString(2, email);
                studentStmt.setString(3, password);
                int studentRowsInserted = studentStmt.executeUpdate();

                if (studentRowsInserted > 0) {
                    System.out.println("Student registered successfully!");
                } else {
                    System.out.println("Failed to register student in students table.");
                }
                studentStmt.close();
            } else {
                System.out.println("Failed to register borrower in borrowers table.");
            }

            borrowerStmt.close();
            DBConnect.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Authenticate an existing student
    public boolean loginStudent(String email, String password) throws Exception {
        boolean isAuthenticated = false;

        try {
            DBConnect.getConnect();
            String query = "SELECT borrower_id FROM students WHERE email_id = ? AND password = ?";
            PreparedStatement stmt = DBConnect.con.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful!");
                int borrowerId = rs.getInt("borrower_id");
                isAuthenticated = true;
            } else {
                System.out.println("Invalid email or password.");
            }

            rs.close();
            stmt.close();
            DBConnect.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }

    // Display books borrowed by the student
    public void displayBorrowedBooks(int borrowerId) throws Exception {
        try {
            DBConnect.getConnect();
            String query = "SELECT book_id, issue_date, DATE_ADD(issue_date, INTERVAL 14 DAY) AS return_deadline "
                         + "FROM lending WHERE borrower_id = ?";
            PreparedStatement stmt = DBConnect.con.prepareStatement(query);
            stmt.setInt(1, borrowerId);

            ResultSet rs = stmt.executeQuery();
            List<String> borrowedBooks = new ArrayList<>();
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                Date issueDate = rs.getDate("issue_date");
                Date returnDeadline = rs.getDate("return_deadline");
                borrowedBooks.add("Book ID: " + bookId + ", Issue Date: " + issueDate + ", Return Deadline: " + returnDeadline);
            }

            if (borrowedBooks.isEmpty()) {
                System.out.println("You have not borrowed any books.");
            } else {
                System.out.println("Your Borrowed Books:");
                for (String book : borrowedBooks) {
                    System.out.println(book);
                }
            }

            rs.close();
            stmt.close();
            DBConnect.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
