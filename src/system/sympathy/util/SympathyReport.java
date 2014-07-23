package system.sympathy.util;

import java.util.ArrayList;

public class SympathyReport {
	private int modeId;
	private ArrayList<String> failures;
	private ArrayList<String> failureSuggestions;
	private ArrayList<String> individualSuggestions;
	
	public SympathyReport(int modeId) {
		super();
		this.modeId = modeId;
		this.failures = new ArrayList<String>();
		this.failureSuggestions = new ArrayList<String>();
		this.individualSuggestions = new ArrayList<String>();
	}
	
	public void addIndividualSuggestion(String s) {
		this.individualSuggestions.add(s);
	}
	
	public void addFailureSuggestion(String s) {
		this.failureSuggestions.add(s);
	}
	
	public void addFailure(String f) {
		this.failures.add(f);
	}

	public int getModeId() {
		return modeId;
	}

	public void setModeId(int modeId) {
		this.modeId = modeId;
	}

	public ArrayList<String> getFailureSuggestions() {
		return failureSuggestions;
	}

	public void setFailureSuggestions(ArrayList<String> failureSuggestions) {
		this.failureSuggestions = failureSuggestions;
	}

	public ArrayList<String> getFailures() {
		return failures;
	}

	public void setFailures(ArrayList<String> failures) {
		this.failures = failures;
	}
	
	public ArrayList<String> getIndividualSuggestions() {
		return individualSuggestions;
	}

	public void setIndividualSuggestions(ArrayList<String> individualSuggestions) {
		this.individualSuggestions = individualSuggestions;
	}

	public void clear() {
		this.failures.clear();
		this.failureSuggestions.clear();
	}

	@Override
	public String toString() {
		return "SympathyReport [modeId=" + modeId + ", failures=" + failures
				+ ", failureSuggestions=" + failureSuggestions
				+ ", individualSuggestions=" + individualSuggestions + "]";
	}
	
}