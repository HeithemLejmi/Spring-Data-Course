package com.pluralsight.coursespringdataoverview.repository;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FlightRepository extends PagingAndSortingRepository<Flight, String> {

    // SELECT * FROM Flight WHERE origin = (?)
    List<Flight> findByOrigin(String origin);

    // SELECT * FROM Flight WHERE origin = (?) AND destination = (?)
    List<Flight> findByOriginAndDestination(String origin, String destination);

    // SELECT * FROM Flight WHERE origin IN (?)
    List<Flight> findByOriginIn(String ... city);

    // SELECT * FROM FLIGHT WHERE upper(origin) = upper(?)
    List<Flight> findByOriginIgnoreCase(String origin);

    Page<Flight> findByOrigin(String origin, Pageable pageable);
}
