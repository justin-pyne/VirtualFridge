package usfca.pyne.cs601.virtualfridge.Controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usfca.pyne.cs601.virtualfridge.Entity.UserEntity;
import usfca.pyne.cs601.virtualfridge.Model.*;
import usfca.pyne.cs601.virtualfridge.Service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user;
        user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/exists")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable("id") Long id) {
        boolean deleted;
        deleted = userService.deleteUser(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", deleted);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        user = userService.updateUser(id, user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/goals")
    public ResponseEntity<String> updateMacroNutritionalGoals(@RequestParam("email") String email,
                                                              @RequestBody MacronutrientGoalsDTO goalsDTO) {
        userService.updateMacroNutritionalGoals(email, goalsDTO);
        return ResponseEntity.ok("Goals updated successfully.");
    }

    @PostMapping("/calculate-calories")
    public ResponseEntity<CalorieCalcResultDTO> calculateMaintenanceCalories(
            @RequestParam("email") String email,
            @RequestBody CalorieCalcDTO calculationDTO) {
        CalorieCalcResultDTO result = userService.calculateMaintenanceCalories(email, calculationDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/macros")
    public ResponseEntity<MacrosDTO> getUserMacros(@RequestParam("email") String email) {
        try {
            MacrosDTO userMacros = userService.getUserMacros(email);
            return ResponseEntity.ok(userMacros);
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PostMapping("/findOrCreate")
    public ResponseEntity<User> findOrCreateUser(@RequestBody User user) {
        UserEntity foundOrCreatedUser = userService.findOrCreateUser(user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName());
        User responseUser = new User();
        BeanUtils.copyProperties(foundOrCreatedUser, responseUser);
        return ResponseEntity.ok(responseUser);
    }
}
