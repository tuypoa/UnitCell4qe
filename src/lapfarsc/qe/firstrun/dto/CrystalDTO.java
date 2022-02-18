package lapfarsc.qe.firstrun.dto;

public class CrystalDTO {

	
	private Integer a;
	private Integer b;
	private Integer c;
	private Integer alpha;
	private Integer beta;
	private Integer gamma;
	
	public CrystalDTO() {
		this.alpha = 90;
		this.beta = 90;
		this.gamma = 90;
	}
	
	public Integer getA() {
		return a;
	}
	public void setA(Integer a) {
		this.a = a;
	}
	public Integer getB() {
		return b;
	}
	public void setB(Integer b) {
		this.b = b;
	}
	public Integer getC() {
		return c;
	}
	public void setC(Integer c) {
		this.c = c;
	}
	public Integer getAlpha() {
		return alpha;
	}
	
	public Integer getBeta() {
		return beta;
	}
	
	public Integer getGamma() {
		return gamma;
	}
	
}
