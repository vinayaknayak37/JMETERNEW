package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.common.DeliveryRequestId;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.dr.pricing.PriceQuote;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class ImportPrintDeliveryRequestsFromXMLTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ImportPrintDeliveryRequestsFromXMLTest";

    private static Logger logger = LoggerFactory.getLogger(ImportPrintDeliveryRequestsFromXMLTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(CONTRACT_ID_TAG, "<contractId>");
        defaultParameters.addArgument(FILE_PATH_TAG, "<filePath>");
        defaultParameters.addArgument(JOB_TAG, "<job>");
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

        // !!!NOTE!!! DeliveryRequest JSON is passed in from the previous JMeter test
        String contractId = javaSamplerContext.getParameter(CONTRACT_ID_TAG, "UndefinedContractId");
        String filePath = javaSamplerContext.getParameter(FILE_PATH_TAG, "UndefinedFilePath");
        String jsonJob = javaSamplerContext.getParameter(JOB_TAG, "{}");

        Job job = new Gson().fromJson(jsonJob, Job.class);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // After Initialization completed successfully, we want to perform a BDT Login
        // with said user.
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "], contractId: [" + contractId + "], job: [" + job.getId() + "]");

        sampleResult.sampleStart();
        // ... do some test
        try {
            // Delivery request import print
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestImportPrint"].record(self.deliveryRequestTest, InstrumentationFilters("importPrintDeliveryRequestsFromXML"))
            numImported = deliveryRequestTest.importPrintDeliveryRequestsFromXML(filePath, job, contractId);
            logger.info(" - Delivery request import/printed [" + numImported + "] from:  [" + filePath + "].");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step DeliveryRequestImportPrint completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestImportPrint"].getNumber(), tests["DeliveryRequestImportPrint"].getDescription(), grinder.statistics.forLastTest.time))
            sampleResult.setResponseOK();
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"numImported\" : " + numImported + "}", UTF_8.name());
            sampleResult.setSuccessful(success);
        }

        return sampleResult;
    }
}
