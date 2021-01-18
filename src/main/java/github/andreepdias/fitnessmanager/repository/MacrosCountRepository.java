package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Diet.MacrosCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MacrosCountRepository extends JpaRepository<MacrosCount, Integer> {

    @Transactional
    void deleteAllByMealEntryId(Integer entryId);

    MacrosCount findByUserMealInfoId(Integer id);
}
