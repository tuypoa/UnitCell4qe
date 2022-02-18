package lapfarsc.qe.firstrun.dto;

import java.util.List;

import lapfarsc.qe.firstrun.util.Dominios.FileTypeEnum;

public class ComplexDTO {

	private FileTypeEnum fileType;
	
	private List<String> file;
	
	private List<AtomDTO> listAtomDTO;
	
	private CrystalDTO crystalDTO;
	

	private Float minX;
	private Float maxX;
	
	private Float minY;
	private Float maxY;
	
	private Float minZ;
	private Float maxZ;
	
	
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
	
	public Float getMinX() {
		return minX;
	}
	public void setMinX(Float minX) {
		this.minX = minX;
	}
	public Float getMaxX() {
		return maxX;
	}
	public void setMaxX(Float maxX) {
		this.maxX = maxX;
	}
	public Float getMinY() {
		return minY;
	}
	public void setMinY(Float minY) {
		this.minY = minY;
	}
	public Float getMaxY() {
		return maxY;
	}
	public void setMaxY(Float maxY) {
		this.maxY = maxY;
	}
	public Float getMinZ() {
		return minZ;
	}
	public void setMinZ(Float minZ) {
		this.minZ = minZ;
	}
	public Float getMaxZ() {
		return maxZ;
	}
	public void setMaxZ(Float maxZ) {
		this.maxZ = maxZ;
	}
	public FileTypeEnum getFileType() {
		return fileType;
	}
	public void setFileType(FileTypeEnum fileType) {
		this.fileType = fileType;
	}
	
	
	
}
