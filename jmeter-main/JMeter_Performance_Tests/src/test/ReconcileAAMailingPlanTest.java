package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.mailing.MailingConst;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class ReconcileAAMailingPlanTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ReconcileAAMailingPlanTest";
    private static final String DR_TAG = "deliveryRequest";
    private static Logger logger = LoggerFactory.getLogger(ReconcileAAMailingPlanTest.class);

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
        //super.setupTest(javaSamplerContext);
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        int numAAReconcileDr = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        String paramJSON = javaSamplerContext.getParameter(DR_TAG, "{}");
        logger.info("aaDR : " + paramJSON);
        DeliveryRequest aaDr = new Gson().fromJson(paramJSON, DeliveryRequest.class);

        sampleResult.sampleStart();

        try{
            aaMailingTest.setupClientStateCategory(MailingConst.MAILING_CATEGORY_GROUP);

            // Reconcile an Addressed Admail mailing plan
            //grinder.statistics.delayReports = 1
            logger.info(" - Reconciling AA DR : " + aaDr.getId().toString());
            //tests[testName].record(self.aaMailingTest, InstrumentationFilters("reconcileAADeliveryRequest"))
            numAAReconcileDr = aaMailingTest.reconcileAADeliveryRequest(aaDr);
            logger.info(" - Reconciled " + numAAReconcileDr + " AA DRs");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step %s completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests[testName].getNumber(), tests[testName].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"numAAReconcileDr\" : " + numAAReconcileDr + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
