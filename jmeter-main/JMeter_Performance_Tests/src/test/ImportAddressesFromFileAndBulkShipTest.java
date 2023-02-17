package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class ImportAddressesFromFileAndBulkShipTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ImportAddressesFromFileAndBulkShipTest";
    private static final String STATUS_TAG = "arg1";
    private static Logger logger = LoggerFactory.getLogger(ImportAddressesFromFileAndBulkShipTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(CONTRACT_ID_TAG, "<contractId>");
        defaultParameters.addArgument(SHIPPING_JOB_TAG, "<shippingJob>");
        defaultParameters.addArgument(FILE_PATH_TAG, "<filePath>");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        int numImported = 0;

        Gson gson = new Gson();
        String shippingJobJson = javaSamplerContext.getParameter(SHIPPING_JOB_TAG, "0");
        Job shippingJob = gson.fromJson(shippingJobJson, Job.class);

        String filePath = javaSamplerContext.getParameter(FILE_PATH_TAG, ADDR_BOOK_IMPORT_200);;
        String contractId = javaSamplerContext.getParameter(CONTRACT_ID_TAG, "UndefinedContractId");

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber + ", contractId: [" + contractId + "], shippingJob: [" + shippingJob.getId() + "].");

        sampleResult.sampleStart();

        try {
            // Address import into Address Group and bulk ship
            //grinder.statistics.delayReports = 1
            //tests["ImportAddressesFromFileAndBulkShip"].record(self.deliveryRequestTest, InstrumentationFilters("importAddressesFromFileAndBulkShip"))
            logger.info(" - Importing and bulk shipping addresses from " + filePath);
            numImported = deliveryRequestTest.importAddressesFromFileAndBulkShip(filePath, shippingJob, contractId);
            logger.info(" - Addresses imported/bulk shipped " + numImported + " from " + filePath);
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step ImportAddressesFromFileAndBulkShip completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["ImportAddressesFromFileAndBulkShip"].getNumber(), tests["ImportAddressesFromFileAndBulkShip"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"numImported\" : " + numImported + "}", UTF_8.name());
        }

        sampleResult.setResponseMessageOK();
        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}