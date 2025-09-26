// This is the Class representing a Book object with ISBN, title, and author
class Book {
    String isbn;
    String title;
    String author;

    // Constructor
    Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "[" + isbn + "] " + title + " by " + author;
    }
}

// This is the Node of the Binary Search Tree
// that stores a book and references to left/right children
class BookNode {
    Book book;        // Book node
    BookNode left;    // Left child node (small)
    BookNode right;   // Right child node (large)

    // This is the Constructor to create a new node with a given book
    BookNode(Book book) {
        this.book = book;
        this.left = this.right = null; // No children initially
    }
}

// Binary Search Tree for managing book inventory
public class BookInventory {
    private BookNode root; // Root node

    // This Checks if the inventory is empty
    public boolean isEmpty() {
        return root == null;
    }

    // Recursive method to insert a book into the BST
    private BookNode insertRec(BookNode root, Book book) {
        // If the tree is empty, place the new book here
        if (root == null) return new BookNode(book);

        // Compare ISBNs: smaller → go left, larger → go right
        if (book.isbn.compareTo(root.book.isbn) < 0)
            root.left = insertRec(root.left, book);
        else if (book.isbn.compareTo(root.book.isbn) > 0)
            root.right = insertRec(root.right, book);
        // If ISBN is equal, do nothing (I assume ISBN is unique)

        return root; // Return the unchanged root
    }

    // Public method to insert a book (It calls recursive insertRec helper)
    public void insert(Book book) {
        root = insertRec(root, book);
    }


    // Recursive method to delete a node with a given ISBN
    private BookNode deleteRec(BookNode root, String isbn) {
        if (root == null) return null; // Base case: book not found

        // Traverse left or right depending on ISBN comparison
        if (isbn.compareTo(root.book.isbn) < 0)
            root.left = deleteRec(root.left, isbn);
        else if (isbn.compareTo(root.book.isbn) > 0)
            root.right = deleteRec(root.right, isbn);
        else {
            // Found the book to delete

            // Case 1: Node has no left child → replace with the right child
            if (root.left == null) return root.right;

                // Case 2: Node has no right child → replace with left child
            else if (root.right == null) return root.left;

            // Case 3: Node has two children
            // Replace a book with the smallest book in the right subtree (inorder successor)
            root.book = minValue(root.right);

            // Delete the inorder successor from the right subtree
            root.right = deleteRec(root.right, root.book.isbn);
        }
        return root; // Return updated root
    }

    // Public method to delete a book by ISBN (It calls recursive deleteRec helper)
    public void delete(String isbn) {
        root = deleteRec(root, isbn);
    }

    // Find the minimum value (leftmost node) in a subtree
    private Book minValue(BookNode root) {
        Book min = root.book;
        while (root.left != null) {
            root = root.left;
            min = root.book;
        }
        return min;
    }

    // -------- Lookup helpers to manage book inventory -------- //

    // Recursive search by ISBN
    private boolean containsIsbnRec(BookNode node, String isbn) {
        if (node == null) return false; // Not found

        int cmp = isbn.compareTo(node.book.isbn);
        if (cmp == 0) return true;             // Found
        if (cmp < 0) return containsIsbnRec(node.left, isbn);  // Search left
        return containsIsbnRec(node.right, isbn);              // Search right
    }

    // Check if a book exists in the tree by ISBN
    public boolean containsIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) return false; // Guard against invalid input
        return containsIsbnRec(root, isbn.trim()); // Call recursive helper
    }


    // Recursive search by title
    private boolean containsTitleRec(BookNode node, String lowerTitle) {
        if (node == null) return false;

        // Compare ignoring case and whitespace
        if (node.book.title != null && node.book.title.trim().toLowerCase().equals(lowerTitle))
            return true;

        // Search both left and right subtrees
        return containsTitleRec(node.left, lowerTitle) || containsTitleRec(node.right, lowerTitle);
    }

    // This Checks if a book exists by title
    public boolean containsTitle(String title) {
        if (title == null || title.trim().isEmpty()) return true; // Edge case
        String t = title.trim().toLowerCase();
        // Return true if NOT found → Notice the negation here (!)
        return !containsTitleRec(root, t);
    }

    // -------- Traversal -------- //

    // This is the Public method to display all books in sorted order using in-order traversal
    public void inorder() {
        inorderRec(root);
    }

    // Recursive inorder traversal: Left → Node → Right
    private void inorderRec(BookNode root) {
        if (root != null) {
            inorderRec(root.left);                 // Looking the left subtree
            System.out.println(root.book);         // Looking current node
            inorderRec(root.right);                // Looking the right subtree
        }
    }
}
