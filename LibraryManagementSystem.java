import java.util.ArrayList;
import java.util.Scanner;

interface LibraryOperations {
    void addBook(Book book);
    void displayBooks();
    Book searchBook(String isbn);
    void borrowBook(String isbn);
    void returnBook(String isbn);
}

abstract class Person {
    protected String name;
    protected String id;

    Person(String name, String id) {
        this.name = name;
        this.id = id;
    }

    abstract void displayInfo();
}

class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;

    Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
    }

    String getTitle() {
        return title;
    }

    String getAuthor() {
        return author;
    }

    String getIsbn() {
        return isbn;
    }

    boolean isAvailable() {
        return isAvailable;
    }

    void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Available: " + isAvailable;
    }
}

class Librarian extends Person {
    Librarian(String name, String id) {
        super(name, id);
    }

    @Override
    void displayInfo() {
        System.out.println("Librarian: " + name + ", ID: " + id);
    }
}

class Member extends Person {
    private ArrayList<Book> borrowedBooks;

    Member(String name, String id) {
        super(name, id);
        borrowedBooks = new ArrayList<>();
    }

    void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    void displayBorrowedBooks() {
        System.out.println("Borrowed books by " + name + ":");
        if (borrowedBooks.isEmpty()) {
            System.out.println("No books borrowed.");
        } else {
            for (Book book : borrowedBooks) {
                System.out.println(book);
            }
        }
    }

    @Override
    void displayInfo() {
        System.out.println("Member: " + name + ", ID: " + id);
        displayBorrowedBooks();
    }
}

class Library implements LibraryOperations {
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private Librarian librarian;
    private Scanner scanner;

    Library(Librarian librarian) {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.librarian = librarian;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.getTitle());
    }

    @Override
    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        System.out.println("Library Books:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    @Override
    public Book searchBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    @Override
    public void borrowBook(String isbn) {
        System.out.println("Enter member ID: ");
        String memberId = scanner.nextLine();
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        Book book = searchBook(isbn);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (book.isAvailable()) {
            book.setAvailable(false);
            member.borrowBook(book);
            System.out.println("Book borrowed by " + member.name);
        } else {
            System.out.println("Book is not available.");
        }
    }

    @Override
    public void returnBook(String isbn) {
        System.out.println("Enter member ID: ");
        String memberId = scanner.nextLine();
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        Book book = searchBook(isbn);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (!book.isAvailable()) {
            book.setAvailable(true);
            member.returnBook(book);
            System.out.println("Book returned by " + member.name);
        } else {
            System.out.println("Book is already available.");
        }
    }

    void addMember() {
        System.out.println("Enter member name: ");
        String name = scanner.nextLine();
        System.out.println("Enter member ID: ");
        String id = scanner.nextLine();
        members.add(new Member(name, id));
        System.out.println("Member added: " + name);
    }

    void displayMembers() {
        if (members.isEmpty()) {
            System.out.println("No members in the library.");
            return;
        }
        System.out.println("Library Members:");
        for (Member member : members) {
            member.displayInfo();
        }
    }

    private Member findMember(String id) {
        for (Member member : members) {
            if (member.id.equals(id)) {
                return member;
            }
        }
        return null;
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter librarian name: ");
        String librarianName = scanner.nextLine();
        System.out.println("Enter librarian ID: ");
        String librarianId = scanner.nextLine();
        Librarian librarian = new Librarian(librarianName, librarianId);
        Library library = new Library(librarian);

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Display Books");
            System.out.println("3. Search Book");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Add Member");
            System.out.println("7. Display Members");
            System.out.println("8. Display Librarian Info");
            System.out.println("9. Exit");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.println("Enter book author: ");
                    String author = scanner.nextLine();
                    System.out.println("Enter book ISBN: ");
                    String isbn = scanner.nextLine();
                    library.addBook(new Book(title, author, isbn));
                    break;
                case 2:
                    library.displayBooks();
                    break;
                case 3:
                    System.out.println("Enter ISBN to search: ");
                    String searchIsbn = scanner.nextLine();
                    Book book = library.searchBook(searchIsbn);
                    if (book != null) {
                        System.out.println("Book found: " + book);
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 4:
                    library.borrowBook(scanner.nextLine());
                    break;
                case 5:
                    library.returnBook(scanner.nextLine());
                    break;
                case 6:
                    library.addMember();
                    break;
                case 7:
                    library.displayMembers();
                    break;
                case 8:
                    librarian.displayInfo();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
