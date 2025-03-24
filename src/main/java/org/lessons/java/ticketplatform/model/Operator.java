package org.lessons.java.ticketplatform.model;

import org.lessons.java.ticketplatform.model.enums.Role;

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

@Entity
@Table(name = "operators")
public class Operator{
    
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

    @Column
    private Boolean notAvailable; // se true, l'operatore non può ricevere ulteriori ticket

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN , OPERATOR

    @OneToMany(mappedBy = "operator")
    private List<Ticket> assignedTickets;
    
    @Column
    private Boolean enable = true;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
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

    public Boolean getEnable() {
        return this.enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "Operator{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", role=" + role +
            ", notAvailable=" + notAvailable +
            ", enable=" + enable +
            '}';
}


}
