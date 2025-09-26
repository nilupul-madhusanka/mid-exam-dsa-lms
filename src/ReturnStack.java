// This ReturnStack Implements a stack data structure for handling returned books
class ReturnStack {
    // Maximum number of books the stack can hold
    private final int MAX = 100;

    // Array to store the returned books
    private final String[] stack = new String[MAX];

    // Pointer to track the top of the stack
    private int top = -1;  // -1 means the stack is currently empty

    // This is the push method to Add a book onto the stack
    public void push(String book) {
        // Check if the stack is already full
        if (top == MAX - 1) {
            System.out.println("Stack is full!");
            return;
        }
        // Increase the top pointer and add a book at that position
        stack[++top] = book;
    }

    // This is the pop method to Remove and return the most recently returned book (LIFO order)
    public String pop() {
        // Check if the stack is empty
        if (top == -1) {
            System.out.println("Stack is empty!");
            return null;
        }
        // Return the book at the current top, then decrease the top pointer
        return stack[top--];
    }

    // Display all books currently in the stack
    public void display() {
        // If no books are in the stack
        if (top == -1) {
            System.out.println("Stack is empty!");
            return;
        }
        System.out.print("Remaining Books: ");
        // Print books from bottom (0) to top
        for (int i = 0; i <= top; i++) {
            System.out.print(stack[i] + " | ");
        }
        System.out.println(); // Move to a new line after printing
    }
}
