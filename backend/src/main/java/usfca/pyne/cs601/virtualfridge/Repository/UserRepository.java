package usfca.pyne.cs601.virtualfridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usfca.pyne.cs601.virtualfridge.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
