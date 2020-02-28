package task1.soft.api.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DtoUtils {


    private  final ModelMapper modelMapper;
    @Autowired
    public DtoUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static DTOEntity convertToDto(Object obj, DTOEntity mapper) {
        return new ModelMapper().map(obj, mapper.getClass());
    }

    public static Object convertToEntity(Object obj, DTOEntity mapper) {
        return new ModelMapper().map(mapper, obj.getClass());
    }
}
