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

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public String getSeriesNumber() {
		return seriesNumber;
	}

	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getSeriesDescription() {
		return seriesDescription;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}

	public String getStudyId() {
		return studyId;
	}

	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}

	@Override
	public String toString() {
		return "Series [seriesId=" + seriesId + ", studyId=" + studyId + ", seriesNumber=" + seriesNumber
				+ ", modality=" + modality + ", seriesDescription=" + seriesDescription + "]";
	}
	
	
}
