package telran.functionality.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.entity.ManagerData;

import java.util.Arrays;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private ClientDataService clientService;

    @Autowired
    private ManagerDataService managerService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException{
        ClientData clientData = clientService.getByLogin(login);
        ManagerData managerData = managerService.getByLogin(login);
        User user = null;
        if (clientData == null && managerData == null) {
            throw new UsernameNotFoundException("User with login " + login + " " + "not found");
        }
        if (clientData != null){
            user = new User(clientData.getLogin(), clientData.getPassword(),
            Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        if (managerData != null){
            user = new User(managerData.getLogin(), managerData.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
        }
        return user;
    }
}
