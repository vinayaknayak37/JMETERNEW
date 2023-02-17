package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.common.DeliveryRequestId;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class MoveDRToJobTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "MoveDRToJob";
    private static String DR_TAG = "deliveryRequest";
    private static Logger logger = LoggerFactory.getLogger(MoveDRToJobTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(MOVE_DR_JOB_TAG,"<moveDRJob>");
        defaultParameters.addArgument(DR_TAG,"<deliveryRequest>");
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
        Gson gson = new Gson();

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // After Initialization completed successfully, we want to perform a BDT Login
        // with said user.
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        String jsonString = javaSamplerContext.getParameter(DR_TAG, "null");

        // Move a DR from one Job to another
        // Every run, create Single Delivery Request
        // After moving, delete printed/processed/accepted/rejected DRs from the target Job,
        // to prevent DRs from accumulating there
        // DeliveryRequest moveDr = createSingleDeliveryRequest(shippingJob, contractId, result);
        // moveDrToJob(moveDr, moveDRJob);
        DeliveryRequest moveDr = gson.fromJson(jsonString, DeliveryRequest.class);

        jsonString = javaSamplerContext.getParameter(MOVE_DR_JOB_TAG, "null");
        Job moveDRJob = gson.fromJson(jsonString, Job.class);

        List<DeliveryRequestId> drIdList = new ArrayList<>();
        StringBuilder strBuilderResponse = new StringBuilder();
        strBuilderResponse.append("[");

        // ... do some test
        sampleResult.sampleStart();
        try {
            // #Export history
            //grinder.statistics.delayReports = 1
            //tests["MoveDRToJob"].record(self.deliveryRequestTest, InstrumentationFilters("moveDRtoJob"))
            drIdList = deliveryRequestTest.moveDRtoJob(moveDr.getId(), moveDRJob);

            logger.info(" - Successfully moved " + drIdList.size() + " DR to alternate job.");
            logger.info("*** Test step MoveDRToJob completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["MoveDRToJob"].getNumber(), tests["MoveDRToJob"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            strBuilderResponse.append(drIdList);
        }

        sampleResult.setResponseData(strBuilderResponse.toString(), UTF_8.name());
        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
