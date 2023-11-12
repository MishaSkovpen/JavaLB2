import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleLibrary {
    public static void main(String[] args) {
        Library library = new Library();

        // Додавання книг у бібліотеку
        library.addItem(new Book("Book 1", "Author 1", "12345", 2000));
        library.addItem(new Book("Book 2", "Author 2", "67890", 2010));
        library.addItem(new Book("Book 3", "Author 3", "54321", 2015));

        // Вивід всіх книг у бібліотеці
        System.out.println("All Books in the Library:");
        library.listAvailableItems();

        // Пошук книги за назвою
        String searchTitle = "Book 2";
        library.searchItemByTitle(searchTitle);

        // Видалення книги за ISBN
        String isbnToRemove = "12345";
        library.removeItem(isbnToRemove);

        // Вивід оновленого списку книг у бібліотеці
        System.out.println("Updated List of Books in the Library:");
        library.listAvailableItems();

        // Реєстрація читачів
        Patron patron1 = new Patron("Alice", "0001");
        Patron patron2 = new Patron("Bob", "0002");
        library.registerPatron(patron1);
        library.registerPatron(patron2);

        // Видача та повернення предметів читачам
        library.lendItem(patron1, "67890");
        library.returnItem(patron1, "67890");

        // Виведення списків доступних та взятих предметів
        System.out.println("\nAvailable Items:");
        library.listAvailableItems();

        System.out.println("\nBorrowed Items:");
        library.listBorrowedItems();
    }
}

abstract class Item {
    private String title;
    private String uniqueID;
    private boolean isBorrowed;

    public Item(String title, String uniqueID) {
        this.title = title;
        this.uniqueID = uniqueID;
        this.isBorrowed = false;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void borrowItem() {
        isBorrowed = true;
    }

    public void returnItem() {
        isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Unique ID: " + uniqueID + ", Borrowed: " + isBorrowed;
    }
}

class Book extends Item {
    private String author;

    public Book(String title, String author, String uniqueID, int year) {
        super(title, uniqueID);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}

class DVD extends Item {
    private String director;
    private int duration; // in minutes

    public DVD(String title, String director, String uniqueID, int duration) {
        super(title, uniqueID);
        this.director = director;
        this.duration = duration;
    }
}

class Patron {
    private String name;
    private String ID;
    private List<Item> borrowedItems;

    public Patron(String name, String ID) {
        this.name = name;
        this.ID = ID;
        this.borrowedItems = new ArrayList<>();
    }

    public List<Item> getBorrowedItems() {
        return borrowedItems;
    }
}

interface IManageable {
    void addItem(Item item);
    void removeItem(String uniqueID);
    void listAvailableItems();
    void listBorrowedItems();
}

class Library implements IManageable {
    private List<Item> items;
    private List<Patron> patrons;

    public Library() {
        items = new ArrayList<>();
        patrons = new ArrayList<>();
    }

    @Override
    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public void removeItem(String uniqueID) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getUniqueID().equals(uniqueID)) {
                iterator.remove();
                item.returnItem();
            }
        }
    }

    public void registerPatron(Patron patron) {
        patrons.add(patron);
    }

    public void lendItem(Patron patron, String uniqueID) {
        for (Item item : items) {
            if (item.getUniqueID().equals(uniqueID) && !item.isBorrowed()) {
                item.borrowItem();
                patron.getBorrowedItems().add(item);
            }
        }
    }

    public void returnItem(Patron patron, String uniqueID) {
        for (Item item : patron.getBorrowedItems()) {
            if (item.getUniqueID().equals(uniqueID)) {
                item.returnItem();
                patron.getBorrowedItems().remove(item);
                break;
            }
        }
    }

    @Override
    public void listAvailableItems() {
        for (Item item : items) {
            if (!item.isBorrowed()) {
                if (item instanceof Book) {
                    System.out.println(item + ", Author: " + ((Book) item).getAuthor());
                } else {
                    System.out.println(item);
                }
            }
        }
    }

    @Override
    public void listBorrowedItems() {
        for (Patron patron : patrons) {
            for (Item item : patron.getBorrowedItems()) {
                System.out.println("Patron: " + patron.toString() + ", Borrowed Item: " + item.toString());
            }
        }
    }

    public void searchItemByTitle(String searchTitle) {
        boolean found = false;
        for (Item item : items) {
            if (item instanceof Book && ((Book) item).getTitle().equalsIgnoreCase(searchTitle)) {
                System.out.println("Book found: " + item);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Book not found with title: " + searchTitle);
        }
    }
}

