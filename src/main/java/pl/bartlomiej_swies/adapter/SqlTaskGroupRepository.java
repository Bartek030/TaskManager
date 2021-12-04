package pl.bartlomiej_swies.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bartlomiej_swies.model.TaskGroup;
import pl.bartlomiej_swies.model.TaskGroupRepository;

import java.util.List;

@Repository
public interface SqlTaskGroupRepository extends JpaRepository<TaskGroup, Integer>, TaskGroupRepository {
    @Override
    @Query("FROM TaskGroup g JOIN FETCH g.tasks")
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
