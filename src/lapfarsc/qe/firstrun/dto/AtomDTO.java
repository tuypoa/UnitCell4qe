package lapfarsc.qe.firstrun.dto;

public class AtomDTO {

	private String elementId;
	private String elementType;
	private Float x;
	private Float y;
	private Float z;
	
	private String linha;
	

	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getLinha() {
		return linha;
	}
	public void setLinha(String linha) {
		this.linha = linha;
	}
	public Float getX() {
		return x;
	}
	public void setX(Float x) {
		this.x = x;
	}
	public Float getY() {
		return y;
	}
	public void setY(Float y) {
		this.y = y;
	}
	public Float getZ() {
		return z;
	}
	public void setZ(Float z) {
		this.z = z;
	}

}
