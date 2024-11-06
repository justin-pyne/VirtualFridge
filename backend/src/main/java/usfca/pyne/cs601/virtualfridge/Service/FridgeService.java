package usfca.pyne.cs601.virtualfridge.Service;

import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Model.Fridge;
import usfca.pyne.cs601.virtualfridge.Repository.FridgeRepository;
import java.util.Optional;

@Service
public class FridgeService {
    private final FridgeRepository fridgeRepository;

    public FridgeService(FridgeRepository fridgeRepository) {
        this.fridgeRepository = fridgeRepository;
    }

    public Fridge getFridgeByUserId(Long userId) {
        return fridgeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Fridge not found for user ID: " + userId));
    }
}
