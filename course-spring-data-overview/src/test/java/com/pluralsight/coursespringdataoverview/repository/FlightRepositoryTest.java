package com.pluralsight.coursespringdataoverview.repository;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shouldPerformCRUDOperations(){
        // Given
        Flight flight = new Flight();
        flight.setOrigin("Paris");
        flight.setDestination("Monastir");
        flight.setScheduleAt(LocalDateTime.now().plusDays(7));

        // When
        flightRepository.save(flight);

        // Then
        assertThat(flightRepository.findAll())
                .hasSize(1)
                .first()
                .isEqualToComparingFieldByField(flight);

        // When
        flightRepository.deleteById(flight.getId());

        // Then
        assertThat(flightRepository.count())
                .isZero();
    }
}
