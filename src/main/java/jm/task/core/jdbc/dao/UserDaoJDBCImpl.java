package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;

    public UserDaoJDBCImpl() {
        this.connection = Util.getConnection();
    }

    public UserDaoJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createUsersTable() {
        String sql = """
                create table if not exists users (
                    id serial primary key,
                    name varchar(255) not null,
                    lastName varchar(255) not null,
                    age smallint not null
                )""";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы: ", e);
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "drop table if exists users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы: ", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "insert into users (name, lastName, age) values (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении пользователя: ", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = "delete from users where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Пользователь с id " + id + " не найден.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя: ", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "select id, name, lastName, age from users order by id ASC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age")
                );
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка пользователей: ", e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users RESTART IDENTITY";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке таблицы", e);
        }
    }
}

