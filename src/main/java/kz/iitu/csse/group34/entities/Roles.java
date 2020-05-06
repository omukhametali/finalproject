package kz.iitu.csse.group34.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Roles implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @Override
    public String getAuthority() {
        return this.role;
    }
}
