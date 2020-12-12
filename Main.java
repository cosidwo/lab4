import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String driver = "com.mysql.jdbc.Driver";
        String URL = "jdbc:mysql://10.0.10.3:3306/pkosidlo";
        String user = "pkosidlo";
        String password = "pkosidlo";

        Statement statement = null;
        Connection connection = null;
        while(connection == null){
            try {
                connection = DriverManager.getConnection(URL,user,password);
                if(connection != null)
                {
                    System.out.println("Connected!");
                    createDatabase(connection);
                    insertUser(connection,1, "uzytkownik1", "test1");
                    insertUser(connection,2, "uzytkownik2", "test2");
                    insertUser(connection,3, "uzytkownik3", "test3");
                    menu();
                    String input = new Scanner(System.in).next();
                    while((input.equals("1") || input.equals("2")|| input.equals("3") || input.equals("4")))
                    {
                        switch(input)
                        {
                            case "1":
                                System.out.println("Id: ");
                                String ID = new Scanner(System.in).next();
                                int id=Integer.parseInt(ID);
                                System.out.println("Login: ");
                                String login = new Scanner(System.in).next();
                                System.out.println("Password: ");
                                String user_password = new Scanner(System.in).next();
                                insertUser(connection, id, login, user_password);
                                break;

                            case "2":
                                displayUsers(connection);
                                break;

                            case "3":
                                System.out.println("Podaj id uzytkownika ktory ma zostac usuniety: ");
                                ID = new Scanner(System.in).next();
                                id=Integer.parseInt(ID);
                                deleteUser(connection, id);
                                break;

                            case "4":
                                System.out.println("Podaj id uzytkownika ktorego dane maja zostac edytowane: ");
                                ID = new Scanner(System.in).next();
                                id=Integer.parseInt(ID);
                                System.out.println("Podaj login: ");
                                login = new Scanner(System.in).next();
                                System.out.println("Podaj haslo: ");
                                password = new Scanner(System.in).next();
                                updateUser(connection, id, login, password);
                                break;

                            case "5":
                                System.exit(0);
                            default:
                                System.out.println("Wybierz opcje: ");
                                menu();
                                break;
                        }

                        menu();
                        System.out.println("Wybierz opcje: ");
                        input = new Scanner(System.in).next();
                    }
                }
            }
            catch (SQLException ex)
            {
                System.out.println("Blad w polaczeniu do bazy danych");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void createDatabase(Connection connection) throws SQLException
    {
        String sql = "CREATE TABLE User (ID INT NOT NULL, LOGIN VARCHAR(30) NOT NULL, PASS VARCHAR(30) NOT NULL, PRIMARY KEY(ID));";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.execute(sql);
        System.out.println("Tabela User zostala utworzona");

    }

    public static void insertUser(Connection connection, int id, String login, String password) throws SQLException
    {

        String query = "INSERT INTO User (ID, LOGIN, PASS) VALUES(?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, login);
        preparedStatement.setString(3, password);

        int inserted = preparedStatement.executeUpdate();
        if(inserted > 0)
        {
            System.out.println("Dodano uzytkownika");
        }

    }
    public static void displayUsers(Connection conn) throws SQLException
    {
        String query = "SELECT * FROM User;";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while(resultSet.next())
        {
            int id = resultSet.getInt("ID");
            String login = resultSet.getString("LOGIN");
            String password = resultSet.getString("PASS");

            System.out.println("ID: "+id+", LOGIN: "+login+", PASSWORD: "+password);
        }
        resultSet.close();
    }

    public static void deleteUser(Connection connection, int id) throws SQLException
    {
        String sql = "DELETE FROM User WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        int deleted = preparedStatement.executeUpdate();
        if(deleted > 0)
        {
            System.out.println("Uzytkownik "+id+" zostal usuniety z bazy danych");
        }
    }

    public static void updateUser(Connection connection, int id, String login, String password) throws SQLException
    {
        String sql = "UPDATE User SET LOGIN = ?, PASS = ? WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        preparedStatement.setInt(3, id);

        int updated = preparedStatement.executeUpdate();
        if(updated > 0)
        {
            System.out.println("Dane uzytkownika "+id+" zostaly zedytowane");
        }
        preparedStatement.close();
    }

    public static void menu()
    {
        System.out.println();
        System.out.println("Wybierz opcje:");
        System.out.println("1. Dodaj uzytkownika");
        System.out.println("2. Wyswietll uzytkownikow");
        System.out.println("3. Usun uzytkownika");
        System.out.println("4. Edytuj uzytkownika");
        System.out.println("5. Wyjdz\n");
    }


}