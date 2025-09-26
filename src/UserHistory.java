import java.util.HashMap;

// This is the Node class to represent each book in a user's history (Linked List node)
class HistoryNode {
    String bookTitle;   // Title of the borrowed book
    HistoryNode next;   // Pointer to the next book in the list

    // This is the Constructor to create a new history node
    HistoryNode(String bookTitle) {
        this.bookTitle = bookTitle;
        this.next = null;
    }
}

// This is the UserHistory class: manages borrowed/returned book histories for multiple users
public class UserHistory {
    // A HashMap to store each user's history (username -> linked list of HistoryNodes)
    private final HashMap<String, HistoryNode> userHistories = new HashMap<>();

    // Add a book to a user's history
    // Creates a new node and inserts it at the head of the linked list
    public void addBook(String username, String title) {
        HistoryNode newNode = new HistoryNode(title);
        // The new book points to the current head of the user's history
        newNode.next = userHistories.get(username);
        // Update head to new node
        userHistories.put(username, newNode);
    }

    // Remove a book from a user's history
    public void removeBook(String username, String title) {
        // If a user has no history, exit
        if (!userHistories.containsKey(username)) return;

        HistoryNode head = userHistories.get(username);
        if (head == null) return;

        // Case 1: The book to remove is at the head
        if (head.bookTitle.equals(title)) {
            userHistories.put(username, head.next); // Move head forward
            return;
        }

        // Case 2: Traverse the list to find the book
        HistoryNode temp = head;
        while (temp.next != null && !temp.next.bookTitle.equals(title)) {
            temp = temp.next;
        }

        // If found, unlink the node
        if (temp.next != null) {
            temp.next = temp.next.next;
        }
    }

    // Display a specific user's history
    public void displayHistory(String username) {
        // If no history exists for the user
        if (!userHistories.containsKey(username) || userHistories.get(username) == null) {
            System.out.println("No history found for " + username);
            return;
        }

        // Traverse the linked list for that user
        System.out.print(username + "'s History: ");
        HistoryNode temp = userHistories.get(username);
        while (temp != null) {
            System.out.print(temp.bookTitle + " -> ");
            temp = temp.next;
        }
        System.out.println("null"); // End of a linked list
    }

    // Display all users and their histories
    public void displayAllHistories() {
        // If no user has any history
        if (userHistories.isEmpty()) {
            System.out.println("No user histories available.");
            return;
        }

        // Loop through all users and show their history
        for (String user : userHistories.keySet()) {
            displayHistory(user);
        }
    }
}
