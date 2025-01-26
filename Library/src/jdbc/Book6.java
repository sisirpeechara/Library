package jdbc;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Book6 {

    // Method to calculate fine and return book if fine is paid
    public void calculateFineAndReturnBook(int lendingId, int bookId) {
        try {
            DBConnect.getConnect();

            // Get the issue date and return date (if any)
            PreparedStatement psGetDates = DBConnect.con.prepareStatement(
                "SELECT issue_date, return_date FROM Lending WHERE lending_id = ?"
            );
            psGetDates.setInt(1, lendingId);
            ResultSet rs = psGetDates.executeQuery();

            if (rs.next()) {
                String issueDateStr = rs.getString("issue_date");
                String returnDateStr = rs.getString("return_date");

                LocalDate issueDate = LocalDate.parse(issueDateStr);
                LocalDate returnDate = returnDateStr != null ? LocalDate.parse(returnDateStr) : null;
                
                // If the book has been returned
                if (returnDate != null) {
                    System.out.println("Book has already been returned.");
                    return;
                }

                // Calculate fine based on delay
                long daysLate = ChronoUnit.DAYS.between(issueDate, LocalDate.now());
                if (daysLate <= 14) {
                    System.out.println("No fine applied. You are within the grace period.");
                    return;
                } else if (daysLate <= 21) {
                    System.out.println("Fine for delay: Rs. " + (daysLate - 14));
                } else if (daysLate <= 30) {
                    System.out.println("Fine for delay: Rs. " + (2 * (daysLate - 14)));
                } else {
                    System.out.println("You are too late. Your account has been canceled due to exceeding 30 days.");
                    deleteBorrower(lendingId);
                    return;
                }

                // Ask user if fine is paid
                Scanner scan = new Scanner(System.in);
                System.out.println("Has the fine been paid? (yes/no)");
                String finePaid = scan.nextLine();

                if (finePaid.equalsIgnoreCase("yes")) {
                    System.out.println("Fine paid. Returning the book...");
                    updateReturnAndBookQuantity(bookId, lendingId);
                } else {
                    System.out.println("Fine not paid. The book cannot be returned.");
                }

            } else {
                System.out.println("Lending ID not found.");
            }

            rs.close();
            psGetDates.close();
            DBConnect.con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to update the return date and increase book quantity
    private void updateReturnAndBookQuantity(int bookId, int lendingId) {
        try {
            // Update the return date
            PreparedStatement psReturnDate = DBConnect.con.prepareStatement(
                "UPDATE Lending SET return_date = ? WHERE lending_id = ?"
            );
            psReturnDate.setString(1, LocalDate.now().toString());
            psReturnDate.setInt(2, lendingId);
            psReturnDate.executeUpdate();

            // Increase book quantity
            PreparedStatement psIncreaseQty = DBConnect.con.prepareStatement(
                "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?"
            );
            psIncreaseQty.setInt(1, bookId);
            psIncreaseQty.executeUpdate();

            System.out.println("Book returned successfully and quantity updated.");

            psReturnDate.close();
            psIncreaseQty.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to delete borrower after 30 days without returning the book
    public void deleteBorrower(int lendingId) {
        try {
            PreparedStatement psDelete = DBConnect.con.prepareStatement(
                "DELETE FROM Lending WHERE lending_id = ?"
            );
            psDelete.setInt(1, lendingId);
            psDelete.executeUpdate();

            PreparedStatement psDeleteBorrower = DBConnect.con.prepareStatement(
                "DELETE FROM borrowers WHERE borrower_id = (SELECT borrower_id FROM Lending WHERE lending_id = ?)"
            );
            psDeleteBorrower.setInt(1, lendingId);
            psDeleteBorrower.executeUpdate();

            System.out.println("Borrower has been deleted due to excessive delay.");

            psDelete.close();
            psDeleteBorrower.close();
            DBConnect.con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
