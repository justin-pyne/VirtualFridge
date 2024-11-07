package usfca.pyne.cs601.virtualfridge.Service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Entity.UserEntity;
import usfca.pyne.cs601.virtualfridge.Model.User;
import usfca.pyne.cs601.virtualfridge.Repository.UserRepository;
import usfca.pyne.cs601.virtualfridge.Model.Fridge;


import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final FridgeService fridgeService;

    public UserService(UserRepository userRepository, FridgeService fridgeService) {
        this.userRepository = userRepository;
        this.fridgeService = fridgeService;
    }

    @Override
    public User saveUser(User user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        Fridge fridge = new Fridge();
        fridge.setUser(userEntity);
        userEntity.setFridge(fridge);
        userRepository.save(userEntity);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(userEntity -> new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail()
        )).toList();
    }

    @Override
    public User getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        User user = new User();
        if (userEntity != null) {
            BeanUtils.copyProperties(userEntity, user);
        }
        return user;
    }

    @Override
    public boolean deleteUser(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Override
    public User updateUser(Long id, User user) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity != null) {
            userEntity.setUsername(user.getUsername());
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setEmail(user.getEmail());
            userRepository.save(userEntity);
        }
        return user;
    }

    public User getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        if(userEntity != null) {
          User user = new User();
          BeanUtils.copyProperties(userEntity, user);
          return user;
        }
        return null;
    }

    public UserEntity getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserEntity findOrCreateUser(String email, String username, String firstName, String lastName) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setUsername(username);
            userEntity.setFirstName(firstName);
            userEntity.setLastName(lastName);

            Fridge fridge = new Fridge();
            fridge.setUser(userEntity);
            userEntity.setFridge(fridge);
            return userRepository.save(userEntity);
        }
    }

}
