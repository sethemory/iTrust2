package edu.ncsu.csc.iTrust2.forms;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.iTrust2.models.PatientAdvocate;

/**
 * Form for user to fill out to add a Patient Advocate to the system.
 *
 * @author Seth Emory
 * @author Gabe Sarver
 * @author John Gobran
 * @author Raphael Phillips
 */
public class PatientAdvocateForm {

    /**
     * For Spring
     */
    public PatientAdvocateForm () {

    }

    /** Username of this Patient **/
    @Length ( max = 20 )
    private String username;

    /** The first name of the patient **/
    @Length ( max = 20 )
    private String firstName;

    /** The middle name of the patient **/
    @Length ( max = 20 )
    private String middleName;

    /** The preferred name of the patient **/
    @Length ( max = 20 )
    private String preferredName;

    /** The last name of the patient **/
    @Length ( max = 30 )
    private String lastName;

    /** The email of the patient **/
    @Length ( max = 30 )
    private String email;

    /** The address line 1 of the patient **/
    @Length ( max = 50 )
    private String address1;

    /** The phone number of the patient **/
    @Pattern ( regexp = "(^[0-9]{3}-[0-9]{3}-[0-9]{4}$)", message = "Phone number must be formatted as xxx-xxx-xxxx" )
    private String phone;

    /**
     * Populate the patient advocate form from a patient advocate object
     *
     * @param patientAdvocate
     *            the patient advocate object to set the form with
     */
    public PatientAdvocateForm ( final PatientAdvocate patientAdvocate ) {
        if ( patientAdvocate == null ) {
            return; /* Nothing to do here */
        }
        setFirstName( patientAdvocate.getFirstName() );
        setLastName( patientAdvocate.getLastName() );
        setPreferredName( patientAdvocate.getPreferredName() );
        setEmail( patientAdvocate.getEmail() );
        setAddress1( patientAdvocate.getAddress1() );
        setPhone( patientAdvocate.getPhone() );
    }

    /**
     * This method gets Patient Advocate's username
     *
     * @return the username of the Patient Advocate
     */
    public String getUsername () {
        return username;
    }

    /**
     * This method sets the Patient Advocate's username
     *
     * @param username
     *            the username of the Patient Advocate
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * This method gets the Patient Adovcate's first name
     *
     * @return the Patient Advocate's first name
     */
    public String getFirstName () {
        return firstName;
    }

    /**
     * This method sets the Patient Advocate's first name
     *
     * @param firstName
     *            the Patient Advocate's first name
     */
    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    /**
     * This method gets the Patient Adovcate's middle name
     *
     * @return the Patient Advocate's middle name
     */
    public String getMiddleName () {
        return middleName;
    }

    /**
     * This method sets the Patient Advocate's middle name
     *
     * @param middleName
     *            the Patient Advocate's middle name
     */
    public void setMiddleName ( final String middleName ) {
        this.middleName = middleName;
    }

    /**
     * This method gets the Patient Adovcate's preferred name
     *
     * @return the Patient Advocate's preferred name
     */
    public String getPreferredName () {
        return preferredName;
    }

    /**
     * This method sets the Patient Advocate's preferred name
     *
     * @param preferredName
     *            the Patient Advocate's preferred name
     */
    public void setPreferredName ( final String preferredName ) {
        this.preferredName = preferredName;
    }

    /**
     * This method gets the Patient Adovcate's last name
     *
     * @return the Patient Advocate's last name
     */
    public String getLastName () {
        return lastName;
    }

    /**
     * This method sets the Patient Advocate's last name
     *
     * @param lastName
     *            the Patient Advocate's last name
     */
    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

    /**
     * This method gets the Patient Adovcate's email
     *
     * @return the Patient Advocate's email
     */
    public String getEmail () {
        return email;
    }

    /**
     * This method sets the Patient Advocate's email
     *
     * @param email
     *            the Patient Advocate's email
     */
    public void setEmail ( final String email ) {
        this.email = email;
    }

    /**
     * This method gets the Patient Adovcate's address
     *
     * @return the Patient Advocate's address
     */
    public String getAddress1 () {
        return address1;
    }

    /**
     * This method sets the Patient Advocate's address
     *
     * @param address1
     *            the Patient Advocate's address
     */
    public void setAddress1 ( final String address1 ) {
        this.address1 = address1;
    }

    /**
     * This method gets the Patient Adovcate's phone number
     *
     * @return the Patient Advocate's phone number
     */
    public String getPhone () {
        return phone;
    }

    /**
     * This method sets the Patient Advocate's phone number
     *
     * @param phone
     *            the Patient Advocate's phone number
     */
    public void setPhone ( final String phone ) {
        this.phone = phone;
    }

}
