package uk.co.onsdigital.discovery.dao.parameters;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class NamedParameterFactoryImpl implements NamedParameterFactory {

    @Override
    public MapSqlParameterSource create(NamedParam.ListBuilder builder) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        for (NamedParam namedParam : builder.toList()) {
            sqlParameterSource.addValue(namedParam.getKey(), namedParam.getValue());
        }
        return sqlParameterSource;
    }
}

