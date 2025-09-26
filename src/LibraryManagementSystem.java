import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // --- These are Our 4 main modules (from the separate files) ---
        BookInventory inventory = new BookInventory();   // BST for storing books
        BorrowQueue borrowQueue = new BorrowQueue();     // Queue for borrow requests
        ReturnStack returnStack = new ReturnStack();     // Stack for returned books
        UserHistory history = new UserHistory();         // Linked List for user histories

        // Track currently issued books by normalized title (case-insensitive)
        Map<String, Integer> issuedCounts = new HashMap<>();

        // For menu selection variable
        int choice;

        do {
            // --- Display the Menu ---
            System.out.println("===================================");
            System.out.println("==== Library Management System ====");
            System.out.println("===================================");

            // ANSI escape codes for colorize the menu options
            final String GREEN = "\u001B[32m";
            final String RESET = "\u001B[0m";

            // Menu Options
            System.out.println(GREEN + "1. Add Book to Inventory" + RESET);
            System.out.println(GREEN + "2. Delete Book from Inventory" + RESET);
            System.out.println(GREEN + "3. Display Books (In-order)" + RESET);

            System.out.println(GREEN + "4. Borrow a Book (Add Request)" + RESET);
            System.out.println(GREEN + "5. Issue Book (Process Borrow Request)" + RESET);
            System.out.println(GREEN + "6. Display Borrow Queue" + RESET);

            System.out.println(GREEN + "7. Return a Book" + RESET);
            System.out.println(GREEN + "8. Process to Return a Book" + RESET);

            System.out.println(GREEN + "9. Remove Book from User History" + RESET);
            System.out.println(GREEN + "10. Display User History" + RESET);
            System.out.println(GREEN + "11. Display All Users' Histories" + RESET);

            System.out.println(GREEN + "0. Exit" + RESET);

            // Read user input (choice must be between 0–11)
            choice = readIntInRange(sc);

            // --- Handle menu actions ---
            switch (choice) {
                // 1. Add a new book to inventory (BST)
                case 1:
                    String isbn = readNonEmpty(sc, "Enter ISBN: ");
                    String title = readNonEmpty(sc, "Enter Title: ");
                    String author = readNonEmpty(sc, "Enter Author: ");
                    inventory.insert(new Book(isbn, title, author));
                    System.out.println("Book added!");
                    break;

                // 2. Delete a book from inventory
                case 2:
                    String delIsbn = readNonEmpty(sc, "Enter ISBN to delete: ");
                    if (!inventory.containsIsbn(delIsbn)) {
                        System.out.println("Book with ISBN '" + delIsbn + "' not found in inventory.");
                    } else {
                        inventory.delete(delIsbn);
                        System.out.println("Book deleted.");
                    }
                    break;

                // 3. Display all books in sorted order (in-order traversal of BST)
                case 3:
                    if (inventory.isEmpty()) {
                        System.out.println("No books available.");
                    } else {
                        System.out.println("Books in Inventory (Sorted):");
                        inventory.inorder();
                    }
                    break;

                // 4. Borrow a book (add to borrow queue)
                case 4:
                    String borrowUser = readNonEmpty(sc, "Enter Username: ");
                    String borrowTitle = readNonEmpty(sc, "Enter Book Title to Borrow: ");
                    if (inventory.containsTitle(borrowTitle)) {
                        System.out.println("Cannot request: '" + borrowTitle + "' is not available in inventory.");
                    } else {
                        // Request stored as "username|title" in queue
                        borrowQueue.enqueue(borrowUser + "|" + borrowTitle);
                        // History not updated yet → will be updated only when issued
                        System.out.println("Borrow request added for " + borrowUser + " => " + borrowTitle);
                    }
                    break;

                // 5. Issue a book (dequeue from borrow queue and update user history)
                case 5:
                    String issuedItem = borrowQueue.dequeue();
                    if (issuedItem == null) {
                        System.out.println("No pending borrow requests.");
                        break;
                    }

                    // Split request into username and title
                    String[] parts = issuedItem.split("\\|", 2);
                    String issuedUser = parts.length > 0 ? parts[0].trim() : "";
                    String issuedTitle = parts.length > 1 ? parts[1].trim() : "";

                    if (issuedUser.isEmpty() || issuedTitle.isEmpty()) {
                        System.out.println("Malformed borrow request: " + issuedItem);
                        break;
                    }

                    // Double-check if a book is still in inventory
                    if (inventory.containsTitle(issuedTitle)) {
                        System.out.println("Cannot issue: '" + issuedTitle + "' is not available in inventory.");
                    } else {
                        // Update a user's linked-list history on an issue
                        history.addBook(issuedUser, issuedTitle);
                        // Mark the book as issued (for return validation)
                        incIssued(issuedCounts, issuedTitle);
                        System.out.println("Issued: " + issuedTitle + " to " + issuedUser + " (history updated)");
                    }
                    break;

                // 6. Display current borrow queue
                case 6:
                    borrowQueue.display();
                    break;

                // 7. Record a book being returned (push onto stack)
                case 7:
                    String retBook = readNonEmpty(sc, "Enter Returned Book Title: ");
                    // Validate: the title must exist in the catalog
                    if (inventory.containsTitle(retBook)) {
                        System.out.println("Cannot record return: '" + retBook + "' is not recognized in inventory.");
                    } else if (!hasIssued(issuedCounts, retBook)) {
                        // Validate: it must have been issued before
                        System.out.println("Cannot record return: '" + retBook + "' was not issued.");
                    } else {
                        // Record a pending return
                        returnStack.push(retBook);
                        System.out.println("Book return recorded (awaiting processing).");
                    }
                    break;

                // 8. Process the latest return (pop from stack)
                case 8:
                    String processed = returnStack.pop();
                    if (processed != null) {
                        // Finalize the return only if it was issued
                        if (hasIssued(issuedCounts, processed)) {
                            decIssued(issuedCounts, processed);
                            System.out.println(processed + " Book Has Been Returned");
                        } else {
                            System.out.println("Warning: '" + processed + "' was not marked as issued. Skipping.");
                        }
                    } else {
                        System.out.println("No books to process.");
                    }
                    returnStack.display();
                    break;

                // 9. Manually remove a book from a user's history
                case 9:
                    String user2 = readNonEmpty(sc, "Enter Username: ");
                    String remBook = readNonEmpty(sc, "Enter Book Title to remove from History: ");
                    history.removeBook(user2, remBook);
                    break;

                // 10. Display history for one user
                case 10:
                    String userHis = readNonEmpty(sc, "Enter Username: ");
                    history.displayHistory(userHis);
                    break;

                // 11. Display all users' histories
                case 11:
                    System.out.println("All Users' Histories:");
                    history.displayAllHistories();
                    break;

                // 0. Exit program
                case 0:
                    System.out.println("Exiting System...");
                    break;

                // Invalid option handling
                default:
                    System.out.println("Invalid choice. Try again!");
            }

        } while (choice != 0); // Keep looping until the user chooses Exit

        sc.close();
    }

    // --- Helper Methods ---

    // Normalize titles for consistent lookups (trim and lowercase)
    private static String normTitle(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }

    // Increase issued count for a title
    private static void incIssued(Map<String, Integer> issuedCounts, String title) {
        String key = normTitle(title);
        issuedCounts.put(key, issuedCounts.getOrDefault(key, 0) + 1);
    }

    // Check if a title is currently issued
    private static boolean hasIssued(Map<String, Integer> issuedCounts, String title) {
        return issuedCounts.getOrDefault(normTitle(title), 0) > 0;
    }

    // Decrease issued count (remove key when it reaches zero)
    private static void decIssued(Map<String, Integer> issuedCounts, String title) {
        String key = normTitle(title);
        int c = issuedCounts.getOrDefault(key, 0);
        if (c <= 1) {
            issuedCounts.remove(key);
        } else {
            issuedCounts.put(key, c - 1);
        }
    }

    // Simple validations for the menu options
    // Read a valid menu choice (0–11)
    private static int readIntInRange(Scanner sc) {
        while (true) {
            System.out.print("Enter choice: ");
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < 0 || value > 11) {
                    System.out.println("Please enter a number between 0 and 11.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    // Read a non-empty string input
    private static String readNonEmpty(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("Input cannot be empty. Try again.");
        }
    }
}