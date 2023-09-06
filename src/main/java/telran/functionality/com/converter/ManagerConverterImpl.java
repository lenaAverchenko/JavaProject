package telran.functionality.com.converter;

/**
 * Class - Converter for the Manager entities
 *
 * @author Olena Averchenko
 * */

import org.springframework.stereotype.Component;
import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.dto.ManagerCreateDto;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Manager;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class ManagerConverterImpl implements Converter<Manager, ManagerDto, ManagerCreateDto> {

    /**
     * Method to convert Manager entity to ManagerDto, showed to user.
     * @param manager entity from database
     * @return ManagerDto object
     * */
    @Override
    public ManagerDto toDto(Manager manager) {
        return new ManagerDto(manager.getId(), manager.getFirstName(), manager.getLastName(),
                manager.getStatus(), manager.getClients().stream().map(client ->
                        new ClientDto(client.getId(), client.getStatus(), client.getFirstName(),
                                client.getLastName(), null, null))
                .collect(Collectors.toList()),
                manager.getProducts().stream().map(product ->
                                new ProductDto(product.getId(), product.getName(), product.getStatus(),
                                        product.getLimitValue(), null))
                        .collect(Collectors.toList()));
    }

    /**
     * Method to convert ManagerCreateDto (data provided by user) to entity Manager, stored in database.
     * @param createdDto provided data
     * @return Manager entity
     * */
    @Override
    public Manager toEntity(ManagerCreateDto createdDto) {
        return new Manager(createdDto.getFirstName(),
                createdDto.getLastName(),
                new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime()));
    }
}