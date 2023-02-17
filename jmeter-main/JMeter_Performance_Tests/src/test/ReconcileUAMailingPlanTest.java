package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.dr.DeliveryRequest;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class ReconcileUAMailingPlanTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ReconcileUAMailingPlanTest";
    private static final String DR_TAG = "deliveryRequest";
    private static Logger logger = LoggerFactory.getLogger(ReconcileUAMailingPlanTest.class);

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
        int numUAReconcileDr = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        String paramJSON = javaSamplerContext.getParameter(DR_TAG, "{}");
        DeliveryRequest uaDr = new Gson().fromJson(paramJSON, DeliveryRequest.class);

        sampleResult.sampleStart();

        try{
            // Reconcile an UnAddressed Admail mailing plan
            //grinder.statistics.delayReports = 1
            logger.info(" - Reconciling UA DR : " + uaDr.getId().toString());
            //tests["ReconcileUAMailingPlan"].record(self.uaMailingTest, InstrumentationFilters("reconcileUADeliveryRequest"))
            numUAReconcileDr = uaMailingTest.reconcileUADeliveryRequest(uaDr);
            logger.info(" - Reconciled " + numUAReconcileDr + " UA DRs");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step ReconcileUAMailingPlan completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["ReconcileUAMailingPlan"].getNumber(), tests["ReconcileUAMailingPlan"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"numUAReconcileDr\" : " + numUAReconcileDr + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
