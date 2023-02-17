package ca.cpggpc.est2_0.desktop.perftest;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class SearchDRHistoryNoCriteriaTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "SearchDRHistoryNoCriteriaTest";
    private static final String CRITERION_TAG = "criterionString";
    private static Logger logger = LoggerFactory.getLogger(SearchDRHistoryNoCriteriaTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(CRITERION_TAG, "");
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
        int numSearchHistoryNoCriteria = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();

        try{
            // Delivery Request History search
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestHistorySearchNoCriteria"].record(self.deliveryRequestTest, InstrumentationFilters("getShipmentHistoryNoCriteria"))
            numSearchHistoryNoCriteria = deliveryRequestTest.getShipmentHistoryNoCriteria(null);
            logger.info(" - Search history with no criteria hit: " + numSearchHistoryNoCriteria + " results");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step DeliveryRequestHistorySearchNoCriteria completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestHistorySearchNoCriteria"].getNumber(), tests["DeliveryRequestHistorySearchNoCriteria"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"searchCriterion\" : \"None\", \"numSearchHistoryNoCriteria\" : " + numSearchHistoryNoCriteria + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
