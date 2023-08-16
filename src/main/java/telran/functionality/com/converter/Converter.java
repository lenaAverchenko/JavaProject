package telran.functionality.com.converter;

public interface Converter<ENTITY, DTO> {
    DTO toDto(ENTITY entity);

    ENTITY toEntity(DTO dto);
}
