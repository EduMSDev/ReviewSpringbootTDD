package org.edu.springboot_test.models;

import javax.persistence.*;

@Entity
@Table(name = "banks")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "total_transfers")
    private Integer totalTransfers;

    public Bank() {
    }

    public Bank(Long id, String name, Integer totalTransfers) {
        this.id = id;
        this.name = name;
        this.totalTransfers = totalTransfers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalTransfers() {
        return totalTransfers;
    }

    public void setTotalTransfers(Integer totalTransfers) {
        this.totalTransfers = totalTransfers;
    }


}
