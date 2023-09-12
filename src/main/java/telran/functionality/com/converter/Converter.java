package telran.functionality.com.converter;

/**
 * Interface - Converter with generics for the different entities
 *
 * @author Olena Averchenko
 */
public interface Converter<ENTITY, DTO, CREATEDDTO> {

    DTO toDto(ENTITY entity);

    ENTITY toEntity(CREATEDDTO createdDto);
}