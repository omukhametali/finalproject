package kz.iitu.csse.group34.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Machines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gosNomer;
    private String marka;
    private String type;
    private String year;
    private String opisanye;
    @ManyToOne
    private Users booker;
    @ManyToOne
    private Users author;

}
