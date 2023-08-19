package telran.functionality.com.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    private static final Logger logger = LoggerFactory.getLogger(ManagerConverterImpl.class);

    @Override
    public ManagerDto toDto(Manager manager) {
        logger.debug("Call method toDto for Manager: {}", manager);
        ManagerDto managerDto = new ManagerDto(manager.getId(), manager.getFirstName(), manager.getLastName(),
                manager.getStatus(), manager.getClients().stream().map(client ->
                        new ClientDto(client.getId(), client.getStatus(), client.getFirstName(),
                                client.getLastName(), null, null))
                .collect(Collectors.toList()),
                manager.getProducts().stream().map(product ->
                                new ProductDto(product.getId(), product.getName(), product.getStatus(),
                                        product.getLimitValue(), null))
                        .collect(Collectors.toList()));
        logger.debug("Method toDto has ended with the result: {}", managerDto);
        return managerDto;
    }


    @Override
    public Manager toEntity(ManagerCreateDto createdDto) {
        logger.debug("Call method toEntity for Manager: {}", createdDto);
        Manager manager = new Manager(createdDto.getFirstName(),
                createdDto.getLastName(),
                new ArrayList<>(), new ArrayList<>(), new Timestamp(new Date().getTime()));
        logger.debug("Method toEntity has ended with the result: {}", manager);
        return manager;
    }
}

