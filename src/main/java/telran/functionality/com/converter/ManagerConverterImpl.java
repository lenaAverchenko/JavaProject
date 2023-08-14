package telran.functionality.com.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.entity.Manager;

import java.util.stream.Collectors;

@Component
public class ManagerConverterImpl implements ManagerConverter {

    @Autowired
    private ClientConverter clientConverter;
    @Autowired
    private ProductConverter productConverter;
    @Override
    public ManagerDto toDto(Manager manager) {
        return new ManagerDto(manager.getId(), manager.getFirstName(), manager.getLastName(),
                manager.getClients().stream().map(client -> clientConverter.toDto(client)).collect(Collectors.toList()),
                manager.getProducts().stream().map(product -> productConverter.toDto(product)).collect(Collectors.toList()));
    }

    @Override
    public Manager toEntity(ManagerDto managerDto) {

        return new Manager(managerDto.getId(), managerDto.getFirstName(), managerDto.getLastName(),
                managerDto.getClients().stream().map(client -> clientConverter.toEntity(client)).collect(Collectors.toList()),
                managerDto.getProducts().stream().map(product -> productConverter.toEntity(product)).collect(Collectors.toList()));
    }
}
