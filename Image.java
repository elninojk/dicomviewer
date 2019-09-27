package dicomviewer;

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

	public void setImageNumber(String imageNumber) {
		this.imageNumber = imageNumber;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getBitsAllocated() {
		return bitsAllocated;
	}

	public void setBitsAllocated(String bitsAllocated) {
		this.bitsAllocated = bitsAllocated;
	}

	public String getBitsStored() {
		return bitsStored;
	}

	public void setBitsStored(String bitsStored) {
		this.bitsStored = bitsStored;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public String getStudyId() {
		return studyId;
	}

	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}
	
	

}
