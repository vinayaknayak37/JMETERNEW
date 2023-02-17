package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.common.Message;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class ImportAAMachMailingPlanTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ImportAAMachMailingPlanTest";
    private static String TEST_NAME_TAG = "testName";
    private static String JOB_NAME_TAG = "jobName";
    private static Logger logger = LoggerFactory.getLogger(ImportAAMachMailingPlanTest.class);

    private String fileName = "";
    private String testName = "";
    private String jobName = "";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(FILE_PATH_TAG, "<filePath>");
        defaultParameters.addArgument(TEST_NAME_TAG, "<testName>");
        defaultParameters.addArgument(JOB_NAME_TAG, "<jobName>");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);
        fileName = javaSamplerContext.getParameter(FILE_PATH_TAG, AA_MACH_LARGE);
        testName = javaSamplerContext.getParameter(TEST_NAME_TAG, "ImportLargeAAMachinable");
        jobName = javaSamplerContext.getParameter(JOB_NAME_TAG, "GrinderAATesting");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        List<Message> importAAMachResults = new ArrayList<>();

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "], jobName: [" + jobName + "], testName: [" + testName + "]");

        // New test case import large aa machineable
        // Get the AA Job
        Job aaMailingJob = aaMailingTest.getJob(jobName);

        sampleResult.sampleStart();

        try{
            // """Import Large Address Admail Machineable """
            // Delay the grinder statistics so our test can control it.
            //grinder.statistics.delayReports = 1

            //instrument the test, recording the results
            //tests[testName].record(self.aaMailingTest, InstrumentationFilters("importMachMailingPlan"))

            File fileToUpload = new File(fileName);
            logger.info("File to be uploaded is: " + fileName);

            // Create the DR
            DeliveryRequest dr = aaMailingTest.createSkeletonAAdeliveryRequest(aaMailingJob.getId());
            logger.info("  DR: " + dr.toString());

            logger.info("About to start Importing a Large AA Machineable file.");
            importAAMachResults = aaMailingTest.importMachMailingPlan(fileToUpload, aaMailingJob.getId(), dr);

            //grinder.statistics.forLastTest.setSuccess(1)
            logger.debug(" - Import Large AA mailing plan. Number of messages is: [" + importAAMachResults.size() + "]");
            Gson gson = new Gson();
            logger.debug(" - Import Large AA mailing plan. Messages are: ");
            for (Message msg : importAAMachResults) {
                logger.debug(gson.toJson(msg));
            }

            logger.info("*** Test step %s completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests[testName].getNumber(), tests[testName].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData("{\"nResults\" : " + importAAMachResults.size() + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }

}
