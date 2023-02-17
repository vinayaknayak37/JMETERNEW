package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.common.PredefinedGood;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.google.common.base.Charsets.UTF_8;

public class GetAvailablePredefinedGoodsTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "AvailablePredefinedGoodsTest";
    private static Logger logger = LoggerFactory.getLogger(GetAvailablePredefinedGoodsTest.class);

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
        PredefinedGood predefinedGood = null;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "]");

        StringBuilder strBuilderResponse = new StringBuilder();
        strBuilderResponse.append("[");

        sampleResult.sampleStart();
        try {
            // Search for available predefined goods
            //grinder.statistics.delayReports = 1
            //tests["GetAvailablePredefinedGoods"].record(self.deliveryRequestTest, InstrumentationFilters("getPredefinedGoods"))
            Collection<PredefinedGood> availablePredefinedGoods = deliveryRequestTest.getPredefinedGoods();
            logger.info(" - Total number of predefined goods available is: " + availablePredefinedGoods.size());
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step GetAvailablePredefinedGoods completed successfully. ***");
            //logger.info("*** Test %d %s took %d milliseconds ***" % (tests["GetAvailablePredefinedGoods"].getNumber(), tests["GetAvailablePredefinedGoods"].getDescription(), grinder.statistics.forLastTest.time))
            // Create DR from predefined goods
            // Every run
            if (availablePredefinedGoods != null && availablePredefinedGoods.size() > 0) {
                int selection = Math.toIntExact(Math.round(Math.random() * availablePredefinedGoods.size()) - 1);
                PredefinedGood[] predefinedGoodsArray = (PredefinedGood[])availablePredefinedGoods.toArray();
                predefinedGood = predefinedGoodsArray[selection];
            } else {
                logger.info(" - No predefined goods defined - skipping block");
            }
        } catch (Exception ex) {
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
            //self.getAvailablePredefinedGoods.__name__, instance, tests["GetAvailablePredefinedGoods"].getNumber(), tests["GetAvailablePredefinedGoods"].getDescription());
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"predefinedGood\" : " + (predefinedGood == null ? "\"null\"" : new Gson().toJson(predefinedGood)) + "}", UTF_8.name());
            sampleResult.setSuccessful(success);
        }

        return sampleResult;
    }
}
