package kz.iitu.csse.group34.repositories;

import kz.iitu.csse.group34.entities.Comments;
import kz.iitu.csse.group34.entities.Machines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Optional<Comments> findById(Long id);
    List<Comments> findByMachines(Machines machine);
}
