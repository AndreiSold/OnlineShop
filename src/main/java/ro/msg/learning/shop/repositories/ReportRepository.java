package ro.msg.learning.shop.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.entities.documents.Report;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {

    Report findByMonthEqualsAndYearEquals(int month, int year);
}
