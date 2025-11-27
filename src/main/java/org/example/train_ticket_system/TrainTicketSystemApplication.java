// TrainTicketSystemApplication.java
package org.example.train_ticket_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TrainTicketSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainTicketSystemApplication.class, args);
    }
}