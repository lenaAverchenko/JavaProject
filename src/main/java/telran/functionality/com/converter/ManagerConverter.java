package telran.functionality.com.converter;

import telran.functionality.com.dto.ManagerDto;
import telran.functionality.com.entity.Manager;

public interface ManagerConverter {

    ManagerDto toDto(Manager manager);

    Manager toEntity(ManagerDto managerDto);
}
