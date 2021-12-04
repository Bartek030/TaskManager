package pl.bartlomiej_swies.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bartlomiej_swies.model.Project;
import pl.bartlomiej_swies.model.ProjectRepository;

import java.util.List;

@Repository
public interface SqlProjectRepository extends JpaRepository<Project, Integer>, ProjectRepository {
    @Override
    @Query("SELECT DISTICNT p FROM Project p JOIN FETCH p.steps")
    List<Project> findAll();
}
