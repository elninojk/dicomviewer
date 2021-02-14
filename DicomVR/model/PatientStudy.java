package model;

public class PatientStudy {

	private String patientId;

	private String patientName;

	private String patientDOB;

	private String accessionNumber;

	private String studyId;

	private String studyDescription;

	private String studyDateTime;

	public PatientStudy(String patientId, String patientName, String patientDOB, String accessionNumber, String studyId,
			String studyDescription, String studyDateTime) {
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

	public String getPatientName() {
		return patientName;
	}

	public String getPatientDOB() {
		return patientDOB;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public String getStudyId() {
		return studyId;
	}

	public String getStudyDescription() {
		return studyDescription;
	}

	public String getStudyDateTime() {
		return studyDateTime;
	}

}
