package pl.bartlomiej_swies.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    List<Task> findByDone(boolean done);

    Optional<Task> findById(Integer id);

    Task save(Task entity);

    List<Task> findAllByGroup_Id(Integer groupId);
}
