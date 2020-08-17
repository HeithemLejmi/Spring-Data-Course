package com.pluralsight.coursespringdataoverview.repository;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Long> {
}
