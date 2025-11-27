// 5. SeatType.java（商务座、一等座、二等座）
package org.example.train_ticket_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_seat_type")
public class SeatType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    private Double priceRate = 1.0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPriceRate() { return priceRate; }
    public void setPriceRate(Double priceRate) { this.priceRate = priceRate; }
}