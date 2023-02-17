package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.common.Message;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.job.Job;
import cpdt.domain.mailing.MailingConst;
import cpdt.test.wrapper.util.GrinderTestUtil;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class UploadSERPMailingPlanTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "UploadSERPMailingPlanTest";
    private static final String FILENAME_TAG = "fileName";
    private static final String TESTNAME_TAG = "testName";
    private static Logger logger = LoggerFactory.getLogger(UploadSERPMailingPlanTest.class);

    private String fileName = "";
    private String testName = "";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(FILENAME_TAG, "fn");
        defaultParameters.addArgument(TESTNAME_TAG, "tn");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        //super.setupTest(javaSamplerContext);

        fileName = javaSamplerContext.getParameter(FILENAME_TAG, AA_SERP_MAILING_PLAN_MEDIUM);
        testName = javaSamplerContext.getParameter(TESTNAME_TAG, "");

        logger.info(BDT_TEST_NAME + " set up with fileName: [" + fileName + "], testName: [" + testName + "]");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        String mailingRefId = "null";
        File fileToUpload = null;
        Gson gson = new Gson();

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();
        try{
            aaMailingTest.setupClientStateCategory(MailingConst.MAILING_CATEGORY_GROUP);

            // Upload SERP mailing plan
            //grinder.statistics.delayReports = 1
            //tests[testName].record(self.aaMailingTest, InstrumentationFilters("upload"))
            mailingRefId = GrinderTestUtil.getUniqueRandomString(1).toUpperCase() + GrinderTestUtil.getUniqueRandomAlfaNumeric(10);
            mailingRefId = mailingRefId.substring(0, 10);   // Force truncation of mailingRefId to 10 characters!!!
            logger.info("Mailing plan ref ID is: " + mailingRefId);
            fileToUpload = new File(fileName);
            logger.info("File to be uploaded is: " + fileToUpload.toString());

            List<Message> uploadMailingResults = aaMailingTest.upload(mailingRefId, fileToUpload);

            //logger.info("*** Upload results: %s ***" % uploadMailingResults)
            logger.info(" - Uploaded SERP mailing plan. Messages are: [");
            for (Message msg : uploadMailingResults) {
                logger.info("    -- " + gson.toJson(msg));
            }
            logger.info("]");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step Uploading SERP mailing plan completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests[testName].getNumber(), tests[testName].getDescription(), grinder.statistics.forLastTest.time))

            sampleResult.setResponseCodeOK();
            sampleResult.setResponseMessageOK();
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("{\"mailingRefId\" : \"");
            strBuilder.append(mailingRefId);
            strBuilder.append("\"}");
            sampleResult.setResponseData(strBuilder.toString(), UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }

}
