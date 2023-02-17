package ca.cpggpc.est2_0.desktop.perftest;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class GetAvailableAddressGroupsTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "AvailableAddressGroupsTest";
    private static Logger logger = LoggerFactory.getLogger(GetAvailableAddressGroupsTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(ARG1_TAG, "zarg1");
        defaultParameters.addArgument(ARG2_TAG, "zarg2");
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
        int numAvailableAddressGroups = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();
        try {
            // Search for available address groups
            //grinder.statistics.delayReports = 1
            //tests["GetAvailableAddressGroups"].record(self.deliveryRequestTest, InstrumentationFilters("getAddressGroupsCount"))
            numAvailableAddressGroups = deliveryRequestTest.getAddressGroupsCount();
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info(" - Total number of address groups available is: " + numAvailableAddressGroups);
            logger.info("*** Test step GetAvailableAddressGroups completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["GetAvailableAddressGroups"].getNumber(), tests["GetAvailableAddressGroups"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"numAvailableAddressGroups\" : " + numAvailableAddressGroups + "}", UTF_8.name());
        }

        sampleResult.setResponseMessageOK();
        sampleResult.setSuccessful(success);
        return sampleResult;
    }

}
