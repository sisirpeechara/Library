package jdbc;

import java.sql.*;

public class Book4 {
    public void recordLending(int lid, int bkid, int id, String issue) {
        try {
            DBConnect.getConnect();

            // Step 1: Check if borrower exists (ensure borrower_id exists in the database)
            PreparedStatement checkBorrowerStmt = DBConnect.con.prepareStatement(
                "SELECT * FROM borrowers WHERE borrower_id = ?"
            );
            checkBorrowerStmt.setInt(1, id);
            ResultSet borrowerRs = checkBorrowerStmt.executeQuery();

            // Ensure borrower exists in the system
            if (!borrowerRs.next()) {
                System.out.println("Borrower ID not found. Please register the borrower first.");
                borrowerRs.close();
                checkBorrowerStmt.close();
                DBConnect.con.close();
                return;
            }

            // Step 2: Check book availability (check if book exists and if there's stock)
            PreparedStatement checkBookStmt = DBConnect.con.prepareStatement(
                "SELECT quantity FROM books WHERE book_id = ?"
            );
            checkBookStmt.setInt(1, bkid);
            ResultSet bookRs = checkBookStmt.executeQuery();

            // Ensure the book exists and check availability
            if (bookRs.next()) {
                int quantity = bookRs.getInt("quantity");

                // Step 3: Verify book availability
                if (quantity > 0) {
                    // Reduce quantity by 1 (lend the book)
                    PreparedStatement updateBookStmt = DBConnect.con.prepareStatement(
                        "UPDATE books SET quantity = quantity - 1 WHERE book_id = ?"
                    );
                    updateBookStmt.setInt(1, bkid);
                    updateBookStmt.executeUpdate();

                    // Step 4: Insert lending record
                    PreparedStatement insertLendingStmt = DBConnect.con.prepareStatement(
                        "INSERT INTO Lending (lending_id, book_id, borrower_id, issue_date, return_date) VALUES (?, ?, ?, ?, NULL)"
                    );
                    insertLendingStmt.setInt(1, lid);
                    insertLendingStmt.setInt(2, bkid);
                    insertLendingStmt.setInt(3, id);
                    insertLendingStmt.setString(4, issue);

                    int result = insertLendingStmt.executeUpdate();
                    if (result > 0) {
                        System.out.println("Lending record inserted successfully.");
                    } else {
                        System.out.println("Failed to insert lending record.");
                    }

                    insertLendingStmt.close();
                    updateBookStmt.close();
                } else {
                    // Book not available
                    System.out.println("The book is currently unavailable.");
                }
            } else {
                System.out.println("Book ID not found.");
            }

            bookRs.close();
            checkBookStmt.close();
            borrowerRs.close();
            checkBorrowerStmt.close();
            DBConnect.con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateReturnDate(int lid, String returnDate) {
        try {
            DBConnect.getConnect();
            PreparedStatement ps = DBConnect.con.prepareStatement(
                "UPDATE Lending SET return_date = ? WHERE lending_id = ?"
            );
            ps.setString(1, returnDate);
            ps.setInt(2, lid);

            int a = ps.executeUpdate();
            if (a > 0) {
                System.out.println("Return date updated successfully.");
            } else {
                System.out.println("Failed to update return date. Please check the Lending ID.");
            }
            ps.close();
            DBConnect.con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
