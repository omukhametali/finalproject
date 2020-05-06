package kz.iitu.csse.group34.entities;

import lombok.*;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Indexed
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String fullName;

    private String surname;

    private String otchestvo;

    private String IIN;

    private Boolean isActive=true;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Roles> roles;

}
