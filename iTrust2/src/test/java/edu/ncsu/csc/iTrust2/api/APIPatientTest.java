package edu.ncsu.csc.iTrust2.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.forms.PatientForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.Permission;
import edu.ncsu.csc.iTrust2.models.enums.BloodType;
import edu.ncsu.csc.iTrust2.models.enums.Ethnicity;
import edu.ncsu.csc.iTrust2.models.enums.Gender;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.models.enums.State;
import edu.ncsu.csc.iTrust2.models.enums.VaccinationStatus;
import edu.ncsu.csc.iTrust2.services.PatientAdvocateService;
import edu.ncsu.csc.iTrust2.services.PatientService;

/**
 * Test for API functionality for interacting with Patients
 *
 * @author Kai Presler-Marshall
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles ( { "test" } )
public class APIPatientTest {

    /**
     * MVC to use calls
     */
    @Autowired
    private MockMvc                                 mvc;

    /**
     * Service to save patients
     */
    @Autowired
    private PatientService<Patient>                 service;

    /**
     * Service to save patient advocates
     */
    @Autowired
    private PatientAdvocateService<PatientAdvocate> paService;

    /**
     * Patient to be used for testing
     */
    Patient                                         p1;

    /**
     * Patient advocate used for testing
     */
    PatientAdvocate                                 p2;

    /**
     * Patient advocate used for testing
     */
    PatientAdvocate                                 p3;

    /**
     * User name 1 for testing
     */
    private static final String                     USER_1 = "demoTestUser1";

    /**
     * User name 2 for testing
     */
    private static final String                     USER_2 = "demoTestUser2";

    /**
     * User name 2 for testing
     */
    private static final String                     USER_3 = "demoTestUser3";

    /**
     * Password for testing
     */
    private static final String                     PW     = "123456";

    /**
     * Sets up test
     */
    @BeforeEach
    public void setup () {

        service.deleteAll();
    }

    /**
     * Tests that getting a patient that doesn't exist returns the proper
     * status.
     *
     * @throws Exception
     *             if error occurs
     */
    @Test
    @Transactional
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testGetNonExistentPatient () throws Exception {
        mvc.perform( get( "/api/v1/patients/-1" ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests PatientAPI
     *
     * @throws Exception
     *             if error occurs
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    @Transactional
    public void testPatientAPI () throws Exception {

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Antti" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setUsername( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        patient.setVaccinationStatus( VaccinationStatus.FIRST_DOSE.toString() );

        // Editing the patient before they exist should fail
        mvc.perform( put( "/api/v1/patients/antti" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isNotFound() );

        final Patient antti = new Patient( new UserForm( "antti", "123456", Role.ROLE_PATIENT, 1 ) );

        service.save( antti );

        // Creating a User should create the Patient record automatically
        mvc.perform( get( "/api/v1/patients/antti" ) ).andExpect( status().isOk() );

        // Should also now be able to edit existing record with new information
        mvc.perform( put( "/api/v1/patients/antti" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() );

        Patient anttiRetrieved = (Patient) service.findByName( "antti" );

        // Test a few fields to be reasonably confident things are working
        Assertions.assertEquals( "Walhelm", anttiRetrieved.getLastName() );
        Assertions.assertEquals( Gender.Male, anttiRetrieved.getGender() );
        Assertions.assertEquals( "Viipuri", anttiRetrieved.getCity() );

        // Also test a field we haven't set yet
        Assertions.assertNull( anttiRetrieved.getPreferredName() );

        patient.setPreferredName( "Antti Walhelm" );

        mvc.perform( put( "/api/v1/patients/antti" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() );

        anttiRetrieved = (Patient) service.findByName( "antti" );

        Assertions.assertNotNull( anttiRetrieved.getPreferredName() );

        // Editing with the wrong username should fail.
        mvc.perform( put( "/api/v1/patients/badusername" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().is4xxClientError() );

    }

    /**
     * Test accessing the patient PUT request unauthenticated
     *
     * @throws Exception
     *             if error occurs
     */
    @Test
    @Transactional
    @WithMockUser ( username = "user", roles = { "USER" } )
    public void testPatientUnauthenticated () throws Exception {
        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Antti" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setUsername( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        patient.setVaccinationStatus( VaccinationStatus.FIRST_DOSE.toString() );

        final Patient antti = new Patient( new UserForm( "antti", "123456", Role.ROLE_PATIENT, 1 ) );

        service.save( antti );

        mvc.perform( put( "/api/v1/patients/antti" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isUnauthorized() );
    }

    /**
     * Test accessing the patient PUT request as a patient
     *
     * @throws Exception
     *             if error occurs
     */
    @Test
    @Transactional
    @WithMockUser ( username = "antti", roles = { "PATIENT" } )
    public void testPatientAsPatient () throws Exception {
        final Patient antti = new Patient( new UserForm( "antti", "123456", Role.ROLE_PATIENT, 1 ) );

        service.save( antti );

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Antti" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setUsername( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        patient.setVaccinationStatus( VaccinationStatus.FULLY_VACCINATED.toString() );

        // a patient can edit their own info
        mvc.perform( put( "/api/v1/patients/antti" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() );

        final Patient anttiRetrieved = (Patient) service.findByName( "antti" );

        // Test a few fields to be reasonably confident things are working
        Assertions.assertEquals( "Walhelm", anttiRetrieved.getLastName() );
        Assertions.assertEquals( Gender.Male, anttiRetrieved.getGender() );
        Assertions.assertEquals( "Viipuri", anttiRetrieved.getCity() );

        // Also test a field we haven't set yet
        Assertions.assertNull( anttiRetrieved.getPreferredName() );

        // but they can't edit someone else's
        patient.setUsername( "patient" );
        mvc.perform( put( "/api/v1/patients/patient" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isUnauthorized() );

        // we should be able to update our record too
        mvc.perform( get( "/api/v1/patient/" ) ).andExpect( status().isOk() );
    }

    /**
     * Test accessing the patient POST request as a patient
     *
     * @throws Exception
     *             if an error occurs
     */
    @Test
    @Transactional
    @WithMockUser ( username = "adminnn", roles = { "ADMIN" } )
    public void testPatientPermissionsPost () throws Exception {

        // Creates patient advocate and saves it to DB
        assertEquals( 0, service.count() );
        p2 = new PatientAdvocate( new UserForm( USER_2, PW, Role.ROLE_PATIENT_ADVOCATE, 1 ) );
        assertEquals( 0, p2.getPatients().size() );
        paService.save( p2 );
        // Makes sure that it saved PA
        assertEquals( 1, paService.count() );
        assertEquals( 0, p2.getPatients().size() );

        // creats anotehr pa
        p3 = new PatientAdvocate( new UserForm( USER_3, PW, Role.ROLE_PATIENT_ADVOCATE, 1 ) );
        assertEquals( 0, p3.getPatients().size() );
        paService.save( p3 );

        assertEquals( 2, paService.count() );

        // Creates Patient and saves it to DB
        assertEquals( 0, service.count() );
        p1 = new Patient( new UserForm( USER_1, PW, Role.ROLE_PATIENT, 1 ) );
        assertEquals( 0, p1.getPermissions().size() );
        service.save( p1 );
        assertEquals( 1, service.count() );
        assertEquals( 0, p1.getPermissions().size() );

        // Gets them again
        final PatientAdvocate patientAdvocateTest = (PatientAdvocate) paService.findByName( USER_2 );
        final Patient patientTest = (Patient) service.findByName( USER_1 );

        assertEquals( 0, patientTest.getPermissions().size() );
        assertEquals( 0, patientAdvocateTest.getPatients().size() );

        mvc.perform( post( "/api/v1/patients/demoTestUser1/permissions/demoTestUser2" ).with( csrf() ) )
                .andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/patients/demoTestUser1/permissions/demoTestUser3" ).with( csrf() ) )
                .andExpect( status().isOk() );

        // Gets them again
        final PatientAdvocate pa = (PatientAdvocate) paService.findByName( USER_2 );
        final Patient p = (Patient) service.findByName( USER_1 );

        assertEquals( 2, p.getPermissions().size() );
        // Makes sure that the permission is correct in the Patient Database
        assertEquals( USER_2, p.getPermissions().get( 0 ).getAdvocate() );
        assertEquals( USER_1, p.getPermissions().get( 0 ).getPatient() );
        assertEquals( true, p.getPermissions().get( 0 ).isBilling() );
        assertEquals( true, p.getPermissions().get( 0 ).isOfficeVis() );
        assertEquals( true, p.getPermissions().get( 0 ).isPrescript() );

        assertEquals( 1, pa.getPatients().size() );
        assertEquals( USER_1, pa.getPatients().get( 0 ) );

    }

    /**
     * Test accessing the patient GET request as a patient
     *
     * @throws Exception
     *             if error occurs
     */
    @Test
    @Transactional
    @WithMockUser ( username = "adminnn", roles = { "ADMIN" } )
    public void testPatientPermissionsGet () throws Exception {

        // Creates patient advocate and saves it to DB
        assertEquals( 0, service.count() );
        p2 = new PatientAdvocate( new UserForm( USER_2, PW, Role.ROLE_PATIENT_ADVOCATE, 1 ) );
        paService.save( p2 );
        assertEquals( 1, paService.count() );
        assertEquals( 0, p2.getPatients().size() );

        // Creates Patient and saves it to DB
        assertEquals( 0, service.count() );
        p1 = new Patient( new UserForm( USER_1, PW, Role.ROLE_PATIENT, 1 ) );
        service.save( p1 );
        assertEquals( 1, service.count() );
        assertEquals( 0, p1.getPermissions().size() );

        mvc.perform( post( "/api/v1/patients/demoTestUser1/permissions/demoTestUser2" ).with( csrf() ) )
                .andExpect( status().isOk() );

        final String permission = mvc
                .perform( get( "/api/v1/patients/demoTestUser1/permissions/demoTestUser2" ).with( csrf() ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( permission.contains( USER_1 ) );
        assertTrue( permission.contains( USER_2 ) );

    }

    /**
     * Test accessing the patient DELETE request as a patient
     *
     * @throws Exception
     *             if error occurs
     */
    @Test
    @Transactional
    @WithMockUser ( username = "adminnn", roles = { "ADMIN" } )
    public void testPatientPermissionsDelete () throws Exception {

        // Creates patient advocate and saves it to DB
        assertEquals( 0, service.count() );
        p2 = new PatientAdvocate( new UserForm( USER_2, PW, Role.ROLE_PATIENT_ADVOCATE, 1 ) );
        paService.save( p2 );
        assertEquals( 1, paService.count() );
        assertEquals( 0, p2.getPatients().size() );

        // Creates Patient and saves it to DB
        assertEquals( 0, service.count() );
        p1 = new Patient( new UserForm( USER_1, PW, Role.ROLE_PATIENT, 1 ) );
        service.save( p1 );
        assertEquals( 1, service.count() );
        assertEquals( 0, p1.getPermissions().size() );

        // Creates permission and associates patient and advocate
        mvc.perform( post( "/api/v1/patients/demoTestUser1/permissions/demoTestUser2" ).with( csrf() ) )
                .andExpect( status().isOk() );

        // Deletes the permission and removes the association between the
        // patient and advocate
        mvc.perform( delete( "/api/v1/patients/demoTestUser1/permissions/demoTestUser2" ).with( csrf() ) )
                .andExpect( status().isOk() );

        // Gets them again
        final PatientAdvocate pa = (PatientAdvocate) paService.findByName( USER_2 );
        final Patient p = (Patient) service.findByName( USER_1 );

        // Checks that the permission is deleted
        assertEquals( 0, p.getPermissions().size() );
        // Checks that the patient and advocate are not associated anymore
        assertEquals( 0, pa.getPatients().size() );
    }

    /**
     * Test accessing the patient PUT request as a patient
     *
     * @throws Exception
     *             if error occurs
     */
    @Test
    @Transactional
    @WithMockUser ( username = "adminnn", roles = { "ADMIN" } )
    public void testPatientPermissionsPut () throws Exception {
        // Creates patient advocate and saves it to DB
        assertEquals( 0, service.count() );
        p2 = new PatientAdvocate( new UserForm( USER_2, PW, Role.ROLE_PATIENT_ADVOCATE, 1 ) );
        paService.save( p2 );
        assertEquals( 1, paService.count() );
        assertEquals( 0, p2.getPatients().size() );

        // Creates Patient and saves it to DB
        assertEquals( 0, service.count() );
        p1 = new Patient( new UserForm( USER_1, PW, Role.ROLE_PATIENT, 1 ) );
        service.save( p1 );
        assertEquals( 1, service.count() );
        assertEquals( 0, p1.getPermissions().size() );

        // Adds permission
        mvc.perform( post( "/api/v1/patients/demoTestUser1/permissions/demoTestUser2" ).with( csrf() ) )
                .andExpect( status().isOk() );

        // Checks that post works correctly
        final PatientAdvocate pa = (PatientAdvocate) paService.findByName( USER_2 );
        final Patient p = (Patient) service.findByName( USER_1 );
        assertEquals( 1, p.getPermissions().size() );
        assertEquals( USER_2, p.getPermissions().get( 0 ).getAdvocate() );
        assertEquals( USER_1, p.getPermissions().get( 0 ).getPatient() );
        assertEquals( true, p.getPermissions().get( 0 ).isBilling() );
        assertEquals( true, p.getPermissions().get( 0 ).isOfficeVis() );
        assertEquals( true, p.getPermissions().get( 0 ).isPrescript() );
        assertEquals( 1, pa.getPatients().size() );
        assertEquals( USER_1, pa.getPatients().get( 0 ) );

        // Checks that the permission was added correctly
        final String permission = mvc
                .perform( get( "/api/v1/patients/demoTestUser1/permissions/demoTestUser2" ).with( csrf() ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( permission.contains( USER_1 ) );
        assertTrue( permission.contains( USER_2 ) );
        final PatientAdvocate paa = (PatientAdvocate) paService.findByName( USER_2 );
        final Patient pp = (Patient) service.findByName( USER_1 );
        assertEquals( 1, pp.getPermissions().size() );
        assertEquals( USER_2, pp.getPermissions().get( 0 ).getAdvocate() );
        assertEquals( USER_1, pp.getPermissions().get( 0 ).getPatient() );
        assertEquals( true, pp.getPermissions().get( 0 ).isBilling() );
        assertEquals( true, pp.getPermissions().get( 0 ).isOfficeVis() );
        assertEquals( true, pp.getPermissions().get( 0 ).isPrescript() );
        assertEquals( 1, paa.getPatients().size() );
        assertEquals( USER_1, paa.getPatients().get( 0 ) );

        // Creates new permission and updates it
        final Permission updatedPermission = new Permission( USER_1, USER_2, false, false, false );
        assertEquals( USER_1, updatedPermission.getPatient() );
        assertEquals( USER_2, updatedPermission.getAdvocate() );
        mvc.perform( put( "/api/v1/patients/demoTestUser1/permissions/demoTestUser2" ).with( csrf() )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( updatedPermission ) ) )
                .andExpect( status().isOk() );

        // Checks that the permission was updated correctly

        final PatientAdvocate paaa = (PatientAdvocate) paService.findByName( USER_2 );
        final Patient ppp = (Patient) service.findByName( USER_1 );
        assertEquals( 1, ppp.getPermissions().size() );
        assertEquals( USER_2, ppp.getPermissions().get( 0 ).getAdvocate() );
        assertEquals( USER_1, ppp.getPermissions().get( 0 ).getPatient() );
        assertEquals( false, ppp.getPermissions().get( 0 ).isBilling() );
        assertEquals( false, ppp.getPermissions().get( 0 ).isOfficeVis() );
        assertEquals( false, ppp.getPermissions().get( 0 ).isPrescript() );
        assertEquals( 1, paaa.getPatients().size() );
        assertEquals( USER_1, paaa.getPatients().get( 0 ) );

    }

}
