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

import java.io.File;

import static com.google.common.base.Charsets.UTF_8;

public class CreateDRFromMailingPlanTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "CreateDRFromMailingPlanTest";
    private static final String REF_ID_TAG = "mailingPlanRefId";
    private static final String AA_JOB_TAG = "aaJob";
    private static final String TEST_NAME_TAG = "testName";
    private static Logger logger = LoggerFactory.getLogger(CreateDRFromMailingPlanTest.class);

    private String testName = "";
    private String contractId = "";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(REF_ID_TAG, "<mailingRefId>");
        defaultParameters.addArgument(AA_JOB_TAG, "<aaJob>");
        defaultParameters.addArgument(TEST_NAME_TAG, "testName");
        defaultParameters.addArgument(CONTRACT_ID_TAG, "<contractId>");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        //super.setupTest(javaSamplerContext);
        testName = javaSamplerContext.getParameter(TEST_NAME_TAG, "UndefinedTestName");
        contractId = javaSamplerContext.getParameter(CONTRACT_ID_TAG, "UndefinedContractId");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        DeliveryRequest createAASerpDrResult = null;
        StringBuilder response = new StringBuilder();
        Gson gson = new Gson();

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));

        // !!!NOTE!!! mailingRefId comes from the call to UploadSERPMailingPlanTest
        String mailingRefID = javaSamplerContext.getParameter(REF_ID_TAG, "");

        String jsonString = javaSamplerContext.getParameter(AA_JOB_TAG, "UndefinedShippingJob");
        Job aaJob = gson.fromJson(jsonString, Job.class);

        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "], job: [" + jsonString + "], contractId: [" + contractId + "], mailingRefId: [" + mailingRefID + "], testName: [" + testName + "]");

        sampleResult.sampleStart();

        try{
            aaMailingTest.setupClientStateCategory(MailingConst.MAILING_CATEGORY_GROUP);

            // Create DR mailing plan from SERP import
            //grinder.statistics.delayReports = 1
            //tests[testName].record(self.aaMailingTest, InstrumentationFilters("createDRFromPlan"))
            String contract = aaMailingTest.getDefaultContract();
            logger.info("*** Mailing contract id: %s *** [" + contract + "]");

            createAASerpDrResult = aaMailingTest.createDRFromPlan(contract, mailingRefID, aaJob.getId(), MailingConst.AA_CATEGORY);

            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info(" - Created AA mailing plan DR : " + gson.toJson(createAASerpDrResult));
            logger.info("*** Test step %s completed successfully. *** ");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests[testName].getNumber(), tests[testName].getDescription(), grinder.statistics.forLastTest.time))
            sampleResult.setResponseOK();       // This is equivalent to setResponseMessageOK(), setResponseCodeOK(), and setSuccessful(true);
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
            sampleResult.setResponseMessage(BAD_REQUEST_400 + " : [" + ex.getMessage() + "]");
        } finally {
            sampleResult.sampleEnd();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("{\"aaSerpDrResult\" : ");
            strBuilder.append(createAASerpDrResult == null ? "\"null\"" : gson.toJson(createAASerpDrResult));
            strBuilder.append(", \"aaSerpDrStatus\" : \"");
            strBuilder.append(createAASerpDrResult == null ? "null" : createAASerpDrResult.getStatus().toString());
            strBuilder.append("\"}");
            sampleResult.setResponseData(strBuilder.toString(), UTF_8.name());
            sampleResult.setSuccessful(success);
        }

        return sampleResult;
    }



}
