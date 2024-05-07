package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.CPTCode;

/**
 * Repository for interacting with CPTCode model. Method implementations
 * generated by Spring
 *
 * @author yli246
 *
 */
public interface CPTCodeRepository extends JpaRepository<CPTCode, Long> {

    /**
     * Finds an CPTCode by the provided code
     *
     * @param code
     *            Code to search by
     * @return Matching code, if any
     */
    public CPTCode findByCode ( int code );

    /**
     * Find an active CPT code by the provided code
     *
     * @param code
     *            Code to search by
     * @return Matching code, if any
     */
    public CPTCode findByCodeAndIsActiveTrue ( int code );

    /**
     * Finds the most recent code, active or not, that matches
     *
     * @param code
     *            Code by search by
     * @return Matching code, if any
     */
    public CPTCode findFirstByCodeOrderByVersionDesc ( int code );
}
