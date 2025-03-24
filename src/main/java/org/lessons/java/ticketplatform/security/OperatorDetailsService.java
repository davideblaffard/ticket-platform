package org.lessons.java.ticketplatform.security;

import org.lessons.java.ticketplatform.repository.OperatorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OperatorDetailsService implements UserDetailsService{
    
    private final OperatorRepository operatorRepository;

    public OperatorDetailsService(OperatorRepository operatorRepository){
        this.operatorRepository = operatorRepository;
    }

    // @Override
    // public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
    //     return new DatabaseOperatorDetails(operatorRepository.findByEmail(email).get());
    // }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Tentativo di login per: " + email);

        return operatorRepository.findByEmail(email)
                .map(DatabaseOperatorDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
    }
}
