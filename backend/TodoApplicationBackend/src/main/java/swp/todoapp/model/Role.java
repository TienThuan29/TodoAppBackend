package swp.todoapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_code", length = 50, unique = true, nullable = false)
    private String code;

    @Transient
    private Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        this.permissions = initPermission();
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.code));
        return authorities;
    }

    private Set<Permission> initPermission() {
        return new HashSet<>(Arrays.asList(
                Permission.USER_READ,
                Permission.USER_DELETE,
                Permission.USER_CREATE,
                Permission.USER_UPDATE));
    }

}
