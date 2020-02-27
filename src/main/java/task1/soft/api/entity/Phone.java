package task1.soft.api.entity;

import javax.persistence.*;

@Entity
public	class Phone	{

    @Id
    @Column(name = "id_phone")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private	Long id;

    @Column
    private String type;

    @Column
    private String number;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

