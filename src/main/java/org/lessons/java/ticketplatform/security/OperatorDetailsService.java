package org.lessons.java.ticketplatform.security;

import org.lessons.java.ticketplatform.repository.OperatorRepository;
import org.lessons.java.ticketplatform.model.Operator;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return operatorRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
    }
}
