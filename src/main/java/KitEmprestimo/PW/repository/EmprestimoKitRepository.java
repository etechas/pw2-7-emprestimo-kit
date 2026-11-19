package KitEmprestimo.PW.repository;

import KitEmprestimo.PW.entity.EmprestimoKit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoKitRepository extends JpaRepository<EmprestimoKit, Long> {
}