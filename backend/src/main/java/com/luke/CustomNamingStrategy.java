package com.luke;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Created by Lukasz on 2017-01-09.
 */
public class CustomNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("<<<<<<<<<<<<<<<<<< Name: "+identifier);
        return identifier;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("<<<<<<<<<<<<<<<<<< Name: "+identifier);
        return identifier;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("<<<<<<<<<<<<<<<<<< Name: "+identifier);
        return identifier;
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("<<<<<<<<<<<<<<<<<< Name: "+identifier);
        return identifier;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("<<<<<<<<<<<<<<<<<< Name: "+identifier);
        return identifier;
    }
}
