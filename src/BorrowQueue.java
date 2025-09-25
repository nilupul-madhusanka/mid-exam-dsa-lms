// BorrowQueue: Implements a circular queue for handling borrow requests in the library
class BorrowQueue {
    // Maximum number of requests the queue can hold
    private static final int MAX = 100;

    // Array to store the borrow requests
    private final String[] queue = new String[MAX];

    // Pointers to track the front and rear of the queue
    private int front = 0;   // first element
    private int rear = -1;   // last element
    private int size = 0;    // how many elements are currently in the queue?

    // Add a borrow request to the queue
    public void enqueue(String request) {
        if (size == MAX) { // Check if the queue is full or not
            System.out.println("Queue is full!");
            return;
        }
        // Move the rear pointer in a circular manner using modulo
        rear = (rear + 1) % MAX;
        // Store the request at the rear position
        queue[rear] = request;
        // Increase the size for the coming element
        size++;
    }

    // Remove and return the oldest borrow request (FIFO order)
    public String dequeue() {
        if (size == 0) { // Check if the queue is empty or not
            System.out.println("Queue is empty!");
            return null;
        }
        // Get the request at the front of the queue
        String request = queue[front];
        // Move the front pointer to the next element circularly
        front = (front + 1) % MAX;
        // Decrease size since one element was removed
        size--;
        return request; // Return the removed request
    }

    // Display all borrow requests in the queue in the correct order
    public void display() {
        if (size == 0) { // If the queue has no requests
            System.out.println("Queue is empty!");
            return;
        }
        System.out.print("Borrow Requests: ");
        // Loop through all current elements in the queue
        for (int i = 0; i < size; i++) {
            // Use modulo to wrap around an array when necessary
            System.out.print(queue[(front + i) % MAX] + " | ");
        }
        System.out.println(); // New line after displaying all requests
    }
}
