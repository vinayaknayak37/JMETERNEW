package ca.cpggpc.est2_0.desktop.perftest;

import ca.cpggpc.est2_0.desktop.model.BDTInitResult;
import cpdt.domain.common.WorkgroupId;
import cpdt.domain.job.Job;
import cpdt.domain.shipping.ShippingConst;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BDTInitLoginTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "bdtInitLoginTest";
    private static Logger logger = LoggerFactory.getLogger(BDTInitLoginTest.class);
    private static String USERNAME_PARAM = "username";
    private static String USER_PWD_PARAM = "pwd";

    protected String username = USERNAME_PARAM;
    protected String password = USER_PWD_PARAM;

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(USERNAME_PARAM,"username");
        defaultParameters.addArgument(USER_PWD_PARAM,"password");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        // We do *not* perform super.init() because we are not logged in yet.
        // In the runTest() method below, we will perform the login and initialization.
        //super.setupTest(javaSamplerContext);
        if (javaSamplerContext != null) {
            runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
            // Retrieve user name and password from parameter list passed in from JMeter. !!! Note that the parameter keys are case-sensitive!!!
            username = javaSamplerContext.getParameter(USERNAME_PARAM);
            password = javaSamplerContext.getParameter(USER_PWD_PARAM);
            logger.info ("... Retrieved from parameter list - runNumber: " + runNumber + " username: " + username + " pwd: " + password);
        }
    }

    public void teardownTest(JavaSamplerContext context) {
        logger.debug(this.getClass().getName() + ": teardownTest");
    }

    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));

        // After Initialization completed successfully, we want to perform a BDT Login
        // with said user.
        logger.info("BDT Init Login Test - with runNumber: " + runNumber + " user: " + username);

        sampleResult.sampleStart();

        try {
            // loginToBDT(username, password);
            deliveryRequestTest.executeLogin(username, password);
            logger.info("*** Test step BDTProcessLogin completed successfully. ***");
            // logger.info("*** Test %d %s took %d milliseconds ***" % (tests["BDTProcessLogin"].getNumber(), tests["BDTProcessLogin"].getDescription(), grinder.statistics.forLastTest.time));

            // Get the currently bound default workgroup. If a user has never logged on a
            // new workgroup will need to be created or one selected
            WorkgroupId workgroup = deliveryRequestTest.getDefaultWorkgroup();
            logger.info("Call to getDefaultWorkGroup() returned: " + workgroup);

            if (workgroup == null) {
                logger.info("Call to getDefaultWorkGroup() returned None; calling setDefaultWorkgroup()");
                deliveryRequestTest.setDefaultWorkgroup();
                workgroup = deliveryRequestTest.getDefaultWorkgroup();
            }

            // Workgroup exclusion pattern
            String exclusionPattern = "^EXT_.*|^CPO_.*|^BDT_.*";

            //  Check to see if the currently selected workgroup ID is a PWS/TPO workgroup
            if (workgroup != null && workgroup.toString().matches(exclusionPattern)) {
                logger.info("Default workgroup is bound to unwanted ID - switching...");

                // Get a list of workgroups - NON PWS/TPO
                // Provides a list of workgroup names using EXCLUSION regex criteria
                List<String> filteredWorkgroups = deliveryRequestTest.getWorkgroupsWithCriteria(exclusionPattern);
                for (String wg : filteredWorkgroups) {
                    logger.info("Found candidate workgroups: " + wg);
                }

                if (filteredWorkgroups.size() > 0) {
                    // Auto-select the first in the list
                    deliveryRequestTest.setDefaultWorkgroup(filteredWorkgroups.get(0));
                    logger.info("Set default workgroup to " + filteredWorkgroups.get(0));
                } else {
                    // Create the default workgroup - should never happen unless using a user that is
                    // an exclusive user of PWS/TPO
                    logger.info("No compatible workgroup found - setting default \"grinder_testing\" workgroup");
                    deliveryRequestTest.setDefaultWorkgroup("grinder_testing");
                }
            } else {
                // TODO - Add the following to a method for reuse
                // Create the default workgroup - should never happen unless using a user that is
                // an exclusive user of PWS/TPO
                logger.info("username: " + username + "  No compatible workgroup found - setting default \"grinder_testing\" workgroup");
                deliveryRequestTest.setDefaultWorkgroup("grinder_testing");
            }

            logger.info("Default workgroup is set to: " + deliveryRequestTest.getDefaultWorkgroup());

            // Sets the contractId variable to SHIPPING category
            String contractId = deliveryRequestTest.getDefaultContract();

            // Print useful log information about the mailed by customer, behalf of customer and contract
            logger.info("Mailed By customer is set to: " + deliveryRequestTest.getActiveCustomer().toString());
            logger.info("Behalf Of customer is set to: " + deliveryRequestTest.getMoboCustomer().toString());
            logger.info("Contract number is set to: " + deliveryRequestTest.getDefaultContract());

            //javaSamplerContext.getJMeterContext().getSamplerContext().put("workgroup", workgroup);
            //javaSamplerContext.getJMeterContext().getSamplerContext().put("contractId", contractId);

            // Get the currently bound default workgroup. If a user has never logged on a
            // new workgroup will need to be created or one selected
            workgroup = deliveryRequestTest.getDefaultWorkgroup();
            logger.info("Call to getDefaultWorkGroup() returned: " + workgroup);

            // Sets the contractId variable to SHIPPING category
            //self.contractId = self.deliveryRequestTest.getDeliveryRequestsByJob();
            contractId = deliveryRequestTest.getDefaultContract();
            logger.info("Call to getDefaultContract() returned: " + contractId);

            // Establish a job to be used for "pick-pack" shipping testing
            Job shippingJob = deliveryRequestTest.getJob(BDT_JOB_SHIPPING_TEST_NAME);
            logger.info("Shipping job name: " + shippingJob.getName());

            //  Establish a job to be used to test movement of shipments between jobs
            Job moveDRJob = deliveryRequestTest.getJob(BDT_JOB_MOVING_TEST_NAME);
            logger.info("Move testing Job name: " + moveDRJob.getName());

            // Set the operating mode to REMOTE for remote/online testing
            deliveryRequestTest.setRemoteOperatingMode();
            logger.info("Client is operating in REMOTE mode.");

            // Create the directory to be used for history exports
            // Refactor this to use a pythonic way of getting a temp dir.
            // self.historyExportDir = "/tmp/historyExport-" + username
            // self.historyExportDir = tempfile.gettempdir() + username
            // self.log("Create the directory to be used for history exports: %s" % self.historyExportDir)
            deliveryRequestTest.initPostLogin();

            // Set up client state category
            deliveryRequestTest.setupClientStateCategory(ShippingConst.SHIPPING_CATEGORY_GROUP);

            BDTInitResult bdtInitResult = new BDTInitResult(workgroup, contractId, shippingJob, moveDRJob);
            logger.info(bdtInitResult.toString());
            sampleResult.setResponseData(bdtInitResult.toJsonString(), UTF_8.name());
            sampleResult.setResponseOK();       // This is equivalent to setResponseMessageOK(), setResponseCodeOK(), and setSuccessful(true);
        } catch(Exception ex) {
            errorHandling(ex, BDT_TEST_NAME);
            sampleResult.setResponseData("{}", UTF_8.name());
            sampleResult.setResponseMessage(ex.getMessage());
            sampleResult.setResponseCode(BAD_REQUEST_400);
            sampleResult.setSuccessful(false);
        }

        sampleResult.sampleEnd();
        return sampleResult;
    }
}
