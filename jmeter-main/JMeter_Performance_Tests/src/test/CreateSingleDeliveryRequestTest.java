package ca.cpggpc.est2_0.desktop.perftest;

import ca.cpggpc.est2_0.desktop.model.RemoteDeliveryResult;
import com.google.gson.Gson;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CreateSingleDeliveryRequestTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "CreateSingleDeliveryRequestTest";
    private static Logger logger = LoggerFactory.getLogger(CreateSingleDeliveryRequestTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(CONTRACT_ID_TAG,"<contractId>");
        defaultParameters.addArgument(SHIPPING_JOB_TAG,"<shippingJob>");
        //defaultParameters.addArgument(MOVE_DR_JOB_TAG,"<moveDRJob>");
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
        DeliveryRequest dr = null;
        StringBuilder strBuilder = new StringBuilder();
        Gson gson = new Gson();

        String jsonString = javaSamplerContext.getParameter(SHIPPING_JOB_TAG, "UndefinedShippingJob");
        Job shippingJob = gson.fromJson(jsonString, Job.class);
        //jsonString = javaSamplerContext.getParameter(MOVE_DR_JOB_TAG, "UndefinedMoveDRJob");
        //Job moveDRJob = gson.fromJson(jsonString, Job.class);

        String contractId = javaSamplerContext.getParameter(CONTRACT_ID_TAG, "UndefinedContractId");

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        logger.info("Create Remote Delivery Request Test in to BDT application with runNumber: [" + runNumber + "], shippingJob: [" + shippingJob.getId() + "]");

        // ... do some test
        logger.info("  ... in to BDT application with shippingJob: " + shippingJob + " contractId: " + contractId);

        sampleResult.sampleStart();
        try {
            // Create remote delivery request TEST
            //grinder.statistics.delayReports = 1
            //tests["CreateRemoteDeliveryRequest"].record(self.deliveryRequestTest, InstrumentationFilters("createRemoteDeliveryRequest"))
            logger.info("Create Remote Delivery Request for job: " + shippingJob.getName() + " contractId: " + contractId);
            dr = deliveryRequestTest.createRemoteDeliveryRequest(shippingJob, contractId);
            logger.info(" - Created DR: " + dr.getId().toString());
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step CreateRemoteDeliveryRequest completed successfully. ***");
            //logger.info("*** Test %d %s took %d milliseconds ***" % (tests["CreateRemoteDeliveryRequest"].getNumber(), tests["CreateRemoteDeliveryRequest"].getDescription(), grinder.statistics.forLastTest.time))

            RemoteDeliveryResult remoteDeliveryResult = new RemoteDeliveryResult(contractId, shippingJob, dr);
            String rdString = remoteDeliveryResult.toString();
            logger.info(rdString);
        } catch (Exception ex) {
            errorHandling(ex, BDT_TEST_NAME);
            success = false;
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setSuccessful(success);
            if (dr == null) {
                strBuilder.append("{\"deliveryRequest\" : \"null\", \"drId\" : \"null\" , \"status\" : \"null\"}");
            } else {
                strBuilder.append("{\"deliveryRequest\" : ");
                strBuilder.append(gson.toJson(dr));
                strBuilder.append(", \"drId\" : ");
                strBuilder.append(gson.toJson(dr.getId()));
                strBuilder.append(", \"status\" : \"");
                strBuilder.append(dr.getStatus().toString());
                strBuilder.append("\"}");
            }
            sampleResult.setResponseData(strBuilder.toString(), UTF_8.name());
        }

        return sampleResult;
    }
}
