package com.pluralsight.coursespringdataoverview.repository;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class DerivedQueriesTest {

    @Autowired
    private FlightRepository flightRepository;

    @Before
    public void setUp(){
        flightRepository.deleteAll();
    }

    @Test
    public void shouldFindFlightsFromLondon(){
        // Given
        final Flight flight1 = createFlightWithOrigin("London");
        final Flight flight2 = createFlightWithOrigin("London");
        final Flight flight3 = createFlightWithOrigin("Montreal");
        final Flight flight4 = createFlightWithOrigin("New York");
        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);
        flightRepository.save(flight4);
        List<Flight> expected = List.of(flight1, flight2);

        // When
        List<Flight> flightsFromLondon = flightRepository.findByOrigin("London");

        // Then
        assertThat(flightsFromLondon)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .usingRecursiveComparison().isEqualTo(expected);

        assertThat(flightsFromLondon)
                .isNotNull()
                .isNotEmpty()
                .first().isEqualToComparingFieldByField(flight1);
    }

    @Test
    public void shouldFindFlightsFromLondonToMadrid(){
        // Given
        final Flight flight1 = createFlightWithOriginAndDestination("London", "Madrid");
        final Flight flight2 = createFlightWithOriginAndDestination("London", "Madrid");
        final Flight flight3 = createFlightWithOriginAndDestination("Montreal", "London");
        final Flight flight4 = createFlightWithOriginAndDestination("New York", "Paris");
        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);
        flightRepository.save(flight4);
        List<Flight> expected = List.of(flight1, flight2);

        // When
        List<Flight> flightsFromLondonToMadrid = flightRepository.findByOriginAndDestination("London", "Madrid");

        // Then
        assertThat(flightsFromLondonToMadrid)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .usingRecursiveComparison().isEqualTo(expected);

        assertThat(flightsFromLondonToMadrid)
                .isNotNull()
                .isNotEmpty()
                .first().isEqualToComparingFieldByField(flight1);
    }

    @Test
    public void shouldFindFlightFromLondonOrMadrid(){
        // Given
        Flight flight1 = createFlightWithOriginAndDestination("London", "Paris");
        Flight flight2 = createFlightWithOriginAndDestination("New York", "Tokyo");
        Flight flight3 = createFlightWithOriginAndDestination("Madrid", "Sao Paolo");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        List<Flight> expected = List.of(flight1, flight3);

        // When
        List<Flight> result = flightRepository.findByOriginIn("London", "Madrid");

        // Then
        assertThat(result).isNotNull().isNotEmpty().hasSize(2).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void shouldFindFlightFromLondon_NotCaseSensitive(){
        // Given
        Flight flight1 = createFlightWithOrigin("London");

        flightRepository.save(flight1);


        // When
        List<Flight> resultCaseSensitive = flightRepository.findByOrigin("LONDON");
        List<Flight> resultNotCaseSensitive = flightRepository.findByOriginIgnoreCase("LONDON");


        // Then
        //Case sensitive: LONDON is different than London, so the flight1 won't be found by a simple findByOrigin()
        assertThat(resultCaseSensitive).isEmpty();
        //Case sensitive: LONDON is different than London, so the flight1 will be found be findByOriginIgnoreCase()
        assertThat(resultNotCaseSensitive).isNotEmpty().hasSize(1).first().isEqualToComparingFieldByField(flight1);
    }

    private Flight createFlightWithOrigin(String origin){
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setScheduleAt(LocalDateTime.now());
        return flight;
    }

    private Flight createFlightWithOriginAndDestination(String origin, String destination){
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setScheduleAt(LocalDateTime.now());
        return flight;
    }
}
