package ca.cpggpc.est2_0.desktop.perftest;

import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.job.Job;
import cpdt.test.wrapper.communication.CreateAddressedMailingPlanTest;
import cpdt.test.wrapper.communication.CreateUnAddressedMailingPlanTest;
import cpdt.test.wrapper.shipping.CreateShippingDeliveryRequestTest;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractESTDesktopSamplerClient extends AbstractJavaSamplerClient {
    public static final String METHOD_TAG = "method";
    public static final String RUN_NUMBER_TAG = "runNumber";
    public static String FILE_PATH_TAG = "filePath";
    public static String JOB_TAG = "job";
    public static String CONTRACT_ID_TAG = "contractId";
    public static String MOVE_DR_JOB_TAG = "moveDRJob";
    public static String SHIPPING_JOB_TAG = "shippingJob";
    public static String WORKGRROUP_TAG = "workgroup";
    public static final String ARG1_TAG = "arg1";
    public static final String ARG2_TAG = "arg2";
    public static final String BDT_JOB_SHIPPING_TEST_NAME = "GrinderShippingTesting";
    public static final String BDT_JOB_MOVING_TEST_NAME = "Grinder-Move-Test";
    public static final String BAD_REQUEST_400 = "400 Bad Request Error";
    public static final String NOT_IMPLEMENTED_501 = "500 Not Implementd";

    // List of all the required files used in this script
    public static String AA_SERP_MAILING_PLAN_MEDIUM = "/jmeter/test_data/projects/EST_Desktop_Scripts/resources/cpdt/test/import/serp/aa/AA_Medium.xml";
    public static String AA_SERP_MAILING_PLAN_XLARGE = "/jmeter/test_data/projects/EST_Desktop_Scripts/resources/cpdt/test/import/serp/aa/AA_XLarge.xml";
    public static String DELIVERY_REQ_IMPORT_40 = "/jmeter/test_data/projects/EST_Desktop_Scripts/resources/cpdt/test/import/deliveryRequestImport40.xml";
    public static String ADDR_BOOK_IMPORT_200 = "/jmeter/test_data/projects/EST_Desktop_Scripts/resources/cpdt/test/import/addresses/addressBookImport200.txt";
    public static String UA_FF_SMALL = "/jmeter/test_data/projects/EST_Desktop_Scripts/resources/cpdt/test/import/ua/UAFF_Ont_Format4_Small.txt";
    public static String AA_MACH_LARGE = "/jmeter/test_data/projects/EST_Desktop_Scripts/resources/cpdt/test/import/AA/EST_140227144212_mech.txt";

    protected static CreateShippingDeliveryRequestTest deliveryRequestTest;
    protected static CreateAddressedMailingPlanTest aaMailingTest;
    protected static CreateUnAddressedMailingPlanTest uaMailingTest;

    private static Logger logger = LoggerFactory.getLogger(AbstractESTDesktopSamplerClient.class);

    protected int runNumber = 0;

    static {
        try {
            deliveryRequestTest = new CreateShippingDeliveryRequestTest();
            aaMailingTest = new CreateAddressedMailingPlanTest();
            uaMailingTest = new CreateUnAddressedMailingPlanTest();
            logger.info("*** Test step BDTEnvironmentInit completed successfully. ***");
            logger.info("Using BDT build version: " + deliveryRequestTest.getClientAPIBuildVersion());
        } catch (Exception e) {
            logger.error("Exception Type: %s Exception value:%s Exception Traceback: " + e.toString());
        }
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(RUN_NUMBER_TAG,"0");
        return defaultParameters;
    }

    public void setupTest(JavaSamplerContext javaSamplerContext) {
        /*
        if (javaSamplerContext != null) {
            Iterator<String> it = javaSamplerContext.getParameterNamesIterator();
            logger.info("SamplerContext parameters:  ");
            while (it.hasNext()) {
                String paramKey = it.next();
                logger.info(" - " + paramKey + " = " + javaSamplerContext.getParameter(paramKey));
            }
            runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
            logger.info("Run number: " + runNumber);
        }
        */

        /*
        // Get the currently bound default workgroup. If a user has never logged on a
        // new workgroup will need to be created or one selected
        workgroup = deliveryRequestTest.getDefaultWorkgroup();
        logger.info("Call to getDefaultWorkGroup() returned: " + workgroup);

        // Sets the contractId variable to SHIPPING category
        contractId = deliveryRequestTest.getDefaultContract();
        logger.info("Call to getDefaultContract() returned: " + contractId);

        // Establish a job to be used for "pick-pack" shipping testing
        shippingJob = deliveryRequestTest.getJob(BDT_JOB_SHIPPING_TEST_NAME);
        logger.info("Shipping job name: " + shippingJob.getName());

        //  Establish a job to be used to test movement of shipments between jobs
        moveDRJob = deliveryRequestTest.getJob(BDT_JOB_MOVING_TEST_NAME);
        logger.info("Move testing Job name: " + moveDRJob.getName());
        */
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        return null;
    }

    protected void errorHandling(Exception exception, String testName) {
        //logger.error("*** Test %s %s failed! ***" % (testNumber, testDescription));
        //logger.error("Agent::" + str(grinder.agentNumber) + " - Function %s received a Java Exception - continuing test." % function);
        logger.error("Test name: " + testName + ", exception value: " + exception.toString());
    }

    protected DeliveryRequest xxcreateSingleDeliveryRequest(Job job, String contractId, SampleResult parentSampleResult) {
        DeliveryRequest dr = null;
        SampleResult result = new SampleResult();
        result.sampleStart();
        try {
            // Create remote delivery request TEST
            //grinder.statistics.delayReports = 1
            //tests["CreateRemoteDeliveryRequest"].record(self.deliveryRequestTest, InstrumentationFilters("createRemoteDeliveryRequest"))
            logger.info("XXX Create Remote Delivery Request for job: " + job.getName() + " contractId: " + contractId);
            dr = deliveryRequestTest.createRemoteDeliveryRequest(job, contractId);
            logger.info(" - Created DR: " + dr.getId().toString());
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step CreateRemoteDeliveryRequest completed successfully. ***");
            //logger.info("*** Test %d %s took %d milliseconds ***" % (tests["CreateRemoteDeliveryRequest"].getNumber(), tests["CreateRemoteDeliveryRequest"].getDescription(), grinder.statistics.forLastTest.time))

            result.setResponseMessageOK();
            //result.setResponseData(new RemoteDeliveryResult(workgroup.toString(), contractId, job, moveDRJob, dr).toString(), UTF_8.name());
            result.setSuccessful(true);
        }
        catch (Exception ex) {
            errorHandling(ex, "CreateRemoteDeliveryRequest");
            result.setSuccessful(false);
        }
        // We leave it up to the parent sample result to stop
        //  result.sampleEnd();
        parentSampleResult.addSubResult(result);
        return dr;
    }

    protected DeliveryRequest xxprintSingleDeliveryRequest(DeliveryRequest dr, SampleResult parentSampleResult) {
        DeliveryRequest printedDR = null;
        SampleResult result = new SampleResult();
        result.sampleStart();
        try {
            //Print remote delivery request TEST
            //grinder.statistics.delayReports = 1
            //tests["PrintRemoteDeliveryRequest"].record(self.deliveryRequestTest, InstrumentationFilters("printRemoteDeliveryRequest"))
            printedDR = deliveryRequestTest.printRemoteDeliveryRequest(dr);
            logger.info(" - Printed DR: " + printedDR.getId().toString());
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step PrintRemoteDeliveryRequest completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["PrintRemoteDeliveryRequest"].getNumber(), tests["PrintRemoteDeliveryRequest"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            errorHandling(ex, "PrintRemoteDeliveryRequest");
        }
        result.sampleEnd();
        result.setResponseMessageOK();
        //result.setResponseData(new RemoteDeliveryResult(workgroup.toString(), contractId, shippingJob, moveDRJob, printedDR).toString(), UTF_8.name());
        result.setSuccessful(true);
        parentSampleResult.addSubResult(result);
        return printedDR;
    }
}
