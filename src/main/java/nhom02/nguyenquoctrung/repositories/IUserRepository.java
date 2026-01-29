package nhom02.nguyenquoctrung.repositories;

import nhom02.nguyenquoctrung.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhone(String phone);
}
