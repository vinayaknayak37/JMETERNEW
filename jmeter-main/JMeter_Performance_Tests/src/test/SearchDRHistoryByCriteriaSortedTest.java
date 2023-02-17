package ca.cpggpc.est2_0.desktop.perftest;

import cpdt.client.dr.DRSortValueExtractorFactory;
import cpdt.domain.dr.SortCriteria;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class SearchDRHistoryByCriteriaSortedTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "SearchDRHistoryByCriteriaSortedTest";
    private static final String CRITERION_TAG = "criterionString";
    private static Logger logger = LoggerFactory.getLogger(SearchDRHistoryByCriteriaSortedTest.class);

    private String searchCriterion = "";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(CRITERION_TAG, "Innovapost");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);

        searchCriterion = javaSamplerContext.getParameter(CRITERION_TAG, "Innovapost");
        logger.info("Advanced Search String Criterion: " + searchCriterion);
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        int numSearchHistoryByCriteriaSorted = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();

        try{
            // Delivery Request History search sorted
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestHistorySearchByCriteriaSorted"].record(self.deliveryRequestTest, InstrumentationFilters("getShipmentHistoryByCriteria"))
            numSearchHistoryByCriteriaSorted = deliveryRequestTest.getShipmentHistoryByCriteria(searchCriterion, new DRSortValueExtractorFactory().getExtractor(SortCriteria.MailingDate));
            logger.info(" - Sorted search history by criterion: " + searchCriterion + " hit: " + numSearchHistoryByCriteriaSorted + " results");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step DeliveryRequestHistorySearchByCriteriaSorted completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestHistorySearchByCriteriaSorted"].getNumber(), tests["DeliveryRequestHistorySearchByCriteriaSorted"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"searchCriterion\" : \"" + searchCriterion + "\", \"numSearchHistoryByCriteriaSorted\" : " + numSearchHistoryByCriteriaSorted + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
