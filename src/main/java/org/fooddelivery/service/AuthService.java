package org.fooddelivery.service;

import org.fooddelivery.model.AuthUser;
import org.fooddelivery.storage.XMLStorage;

import java.util.ArrayList;
import java.util.List;

public class AuthService {

    private static final String FILE = "data/users.xml";
    private List<AuthUser> users;

    public AuthService() {
        Object obj = XMLStorage.load(FILE);
        users = (obj == null) ? new ArrayList<>() : (List<AuthUser>) obj;
    }

    public void register(String username, String password, String role) {
        users.add(new AuthUser(username, password, role));
        XMLStorage.save(users, FILE);
        System.out.println("Registration successful!");
    }

    public AuthUser login(String username, String password) {
        for (AuthUser u : users) {

            if (username.equals(u.getUsername()) &&
                    password.equals(u.getPassword())) {
                return u;
            }
        }
        return null;
    }
}