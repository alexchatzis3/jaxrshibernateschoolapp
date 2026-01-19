package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Teacher;

import java.util.Optional;

/**
 * DAO interface for {@link Teacher} entities.
 * Extends the generic DAO and adds domain-specific query methods.
 */
public interface ITeacherDAO extends IGenericDAO<Teacher>{

    /**
     * Finds a teacher using their VAT number.
     *
     * @param vat the VAT number to search for
     * @return Optional containing the Teacher if found, otherwise empty
     */
    Optional<Teacher> getByVat(String vat);
}
