package ca.cpggpc.est2_0.desktop.model;

import ca.cpggpc.est2_0.desktop.perftest.AbstractESTDesktopSamplerClient;
import com.google.gson.Gson;
import cpdt.domain.common.WorkgroupId;
import cpdt.domain.job.Job;

public class BDTInitResult {
    private WorkgroupId workgroup;
    private String contractId;
    private Job shippingJob;
    private Job moveDRJob;

    public BDTInitResult(WorkgroupId workgroup, String contractId, Job shippingJob, Job moveDRJob) {
        this.workgroup = workgroup;
        this.contractId = contractId;
        this.shippingJob = shippingJob;
        this.moveDRJob = moveDRJob;
    }

    public WorkgroupId getWorkgroup() {
        return workgroup;
    }

    public String getContractId() {
        return contractId;
    }

    public Job getShippingJob() {
        return shippingJob;
    }

    public Job getMoveDRJob() {
        return moveDRJob;
    }

    public String toJsonString() {
        Gson gson = new Gson();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{\"" + AbstractESTDesktopSamplerClient.WORKGRROUP_TAG + "\" : ");
        strBuilder.append(gson.toJson(workgroup));
        strBuilder.append(", \"" + AbstractESTDesktopSamplerClient.CONTRACT_ID_TAG + "\" : \"");
        strBuilder.append(contractId);
        strBuilder.append("\", \"" + AbstractESTDesktopSamplerClient.SHIPPING_JOB_TAG + "\" : ");
        strBuilder.append(gson.toJson(shippingJob));
        strBuilder.append(", \"" + AbstractESTDesktopSamplerClient.MOVE_DR_JOB_TAG + "\" : ");
        strBuilder.append(gson.toJson(moveDRJob));
        strBuilder.append("}");
        return  strBuilder.toString();
    }
}
