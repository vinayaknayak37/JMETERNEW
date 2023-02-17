package ca.cpggpc.est2_0.desktop.perftest;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class SearchDRHistoryByCriteriaTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "SearchDRHistoryByCriteriaTest";
    private static final String CRITERION_TAG = "criterionString";
    private static Logger logger = LoggerFactory.getLogger(SearchDRHistoryByCriteriaTest.class);

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
        int numSearchHistoryByCriteria = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();

        try{
            // Delivery Request History search
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestHistorySearchByCriteria"].record(self.deliveryRequestTest, InstrumentationFilters("getShipmentHistoryByCriteria"))
            numSearchHistoryByCriteria = deliveryRequestTest.getShipmentHistoryByCriteria(searchCriterion, null);
            logger.info(" - Search history by criterion: " + searchCriterion + " hit: " + numSearchHistoryByCriteria + " results");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step DeliveryRequestHistorySearchByCriteria completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestHistorySearchByCriteria"].getNumber(), tests["DeliveryRequestHistorySearchByCriteria"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"searchCriterion\" : \"" + searchCriterion + "\", \"numSearchHistoryByCriteria\" : " + numSearchHistoryByCriteria + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }

}
