package com.akadatsky.dao;

import com.akadatsky.Const;
import com.akadatsky.model.Group;
import com.akadatsky.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public UserDao() throws SQLException {
        connection = DriverManager.getConnection(Const.JDBC_URL, Const.USER, Const.PASSWORD);
        maybeCreateGroupsTable();
        maybeCreateUsersTable();
    }

    private void maybeCreateUsersTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS groups (\n" +
                    "_id uuid PRIMARY KEY,\n" +
                    "name varchar(100)\n" +
                    ");");
        }
    }

    private void maybeCreateGroupsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (\n" +
                    "_id uuid PRIMARY KEY,\n" +
                    "group_id uuid,\n" +
                    "name varchar(100),\n" +
                    "age int\n" +
                    ");");
        }
    }

    public void clean() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate("DELETE FROM groups;");
            System.out.println("Deleted " + count + " rows from table groups");
        }

        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate("DELETE FROM users;");
            System.out.println("Deleted " + count + " rows from table users");
        }

    }

    public void insetGroup(Group group) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("INSERT INTO groups VALUES ('%s', '%s');", group.getId(), group.getName());
            statement.execute(request);
            for (User user : group.getUsers()) {
                user.setGroupId(group.getId());
                insertUser(user);
            }

        }
    }

    private void insertUser(User user) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("INSERT INTO users VALUES ('%s', '%s', '%s', '%d');", user.getId(), user.getGroupId(), user.getName(), user.getAge());
            statement.execute(request);
        }
    }

    public Group getGroupByName(String name) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT * FROM groups WHERE name = '%s';", name);
            ResultSet resultSet = statement.executeQuery(request);
            if (resultSet.next()) {
                String id = resultSet.getString("_id");
                List<User> users = getUsersByGroupId(id);
                return new Group(id, name, users);
            }
        }
        return null;
    }

    private List<User> getUsersByGroupId(String groupId) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT * FROM users WHERE group_id = '%s';", groupId);
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                String id = resultSet.getString("_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                users.add(new User(id, name, age, groupId));
            }
        }
        return users;
    }

    /*
        SELECT column-list
        FROM table_name
        [WHERE condition]
        [ORDER BY column1, column2, .. columnN] [ASC | DESC];
     */
    public List<User> getUsersByAge(int from, int to) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT * FROM users WHERE age >= '%s' AND age <= '%s' ORDER BY age, name ASC;", from, to);
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                String id = resultSet.getString("_id");
                String groupId = resultSet.getString("group_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                users.add(new User(id, name, age, groupId));
            }
        }
        return users;
    }


}
