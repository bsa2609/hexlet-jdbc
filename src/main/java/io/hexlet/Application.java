package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

public class Application {
    // Нужно указывать базовое исключение,
    // потому что выполнение запросов может привести к исключениям
    public static void main(String[] args) throws SQLException {
        // Соединение с базой данных тоже нужно отслеживать
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var dao = new UserDAO(conn);

            dao.createDB();

            User userTommy = new User("tommy", "8(915)111-22-33");
            User userMike = new User("mike", "8(916)222-33-44");
            User userSarah = new User("sarah", "8(917)333-44-55");

            dao.save(userTommy);
            dao.save(userMike);
            dao.save(userSarah);

            System.out.println("Create users.");

            dao.printAllUsers();

            userMike.setPhone("8(333)444-55-66");
            dao.save(userMike);

            System.out.println("Update user Mike.");

            dao.printAllUsers();

            var userFound = dao.find(3L).orElseGet(User::new);
            if (userFound.getId() != null) {
                dao.delete(userFound.getId());
            }

            System.out.println("Delete user with id = 3.");

            dao.printAllUsers();
        }
     }

    /*
    // Нужно указывать базовое исключение,
    // потому что выполнение запросов может привести к исключениям
    public static void main(String[] args) throws SQLException {
        // Создаем соединение с базой в памяти
        // База создается прямо во время выполнения этой строчки
        // Здесь hexlet_test — это имя базы данных
        var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test");

        var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
        // Чтобы выполнить запрос, создадим объект statement
        var statement = conn.createStatement();
        statement.execute(sql);
        statement.close(); // В конце закрываем

        var sql2 = "INSERT INTO users (username, phone) VALUES ('tommy', '123456789'), ('dan', '333-44-55')";
        var statement2 = conn.createStatement();
        statement2.executeUpdate(sql2);
        statement2.close();

        var sql3 = "SELECT * FROM users";
        var statement3 = conn.createStatement();
        // Здесь вы видите указатель на набор данных в памяти СУБД
        var resultSet = statement3.executeQuery(sql3);
        // Набор данных — это итератор
        // Мы перемещаемся по нему с помощью next() и каждый раз получаем новые значения
        while (resultSet.next()) {
            System.out.println(resultSet.getString("username"));
            System.out.println(resultSet.getString("phone"));
        }
        statement3.close();

        // Закрываем соединение
        conn.close();
    }
     */
}
