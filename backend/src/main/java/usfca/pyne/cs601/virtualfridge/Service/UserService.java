package usfca.pyne.cs601.virtualfridge.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Entity.UserEntity;
import usfca.pyne.cs601.virtualfridge.Model.*;
import usfca.pyne.cs601.virtualfridge.Repository.UserRepository;


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

    public UserEntity saveUserEntity(UserEntity userEntity) {
        return userRepository.save(userEntity);
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

    public void updateMacroNutritionalGoals(String email, MacronutrientGoalsDTO goalsDTO) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        user.setDailyCalorieGoal(goalsDTO.getDailyCalorieGoal());
        user.setDailyProteinGoal(goalsDTO.getDailyProteinGoal());
        user.setDailyCarbGoal(goalsDTO.getDailyCarbGoal());
        user.setDailyFatGoal(goalsDTO.getDailyFatGoal());

        userRepository.save(user);
    }

    @Transactional
    public CalorieCalcResultDTO calculateMaintenanceCalories(String email, CalorieCalcDTO calcDTO) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        double bmr = CalorieCalculatorService.calculateBMR(
                calcDTO.getGender(),
                calcDTO.getWeightKg(),
                calcDTO.getHeightCm(),
                calcDTO.getAge()
        );

        double maintenanceCalories = CalorieCalculatorService.calculateMaintenanceCalories(bmr, calcDTO.getActivityLevel());
        double[] macros = CalorieCalculatorService.calculateMacros(maintenanceCalories);
        double protein = macros[0];
        double carbs = macros[1];
        double fat = macros[2];

        user.setDailyCalorieGoal(maintenanceCalories);
        user.setDailyProteinGoal(protein);
        user.setDailyCarbGoal(carbs);
        user.setDailyFatGoal(fat);

        userRepository.save(user);

        CalorieCalcResultDTO resultDTO = new CalorieCalcResultDTO();
        resultDTO.setMaintenanceCalories(maintenanceCalories);
        resultDTO.setProtein(protein);
        resultDTO.setCarbs(carbs);
        resultDTO.setFat(fat);

        return resultDTO;
    }

    public MacrosDTO getUserMacros(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        MacrosDTO dto = new MacrosDTO();

        dto.setDailyCalorieGoal(userEntity.getDailyCalorieGoal());
        dto.setDailyProteinGoal(userEntity.getDailyProteinGoal());
        dto.setDailyCarbsGoal(userEntity.getDailyCarbGoal());
        dto.setDailyFatGoal(userEntity.getDailyFatGoal());

        dto.setCurrentCalories(userEntity.getCurrentCalories());
        dto.setCurrentProtein(userEntity.getCurrentProtein());
        dto.setCurrentCarbs(userEntity.getCurrentCarbs());
        dto.setCurrentFat(userEntity.getCurrentFat());

        dto.setRemainingCalories(Math.max(0, userEntity.getDailyCalorieGoal() > 0 ?
                userEntity.getDailyCalorieGoal() - userEntity.getCurrentCalories() : 0));

        dto.setRemainingProtein(Math.max(0, userEntity.getDailyProteinGoal() > 0 ?
                userEntity.getDailyProteinGoal() - userEntity.getCurrentProtein() : 0));

        dto.setRemainingCarbs(Math.max(0, userEntity.getDailyCarbGoal() > 0 ?
                userEntity.getDailyCarbGoal() - userEntity.getCurrentCarbs() : 0));

        dto.setRemainingFat(Math.max(0, userEntity.getDailyFatGoal() > 0 ?
                userEntity.getDailyFatGoal() - userEntity.getCurrentFat() : 0));


        if (userEntity.getDailyCalorieGoal() == 0) {
            dto.setRemainingCalories(0);
        }
        if (userEntity.getDailyProteinGoal() == 0) {
            dto.setRemainingProtein(0);
        }
        if (userEntity.getDailyCarbGoal() == 0) {
            dto.setRemainingCarbs(0);
        }
        if (userEntity.getDailyFatGoal() == 0) {
            dto.setRemainingFat(0);
        }

        return dto;
    }
}


