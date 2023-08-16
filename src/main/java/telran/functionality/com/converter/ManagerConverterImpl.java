package telran.functionality.com.converter;

import org.springframework.stereotype.Component;
import telran.functionality.com.dto.ClientDto;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.dto.ProductDto;
import telran.functionality.com.entity.Client;
import telran.functionality.com.entity.Manager;
import telran.functionality.com.entity.Product;

import java.util.stream.Collectors;

@Component
public class ManagerConverterImpl implements Converter<Manager, ManagerDto> {

    @Override
    public ManagerDto toDto(Manager manager) {
        return new ManagerDto(manager.getId(), manager.getFirstName(), manager.getLastName(),
                manager.getClients().stream().map(client ->
                                new ClientDto(client.getId(), client.getFirstName(),
                                        client.getLastName(), null, null))
                        .collect(Collectors.toList()),
                manager.getProducts().stream().map(product ->
                                new ProductDto(product.getId(), product.getName(), product.getStatus(),
                                        product.getLimitValue(), null))
                        .collect(Collectors.toList()));
    }

    @Override
    public Manager toEntity(ManagerDto managerDto) {

        return new Manager(managerDto.getId(), managerDto.getFirstName(), managerDto.getLastName(),
                managerDto.getClients().stream().map(client ->
                                new Client(client.getId(), client.getFirstName(),
                                        client.getLastName(), null, null))
                        .collect(Collectors.toList()),
                managerDto.getProducts().stream().map(product ->
                                new Product(product.getId(), product.getName(), product.getStatus(),
                                        product.getLimitValue(), null))
                        .collect(Collectors.toList()));
    }
}

//    @Override
//    public Manager toEntity(ManagerDto managerDto) {
//        throw new UnsupportedOperationException();
//    }
