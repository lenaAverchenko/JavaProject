package telran.functionality.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import telran.functionality.com.entity.*;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.enums.Status;
import telran.functionality.com.enums.Type;
import telran.functionality.com.exceptions.EmptyRequiredListException;
import telran.functionality.com.exceptions.NotExistingEntityException;
import telran.functionality.com.repository.ClientRepository;
import telran.functionality.com.repository.ManagerRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ManagerRepository managerRepository;

    private List<Manager> managers;
    private List<Client> clients;

    @BeforeEach
    public void init() {
        managers = Arrays.asList(
                new Manager(1, "Oleh", "Olehov", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(2, "Dalim", "Dalimow", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())),
                new Manager(3, "Anna", "Antonova", new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime())));

        clients = Arrays.asList(
                new Client(UUID.randomUUID(), managers.get(0), new ArrayList<>(), Status.ACTIVE, "0000000", "Bank", "Bank", "bank@mail.com", "Banking Street", "000000000000", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(1), new ArrayList<>(), Status.ACTIVE, "FT11111", "Michail", "Michailov", "michail@mail.com", "12/7 Long Street", "111222333444", new Timestamp(new Date().getTime())),
                new Client(UUID.randomUUID(), managers.get(2), new ArrayList<>(), Status.ACTIVE, "FT22222", "Leo", "Leonov", "leo@mail.com", "2 Down Street", "777888999666", new Timestamp(new Date().getTime())));

    }

    @Test
    void getAll() {
        Mockito.when(clientRepository.findAll()).thenReturn(clients);
        assertEquals(3, clientService.getAll().size());
    }

    @Test
    void getAllNotExisting() {
        Mockito.when(clientRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(EmptyRequiredListException.class, () -> clientService.getAll());
    }

    @Test
    void getById() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clients.get(0)));
        Client client = clientService.getById(clientId);
        assertEquals(clientId, client.getId());
        assertEquals(Status.ACTIVE, client.getStatus());
        assertEquals("Bank", client.getFirstName());
        assertEquals("Bank", client.getLastName());
        assertEquals("bank@mail.com", client.getEmail());
        assertEquals("Banking Street", client.getAddress());
        assertEquals("000000000000", client.getPhone());
        assertEquals("0000000", client.getTaxCode());
    }

    @Test
    void getByIdNotExisting() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(null));
        assertThrows(NotExistingEntityException.class, () -> clientService.getById(clientId));
    }

    @Test
    void save() {
        Client client = new Client(UUID.randomUUID(), managers.get(1), new ArrayList<>(), Status.ACTIVE, "FT33333", "New", "NewLeonov", "new_leo@mail.com", "New 2 Down Street", "999888999666", new Timestamp(new Date().getTime()));
        Mockito.when(clientRepository.save(client)).thenReturn(client);
        Client savedClient = clientService.save(client);
        assertEquals(client.getId(), savedClient.getId());
        assertEquals(Status.ACTIVE, savedClient.getStatus());
        assertEquals("New", savedClient.getFirstName());
        assertEquals("NewLeonov", savedClient.getLastName());
        assertEquals("new_leo@mail.com", savedClient.getEmail());
        assertEquals("New 2 Down Street", savedClient.getAddress());
        assertEquals("999888999666", savedClient.getPhone());
        assertEquals("FT33333", savedClient.getTaxCode());
    }

    @Test
    void updatePersonalInfo() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clients.get(0)));
        Client client = new Client(UUID.randomUUID(), managers.get(1), new ArrayList<>(), Status.ACTIVE, "FT33333", "New", "NewLeonov", "new_leo@mail.com", "New 2 Down Street", "999888999666", new Timestamp(new Date().getTime()));
        Client updatedClient = clientService.updatePersonalInfo(clientId, client);
        assertEquals(clientId, updatedClient.getId());
        assertEquals("New", client.getFirstName());
        assertEquals("NewLeonov", client.getLastName());
        assertEquals("new_leo@mail.com", client.getEmail());
        assertEquals("New 2 Down Street", client.getAddress());
        assertEquals("999888999666", client.getPhone());
    }

//    @Test
//    void delete() {
//        UUID clientId = clients.get(1).getId();
//        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clients.get(1)));
//        Client client = clientService.getById(clientId);
//        clientService.delete(clientId);
//        Mockito.verify(clientRepository).delete(client);
//    }

    @Test
    void changeManager() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clients.get(0)));
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.ofNullable(managers.get(2)));
        clientService.changeManager(clientId, 1);
        Client changedClient = clientService.getById(clientId);
        assertEquals(clientId, changedClient.getId());
        assertEquals(3, changedClient.getManager().getId());
    }

    @Test
    void changeStatus() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clients.get(0)));
        clientService.changeStatus(clientId, Status.WAITING);
        assertEquals(Status.WAITING, clientService.getById(clientId).getStatus());
    }

    @Test
    void inactivateStatus() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clients.get(0)));
        clientService.inactivateStatus(clientId);
        assertEquals(Status.INACTIVE, clientService.getById(clientId).getStatus());
    }

    @Test
    void clientExists() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clients.get(0)));
        boolean answer = clientService.clientExists(clientService.getById(clientId));
        assertTrue(answer);
    }

    @Test
    void statusIsValid() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clients.get(0)));
        boolean answer = clientService.statusIsValid(clientId);
        assertTrue(answer);
    }
}