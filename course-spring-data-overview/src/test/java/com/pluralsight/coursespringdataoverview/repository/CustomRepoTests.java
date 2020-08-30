package com.pluralsight.coursespringdataoverview.repository;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomRepoTests {
    @Autowired
    FlightRepository flightRepository;

    @Before
    public void setUp(){
        flightRepository.deleteAll();
    }

    @Test
    public void shouldDeleteByOrigin(){
        Flight toKeep = createFlight("Paris");
        Flight toDelete = createFlight("London");

        flightRepository.save(toDelete);
        flightRepository.save(toKeep);

        assertThat(flightRepository.findAll())
                .hasSize(2)
                .extracting(Flight::getOrigin)
                .containsExactly("London", "Paris");

        flightRepository.deleteByOrigin("London");

        assertThat(flightRepository.findAll())
                .hasSize(1)
                .extracting(Flight::getOrigin)
                .containsExactly("Paris");
    }

    private Flight createFlight(String origin){
        return new Flight()
                .withOrigin(origin)
                .withDestination("Madrid")
                .withScheduleAt(LocalDateTime.now());
    }
}
