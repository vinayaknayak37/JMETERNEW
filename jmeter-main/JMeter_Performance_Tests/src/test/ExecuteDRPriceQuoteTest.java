package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.dr.pricing.PriceQuote;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class ExecuteDRPriceQuoteTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ExecuteDRPriceQuoteTest";
    private static String DR_TAG = "deliveryRequest";
    private static Logger logger = LoggerFactory.getLogger(ExecuteDRPriceQuoteTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(DR_TAG, "<deliveryRequest>");
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
        List<PriceQuote> drQuoteListResult = new ArrayList<>();
        int nQuotes = 0;

        // !!!NOTE!!! DeliveryRequest JSON is passed in from the previous JMeter test
        String jsonDR = javaSamplerContext.getParameter(DR_TAG, "null");
        // Delivery Request Price Quote
        DeliveryRequest drSkeleton = new Gson().fromJson(jsonDR, DeliveryRequest.class);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // After Initialization completed successfully, we want to perform a BDT Login
        // with said user.
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "]  drSkeleton: [" + drSkeleton.getId() + "].");

        sampleResult.sampleStart();
        // ... do some test
        try{
            // Obtain DR price quote TEST
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestPriceQuote"].record(self.deliveryRequestTest, InstrumentationFilters("getDeliveryRequestPriceQuote"))
            drQuoteListResult = deliveryRequestTest.getDeliveryRequestPriceQuote(drSkeleton);
            nQuotes = drQuoteListResult.size();
            logger.info(" ... got [" + nQuotes + "] price quotes.");
            logger.info("*** Test step DeliveryRequestPriceQuote completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestPriceQuote"].getNumber(), tests["DeliveryRequestPriceQuote"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setSuccessful(success);
            sampleResult.setResponseData((new Gson().toJson(drQuoteListResult)), UTF_8.name());
        }

        return sampleResult;
    }
}