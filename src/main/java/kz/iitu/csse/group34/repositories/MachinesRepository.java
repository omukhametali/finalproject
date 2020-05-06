package kz.iitu.csse.group34.repositories;

import kz.iitu.csse.group34.entities.Machines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MachinesRepository extends JpaRepository<Machines, Long> {
    Optional<Machines> findById(Long id);
}
