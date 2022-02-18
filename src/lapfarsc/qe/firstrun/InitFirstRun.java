/**
 * @author tuypoa
 *
 *
 */
package lapfarsc.qe.firstrun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lapfarsc.qe.firstrun.dto.AtomDTO;
import lapfarsc.qe.firstrun.dto.ComplexDTO;
import lapfarsc.qe.firstrun.dto.CrystalDTO;
import lapfarsc.qe.firstrun.util.Dominios.FileTypeEnum;

public class InitFirstRun {

	public static String PREFIX = "UnitCell4qe";
	public static String TEMPLATES = "scripts/";
	
	//PATH OF PDB FILES EXTRACTED FROM MOLEGRO TO BE PROCESSED
	public static String ARQUIVOS_PDB = "/04-unitcell/"+File.separator+PREFIX+File.separator;
	
	private String rootPath = null;
	
	public static void main(String[] args) throws Exception{
		int countGravados = 0;
		int countLidos = 0;
		
		try {
			//System.out.println("InitFirstRun...");
			if(args==null || args.length < 2) {
				System.out.println("--> Arg0 (ROOT SYSTEM FOLDER) and Arg1 (MOLECULE NAME FOLDER) IS REQUIRED.");
				return;
			}
			String rootPath = args[0];
			String molecula = args[1];
			
			File path = new File(rootPath + File.separator + molecula + File.separator + ARQUIVOS_PDB);
			
			if(!path.exists()) {
				System.out.println("--> PATH DOS NOT EXIST: "+path.getAbsolutePath());
				return;
			}
			
			File[] arquivos = path.listFiles(new FileFilter() {	
				@Override
				public boolean accept(File arg0) {
					return !arg0.getName().startsWith(PREFIX) && 
							arg0.getName().toLowerCase().endsWith(".pdb");
				}
			});
			
			InitFirstRun init = new InitFirstRun();
			init.rootPath  = rootPath;
			
			float span = 1; //angstrom

			if(arquivos!=null && arquivos.length > 0) {
				for (File file : arquivos) {
					ComplexDTO complex = init.loadPDBComplexDTO(file);
					countLidos++;
					if(complex!=null) {
						//reposicionar atomos
						float diffX = init.diffCoordCartesiana( complex.getMinX(), span);
						float diffY = init.diffCoordCartesiana( complex.getMinY(), span);
						float diffZ = init.diffCoordCartesiana( complex.getMinZ(), span);
						//System.out.println(diffX+";"+diffY+";"+diffZ);
						
						List<AtomDTO> listAtomDTO = complex.getListAtomDTO();
						for (AtomDTO a : listAtomDTO) {
							a.setX( a.getX() + diffX );
							a.setY( a.getY() + diffY );
							a.setZ( a.getZ() + diffZ );
							//System.out.println( a.getX()+";"+a.getY()+";"+a.getZ() );
						}
						
						//definir tamanho da celula unitaria de acordo com os atomos
						float ladoA = init.diffCoordCartesiana( complex.getMinX(), complex.getMaxX() );
						float ladoB = init.diffCoordCartesiana( complex.getMinY(), complex.getMaxY() );
						float ladoC = init.diffCoordCartesiana( complex.getMinZ(), complex.getMaxZ() );
						
						CrystalDTO crys = new CrystalDTO();
						crys.setA( (int) Math.ceil( ladoA + span*2 ) );
						crys.setB( (int) Math.ceil( ladoB + span*2 ) );
						crys.setC( (int) Math.ceil( ladoC + span*2 ) );
						//System.out.println(crys.getA()+";"+crys.getB()+";"+crys.getC());
	
						complex.setCrystalDTO(crys);
						
						//gravar arquivo de resultado PDB e CIF
						init.gravarComplexDTO(path.getAbsolutePath()+File.separator+ PREFIX+"_"+file.getName(), 
								FileTypeEnum.PDB, complex);
						countGravados++;
						
						init.gravarComplexDTO(path.getAbsolutePath()+File.separator+ PREFIX+"_"+file.getName(), 
								FileTypeEnum.CIF, complex);
						countGravados++;
					}
	
					break;
				}
			}
		}finally {
			System.out.println( ">> "+ countLidos + " READ OK.");
			System.out.println( ">> "+ countGravados + " SAVED OK.");
			System.out.println("END.");	
		}
	}
	
	
	
	
	public List<String> getListLines(String conteudo) throws Exception {
		List<String> lns = new ArrayList<String>();
		if(conteudo!=null) {
			String[] linha = conteudo.split("\n");
			//System.out.println("Count lines: "+linha.length);
			for (String string : linha) {
				lns.add(string);
				//System.out.println(string);
			}
		}
		return lns;
	}
	
	
	public ComplexDTO loadPDBComplexDTO(File file) throws Exception {
		List<AtomDTO> lista = new ArrayList<AtomDTO>();
		List<String> lns = getListLines( loadTextFile(file) );
		
		float minX = 1000;
		float maxX = -1000;
		float minY = 1000;
		float maxY = -1000;
		float minZ = 1000;
		float maxZ = -1000;
		
		for (String linha: lns) {
			if(linha.startsWith("HETATM") || linha.startsWith("ATOM")) {
/* estrutura da linha
HETATM    1 Br   PM6     1     -23.342  12.943   6.922  1.00  0.00          Br  
 */
				AtomDTO ato = new AtomDTO();
				ato.setLinha(linha);
				ato.setElementId( linha.substring( 7, 11).trim() );
				ato.setElementType( linha.substring( 12, 14) );
				ato.setX( Float.parseFloat( linha.substring( 31, 38) ) );
				ato.setY( Float.parseFloat( linha.substring( 39, 46) ) );
				ato.setZ( Float.parseFloat( linha.substring( 47, 54) ) );
				/*
				System.out.println( ato.getElementId() );
				System.out.println( ato.getElementType() );
				System.out.println( ato.getX() );
				System.out.println( ato.getY() );
				System.out.println( ato.getZ() ); System.exit(0); 
				*/				
				lista.add(ato);
								
				if(minX > ato.getX()) minX = ato.getX(); 
				if(minY > ato.getY()) minY = ato.getY(); 
				if(minZ > ato.getZ()) minZ = ato.getZ();
				if(maxX < ato.getX()) maxX = ato.getX(); 
				if(maxY < ato.getY()) maxY = ato.getY(); 
				if(maxZ < ato.getZ()) maxZ = ato.getZ();
			}
		}
		/*
		System.out.println( minX+";"+minY+";"+minZ );
		System.out.println( maxX+";"+maxY+";"+maxZ );
		System.exit(0); 
	     */
		
		ComplexDTO dto = new ComplexDTO();
		dto.setFileType(FileTypeEnum.PDB);
		dto.setListAtomDTO(lista);
		dto.setFile(lns);
		dto.setMinX( minX );
		dto.setMinY( minY );
		dto.setMinZ( minZ );
		dto.setMaxX( maxX );
		dto.setMaxY( maxY );
		dto.setMaxZ( maxZ );
		//dto.setCrystalDTO(null);
		
		//System.out.println( diffCoordCartesiana( maxZ, minZ ) );
		return dto;
		
	}

