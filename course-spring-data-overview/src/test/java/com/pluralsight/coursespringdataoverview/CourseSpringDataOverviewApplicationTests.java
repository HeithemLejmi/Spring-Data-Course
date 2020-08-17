package com.pluralsight.coursespringdataoverview;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CourseSpringDataOverviewApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Test
	public void verifyFlightCanBeSaved() {
		Flight flight = new Flight();
		flight.setOrigin("Paris");
		flight.setDestination("Monastir");
		flight.setScheduleAt(LocalDateTime.now().plusDays(7));
		entityManager.persist(flight);
		//entityManager.flush();

		final TypedQuery<Flight> flights = entityManager.createQuery("SELECT f from Flight f", Flight.class);
		List<Flight> resultList = flights.getResultList();

		Assertions.assertThat(resultList).hasSize(1);
		Assertions.assertThat(resultList.get(0)).isEqualTo(flight);
	}

}
