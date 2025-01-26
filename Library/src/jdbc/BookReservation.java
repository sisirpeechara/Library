package jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookReservation {
    public void reserveBook(int borrowerId, int bookId) throws Exception {
        try {
            DBConnect.getConnect();
            String checkBookQuery = "SELECT quantity FROM books WHERE book_id = ?";
            PreparedStatement checkStmt = DBConnect.con.prepareStatement(checkBookQuery);
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                if (quantity == 0) {
                    // Book is unavailable, proceed with reservation
                    String reserveQuery = "INSERT INTO reservations (borrower_id, book_id, reservation_date) VALUES (?, ?, ?)";
                    PreparedStatement reserveStmt = DBConnect.con.prepareStatement(reserveQuery);
                    reserveStmt.setInt(1, borrowerId);
                    reserveStmt.setInt(2, bookId);
                    reserveStmt.setDate(3, new Date(System.currentTimeMillis()));

                    // Insert the reservation and check if it's successful
                    int rowsInserted = reserveStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Book reserved successfully!");
                    } else {
                        System.out.println("Failed to reserve the book.");
                    }
                } else {
                    System.out.println("Book is available. No need to reserve.");
                }
            } else {
                System.out.println("Book with ID " + bookId + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to notify borrower when a reserved book becomes available
    public void notifyBorrowers(int bookId) throws Exception {
        try {
            DBConnect.getConnect();
            String reservationQuery = "SELECT borrower_id FROM reservations WHERE book_id = ? ORDER BY reservation_date ASC LIMIT 1";
            PreparedStatement notifyStmt = DBConnect.con.prepareStatement(reservationQuery);
            notifyStmt.setInt(1, bookId);
            ResultSet rs = notifyStmt.executeQuery();

            if (rs.next()) {
                int borrowerId = rs.getInt("borrower_id");
                System.out.println("Notifying borrower with ID " + borrowerId + " about the availability of book ID " + bookId);

                // Remove the reservation
                String deleteReservation = "DELETE FROM reservations WHERE borrower_id = ? AND book_id = ?";
                PreparedStatement deleteStmt = DBConnect.con.prepareStatement(deleteReservation);
                deleteStmt.setInt(1, borrowerId);
                deleteStmt.setInt(2, bookId);
                deleteStmt.executeUpdate();
            } else {
                System.out.println("No reservations found for book ID " + bookId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
