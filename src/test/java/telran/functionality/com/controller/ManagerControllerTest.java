package telran.functionality.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.ManagerCreateDto;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.dto.ProductCreateDto;
import telran.functionality.com.dto.ProductDto;

import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;
import telran.functionality.com.service.ManagerDataService;
import telran.functionality.com.service.ManagerService;
import telran.functionality.com.service.UserDetailService;

import java.sql.Timestamp;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;


@WebMvcTest(ManagerController.class)
@AutoConfigureMockMvc(addFilters = false)
class ManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailService userDetailService;
    @MockBean
    private PasswordEncoder encoder;
    @MockBean
    private ManagerService managerService;
    @MockBean
    private Converter<Manager, ManagerDto, ManagerCreateDto> managerConverter;
    @MockBean
    private Converter<Product, ProductDto, ProductCreateDto> productConverter;
    @MockBean
    private ManagerDataService managerDataService;


    @Test
    void getAll() throws Exception {
        Mockito.when(userDetailService.loadUserByUsername(null)).thenReturn(new User("ManagerOne", "ManagerOne", Arrays.asList(new SimpleGrantedAuthority("ADMIN"))));
        Mockito.when(userDetailService.loadUserByUsername(anyString())).thenReturn(new User("ManagerOne", "ManagerOne", Arrays.asList(new SimpleGrantedAuthority("ADMIN"))));
        Mockito.when(userDetailService.loadUserByUsername("ManagerOne")).thenReturn(new User("ManagerOne", "ManagerOne", Arrays.asList(new SimpleGrantedAuthority("ADMIN"))));

        Manager manager = new Manager(1, "Oleh", "Olehov", null, null, new Timestamp(new Date().getTime()));
        Mockito.when(managerService.getAll()).thenReturn(Arrays.asList(manager));
        Mockito.when(managerConverter.toDto(manager)).thenReturn(new ManagerDto(manager.getId(),
                manager.getFirstName(), manager.getLastName(), manager.getStatus(), null, null));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/managers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(new ManagerDto(manager.getId(),
                        manager.getFirstName(), manager.getLastName(), manager.getStatus(), null, null)))));
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}