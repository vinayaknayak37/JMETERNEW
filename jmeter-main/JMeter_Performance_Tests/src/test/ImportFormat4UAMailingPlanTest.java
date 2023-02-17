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

public class ImportFormat4UAMailingPlanTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ImportFormat4UAMailingPlanTest";
    private static final String JOB_NAME_TAG = "jobName";
    private static final String UA_TESTING = "GrinderUATesting";
    private static Logger logger = LoggerFactory.getLogger(ImportFormat4UAMailingPlanTest.class);

    private String jobName = "";
    private String fileName = "";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(JOB_NAME_TAG, "<jobName>");
        defaultParameters.addArgument(FILE_PATH_TAG, "<filePath>");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);

        // We only need to set up these parameters once
        jobName = javaSamplerContext.getParameter(JOB_NAME_TAG, UA_TESTING);
        fileName = javaSamplerContext.getParameter(FILE_PATH_TAG, UA_FF_SMALL);
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        DeliveryRequest uaDrResult = null;
        StringBuilder strBuilder = new StringBuilder();

        // We need to set up this parameter each time through our main thread loop (i.e. our runNumber will increase per loop)
        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: [" + runNumber + "], jobName: [" + jobName + "], fileName: [" + fileName + "]");

        uaMailingTest.setupClientStateCategory(MailingConst.MAILING_CATEGORY_GROUP);
        Job uaJob = uaMailingTest.getJob(jobName);
        //uaDr = self.importFormat4UAMailingPlan(self.uaffSmall, uaJob)

        sampleResult.sampleStart();

        try{
            // Import format 4 UA mailing plan
            //grinder.statistics.delayReports = 1
            File fileToImport = uaMailingTest.getFile(new File(fileName));
            logger.info("File to import is: " + fileToImport.toString());
            //#contract = self.uaMailingTest.getDefaultContract()
            DeliveryRequest uaDr = uaMailingTest.createSkeletonUADeliveryRequest(uaJob.getId());
            logger.info(" - UA format 4 DR created is: " + uaDr.getId().toString());
            //tests["ImportUAFormat4MailingPlan"].record(self.uaMailingTest, InstrumentationFilters("importFormat4MailingPlan"))
            uaDrResult = uaMailingTest.importFormat4MailingPlan(fileToImport, uaJob.getId(), uaDr);
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info(" - Imported UA mailing plan with ID: " + uaDrResult.getId().toString());
            logger.info("*** Test step ImportUAFormat4MailingPlan completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["ImportUAFormat4MailingPlan"].getNumber(), tests["ImportUAFormat4MailingPlan"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            strBuilder.append("{\"deliveryRequest\" : ");
            strBuilder.append(uaDrResult == null ? "\"null\"" : (new Gson().toJson(uaDrResult)));
            strBuilder.append(", \"status\" : \"");
            strBuilder.append(uaDrResult == null ? "\"\"" : uaDrResult.getStatus().toString());
            strBuilder.append("\"}");
            sampleResult.setResponseData(strBuilder.toString(), UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
