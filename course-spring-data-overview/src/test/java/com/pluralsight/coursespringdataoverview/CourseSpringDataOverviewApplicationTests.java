package com.pluralsight.coursespringdataoverview;

import com.pluralsight.coursespringdataoverview.entity.Flight;
import org.assertj.core.api.Assertions;
import org.hibernate.query.NativeQuery;
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
		Flight flight_1 = new Flight();
		flight_1.setOrigin("Paris");
		flight_1.setDestination("Monastir");
		flight_1.setScheduleAt(LocalDateTime.now().plusDays(7));
		entityManager.persist(flight_1);

		Flight flight_2 = new Flight();
		flight_2.setOrigin("Lyon");
		flight_2.setDestination("Tunis");
		flight_2.setScheduleAt(LocalDateTime.now().plusDays(7));
		entityManager.persist(flight_2);

		entityManager.flush();

		final TypedQuery<Flight> flights1 = entityManager.createQuery("SELECT f from Flight f", Flight.class);
		List<Flight> resultList = flights1.getResultList();

		final List<Flight> flights2 = getFlightByOrigin("Lyon");

		Assertions.assertThat(flights2)
				.hasSize(1)
				.first()
				.isEqualTo(flight_2);

		Assertions.assertThat(resultList)
				.hasSize(2)
				.first()
				.isEqualTo(flight_1);
	}

	/**
	 * Native Query using JPA BoilerPlate Code to do the same job of the Derived Query of findByOrigin
	 * @param origin
	 * @return
	 */
	private List<Flight> getFlightByOrigin(String origin){
		List<Flight> flights = entityManager.createNativeQuery(" select * from Flight where origin = ? ", Flight.class).setParameter(1, origin).getResultList();
		return flights;
	}

}
