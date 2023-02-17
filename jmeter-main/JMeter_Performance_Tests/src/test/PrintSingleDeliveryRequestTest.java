package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.dr.DeliveryRequest;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class PrintSingleDeliveryRequestTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "PrintSingleDeliveryRequestTest";
    private static final String DR_TO_PRINT_TAG = "drToPrint";
    private static Logger logger = LoggerFactory.getLogger(PrintSingleDeliveryRequestTest.class);

    private String searchCriterion = "ss";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(DR_TO_PRINT_TAG, "<drToPrint>");
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
        DeliveryRequest printedDR = null;
        StringBuilder strBuilder = new StringBuilder();
        Gson gson = new Gson();

        String jsonString = javaSamplerContext.getParameter(DR_TO_PRINT_TAG, "UndefinedDRToPrint");
        DeliveryRequest drToPrint = gson.fromJson(jsonString, DeliveryRequest.class);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "], drToPrint: [" + drToPrint.getId() + "]");

        sampleResult.sampleStart();
        try {
            //Print remote delivery request TEST
            //grinder.statistics.delayReports = 1
            //tests["PrintRemoteDeliveryRequest"].record(self.deliveryRequestTest, InstrumentationFilters("printRemoteDeliveryRequest"))
            printedDR = deliveryRequestTest.printRemoteDeliveryRequest(drToPrint);
            logger.info(" - Printed DR: " + printedDR.getId().toString());
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step PrintRemoteDeliveryRequest completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["PrintRemoteDeliveryRequest"].getNumber(), tests["PrintRemoteDeliveryRequest"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, "PrintRemoteDeliveryRequest");
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setSuccessful(success);
            strBuilder.append("{\"printedDR\" : ");
            strBuilder.append(printedDR == null ? "\"null\"" : gson.toJson(printedDR));
            strBuilder.append("}");
            sampleResult.setResponseData(strBuilder.toString(), UTF_8.name());
        }
        return sampleResult;
    }
}
