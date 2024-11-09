package usfca.pyne.cs601.virtualfridge.Service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Entity.UserEntity;
import usfca.pyne.cs601.virtualfridge.Repository.UserRepository;

import java.util.List;

@Service
public class DailyResetService {
    private static final Logger logger = LoggerFactory.getLogger(DailyResetService.class);

    private final UserRepository userRepository;

    public DailyResetService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Los_Angeles")
    @Transactional
    public void resetDailyNutritionalConsumption() {
        try {
            logger.info("Starting daily reset of users' nutritional consumption.");
            List<UserEntity> users = userRepository.findAll();
            if (users.isEmpty()) {
                logger.warn("No users found in the system to reset.");
                return;
            }

            for (UserEntity user : users) {
                user.setCurrentCalories(0.0);
                user.setCurrentProtein(0.0);
                user.setCurrentCarbs(0.0);
                user.setCurrentFat(0.0);
            }

            userRepository.saveAll(users);

            logger.info("Successfully reset nutritional consumption for {} users.", users.size());
        } catch (Exception e) {
            logger.error("Error occurred while resetting daily nutritional consumption.", e);
        }
    }
}
