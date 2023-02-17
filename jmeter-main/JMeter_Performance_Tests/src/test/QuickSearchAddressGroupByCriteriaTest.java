package ca.cpggpc.est2_0.desktop.perftest;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class QuickSearchAddressGroupByCriteriaTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "QuickSearchAddressGroupByCriteriaTest";
    private static final String QS_TAG = "quickSearchString";
    private static final String QS_LISTCOUNT_TAG = "listCount";
    private static Logger logger = LoggerFactory.getLogger(QuickSearchAddressGroupByCriteriaTest.class);

    private String quickSearchCriteria = "ss";
    private int listCount = 0;

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(QS_TAG, "John");
        defaultParameters.addArgument(QS_LISTCOUNT_TAG, "10");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);

        quickSearchCriteria = javaSamplerContext.getParameter(QS_TAG, "ss");
        logger.info("Quick Search String Criterion: " + quickSearchCriteria);

        listCount = Integer.valueOf(javaSamplerContext.getParameter(QS_LISTCOUNT_TAG, "10"));
        logger.info("Quick Search list count: " + listCount);
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        int quickSearchResult  = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();

        try {
            // # Quick search default address group
            //grinder.statistics.delayReports = 1
            //tests["QuickSearchDefaultAddressGroup"].record(self.deliveryRequestTest, InstrumentationFilters("quickSearchDefaultAddressBook"))
            quickSearchResult = deliveryRequestTest.quickSearchDefaultAddressBook(quickSearchCriteria, listCount, null);
            success = true;
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info(" - Quick search default address group returned " + quickSearchResult + " results.");
            logger.info("*** Test step QuickSearchDefaultAddressGroup completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["QuickSearchDefaultAddressGroup"].getNumber(), tests["QuickSearchDefaultAddressGroup"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"quickSearchResult\" : " + quickSearchResult + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
