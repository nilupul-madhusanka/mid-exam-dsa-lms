# Library Management System (LMS)

A simple console-based Java LMS that demonstrates core data structures and algorithms:

- Binary Search Tree (BST) for the book inventory
- Circular Queue for borrow requests
- Stack for returned books processing
- HashMap + Singly Linked List for per-user reading history

All features are accessible via a text menu in `LibraryManagementSystem`.

## Main Features

1) Book Inventory (BST)
- Add a book with ISBN, Title, Author
- Delete a book by ISBN
- Display all books in sorted order (in-order traversal by ISBN)
- Lookup helpers used by the menu to validate actions

2) Borrow Requests (Queue)
- Add a borrow request (username + book title) to a circular queue
- Issue the next book in FIFO order (dequeue)
- Display the current queue state

3) Returns Processing (Stack)
- Record a returned book (push)
- Process the most recent return (pop)
- Display pending returns in the stack

4) User Reading History (HashMap + Linked List)
- When a book is issued, it is added to the user’s history (prepended to their list)
- Remove a specific title from a user’s history
- Display a single user’s history
- Display all users’ histories

## Menu Options
The CLI exposes these options:

1. Add Book to Inventory
2. Delete Book from Inventory
3. Display Books (In-order)
4. Borrow a Book (Add Request)
5. Issue Book (Process Borrow Request)
6. Display Borrow Queue
7. Return a Book (record)
8. Process to Return a Book
9. Remove Book from User History
10. Display User History
11. Display All Users' Histories
- 0. Exit

## How it works (quick flow)
- Add books (1) so they appear in the BST inventory.
- Add borrow requests (4) which go to the queue.
- Issue requests (5) which dequeues, updates user history, and marks a book as issued.
- Record returns (7) which pushes titles onto the stack.
- Process returns (8) which pops and finalizes the return.
- Inspect/maintain reading histories (9–11).

## How to Run on any IDE (IntelliJ IDEA Recommended)
Prerequisites: JDK 8+ in PATH.

### Clone the repository and navigate to the root of the mid-exam-dsa-lms folder. Open it in your IDE. After opening, you can see the src/LibraryManagementSystem.java file and run it easily.

```
git clone https://github.com/nilupul-madhusanka/mid-exam-dsa-lms.git
```

## Data Structures at a glance
- `BookInventory` (BST): insert/delete/search/show books by ISBN; in-order traversal prints sorted list.
- `BorrowQueue` (circular array): enqueue/dequeue/display requests.
- `ReturnStack` (array-based): push/pop/display returns.
- `UserHistory` (HashMap<String, LinkedList>): per-user book history, add/remove/display.

## Notes & Limitations
- In-memory only (no database/persistence).
- No authentication/authorization.
- Minimal validation; intended for DSA practice and demo purposes.

## Source Files
- `src/LibraryManagementSystem.java` — CLI menu and orchestration
- `src/BookInventory.java` — Book model + BST inventory
- `src/BorrowQueue.java` — Circular queue for borrow requests
- `src/ReturnStack.java` — Stack of returned books
- `src/UserHistory.java` — User histories using HashMap + linked lists
