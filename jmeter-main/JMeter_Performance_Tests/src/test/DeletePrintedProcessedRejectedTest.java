package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class DeletePrintedProcessedRejectedTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "DeletePrintedProcessedRejected";
    private static Logger logger = LoggerFactory.getLogger(DeletePrintedProcessedRejectedTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(MOVE_DR_JOB_TAG, "<moveDRJob>");
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
        SampleResult result = new SampleResult();
        boolean success = true;

        Gson gson = new Gson();
        String jsonString = javaSamplerContext.getParameter(MOVE_DR_JOB_TAG, "UndefinedMoveDRJob");
        Job moveDRJob = gson.fromJson(jsonString, Job.class);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));

        // After Initialization completed successfully, we want to perform a BDT Login
        // with said user.
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber + " with moveDRJob: [" + moveDRJob.getId() + "].");

        result.sampleStart();
        try {
            //grinder.statistics.delayReports = 1
            //tests["DeletePrintedProcessedRejectedDRsFromJob"].record(self.deliveryRequestTest, InstrumentationFilters("deletePrintedProcessedRejected"))
            logger.info(" - Beginning to void or delete printed/processed/accepted/rejected DRs in job " + moveDRJob.getName());
            int numRemainingToBeDeleted = deliveryRequestTest.deletePrintedProcessedRejected(moveDRJob);
            result.setResponseData("{\"numRemainingToBeDeleted\" : " + numRemainingToBeDeleted + "}", UTF_8.name());
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info(" - Successfully voided or deleted printed/processed/accepted/rejected DRs. Number voided or deleted = " + numRemainingToBeDeleted);
            logger.info("*** Test step DeletePrintedProcessedRejectedDRsFromJob completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeletePrintedProcessedRejectedDRsFromJob"].getNumber(), tests["DeletePrintedProcessedRejectedDRsFromJob"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
            //self.deletePrintedProcessedRejected.__name__, instance, tests["DeletePrintedProcessedRejectedDRsFromJob"].getNumber(), tests["DeletePrintedProcessedRejectedDRsFromJob"].getDescription());
        }

        result.sampleEnd();
        result.setSuccessful(success);
        return result;
    }
}