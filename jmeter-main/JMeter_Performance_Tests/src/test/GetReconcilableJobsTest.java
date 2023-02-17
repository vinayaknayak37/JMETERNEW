package ca.cpggpc.est2_0.desktop.perftest;

import cpdt.domain.common.JobId;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.google.common.base.Charsets.UTF_8;

public class GetReconcilableJobsTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "GetReconcilableJobsTest";
    private static final String STATUS_TAG = "arg1";
    private static Logger logger = LoggerFactory.getLogger(GetReconcilableJobsTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(STATUS_TAG, "zzarg1");
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

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        StringBuilder response = new StringBuilder();
        response.append("[");
        sampleResult.sampleStart();

        try{
            // Search for reconcilable delivery requests by Job
            //grinder.statistics.delayReports = 1
            //tests["FindReconcilableJobs"].record(self.deliveryRequestTest, InstrumentationFilters("getReconcilableJobs"))
            Set<JobId> reconcilableSet = deliveryRequestTest.getReconcilableJobs();
            logger.info(" - Reconcilable Jobs " + reconcilableSet.size());
            for (JobId jobId : reconcilableSet) {
                response.append("{\"jobId\" : \"" + jobId.toString() + "\"},\n");
            }
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step FindReconcilableJobs completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["FindReconcilableJobs"].getNumber(), tests["FindReconcilableJobs"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setSuccessful(success);
            response.append("]");
            sampleResult.setResponseData(response.toString(), UTF_8.name());
        }

        return sampleResult;
    }
}
