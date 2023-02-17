package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.job.Job;
import cpdt.domain.mailing.MailingConst;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class GetAAMailingJob extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "GetAAMailingJob";
    private static final String JOB_NAME_TAG = "jobName";
    private static Logger logger = LoggerFactory.getLogger(GetAAMailingJob.class);

    private String jobName = "";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(JOB_NAME_TAG, "<jobName>");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        //super.setupTest(javaSamplerContext);
        jobName = javaSamplerContext.getParameter(JOB_NAME_TAG, "GrinderAATesting");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        Job aaMailingJob = null;
        StringBuilder response = new StringBuilder();

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));

        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "],  jobName: [" + jobName + "]");

        sampleResult.sampleStart();

        try{
            // AA Mailing plan tests
            // Upload/Create/Reconcile Addressed Admail SERP mailing plan
            // Run 100% of the time
            aaMailingTest.setupClientStateCategory(MailingConst.MAILING_CATEGORY_GROUP);
            aaMailingJob = aaMailingTest.getJob(jobName);
            sampleResult.setResponseOK();       // This is equivalent to setResponseMessageOK(), setResponseCodeOK(), and setSuccessful(true);
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
            sampleResult.setResponseMessage(BAD_REQUEST_400 + " : [" + ex.getMessage() + "]");
        } finally {
            sampleResult.sampleEnd();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("{\"aaMalingJob\" : ");
            strBuilder.append(aaMailingJob == null ? "\"null\"" : new Gson().toJson(aaMailingJob));
            strBuilder.append("}");
            sampleResult.setResponseData(strBuilder.toString(), UTF_8.name());
            sampleResult.setSuccessful(success);
        }

        return sampleResult;
    }
}
