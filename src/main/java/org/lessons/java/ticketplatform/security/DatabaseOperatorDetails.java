package org.lessons.java.ticketplatform.security;

import java.util.Collection;
import java.util.List;

import org.lessons.java.ticketplatform.model.Operator;
import org.lessons.java.ticketplatform.model.Ticket;
import org.lessons.java.ticketplatform.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DatabaseOperatorDetails implements UserDetails{

    private final Integer id;
    private final String username;
    private final String password;
    private final String email;
    private final Boolean notAvailable;
    private final Role role; // ADMIN , OPERATOR
    private final List<Ticket> assignedTickets;
    private final Boolean enable = true;

    public DatabaseOperatorDetails(Operator operator) {
        this.id = operator.getId();
        this.username = operator.getUsername();
        this.password = operator.getPassword();
        this.email = operator.getEmail();
        this.notAvailable = operator.getNotAvailable();
        this.role = operator.getRole();
        this.assignedTickets = operator.getAssignedTickets();
    }


    public Integer getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }


    public Boolean isNotAvailable() {
        return this.notAvailable;
    }

    public Boolean getNotAvailable() {
        return this.notAvailable;
    }


    public Role getRole() {
        return this.role;
    }


    public List<Ticket> getAssignedTickets() {
        return this.assignedTickets;
    }


    public Boolean isEnable() {
        return this.enable;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

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
    public boolean isEnabled() { return this.enable; }
}
