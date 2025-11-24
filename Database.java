import java.sql.*;

public class Database {

    private static final String URL = "jdbc:sqlite:library.db";

    // Connect to SQLite (loads JDBC driver)
    public static Connection connect() {
        Connection conn = null;
        try {
            // IMPORTANT: load driver manually
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }
        return conn;
    }

    // Create table if not exists
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS books(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "author TEXT" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (Exception e) {
            System.out.println("Table Error: " + e.getMessage());
        }
    }

    // Insert book
    public static void addBook(String title, String author) {
        String sql = "INSERT INTO books(title, author) VALUES(?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.executeUpdate();
            System.out.println("Book added successfully!");

        } catch (Exception e) {
            System.out.println("Insert Error: " + e.getMessage());
        }
    }

    // View all books
    public static void viewBooks() {
        String sql = "SELECT * FROM books";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n----- BOOK LIST -----");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        " | Title: " + rs.getString("title") +
                        " | Author: " + rs.getString("author"));
            }

        } catch (Exception e) {
            System.out.println("View Error: " + e.getMessage());
        }
    }

    // Search book by title
    public static void searchBook(String title) {
        String sql = "SELECT * FROM books WHERE title LIKE ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + title + "%");
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n----- SEARCH RESULTS -----");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("ID: " + rs.getInt("id") +
                        " | Title: " + rs.getString("title") +
                        " | Author: " + rs.getString("author"));
            }

            if (!found) System.out.println("No matching book found.");

        } catch (Exception e) {
            System.out.println("Search Error: " + e.getMessage());
        }
    }

    // Delete a book
    public static void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();

            if (rows > 0)
                System.out.println("Book deleted successfully!");
            else
                System.out.println("No book found with that ID.");

        } catch (Exception e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }
}