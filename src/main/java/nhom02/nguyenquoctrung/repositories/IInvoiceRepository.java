package nhom02.nguyenquoctrung.repositories;

import nhom02.nguyenquoctrung.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {
}