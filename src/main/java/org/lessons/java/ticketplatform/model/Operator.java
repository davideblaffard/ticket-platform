package org.lessons.java.ticketplatform.model;

import org.lessons.java.ticketplatform.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Entity
@Table(name = "operators")
public class Operator implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "cannot be blank")
    @Column(name = "username")
    private String username; 

    @NotBlank(message = "cannot be blank")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "cannot be blank")
    @Column(name = "password")
    private String password;

    private Boolean notAvailable; // se true, l'operatore non può ricevere ulteriori ticket

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN , OPERATOR

    @OneToMany(mappedBy = "operator")
    private List<Ticket> assignedTickets;



    /***** SPRING SECURITY *****/
    // Metodo per restituire i ruoli di Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    /***** getters & setters *****/

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isNotAvailable() {
        return this.notAvailable;
    }

    public Boolean getNotAvailable() {
        return this.notAvailable;
    }

    public void setNotAvailable(Boolean notAvailable) {
        this.notAvailable = notAvailable;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Ticket> getAssignedTickets() {
        return this.assignedTickets;
    }

    public void setAssignedTickets(List<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }


}
