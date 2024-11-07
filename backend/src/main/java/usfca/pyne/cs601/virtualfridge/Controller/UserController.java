package usfca.pyne.cs601.virtualfridge.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usfca.pyne.cs601.virtualfridge.Model.CalorieCalcDTO;
import usfca.pyne.cs601.virtualfridge.Model.CalorieCalcResultDTO;
import usfca.pyne.cs601.virtualfridge.Model.MacronutrientGoalsDTO;
import usfca.pyne.cs601.virtualfridge.Model.User;
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

}
