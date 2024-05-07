package edu.ncsu.csc.iTrust2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * This class will hold the permissions that a Patient Advocate will have when
 * representing their . Each object will hold values for the patient, patient
 * advocate, and for each permission type.
 *
 * @author Gabe Sarver
 * @author John Gobran
 */
@Entity
public class Permission extends DomainObject {

    /**
     * ID of this Permission
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long    id;

    /**
     * Patient this permission is associated with
     */
    @NotNull
    @Length ( max = 20 )
    private String  patient;

    /**
     * Patient Advocate this permission is associated with
     */
    @NotNull
    @Length ( max = 20 )
    private String  advocate;

    /**
     * Boolean for office visit visibility
     */
    private boolean officeVis;

    /**
     * Boolean for Billing visibility
     */
    private boolean billing;

    /**
     * Boolean for Prescription visibility
     */
    private boolean prescript;

    /**
     * Empty constructor for Hibernate
     */
    public Permission () {

    }

    /**
     * sdasd asd sd
     *
     * @param patient
     *            sad
     * @param advocate
     *            asdasd
     * @param office
     *            asd sad
     * @param bill
     *            sad
     * @param prescript
     *            dasd
     */
    public Permission ( final String patient, final String advocate, final boolean office, final boolean bill,
            final boolean prescript ) {
        setPatient( patient );
        setAdvocate( advocate );
        setOfficeVis( office );
        setBilling( bill );
        setPrescript( prescript );
    }

    /**
     * Gets the ID of this permission
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the ID of this permission
     *
     * @param id
     *            Long associated with this permission
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets the patient
     *
     * @return Patient Object
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Sets the patient
     *
     * @param patient
     *            Patient that was set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Gets the advocate
     *
     * @return Advocate object
     */
    public String getAdvocate () {
        return advocate;
    }

    /**
     * Sets the advocate
     *
     * @param advocate
     *            Advocate being set
     */
    public void setAdvocate ( final String advocate ) {
        this.advocate = advocate;
    }

    /**
     * Tells us if officeVis is true or false
     *
     * @return True or False
     */
    public boolean isOfficeVis () {
        return officeVis;
    }

    /**
     * Sets the boolean for OfficeVis
     *
     * @param officeVis
     *            True or False
     */
    public void setOfficeVis ( final boolean officeVis ) {
        this.officeVis = officeVis;
    }

    /**
     * Tells us if billing is true or false
     *
     * @return True or False
     */
    public boolean isBilling () {
        return billing;
    }

    /**
     * Sets the billing boolean
     *
     * @param billing
     *            True or false
     */
    public void setBilling ( final boolean billing ) {
        this.billing = billing;
    }

    /**
     * Tells us if prescription is true or false
     *
     * @return True or false
     */
    public boolean isPrescript () {
        return prescript;
    }

    /**
     * Sets the prescript boolean
     *
     * @param prescript
     *            True or false
     */
    public void setPrescript ( final boolean prescript ) {
        this.prescript = prescript;
    }

}
