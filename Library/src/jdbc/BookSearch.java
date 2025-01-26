package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookSearch {

    public void searchBook(String keyword) {
        try {
            // Connect to the database
            DBConnect.getConnect();

            // SQL query to search for books
            PreparedStatement ps = DBConnect.con.prepareStatement(
                "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR publisher LIKE ?"
            );
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");

            // Execute query
            ResultSet rs = ps.executeQuery();

            boolean bookFound = false;

            System.out.println("\nSearch Results:");
            while (rs.next()) {
                bookFound = true;
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int quantity = rs.getInt("quantity");

                // Display book details
                System.out.println(
                    "Book ID: " + bookId +
                    ", Title: " + title +
                    ", Author: " + author +
                    ", Publisher: " + publisher +
                    ", Quantity: " + quantity
                );

                // Inform availability
                if (quantity > 0) {
                    System.out.println("Status: Available\n");
                } else {
                    System.out.println("Status: Not Available\n");
                }
            }

            if (!bookFound) {
                System.out.println("No books found matching the keyword: " + keyword);
            }

            // Close resources
            rs.close();
            ps.close();
            DBConnect.con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
