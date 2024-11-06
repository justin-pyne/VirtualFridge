package usfca.pyne.cs601.virtualfridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usfca.pyne.cs601.virtualfridge.Model.Fridge;

import java.util.Optional;

public interface FridgeRepository extends JpaRepository<Fridge, Long> {
    Optional<Fridge> findByUserId(Long userId);
}