package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.client.sortvalueextractor.SortValueExtractor;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.dr.SortCriteria;
import cpdt.client.dr.DRSortValueExtractorFactory;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class SearchDRByJobAndCriteriaSortedTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "SearchDRByJobAndCriteriaSortedTest";
    private static final String CRITERION_TAG = "criterionString";
    private static Logger logger = LoggerFactory.getLogger(SearchDRByJobAndCriteriaSortedTest.class);

    private String searchCriterion = "ss";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(SHIPPING_JOB_TAG,"<shippingJob>");
        defaultParameters.addArgument(CRITERION_TAG, "Innovapost");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);

        searchCriterion = javaSamplerContext.getParameter(CRITERION_TAG, "Innovapost");
        logger.info("Search String Criterion: " + searchCriterion);
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
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "], shippingJob: [" + shippingJob.getId() + "].");

        sampleResult.sampleStart();

        // Search for DR by Job and keyword criteria
        try {
            // Delivery Request Search by Criteria
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestSearchByJobCriteria"].record(self.deliveryRequestTest, InstrumentationFilters("getDeliveryRequestsByJob"))
            SortValueExtractor<DeliveryRequest, SortCriteria> sortValueExtractor =  new DRSortValueExtractorFactory().getExtractor(SortCriteria.DestName);
            numSearchByCriteriaResults = deliveryRequestTest.getDeliveryRequestsByJob(shippingJob, searchCriterion, sortValueExtractor);
            logger.info(" - Search by job criteria sorted hit: " + numSearchByCriteriaResults + " results");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step DeliveryRequestSearchByJobCriteriaSorted completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestSearchByJobCriteria"].getNumber(), tests["DeliveryRequestSearchByJobCriteria"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"sortBy\" : \"" + SortCriteria.DestName.name() + "\", \"searchCriterion\" : \"" +
                    searchCriterion + "\", \"numSearchByCriteriaResults\" : " + numSearchByCriteriaResults + "}", UTF_8.name());
        }

        sampleResult.setResponseMessageOK();
        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}