	public float diffCoordCartesiana(float num1, float num2) {
		if(num1 <0 && num2 <0) {
			return (num1 - num2)*-1;
		}else if(num1 >0 && num2 >0) {
			return num2 - num1;
		}else if(num1 <0 && num2 >0) {
			return num2 + (num1*-1);
		}else if(num1 >0 && num2 <0) {
			return num1 + (num2*-1);
		}		
		return 0;
	}

	public void gravarComplexDTO(String path, FileTypeEnum tipo, ComplexDTO complex) throws IOException {
		//System.out.println(path);
		String template = loadTextFile(new File(rootPath + File.separator + TEMPLATES + File.separator + tipo.getTemplate() ));
		path = path.substring( 0, path.lastIndexOf( "." ) )+ tipo.getExtensao();
		
		File fileOutput = new File( path );
		
		StringBuilder sba;
		List<AtomDTO> la;
		
		switch (tipo) {
		case PDB:
			//alterar variaveis
			/*COMPND    {FILENAME}
			CRYST1   {CELL_A}.000   {CELL_B}.000   {CELL_C}.000  {CELL_ALPHA}.00  {CELL_BETA}.00  {CELL_GAMMA}.00 P1          1
			{LIST_ATOM_COORD_CRYSTAL}
			{LIST_ATOM_CONNECT}
			*/
			template = template.replace("{FILENAME}", fileOutput.getName().replaceAll(PREFIX,"").replaceAll(tipo.getExtensao(),""));
			template = template.replace("{CELL_A}", String.format("%2d", complex.getCrystalDTO().getA() ) );
			template = template.replace("{CELL_B}", String.format("%2d", complex.getCrystalDTO().getB() ) );
			template = template.replace("{CELL_C}", String.format("%2d", complex.getCrystalDTO().getC() ) );
			template = template.replace("{CELL_ALPHA}", String.format("%2d", complex.getCrystalDTO().getAlpha() ) );
			template = template.replace("{CELL_BETA}", String.format("%2d", complex.getCrystalDTO().getBeta() ) );
			template = template.replace("{CELL_GAMMA}", String.format("%2d", complex.getCrystalDTO().getGamma() ) );			
			
			sba = new StringBuilder();
			la = complex.getListAtomDTO();
			for (AtomDTO a : la) {
				//HETATM    1 Br   PM6     1     -23.342  12.943   6.922  1.00  0.00          Br  
				String linha = a.getLinha();
				linha = linha.substring(0, 31) +  
						String.format("%7.3f", a.getX()) + " " +
						String.format("%7.3f", a.getY()) + " " +
						String.format("%7.3f", a.getZ()) + " " +
						
						linha.substring(54, linha.length()); 
				sba.append( linha ).append("\n");
			}
			template = template.replace("{LIST_ATOM_COORD_CRYSTAL}", sba.toString().substring(0, sba.length()-1)); 
				
			sba = new StringBuilder();
			List<String> ls = complex.getFile();
			for (String s : ls) {
				if(s.startsWith("CONECT")) {
					sba.append( s ).append("\n");
				}
			}
			template = template.replace("{LIST_ATOM_CONNECT}", sba.toString().substring(0, sba.length()-1)); 
			
			break;
		case CIF:
			/*
			 UC_{FILENAME}
			_cell_length_a                   {CELL_A}
			_cell_length_b                   {CELL_A}
			_cell_length_c                   {CELL_C}
			_cell_angle_alpha                {CELL_ALPHA}
			_cell_angle_beta                 {CELL_BETA}
			_cell_angle_gamma                {CELL_GAMMA}
			_cell_volume                     {CELL_VOLUME} 
			 {LIST_ATOM_COORD_CRYSTAL}
			 */
			template = template.replace("{FILENAME}", fileOutput.getName().replaceAll(PREFIX,"").replaceAll(tipo.getExtensao(),""));
			template = template.replace("{CELL_A}", String.format("%d", complex.getCrystalDTO().getA() ) );
			template = template.replace("{CELL_B}", String.format("%d", complex.getCrystalDTO().getB() ) );
			template = template.replace("{CELL_C}", String.format("%d", complex.getCrystalDTO().getC() ) );
			template = template.replace("{CELL_ALPHA}", String.format("%d", complex.getCrystalDTO().getAlpha() ) );
			template = template.replace("{CELL_BETA}", String.format("%d", complex.getCrystalDTO().getBeta() ) );
			template = template.replace("{CELL_GAMMA}", String.format("%d", complex.getCrystalDTO().getGamma() ) );			
			template = template.replace("{CELL_VOLUME}", String.format("%d", complex.getCrystalDTO().getA() * complex.getCrystalDTO().getB() * complex.getCrystalDTO().getC() ) );			
			
			sba = new StringBuilder();
			la = complex.getListAtomDTO();
			for (AtomDTO a : la) {
				//BR Br 0.287937 0.572722 0.729308
				String linha = a.getLinha();
				linha = a.getElementType().trim().toUpperCase() + " " +
						a.getElementType().trim() + " " +
						String.format("%6f", a.getX() / complex.getCrystalDTO().getA() ) + " " +
						String.format("%6f", a.getY() / complex.getCrystalDTO().getB() ) + " " +
						String.format("%6f", a.getZ() / complex.getCrystalDTO().getC() ) ; 
				sba.append( linha ).append("\n");
			}
			template = template.replace("{LIST_ATOM_COORD_CRYSTAL}", sba.toString().substring(0, sba.length()-1)); 
				
			break;
		}
		saveTextFile(fileOutput, template);
	}

	
	public String loadTextFile(File file) throws IOException {
		FileReader fr = null;
	    BufferedReader br = null;
		try{
			StringBuilder conteudo = new StringBuilder();
			fr = new FileReader(file);											
			br = new BufferedReader(fr);
	        int read, N = 1024;
	        char[] buffer = new char[N];
	        
	        //int i = 0;			        
	        while(true) {
	            read = br.read(buffer, 0, N);
	            String text = new String(buffer, 0, read);
	            conteudo.append(text);
	            if(read < N){
	            	if(conteudo.length()>0){
	            		return conteudo.toString();
	            	}
	            	break;
	            }		            
	        }
		}finally{
			if(br!=null) br.close();
			if(fr!=null) fr.close();
		}
		return null;
	}

	public void saveTextFile(File fileOutput, String conteudo) throws IOException {
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(fileOutput);
			fos.write(conteudo.getBytes());
			fos.flush();
		}finally{
			if(fos!=null) fos.close();
		}
	}
	
	
}
