package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.job.Job;
import cpdt.domain.recon.InductionSetHeader;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.google.common.base.Charsets.UTF_8;

public class ImportSaveDeliveryRequestsFromXMLTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ImportSaveDeliveryRequestsFromXMLTest";
    private static final String STATUS_TAG = "arg1";
    private static Logger logger = LoggerFactory.getLogger(ImportSaveDeliveryRequestsFromXMLTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(SHIPPING_JOB_TAG, "<shippingJob>");
        defaultParameters.addArgument(CONTRACT_ID_TAG, "<contractId>");
        defaultParameters.addArgument(FILE_PATH_TAG, "<fileName>");
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
        Collection<InductionSetHeader> indSetHeaders = null;
        int numImportSaved = 0;

        Gson gson = new Gson();
        String shippingJobJson = javaSamplerContext.getParameter(SHIPPING_JOB_TAG, "0");
        Job shippingJob = gson.fromJson(shippingJobJson, Job.class);

        String contractId = javaSamplerContext.getParameter(CONTRACT_ID_TAG, "UndefinedContractId");
        String fileName = javaSamplerContext.getParameter(FILE_PATH_TAG, DELIVERY_REQ_IMPORT_40);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();
        try{
            // Delivery request import save
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestImportSave"].record(self.deliveryRequestTest, InstrumentationFilters("importSaveDeliveryRequestsFromXML"))
            numImportSaved = deliveryRequestTest.importSaveDeliveryRequestsFromXML(fileName, shippingJob, contractId);
            logger.info(" - Delivery request import/saved " + numImportSaved + " from " + fileName);
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step DeliveryRequestImportSave completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestImportSave"].getNumber(), tests["DeliveryRequestImportSave"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"numImportSaved\" : " + numImportSaved + "}", UTF_8.name());
        }

        sampleResult.setResponseMessageOK();
        sampleResult.setSuccessful(success);
        return sampleResult;

    }

}
