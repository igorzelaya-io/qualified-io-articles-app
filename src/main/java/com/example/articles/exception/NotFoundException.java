package com.example.articles.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@ResponseStatus
public class NotFoundException extends RuntimeException{

    private static final long serialVersionUID = -7272303639193378954L;

    public NotFoundException(){
        super();
    }

    public NotFoundException(Class entityClass, String... searchParamsMap){
        super(NotFoundException.generateExceptionMessage(entityClass.getSimpleName(),
                toMap(String.class, String.class, (Object[]) searchParamsMap)));
    }

    private static String generateExceptionMessage(String entity, Map<String, String> searchParams){
        return new StringBuilder(entity)
                .append(" was not found for parameters ")
                .append(searchParams)
                .toString();
    }

    private static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries){
        if(entries.length % 2 != 0)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream
                .range(0, entries.length / 2)
                .map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])), Map::putAll);
    }
}
