package user.service.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    private int age;
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Role role;

    public User(String firstName, String secondName, int age, Role role) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.role = role;
    }
}
