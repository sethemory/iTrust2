package edu.ncsu.csc.iTrust2.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

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
import edu.ncsu.csc.iTrust2.forms.PatientAdvocateForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.PatientAdvocateService;
import edu.ncsu.csc.iTrust2.services.PatientService;

/**
 * Test for API functionality for interacting with patient advocates
 *
 *
 * @author johngobran
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles ( { "test" } )
public class APIPatientAdvocateTest {

    /**
     * Used for calls
     */
    @Autowired
    private MockMvc                                 mvc;

    /**
     * Service to save patient advocates
     */
    @Autowired
    private PatientAdvocateService<PatientAdvocate> service;

    /**
     * Service to save patients
     */
    @Autowired
    private PatientService<Patient>                 pService;

    /**
     * Patient used for testing
     */
    Patient                                         p1;

    /**
     * Patient advocate used for testing
     */
    PatientAdvocate                                 p2;

    /**
     * User name 1 used for testing
     */
    private static final String                     USER_1 = "demoTestUser1";

    /**
     * User name 2 for testing
     */
    private static final String                     USER_2 = "demoTestUser2";

    /**
     * Password used for testing
     */
    private static final String                     PW     = "123456";

    /**
     * Sets up test
     */
    @BeforeEach
    public void setup () {

        service.deleteAll();
        pService.deleteAll();
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
    @WithMockUser ( username = "Jmgobran", roles = { "ADMIN" } )
    public void testGetNonExistentPatient () throws Exception {
        final PatientAdvocateForm patientAdvocate = new PatientAdvocateForm();
        patientAdvocate.setFirstName( "John" );
        patientAdvocate.setLastName( "Gobran" );
        patientAdvocate.setPreferredName( "jo" );
        patientAdvocate.setUsername( "Jmgobran" );
        patientAdvocate.setAddress1( "1 test street" );
        patientAdvocate.setEmail( "jgsts@itrust.fi" );
        patientAdvocate.setPhone( "123-456-7790" );

        final PatientAdvocate johnnn = new PatientAdvocate(
                new UserForm( "Jmgobran", "123456", Role.ROLE_PATIENT_ADVOCATE, 1 ) );

        service.save( johnnn );
        mvc.perform( get( "/api/v1/patientadvocate/-12" ) ).andExpect( status().isNotFound() );

    }

    /**
     * Tests that getting a patient advocate works correctly
     *
     * @throws Exception
     *             if the patient advocate does not exist
     */
    @Test
    @WithMockUser ( username = "Jmgobran", roles = { "ADMIN" } )
    @Transactional
    public void testGetPatientAdvocates () throws Exception {
        final PatientAdvocateForm patientAdvocate = new PatientAdvocateForm();
        patientAdvocate.setFirstName( "John" );
        patientAdvocate.setLastName( "Gobran" );
        patientAdvocate.setPreferredName( "jo" );
        patientAdvocate.setUsername( "Jmgobran" );
        patientAdvocate.setAddress1( "1 test street" );
        patientAdvocate.setEmail( "jgsts@itrust.fi" );
        patientAdvocate.setPhone( "123-456-7790" );

        final PatientAdvocate johnnn = new PatientAdvocate(
                new UserForm( "Jmgobran", "123456", Role.ROLE_PATIENT_ADVOCATE, 11 ) );

        service.save( johnnn );
        assertEquals( 1, service.count() );

        // Creating a User should create the Patient record automatically
        mvc.perform( get( "/api/v1/patientadvocate/Jmgobran" ) ).andExpect( status().isOk() );

    }

    /**
     * Tests that creating and deleting patient advocates work correctly
     *
     * @throws Exception
     *             if patient advocate is already added
     */
    @Test
    @WithMockUser ( username = "Jmgobran", roles = { "ADMIN" } )
    @Transactional
    public void testCreateDeletePatientAdvocates () throws Exception {
        assertEquals( 0, service.count() );

        final PatientAdvocateForm patientAdvocateForm = new PatientAdvocateForm();
        patientAdvocateForm.setFirstName( "John" );
        patientAdvocateForm.setLastName( "Gobran" );
        patientAdvocateForm.setPreferredName( "jo" );
        patientAdvocateForm.setUsername( "Jmgobran" );
        patientAdvocateForm.setAddress1( "1 test street" );
        patientAdvocateForm.setEmail( "jgsts@itrust.fi" );
        patientAdvocateForm.setPhone( "123-456-7790" );

        mvc.perform( post( "/api/v1/patientadvocate" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patientAdvocateForm ) ) ).andExpect( status().isOk() );

        final PatientAdvocateForm patientAdvocateFormm = new PatientAdvocateForm();
        patientAdvocateFormm.setFirstName( "John" );
        patientAdvocateFormm.setLastName( "Gobran" );
        patientAdvocateFormm.setPreferredName( "jo" );
        patientAdvocateFormm.setUsername( "maxest" );
        patientAdvocateFormm.setAddress1( "1 test street" );
        patientAdvocateFormm.setEmail( "jgsts@itrust.fi" );
        patientAdvocateFormm.setPhone( "123-456-7790" );

        mvc.perform( post( "/api/v1/patientadvocate" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patientAdvocateFormm ) ) ).andExpect( status().isOk() );

        assertEquals( 2, service.count() );
        mvc.perform( delete( "/api/v1/patientadvocate/afklhadkjgh" ).with( csrf() ) )
                .andExpect( status().isNotFound() );

        assertEquals( 2, service.count() );

        mvc.perform( delete( "/api/v1/patientadvocate/Jmgobran" ).with( csrf() ) ).andExpect( status().isOk() );
        assertEquals( 1, service.count() );
    }

    /**
     * Tests that creating and deleting patient advocates work correctly
     *
     * @throws Exception
     *             if patient advocate is already added
     */
    @Test
    @WithMockUser ( username = "Jmgobran", roles = { "ADMIN" } )
    @Transactional
    public void testAssociatePatientData () throws Exception {

        // Creates patient advocate and saves it to DB
        assertEquals( 0, service.count() );
        p2 = new PatientAdvocate( new UserForm( USER_2, PW, Role.ROLE_PATIENT_ADVOCATE, 1 ) );
        service.save( p2 );
        assertEquals( 1, service.count() );
        assertEquals( 0, p2.getPatients().size() );

        // Creates Patient and saves it to DB
        assertEquals( 0, pService.count() );
        p1 = new Patient( new UserForm( USER_1, PW, Role.ROLE_PATIENT, 1 ) );
        pService.save( p1 );
        assertEquals( 1, pService.count() );
        assertEquals( 0, p1.getPermissions().size() );

        // Calls endpoint to associate them
        // Adds permission inside patient
        // Adds patient inside patient advocate
        mvc.perform( post( "/api/v1/patientadvocate/demoTestUser2/patients/demoTestUser1" ).with( csrf() ) )
                .andExpect( status().isOk() );

        // Gets them again
        final PatientAdvocate pa = (PatientAdvocate) service.findByName( USER_2 );
        final Patient p = (Patient) pService.findByName( USER_1 );

        assertEquals( 1, p.getPermissions().size() );
        assertEquals( 1, pa.getPatients().size() );
    }
}
