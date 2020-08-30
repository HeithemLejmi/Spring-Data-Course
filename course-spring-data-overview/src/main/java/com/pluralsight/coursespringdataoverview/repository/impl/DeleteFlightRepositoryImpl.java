package com.pluralsight.coursespringdataoverview.repository.impl;

import com.pluralsight.coursespringdataoverview.repository.DeleteFlightRepository;

import javax.persistence.EntityManager;

public class DeleteFlightRepositoryImpl implements DeleteFlightRepository {

    /**@Autowired
    private EntityManager entityManager;
     **/

    private EntityManager entityManager;

    public DeleteFlightRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public void deleteByOrigin(String origin) {
        entityManager.createNativeQuery("DELETE from FLIGHT WHERE origin = ?")
                .setParameter(1, origin)
                .executeUpdate();
    }
}
