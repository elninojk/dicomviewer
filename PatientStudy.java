package dicomviewer;

import java.util.*;              /// POSSIBLE EXCEPTION//UTIL DATE

public class PatientStudy {

	private String patientId;

	private String patientName;

	private String patientDOB;

	private String accessionNumber;

	private String studyId;

	private String studyDescription;

	private String studyDateTime;

	public PatientStudy(String patientId, String patientName, String patientDOB, String accessionNumber,
			String studyId, String studyDescription, String studyDateTime) {
		super();
		this.patientId = patientId;
		this.patientName = patientName;
		this.patientDOB = patientDOB;
		this.accessionNumber = accessionNumber;
		this.studyId = studyId;
		this.studyDescription = studyDescription;
		this.studyDateTime = studyDateTime;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientDOB() {
		return patientDOB;
	}

	public void setPatientDOB(String patientDOB) {
		this.patientDOB = patientDOB;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getStudyId() {
		return studyId;
	}

	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}

	public String getStudyDescription() {
		return studyDescription;
	}

	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}

	public String getStudyDateTime() {
		return studyDateTime;
	}

	public void setStudyDateTime(String studyDateTime) {
		this.studyDateTime = studyDateTime;
	}
	
	
}
