package com.pluralsight.coursespringdataoverview.repository;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;


import java.time.LocalDateTime;
import java.util.Iterator;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PagingAndSortingTests {
    @Autowired
    FlightRepository flightRepository;

    @Before
    public void setUp(){
        flightRepository.deleteAll();
    }

    @Test
    public void shouldSortFlightWithOrigin(){
        // Given
        Flight flight1 = createFlightWithOrigin("Amsterdam");
        Flight flight2 = createFlightWithOrigin("Berlin");
        Flight flight3 = createFlightWithOrigin("London");
        Flight flight4 = createFlightWithOrigin("Paris");

        flightRepository.save(flight4);
        flightRepository.save(flight2);
        flightRepository.save(flight1);
        flightRepository.save(flight3);

        // When
        // The result of findAll() is going to be sorted, by default, in ascending order (from A to Z if string, or from small to greater if numbers)
        Iterable<Flight> flights = flightRepository.findAll(Sort.by("origin"));
        Iterator<Flight> iterator = flights.iterator();

        // Then
        assertThat(flights)
                .isNotEmpty()
                .hasSize(4)
                .first().isEqualToComparingFieldByField(flight1);

        assertThat(iterator.next().getOrigin()).isEqualTo("Amsterdam");
        assertThat(iterator.next().getOrigin()).isEqualTo("Berlin");
        assertThat(iterator.next().getOrigin()).isEqualTo("London");
        assertThat(iterator.next().getOrigin()).isEqualTo("Paris");

    }

    @Test
    public void shouldSortFlightWithScheduledDateAndThenOrigin(){
        // Given
        Flight amsterdam1 = createFlightWithOriginAndSchedule("Amsterdam", LocalDateTime.now());
        Flight amsterdam2 = createFlightWithOriginAndSchedule("Amsterdam", LocalDateTime.now().plusHours(1));
        Flight london1 = createFlightWithOriginAndSchedule("London", LocalDateTime.now());
        Flight london2 = createFlightWithOriginAndSchedule("London", LocalDateTime.now().plusHours(2));
        Flight london3 = createFlightWithOriginAndSchedule("London", LocalDateTime.now().plusHours(3));

        flightRepository.save(amsterdam1);
        flightRepository.save(amsterdam2);
        flightRepository.save(london1);
        flightRepository.save(london2);
        flightRepository.save(london3);

        // When
        // The result of findAll() is going to be sorted, by default, in ascending order based on the first field "origin", and then
        // if we have instances with same value of "origin" we are going to sort them by the second field "scheduleAt"
        Iterable<Flight> flights = flightRepository.findAll(Sort.by("origin", "scheduleAt"));
        Iterator<Flight> iterator = flights.iterator();

        // Then
        assertThat(flights)
                .isNotEmpty()
                .hasSize(5);

        assertThat(iterator.next()).isEqualToComparingFieldByField(amsterdam1);
        assertThat(iterator.next()).isEqualToComparingFieldByField(amsterdam2);
        assertThat(iterator.next()).isEqualToComparingFieldByField(london1);
        assertThat(iterator.next()).isEqualToComparingFieldByField(london2);
        assertThat(iterator.next()).isEqualToComparingFieldByField(london3);
    }

    @Test
    public void shouldPageResult(){
        // Given
        for (int i =0; i<50; i++){
            flightRepository.save(createFlightWithOrigin(String.valueOf(i)));
        }

        // When
        Page<Flight> flights = flightRepository.findAll(PageRequest.of(2, 5));

        // Then
        assertThat(flights.getTotalElements()).isEqualTo(50);
        assertThat(flights.getTotalPages()).isEqualTo(10);
        assertThat(flights.getNumberOfElements()).isEqualTo(5);
        assertThat(flights.getNumber()).isEqualTo(2);

        assertThat(flights.getContent())
                .extracting(Flight::getOrigin)
                .containsExactly("10", "11", "12", "13", "14");
    }

    @Test
    public void shouldPageAndSortResult(){
        // Given
        for (int i =0; i<50; i++){
            flightRepository.save(createFlightWithOrigin(String.valueOf(i)));
        }

        // When
        Page<Flight> flights = flightRepository.findAll(PageRequest.of(2, 5, Sort.by(DESC, "origin")));

        // Then
        assertThat(flights.getTotalElements()).isEqualTo(50);
        assertThat(flights.getTotalPages()).isEqualTo(10);
        assertThat(flights.getNumberOfElements()).isEqualTo(5);
        assertThat(flights.getNumber()).isEqualTo(2);

        assertThat(flights.getContent())
                .extracting(Flight::getOrigin)
                .containsExactly("44", "43", "42", "41", "40");
    }

    @Test
    public void shouldPageAndSortResultWithDerivedQuery(){
        // Given
        for (int i =0; i<10; i++){
            flightRepository.save(createFlightWithOriginAndDestination("London", String.valueOf(i)));
        }
        for (int i =0; i<10; i++){
            flightRepository.save(createFlightWithOriginAndDestination("Paris", String.valueOf(i)));
        }

        // When
        Page<Flight> flightsToLondon = flightRepository.findByOrigin("London", PageRequest.of(0, 5));

        // Then
        assertThat(flightsToLondon.getTotalElements()).isEqualTo(10);
        assertThat(flightsToLondon.getTotalPages()).isEqualTo(2);
        assertThat(flightsToLondon.getNumberOfElements()).isEqualTo(5);
        assertThat(flightsToLondon.getNumber()).isEqualTo(0);

        assertThat(flightsToLondon.getContent())
                .extracting(Flight::getOrigin)
                .containsExactly("London", "London", "London", "London", "London");
    }


    private Flight createFlightWithOrigin(String origin){
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setScheduleAt(LocalDateTime.now());
        return flight;
    }

    private Flight createFlightWithOriginAndSchedule(String origin, LocalDateTime schedule){
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setScheduleAt(schedule);
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
