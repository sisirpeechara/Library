package jdbc;

import java.util.Scanner;

public class lmsMain {

    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        Book b1 = new Book();
        Book1 b2 = new Book1();
        Book2 b3 = new Book2();
        Book3 b4 = new Book3();
        Book4 b5 = new Book4();
        Book5 b6 = new Book5();
        Book6 b7 = new Book6();
        BookSearch b8 = new BookSearch();
        BookReservation b9 = new BookReservation();
        AdminLogin admin = new AdminLogin();
        StudentLogin student = new StudentLogin();
        boolean a = true;

        while (a) {
            // Move this outside the loop to avoid repeated prompts
            System.out.println("Are you a Student or Admin? (Enter 'student' or 'admin')");
            String userType = scan.nextLine().toLowerCase();

            if (userType.equals("student")) {
                System.out.println("Are you a new student? (yes/no)");
                String isNewStudent = scan.nextLine().toLowerCase();

                if (isNewStudent.equals("yes")) {
                    System.out.print("Enter Borrower ID: ");
                    int borrowerId = scan.nextInt();
                    scan.nextLine(); // Consume newline
                    System.out.print("Enter Your Name: ");
                    String name = scan.nextLine();
                    System.out.print("Enter Your Email: ");
                    String email = scan.nextLine();
                    System.out.print("Enter Your Password: ");
                    String password = scan.nextLine();
                    System.out.println("Enter Phone Number:");
                    String phno = scan.nextLine();
                    student.registerStudent(borrowerId, email, password, name, phno);
                } else if (isNewStudent.equals("no")) {
                    System.out.print("Enter Your Email: ");
                    String email = scan.nextLine();
                    System.out.print("Enter Your Password: ");
                    String password = scan.nextLine();

                    if (student.loginStudent(email, password)) {
                        // After successful login, show student options
                        boolean studentActions = true;
                        int borrowerId = -1; // Initialize borrowerId here
                        while (studentActions) {
                            System.out.println("\nStudent Options:");
                            System.out.println("1. Display Borrowed Books");
                            System.out.println("2. Book Lending");
                            System.out.println("3. Search Book");
                            System.out.println("4. Reserve a Book");
                            System.out.println("5. Exit");

                            int studentChoice = scan.nextInt();
                            switch (studentChoice) {
                                case 1:
                                    System.out.println("Enter Borrower ID:");
                                    borrowerId = scan.nextInt();
                                    student.displayBorrowedBooks(borrowerId);
                                    break;

                                case 2:
                                    System.out.println("Enter Borrower ID:");  // Prompt for Borrower ID here
                                    borrowerId = scan.nextInt();  // Store Borrower ID
                                    System.out.println("Enter Lending ID:");
                                    int lendingId = scan.nextInt();
                                    System.out.println("Enter Book ID:");
                                    int bookId = scan.nextInt();
                                    System.out.println("Enter Issue Date (YYYY-MM-DD):");
                                    scan.nextLine();
                                    String issueDate = scan.nextLine();
                                    b5.recordLending(lendingId, bookId, borrowerId, issueDate);  // Pass Borrower ID here
                                    break;

                                case 3:
                                    System.out.println("Enter keyword to search for a book:");
                                    scan.nextLine(); // Consume newline
                                    String keyword = scan.nextLine();
                                    b8.searchBook(keyword);
                                    break;

                                case 4:
                                    System.out.println("Enter Book ID:");
                                    bookId = scan.nextInt();
                                    System.out.println("Enter Borrower ID:");
                                    borrowerId = scan.nextInt();
                                    b9.reserveBook(borrowerId, bookId);
                                    break;

                                case 5:
                                    studentActions = false;
                                    break;

                                default:
                                    System.out.println("Invalid Option");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Login failed. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            } else if (userType.equals("admin")) {
                System.out.println("Are you a new admin? (yes/no)");
                String isNewAdmin = scan.nextLine().toLowerCase();

                if (isNewAdmin.equals("yes")) {
                    System.out.print("Enter your email: ");
                    String newEmail = scan.nextLine();
                    System.out.print("Enter your password: ");
                    String newPassword = scan.nextLine();
                    admin.addAdmin(newEmail, newPassword);
                } else if (isNewAdmin.equals("no")) {
                    System.out.print("Enter Admin Email: ");
                    String adminEmail = scan.nextLine();
                    System.out.print("Enter Password: ");
                    String adminPassword = scan.nextLine();

                    if (admin.login(adminEmail, adminPassword)) {
                        // After successful login, show admin options
                        boolean adminActions = true;
                        while (adminActions) {
                            System.out.println("\nAdmin Options:");
                            System.out.println("1. Add Book");
                            System.out.println("2. Update Book");
                            System.out.println("3. Delete Book");
                            System.out.println("4. Update Return Date");
                            System.out.println("5. Delete a Borrower");
                            System.out.println("6. Calculate Fine");
                            System.out.println("7. Notify Borrower");
                            System.out.println("8. Exit");

                            int adminChoice = scan.nextInt();
                            switch (adminChoice) {
                                case 1:
                                    System.out.println("Enter Book ID:");
                                    int bkid = scan.nextInt();
                                    scan.nextLine();
                                    System.out.println("Enter Book Title:");
                                    String title = scan.nextLine();
                                    System.out.println("Enter Author Name:");
                                    String author = scan.nextLine();
                                    System.out.println("Enter Publisher:");
                                    String publisher = scan.nextLine();
                                    System.out.println("Enter Number of Books:");
                                    int numBooks = scan.nextInt();
                                    b1.addBook(bkid, title, author, publisher, numBooks);
                                    break;

                                case 2:
                                    System.out.println("Enter Book ID to update:");
                                    bkid = scan.nextInt();
                                    System.out.println("Enter New Number of Books:");
                                    numBooks = scan.nextInt();
                                    b2.updateBook(bkid, numBooks);
                                    break;

                                case 3:
                                    System.out.println("Enter Book ID to delete:");
                                    bkid = scan.nextInt();
                                    b3.deleteBook(bkid);
                                    break;

                                case 4:
                                    System.out.println("Enter Lending ID to update return date:");
                                    int lendingId = scan.nextInt();
                                    scan.nextLine();
                                    System.out.println("Enter Return Date (YYYY-MM-DD):");
                                    String returnDate = scan.nextLine();
                                    b5.updateReturnDate(lendingId, returnDate);
                                    break;

                                case 5:
                                    System.out.println("Enter Borrower ID to delete:");
                                    int borrowerId = scan.nextInt();
                                    b6.deleteBorrower(borrowerId);
                                    break;

                                case 6:
                                    System.out.println("Enter Lending ID:");
                                    lendingId = scan.nextInt();
                                    System.out.println("Enter Book ID:");
                                    bkid = scan.nextInt();
                                    b7.calculateFineAndReturnBook(lendingId, bkid);
                                    break;

                                case 7:
                                    System.out.println("Enter Book ID to notify borrower:");
                                    bkid = scan.nextInt();
                                    b9.notifyBorrowers(bkid);
                                    break;

                                case 8:
                                    adminActions = false;
                                    break;

                                default:
                                    System.out.println("Invalid Option");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Access Denied.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            } else {
                System.out.println("Invalid input. Please enter 'student' or 'admin'.");
            }
            System.out.println("Do you want to continue? true/false");
            a = scan.nextBoolean();
            scan.nextLine(); // Consume newline after boolean input
        }

        scan.close();
    }
}
