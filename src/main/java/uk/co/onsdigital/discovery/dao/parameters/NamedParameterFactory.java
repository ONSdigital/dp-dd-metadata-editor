package uk.co.onsdigital.discovery.dao.parameters;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Created by dave on 06/02/2017.
 */
@FunctionalInterface
public interface NamedParameterFactory {

    MapSqlParameterSource create(NamedParam.ListBuilder builder);
}
