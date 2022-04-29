package org.edu.springboot_test.models;

public class Bank {
    private Long id;
    private String name;
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
