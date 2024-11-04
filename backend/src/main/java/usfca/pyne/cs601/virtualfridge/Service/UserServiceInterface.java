package usfca.pyne.cs601.virtualfridge.Service;

import usfca.pyne.cs601.virtualfridge.Model.User;

import java.util.List;

public interface UserServiceInterface {
    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    boolean deleteUser(Long id);

    User updateUser(Long id, User user);
}
