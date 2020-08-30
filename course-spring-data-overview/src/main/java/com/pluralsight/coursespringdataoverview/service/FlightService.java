package com.pluralsight.coursespringdataoverview.service;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import com.pluralsight.coursespringdataoverview.repository.FlightRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FlightService {

    private FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    /**
     * Method without Transactions
     */
    public void saveFlightWithoutTransaction(Flight flight) throws Exception {
        flightRepository.save(flight);
        throw new RuntimeException("I failed");
    }

    /**
     * Method without Transactions
     */
    @Transactional
    public void saveFlightWithTransaction(Flight flight) throws Exception {
        flightRepository.save(flight);
        throw new RuntimeException("I failed");
    }
}
