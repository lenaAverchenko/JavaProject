package telran.functionality.com.service;
/**
 * Class implementing UserDetailsService, is used to provide the answer - User, found by login
 * for a future user's authentication and authorisation attempt
 * @see org.springframework.security.core.userdetails.UserDetailsService
 *
 * @author Olena Averchenko
 */

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final ClientDataService clientService;

    private final ManagerDataService managerService;

    /**
     * Method to check provided login with existing in database
     * @param login user's login
     * @throws UsernameNotFoundException if user with login doesn't exist in database
     * @return user object for a future compare
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        ClientData clientData = clientService.getByLogin(login);
        ManagerData managerData = managerService.getByLogin(login);
        User user = null;
        if (clientData == null && managerData == null) {
            throw new UsernameNotFoundException("User with login " + login + " " + "not found");
        }
        if (clientData != null) {
            user = new User(clientData.getLogin(), clientData.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        if (managerData != null) {
            user = new User(managerData.getLogin(), managerData.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
        }
        return user;
    }
}
