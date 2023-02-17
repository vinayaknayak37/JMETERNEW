package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.common.DeliveryRequestId;
import cpdt.domain.dr.DeliveryRequest;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UpdateDeliveryRequestSenderTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "UpdateDeliveryRequestSenderTest";
    private static String DR_ID_TAG = "drId";
    private static String SENDER_NAME_TAG = "senderName";
    private static Logger logger = LoggerFactory.getLogger(UpdateDeliveryRequestSenderTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(DR_ID_TAG, "zarg1");
        defaultParameters.addArgument(SENDER_NAME_TAG, "zarg2");
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
        DeliveryRequest dr = null;
        boolean success = true;

        // !!!NOTE!!! DeliveryRequestId JSON is passed in from the previous JMeter test
        String senderName = javaSamplerContext.getParameter(SENDER_NAME_TAG, "Grinder");
        // Delivery Request Price Quote
        String jsonDRId = javaSamplerContext.getParameter(DR_ID_TAG, "null");
        DeliveryRequestId drId = new Gson().fromJson(jsonDRId, DeliveryRequestId.class);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // After Initialization completed successfully, we want to perform a BDT Login
        // with said user.
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "], jsonDRId: [" + drId.toString() + "], senderName: [" + senderName + "]");

        sampleResult.sampleStart();
        // ... do some test
        try{
            // update remote delivery request TEST
            //grinder.statistics.delayReports = 1
            //tests["UpdateRemoteDeliveryRequest"].record(self.deliveryRequestTest, InstrumentationFilters("updateRemoteDeliveryRequestSender"))
            dr = deliveryRequestTest.updateRemoteDeliveryRequestSender(drId, senderName);
            logger.info(" - Updated DR: " + dr.getId().toString());
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step UpdateRemoteDeliveryRequest completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["UpdateRemoteDeliveryRequest"].getNumber(), tests["UpdateRemoteDeliveryRequest"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setSuccessful(success);
            sampleResult.setResponseData("{\"deliveryRequest\" : " + (new Gson().toJson(dr)) + "}", UTF_8.name());
        }

        return sampleResult;
    }

}
