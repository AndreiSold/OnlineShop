package ro.msg.learning.shop.entities.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import ro.msg.learning.shop.wrappers.MonthlyProductWrapper;

import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Products")
@Builder
public class Report {

    @Id
    private String id;

    private byte[] file;

    private List<MonthlyProductWrapper> monthlyProductWrapperList;

    private Integer month;

    private Integer year;
}
