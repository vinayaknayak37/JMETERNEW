package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.dr.SortCriteria;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class SearchDRByJobAndStatusTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "SearchDRByJobAndStatusTest";
    private static final String STATUS_TAG = "status";
    private static Logger logger = LoggerFactory.getLogger(SearchDRByJobAndStatusTest.class);

    private String statusCriterion = "ss";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(SHIPPING_JOB_TAG,"<shippingJob>");
        defaultParameters.addArgument(STATUS_TAG, "printed");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);

        statusCriterion = javaSamplerContext.getParameter(STATUS_TAG, "printed");
        logger.info("Status String Criterion: " + statusCriterion);
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        int numSearchByCriteriaResults = 0;
        Gson gson = new Gson();

        String jsonString = javaSamplerContext.getParameter(SHIPPING_JOB_TAG, "UndefinedShippingJob");
        Job shippingJob = gson.fromJson(jsonString, Job.class);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "], shippingJob: [" + shippingJob.getId() + "]");

        sampleResult.sampleStart();

        try{
            // Delivery Request Search by Job and status
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestSearchByJobStatus"].record(self.deliveryRequestTest, InstrumentationFilters("getDeliveryRequestsByJobAndStatus"))
            int numSearchByJobStatusResults = deliveryRequestTest.getDeliveryRequestsByJobAndStatus(shippingJob, statusCriterion, null);
            logger.info(" - Search by job status " + statusCriterion + " hit " + numSearchByJobStatusResults + " results");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step DeliveryRequestSearchByJobStatus completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestSearchByJobStatus"].getNumber(), tests["DeliveryRequestSearchByJobStatus"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"statusCriterion\" : \"" + statusCriterion + "\", \"numSearchByCriteriaResults\" : " + numSearchByCriteriaResults + "}", UTF_8.name());
        }

        sampleResult.setResponseMessageOK();
        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
