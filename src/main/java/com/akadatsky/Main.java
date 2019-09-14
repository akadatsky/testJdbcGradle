package com.akadatsky;

import com.akadatsky.dao.UserDao;
import com.akadatsky.model.Group;
import com.akadatsky.model.User;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Group javaGroup = createJavaGroup();
        Group jsGroup = createJsGroup();

        try {
            UserDao userDao = new UserDao();
            userDao.clean();
            userDao.insetGroup(javaGroup);
            userDao.insetGroup(jsGroup);

            Group testGroup = userDao.getGroupByName("Java");
            System.out.println("Founded group: " + testGroup);

            List<User> users = userDao.getUsersByAge(25, 28);
            System.out.println("Users: " + users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Group createJavaGroup() {
        Group group = new Group(Helper.generateId(), "Java");
        group.addUser(new User(Helper.generateId(), "Alex", 26));
        group.addUser(new User(Helper.generateId(), "Oleg", 28));
        group.addUser(new User(Helper.generateId(), "Igor", 15));
        return group;
    }

    private static Group createJsGroup() {
        Group group = new Group(Helper.generateId(), "Js");
        group.addUser(new User(Helper.generateId(), "Sofia", 26));
        group.addUser(new User(Helper.generateId(), "Alex", 27));
        group.addUser(new User(Helper.generateId(), "Emilia", 30));
        return group;
    }

}
