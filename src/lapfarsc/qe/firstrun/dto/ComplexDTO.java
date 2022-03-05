package lapfarsc.qe.firstrun.dto;

import java.util.List;

import lapfarsc.qe.firstrun.util.Dominios.FileTypeEnum;

public class ComplexDTO {

	private FileTypeEnum fileType;
	
	private List<String> file;
	
	private List<AtomDTO> listAtomDTO;
	
	private CrystalDTO crystalDTO;
	

	private Double minX;
	private Double maxX;
	
	private Double minY;
	private Double maxY;
	
	private Double minZ;
	private Double maxZ;
	
	
	public List<AtomDTO> getListAtomDTO() {
		return listAtomDTO;
	}
	public void setListAtomDTO(List<AtomDTO> listAtomDTO) {
		this.listAtomDTO = listAtomDTO;
	}
	
	public CrystalDTO getCrystalDTO() {
		return crystalDTO;
	}
	public void setCrystalDTO(CrystalDTO crystalDTO) {
		this.crystalDTO = crystalDTO;
	}
	public List<String> getFile() {
		return file;
	}
	public void setFile(List<String> file) {
		this.file = file;
	}
	
	public Double getMinX() {
		return minX;
	}
	public void setMinX(Double minX) {
		this.minX = minX;
	}
	public Double getMaxX() {
		return maxX;
	}
	public void setMaxX(Double maxX) {
		this.maxX = maxX;
	}
	public Double getMinY() {
		return minY;
	}
	public void setMinY(Double minY) {
		this.minY = minY;
	}
	public Double getMaxY() {
		return maxY;
	}
	public void setMaxY(Double maxY) {
		this.maxY = maxY;
	}
	public Double getMinZ() {
		return minZ;
	}
	public void setMinZ(Double minZ) {
		this.minZ = minZ;
	}
	public Double getMaxZ() {
		return maxZ;
	}
	public void setMaxZ(Double maxZ) {
		this.maxZ = maxZ;
	}
	public FileTypeEnum getFileType() {
		return fileType;
	}
	public void setFileType(FileTypeEnum fileType) {
		this.fileType = fileType;
	}
	
	
	
}
