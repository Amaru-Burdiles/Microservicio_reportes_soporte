package duoc.amaru.reportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.reportes.model.Review;

public interface ReviewRepo extends JpaRepository<Review, Long> {
    boolean existsByIdCliente(Long userId);
    boolean existsByIdProducto(Long prodId);
    List<Review> findAllByIdProducto(Long prodId);
}
