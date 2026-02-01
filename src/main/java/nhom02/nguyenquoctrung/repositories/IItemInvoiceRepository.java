package nhom02.nguyenquoctrung.repositories;

import nhom02.nguyenquoctrung.entities.ItemInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemInvoiceRepository extends
        JpaRepository<ItemInvoice, Long> {
}