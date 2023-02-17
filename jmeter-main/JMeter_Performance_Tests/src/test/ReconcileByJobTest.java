package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.job.Job;
import cpdt.domain.recon.InductionSetHeader;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.google.common.base.Charsets.UTF_8;

public class ReconcileByJobTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ReconcileByJobTest";
    private static final String ARG1_TAG = "arg1";
    private static Logger logger = LoggerFactory.getLogger(ReconcileByJobTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(SHIPPING_JOB_TAG, "<shippingJob>");
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
        Collection<InductionSetHeader> indSetHeaders = null;
        int numReconcileDr = 0;

        Gson gson = new Gson();
        String shippingJobJson = javaSamplerContext.getParameter(SHIPPING_JOB_TAG, "0");
        Job shippingJob = gson.fromJson(shippingJobJson, Job.class);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber + "  shippingJob: " + shippingJob.getId());

        StringBuilder response = new StringBuilder();
        sampleResult.sampleStart();

        try{
            // Reconcile delivery requests by Job
            //grinder.statistics.delayReports = 1
            indSetHeaders = deliveryRequestTest.setupReconcile(shippingJob.getId());
            logger.info(" - Obtained an induction set of " + indSetHeaders.size() + " orders.");
            //tests["ReconcileDeliveryRequestsByJob"].record(self.deliveryRequestTest, InstrumentationFilters("reconcileDeliveryRequestByJobId"))
            numReconcileDr = deliveryRequestTest.reconcileDeliveryRequestByJobId(indSetHeaders);
            logger.info(" - Reconciled " + numReconcileDr + " DRs from job folder " + shippingJob.getId());
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step ReconcileDeliveryRequestsByJob completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["ReconcileDeliveryRequestsByJob"].getNumber(), tests["ReconcileDeliveryRequestsByJob"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            response.append("{");
            if (indSetHeaders != null) {
                response.append("\"indSetHeaders\" : [");
                for (InductionSetHeader hdr : indSetHeaders) {
                    response.append("{\"contractId\" : \"");
                    response.append(hdr == null ? "null" : hdr.getContractId().getContractId());
                    response.append("\", \"status\" : \"");
                    response.append(hdr == null ? "null" : hdr.getStatus().toString());
                    response.append("\"},\n");
                }
                response.append("],");
            }
            response.append("\"numReconcileDR\" : ");
            response.append(numReconcileDr);
            response.append("}");
            sampleResult.setResponseData(response.toString(), UTF_8.name());
        }

        sampleResult.setResponseMessageOK();
        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
