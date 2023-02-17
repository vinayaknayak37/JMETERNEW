package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeliveryRequestImportPrintTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "deliveryRequestImportPrintTest";
    private static Logger logger = LoggerFactory.getLogger(DeliveryRequestImportPrintTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(CONTRACT_ID_TAG,"<contractId>");
        defaultParameters.addArgument(SHIPPING_JOB_TAG,"<shippingJob>");
        defaultParameters.addArgument(FILE_PATH_TAG,"<filePath>");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        boolean success = true;

        Gson gson = new Gson();
        String jsonString = javaSamplerContext.getParameter(SHIPPING_JOB_TAG, "UndefinedShippingJob");
        Job shippingJob = gson.fromJson(jsonString, Job.class);
        String contractId = javaSamplerContext.getParameter(CONTRACT_ID_TAG, "UndefinedContracId");
        String fileName = javaSamplerContext.getParameter(FILE_PATH_TAG, DELIVERY_REQ_IMPORT_40);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        logger.info("Import Print Delivery Requests from XML Test in to BDT application with runNumber: [" + runNumber + "]");
        logger.info("     shippingJob: [" + shippingJob + "] contractId: [" + contractId + "] fileName: [" + fileName + "].");

        // ... do some test
        result.sampleStart();
        try {
            // Delivery request import print
            int numImported = deliveryRequestTest.importPrintDeliveryRequestsFromXML(fileName, shippingJob, contractId);
            logger.info("Delivery request import/printed " + numImported + " from: " + fileName);
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step DeliveryRequestImportPrint completed successfully. ***");
            //logger.info("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestImportPrint"].getNumber(), tests["DeliveryRequestImportPrint"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        }

        result.sampleEnd();
        result.setSuccessful(success);
        return result;
    }
}
