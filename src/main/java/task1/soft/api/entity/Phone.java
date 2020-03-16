package task1.soft.api.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "phones")
public class Phone {

    @Id
    @Column(name = "phone_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column
    @NotNull
    private PhoneType type;

    @Column
    @NotNull
    private String number;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private	User user;

}

