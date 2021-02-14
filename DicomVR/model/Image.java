package model;

public class Image {
	private String imageNumber;

	private String studyId;

	private String seriesId;

	private String imageType;

	private String rows;

	private String columns;

	private String bitsAllocated;

	private String bitsStored;

	public Image(String imageNumber, String studyId, String seriesId, String imageType, String rows, String columns,
			String bitsAllocated, String bitsStored) {
		super();
		this.imageNumber = imageNumber;
		this.studyId = studyId;
		this.seriesId = seriesId;
		this.imageType = imageType;
		this.rows = rows;
		this.columns = columns;
		this.bitsAllocated = bitsAllocated;
		this.bitsStored = bitsStored;
	}

	public String getImageNumber() {
		return imageNumber;
	}

	public String getImageType() {
		return imageType;
	}

	public String getRows() {
		return rows;
	}

	public String getColumns() {
		return columns;
	}

	public String getBitsAllocated() {
		return bitsAllocated;
	}

	public String getBitsStored() {
		return bitsStored;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public String getStudyId() {
		return studyId;
	}

}
