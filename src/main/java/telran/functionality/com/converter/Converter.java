package telran.functionality.com.converter;

public interface Converter<ENTITY, DTO, CREATEDDTO> {
    DTO toDto(ENTITY entity);

    ENTITY toEntity(CREATEDDTO createdDto);
}
