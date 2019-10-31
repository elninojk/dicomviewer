package model;

public class Series {

	private String seriesId;

	private String studyId;

	private String seriesNumber;

	private String modality;

	private String seriesDescription;

	public Series(String seriesId, String patientId, String seriesNumber, String modality, String seriesDescription) {
		super();
		this.seriesId = seriesId;
		this.studyId = patientId;
		this.seriesNumber = seriesNumber;
		this.modality = modality;
		this.seriesDescription = seriesDescription;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public String getSeriesNumber() {
		return seriesNumber;
	}

	public String getModality() {
		return modality;
	}

	public String getSeriesDescription() {
		return seriesDescription;
	}

	public String getStudyId() {
		return studyId;
	}

}
