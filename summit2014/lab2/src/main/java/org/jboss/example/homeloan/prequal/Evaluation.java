
package org.jboss.example.homeloan.prequal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.xml.namespace.QName;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.Qualification;
import org.kie.api.task.model.TaskSummary;
import org.switchyard.component.bpm.runtime.BPMTaskService;
import org.switchyard.component.bpm.runtime.BPMTaskServiceRegistry;

@Named("evaluation")
@RequestScoped
public class Evaluation {

    private static final String EN_UK = "en-UK";

    private final BPMTaskService taskService;
    private String _userId = "mrbill";
    private TaskSummary task;
    private Applicant applicant;
    private Qualification qualification;

    public Evaluation() {
        taskService = BPMTaskServiceRegistry.getTaskService(
        		null, new QName("urn:homeloan:1.0", "PreQualificationService"));
        fetchTasks();
    }
    
    public void complete() {
    	HashMap<String, Object> results = new HashMap<String, Object>();
    	results.put("qualification", qualification);
    	taskService.claim(task.getId(), _userId);
        taskService.start(task.getId(), _userId);
        taskService.complete(task.getId(), _userId, results);
        
        applicant = null;
        qualification = null;
    }
    

    private void fetchTasks() {
    	List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(_userId, EN_UK);
        for (TaskSummary task : tasks) {
            this.task = task;
            Map<String, Object> params = taskService.getTaskContent(task.getId());
            applicant = (Applicant)params.get("applicant");
            qualification = (Qualification)params.get("qualification");
            // only one task at a time at this point
            break;
        }
    }

	public String getName() {
		return applicant != null ? applicant.getFirstName() + " " + applicant.getLastName() : "";
	}
	
	public String getSsn() {
		return applicant != null ? applicant.getSsn() : "";
	}
	
	public int getCreditScore() {
		return applicant != null ? applicant.getCreditScore() : 0;
	}
	
	public String getRate() {
		return qualification != null ? qualification.getRate() : "";
	}
	
	public void setRate(String rate) {
		qualification.setRate(rate);
	}
	
	public String getExplanation() {
		return qualification != null ? qualification.getExplanation() : "";
	}
	
	public void setExplanation(String explanation) {
		qualification.setExplanation(explanation);
	}
	
	public void setInsuranceCost(int cost) {
		qualification.setCost(cost);
	}
	
	public int getInsuranceCost() {
		return qualification != null ? qualification.getCost() : 0;
	}
	
	public void setStatus(String status) {
		qualification.setStatus(status);
	}
	
	public String getStatus() {
		return qualification != null ? qualification.getStatus() : "";
	}

}
