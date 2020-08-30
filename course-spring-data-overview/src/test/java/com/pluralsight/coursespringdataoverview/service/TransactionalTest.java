package com.pluralsight.coursespringdataoverview.service;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import com.pluralsight.coursespringdataoverview.repository.FlightRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionalTest {
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    FlightService flightService;

    @Before
    public void setUp(){
        flightRepository.deleteAll();
    }

    @Test
    public void shouldNot_RollBack_When_ThereIsNoTransaction() {
        try {
            Flight flight = createFlight("Marseille");
            flightService.saveFlightWithoutTransaction(flight);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            assertThat(flightRepository.findAll()).isNotEmpty();
        }
    }

    @Test
    public void should_RollBack_When_ThereIsTransaction() {
        try {
            Flight flight = createFlight("Marseille");
            flightService.saveFlightWithTransaction(flight);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            assertThat(flightRepository.findAll()).isEmpty();

        }
    }

    private Flight createFlight(String origin){
        return new Flight()
                .withOrigin(origin)
                .withDestination("Paris")
                .withScheduleAt(LocalDateTime.now());
    }
}
