package ca.cpggpc.est2_0.desktop.perftest;

import cpdt.client.address.service.AddressSearchCriteria;
import cpdt.client.address.service.AddressSearchField;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class AdvancedSearchAddressGroupTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "AdvancedSearchAddressGroupTest";
    private static final String AS_TAG = "advancedSearchString";
    private static final String AS_LISTCOUNT_TAG = "listCount";
    private static Logger logger = LoggerFactory.getLogger(AdvancedSearchAddressGroupTest.class);

    private String advSearchCriterion = "ss";
    private int listCount = 0;

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(AS_TAG, "A");
        defaultParameters.addArgument(AS_LISTCOUNT_TAG, "10");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);

        advSearchCriterion = javaSamplerContext.getParameter(AS_TAG, "A");
        logger.info("Advanced Search String Criterion: " + advSearchCriterion);

        listCount = Integer.valueOf(javaSamplerContext.getParameter(AS_LISTCOUNT_TAG, "10"));
        logger.info("Advanced Search list count: " + listCount);
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        int advancedSearchResult = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();

        try {
            // Advanced search address group by client ID "A"
            // Every run, create Single Delivery Request
            AddressSearchCriteria addressSearchCriteria = new AddressSearchCriteria();
            addressSearchCriteria.addFieldQuery(AddressSearchField.clientId, advSearchCriterion);
            //tests["AdvancedSearchDefaultAddressGroup"].record(self.deliveryRequestTest, InstrumentationFilters("advancedSearchDefaultAddressBook"))
            advancedSearchResult = deliveryRequestTest.advancedSearchDefaultAddressBook(addressSearchCriteria, listCount, null);
            success = true;
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info(" - Advanced search default address group returned " + advancedSearchResult + " results.");
            logger.info("*** Test step AdvancedSearchDefaultAddressGroup completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["AdvancedSearchDefaultAddressGroup"].getNumber(), tests["AdvancedSearchDefaultAddressGroup"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"advancedSearchResult\" : " + advancedSearchResult + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}