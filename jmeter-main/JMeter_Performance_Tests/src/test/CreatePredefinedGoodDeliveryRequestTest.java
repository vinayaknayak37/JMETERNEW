package ca.cpggpc.est2_0.desktop.perftest;

import ca.cpggpc.est2_0.desktop.model.RemoteDeliveryResult;
import com.google.gson.Gson;
import cpdt.domain.common.PredefinedGood;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class CreatePredefinedGoodDeliveryRequestTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "CreatePredefinedGoodDeliveryRequestTest";
    private static String PR_GOOD_TAG = "prGood";
    private static Logger logger = LoggerFactory.getLogger(CreatePredefinedGoodDeliveryRequestTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(CONTRACT_ID_TAG,"<contractId>");
        defaultParameters.addArgument(SHIPPING_JOB_TAG,"<shippingJob>");
        defaultParameters.addArgument(MOVE_DR_JOB_TAG,"<moveDRJob>");
        defaultParameters.addArgument(PR_GOOD_TAG, "<prGood>");
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
        DeliveryRequest predefinedGoodDr = null;
        StringBuilder strBuilder = new StringBuilder();
        Gson gson = new Gson();

        String jsonString = javaSamplerContext.getParameter(SHIPPING_JOB_TAG, "UndefinedShippingJob");
        Job shippingJob = gson.fromJson(jsonString, Job.class);
        jsonString = javaSamplerContext.getParameter(MOVE_DR_JOB_TAG, "UndefinedMoveDRJob");
        Job moveDRJob = gson.fromJson(jsonString, Job.class);
        jsonString = javaSamplerContext.getParameter(PR_GOOD_TAG, "UndefinedPRGood");
        PredefinedGood predefinedGood = gson.fromJson(jsonString, PredefinedGood.class);

        String contractId = javaSamplerContext.getParameter(CONTRACT_ID_TAG, "UndefinedContractId");

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        logger.info("Create Predefined Good Delivery Request Test in to BDT application with runNumber: " + runNumber);
        logger.info("  ... in to BDT application with shippingJob: " + shippingJob + " contractId: " + contractId);

        // ... do some test
        sampleResult.sampleStart();
        try {
            // Create predefined good delivery request TEST
            //grinder.statistics.delayReports = 1
            //tests["CreatePredefinedGoodDeliveryRequest"].record(self.deliveryRequestTest, InstrumentationFilters(";createRemoteDeliveryRequest"))
            predefinedGoodDr = deliveryRequestTest.createRemoteDeliveryRequest(shippingJob, contractId, predefinedGood);
            logger.info(" - Created DR: " + predefinedGoodDr.getId().toString());
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step CreatePredefinedGoodDeliveryRequest completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["CreatePredefinedGoodDeliveryRequest"].getNumber(), tests["CreatePredefinedGoodDeliveryRequest"].getDescription(), grinder.statistics.forLastTest.time))
            strBuilder.append("{\"predefinedGoodDR\" : ");
            strBuilder.append(gson.toJson(predefinedGoodDr));
            strBuilder.append(", \"status\" : \"");
            strBuilder.append(predefinedGoodDr.getStatus().toString().toLowerCase());
            strBuilder.append("\"}");
        } catch (Exception ex) {
            //instance = sys.exc_info()
            //self.errorHandling(self.createPredefinedGoodDeliveryRequest.__name__, instance, tests["CreatePredefinedGoodDeliveryRequest"].getNumber(), tests["CreatePredefinedGoodDeliveryRequest"].getDescription())
            success = false;
            strBuilder.append("{\"predefinedGoodDR\" : \"null\", \"status\" : \"null\"}");
            errorHandling(ex, "CreatePredefinedGoodDeliveryRequest");
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setSuccessful(success);
            sampleResult.setResponseData(strBuilder.toString(), UTF_8.name());
        }
        return sampleResult;
    }
}
