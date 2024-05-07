package edu.ncsu.csc.iTrust2.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ncsu.csc.iTrust2.forms.PatientAdvocateForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.models.enums.State;

/**
 * Represents a Patient Advocate in the iTrust2 system
 *
 * @author Gabe Sarver
 *
 */
@Entity
public class PatientAdvocate extends User {

    /**
     * The first name of this patient advocate
     */
    @Length ( min = 1 )
    private String       firstName;

    /**
     * The first name of this patient advocate
     */
    @Length ( min = 1 )
    private String       middleName;

    /**
     * The preferred name of this patient advocate
     */
    @Length ( max = 20 )
    private String       preferredName;

    /**
     * The last name of this patient advocate
     */
    @Length ( min = 1 )
    private String       lastName;

    /**
     * The email address of this patient advocate
     */
    @Length ( max = 30 )
    private String       email;

    /**
     * The address line 1 of this patient advocate
     */
    @Length ( max = 50 )
    private String       address1;

    /**
     * The address line 2 of this patient advocate
     */
    @Length ( max = 50 )
    private String       address2;

    /**
     * The city of residence of this patient advocate
     */
    @Length ( max = 15 )
    private String       city;

    /**
     * The state of residence of this patient advocate
     */
    @Enumerated ( EnumType.STRING )
    private State        state;

    /**
     * The zip code of this patient advocate
     */
    @Length ( min = 5, max = 10 )
    private String       zip;

    /**
     * The phone number of this patient advocate
     */
    @Length ( min = 12, max = 12 )
    private String       phone;

    /**
     * Holds all the PA's patient objects
     */
    @ElementCollection ( targetClass = String.class, fetch = FetchType.EAGER )
    private List<String> patients;

    /**
     * For Hibernate
     */
    public PatientAdvocate () {

    }

    /**
     * Creates a Patient Advocate from the provided UserForm
     *
     * @param uf
     *            UserForm to build a Patient Advocate from
     */
    public PatientAdvocate ( final UserForm uf ) {
        super( uf );
        if ( !getRoles().contains( Role.ROLE_PATIENT_ADVOCATE ) ) {
            throw new IllegalArgumentException(
                    "Attempted to create a Patient Advocate record for a non-Patient Advocate user!" );
        }
        patients = new ArrayList<String>();

    }

    /**
     * Adds a patient under the PA
     *
     * @param patient
     *            Patient being added
     * @return True if patient can be added, false if not
     */
    public boolean addPatient ( final Patient patient ) {
        if ( patient == null ) {
            return false;
        }
        patients.add( patient.getUsername() );
        System.out.println( "added patient to list" );
        return true;
    }

    /**
     * Removes the patient from the PA's records
     *
     * @param patient
     *            Patient being removed
     * @return True if patient is removed, false if not
     */
    public boolean removePatient ( final Patient patient ) {

        if ( patient == null ) {
            return false;
        }
        return patients.remove( patient.getUsername() );

    }

    /**
     * Returns list patients
     *
     * @return Patient list
     */
    @JsonIgnore
    public List<String> getPatients () {
        return patients;
    }

    /**
     * Returns first name
     *
     * @return firstName first name
     */
    public String getFirstName () {
        return firstName;
    }

    /**
     * Sets first name
     *
     * @param firstName
     *            first name
     */
    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    /**
     * Gets middle name
     *
     * @return middleName middle name
     */
    public String getMiddleName () {
        return middleName;
    }

    /**
     * Sets middle name
     *
     * @param middleName
     *            middle name
     */
    public void setMiddleName ( final String middleName ) {
        this.middleName = middleName;
    }

    /**
     * Gets preffered name
     *
     * @return preferredName preferred name
     */
    public String getPreferredName () {
        return preferredName;
    }

    /**
     * Sets preferred name
     *
     * @param preferredName
     *            preferred name
     */
    public void setPreferredName ( final String preferredName ) {
        this.preferredName = preferredName;
    }

    /**
     * Gets last name
     *
     * @return lastName last name
     */
    public String getLastName () {
        return lastName;
    }

    /**
     * Sets last name
     *
     * @param lastName
     *            last name
     */
    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

    /**
     * Gets email
     *
     * @return email email of patient advocate
     */
    public String getEmail () {
        return email;
    }

    /**
     * Sets email
     *
     * @param email
     *            patient advocate email
     */
    public void setEmail ( final String email ) {
        this.email = email;
    }

    /**
     * Gets the first address
     *
     * @return address1 first address
     */
    public String getAddress1 () {
        return address1;
    }

    /**
     * Sets address first
     *
     * @param address1
     *            address one
     */
    public void setAddress1 ( final String address1 ) {
        this.address1 = address1;
    }

    /**
     * Gets city
     *
     * @return city of pa
     */
    public String getCity () {
        return city;
    }

    /**
     * Sets the city of pa
     *
     * @param city
     *            of pa
     */
    public void setCity ( final String city ) {
        this.city = city;
    }

    /**
     * Gets state of pa
     *
     * @return state of pa
     */
    public State getState () {
        return state;
    }

    /**
     * Sets state of pa
     *
     * @param state
     *            of pa
     */
    public void setState ( final State state ) {
        this.state = state;
    }

    /**
     * Gets zipcode of pa
     *
     * @return zip of pa
     */
    public String getZip () {
        return zip;
    }

    /**
     * Sets zip of pa
     *
     * @param zip
     *            of pa
     */
    public void setZip ( final String zip ) {
        this.zip = zip;
    }

    /**
     * Gets phone of pa
     *
     * @return phone of pa
     */
    public String getPhone () {
        return phone;
    }

    /**
     * Sets phone of pa
     *
     * @param phone
     *            of pa
     */
    public void setPhone ( final String phone ) {
        this.phone = phone;
    }

    /**
     * Updates this patient advocate based on provided UserForm
     *
     * @param form
     *            Form to update form
     * @return updated patient advocate
     */
    public PatientAdvocate update ( final PatientAdvocateForm form ) {
        setFirstName( form.getFirstName() );
        setPreferredName( form.getPreferredName() );
        setLastName( form.getLastName() );
        setEmail( form.getEmail() );
        setAddress1( form.getAddress1() );
        setPhone( form.getPhone() );
        setMiddleName( form.getMiddleName() );
        setPhone( form.getPhone() );
        return this;

    }

}
