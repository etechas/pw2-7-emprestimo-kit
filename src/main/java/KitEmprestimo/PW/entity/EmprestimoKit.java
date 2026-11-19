package KitEmprestimo.PW.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "TBL_EMPRESTIMO_KIT", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmprestimoKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "O código do kit é obrigatório.")
    @Size(max = 100, message = "O código do kit deve ter no máximo 100 caracteres.")
    @Column(name = "TX_CODIGO_KIT", nullable = false, length = 100)
    private String codigoKit;

    @NotBlank(message = "O nome do aluno é obrigatório.")
    @Size(max = 150, message = "O nome do aluno deve ter no máximo 150 caracteres.")
    @Column(name = "TX_NOME_ALUNO", nullable = false, length = 150)
    private String nomeAluno;

    @Column(name = "HR_RETIRADA", nullable = false)
    private LocalTime horarioRetirada;

    @Column(name = "HR_LIMITE_DEVOLUCAO", nullable = false)
    private LocalTime horarioLimiteDevolucao;

    @Column(name = "HR_DEVOLUCAO_REAL")
    private LocalTime horarioDevolucaoReal;

    @Column(name = "TX_STATUS", nullable = false, length = 30)
    private String status;
}