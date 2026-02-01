package nhom02.nguyenquoctrung.repositories;

import nhom02.nguyenquoctrung.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends
        JpaRepository<Category, Long> {
}
