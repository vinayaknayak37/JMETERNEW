package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.dr.DeliveryRequest;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class ListDeliveryGroupsTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ListDeliveryGroupsTest";
    private static final String DR_TAG = "deliveryRequest";
    private static Logger logger = LoggerFactory.getLogger(ListDeliveryGroupsTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(DR_TAG, "{}");
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
        int numDGs = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        String paramJSON = javaSamplerContext.getParameter(DR_TAG, "<uaDr>");
        DeliveryRequest uaDr = new Gson().fromJson(paramJSON, DeliveryRequest.class);

        sampleResult.sampleStart();

        try{
            // List DGs on UA mailing
            //grinder.statistics.delayReports = 1
            //tests["DeliveryGroupListing"].record(self.uaMailingTest, InstrumentationFilters("getDeliveryGroupListingForMailing"))
            numDGs = uaMailingTest.getDeliveryGroupListingForMailing(uaDr);
            //grinder.statistics.forLastTest.setSuccess(1)
            //self.log("Agent::" + str(grinder.agentNumber) + " - Successfully listed " + str(numDGs) + " DGs for mailing " + uaDr.getId().toString())
            logger.info("*** Test step DeliveryGroupListing completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryGroupListing"].getNumber(), tests["DeliveryGroupListing"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"numDGs\" : " + numDGs + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
