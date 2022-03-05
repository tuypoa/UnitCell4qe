package lapfarsc.qe.firstrun.util;

public class Dominios {

	public static String PREFIX = "UnitCell4qe";
	
	
	public enum FileTypeEnum{
		PDB(1,".pdb", "TEMPLATE_PDB_FILE.pdb"),
		CIF(2,".cif", "TEMPLATE_CIF_FILE.cif"),
		IN(2,".in", "TEMPLATE_IN_FILE.in"),;
		int i;
		String extensao;
		String template;
		FileTypeEnum(int i, String extensao, String template){this.i = i; this.extensao=extensao; this.template=template; }
		public int getIndex(){return this.i;}
		public String getExtensao(){return this.extensao;}
		public String getTemplate(){return this.template;}
		public static FileTypeEnum getByIndex(int index){
			for (FileTypeEnum e : FileTypeEnum.values()) {
				if( e.i == index ){
					return e;
				}
			}
			return null;
		}
	}
}
