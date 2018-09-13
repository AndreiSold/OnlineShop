package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    //Implement needed queries here

}
