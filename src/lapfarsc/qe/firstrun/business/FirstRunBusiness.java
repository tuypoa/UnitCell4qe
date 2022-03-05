/**
 * @author tuypoa
 *
 *
 */
package lapfarsc.qe.firstrun.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lapfarsc.qe.firstrun.dto.AtomDTO;
import lapfarsc.qe.firstrun.dto.ComplexDTO;
import lapfarsc.qe.firstrun.dto.CrystalDTO;
import lapfarsc.qe.firstrun.util.Dominios;
import lapfarsc.qe.firstrun.util.Dominios.FileTypeEnum;

public class FirstRunBusiness {
	
	public void firstFitAtomsCrystal(ComplexDTO complex) {
		double span = 1; //angstrom

		double diffX = diffCoordCartesiana( complex.getMinX(), span);
		double diffY = diffCoordCartesiana( complex.getMinY(), span);
		double diffZ = diffCoordCartesiana( complex.getMinZ(), span);
		//System.out.println(diffX+";"+diffY+";"+diffZ);
		
		List<AtomDTO> listAtomDTO = complex.getListAtomDTO();
		for (AtomDTO a : listAtomDTO) {
			a.setX( a.getX() + diffX );
			a.setY( a.getY() + diffY );
			a.setZ( a.getZ() + diffZ );
			//System.out.println( a.getX()+";"+a.getY()+";"+a.getZ() );
		}
		
		//definir tamanho da celula unitaria de acordo com os atomos
		double ladoA = diffCoordCartesiana( complex.getMinX(), complex.getMaxX() );
		double ladoB = diffCoordCartesiana( complex.getMinY(), complex.getMaxY() );
		double ladoC = diffCoordCartesiana( complex.getMinZ(), complex.getMaxZ() );
		
		CrystalDTO crys = new CrystalDTO();
		crys.setA( (int) Math.ceil( ladoA + span*2 ) );
		crys.setB( (int) Math.ceil( ladoB + span*2 ) );
		crys.setC( (int) Math.ceil( ladoC + span*2 ) );
		//System.out.println(crys.getA()+";"+crys.getB()+";"+crys.getC());

		complex.setCrystalDTO(crys);
	}
	
	public void bestFitAtomsCrystal(ComplexDTO complex) {
		double span = .5; //angstrom
		
		if(complex.getCrystalDTO()==null) {
			firstFitAtomsCrystal(complex);
		}
		
		CrystalDTO crys = complex.getCrystalDTO();
		double menorVolume = crys.getA() * crys.getB() * crys.getC();
		List<AtomDTO> listAtomDTO = complex.getListAtomDTO();
		double melhorAtoms[][] = null;
		CrystalDTO melhorCrys = new CrystalDTO();
		
		
		double rotationX[][] = null; 
		double rotationY[][] = null;
		//double rotationZ[][] = null;
		
		//definir array xyz
		double atoms[][] = new double[listAtomDTO.size()][3];
		for (int i = 0; i < atoms.length; i++) {
			atoms[i][0] = listAtomDTO.get(i).getX();
			atoms[i][1] = listAtomDTO.get(i).getY();
			atoms[i][2] = listAtomDTO.get(i).getZ();
		}
	
		for (int anguloX = 0; anguloX <= 360; anguloX++) {
			rotationX = new double[][]{{1,0,0}, {0, Math.cos(anguloX), -1*Math.sin(anguloX)}, {0, Math.sin(anguloX), Math.cos(anguloX)}};
			
			for (int anguloY = 0; anguloY <= 360; anguloY++) {
				rotationY = new double[][]{{Math.cos(anguloY),0,Math.sin(anguloY)}, {0, 1, 0}, {-1*Math.sin(anguloY), 0, Math.cos(anguloY)}};
				
				//for (int anguloZ = 1; anguloZ <= 360; anguloZ++) {
				//	rotationZ = new double[][]{{Math.cos(anguloZ),-1*Math.sin(anguloZ),0}, {Math.sin(anguloZ), Math.cos(anguloZ), 0}, {0, 0, 1}};
				
				double movedAtoms[][] = new double[listAtomDTO.size()][3];
				
				double minX = 1000;
				double maxX = -1000;
				double minY = 1000;
				double maxY = -1000;
				double minZ = 1000;
				double maxZ = -1000;
				
				//mover molecula em 2 eixos	
				movedAtoms = multiplyMatrices(atoms, rotationX);
				movedAtoms = multiplyMatrices(movedAtoms, rotationY);
				//movedAtoms = multiplyMatrices(movedAtoms, rotationZ);
				
						
				for (int i = 0; i < movedAtoms.length; i++) {
					if(minX > movedAtoms[i][0]) minX = movedAtoms[i][0]; 
					if(minY > movedAtoms[i][1]) minY = movedAtoms[i][1]; 
					if(minZ > movedAtoms[i][2]) minZ = movedAtoms[i][2];
					if(maxX < movedAtoms[i][0]) maxX = movedAtoms[i][0]; 
					if(maxY < movedAtoms[i][1]) maxY = movedAtoms[i][1]; 
					if(maxZ < movedAtoms[i][2]) maxZ = movedAtoms[i][2];
				}
				
				if(minX <0) {
					double diffX = diffCoordCartesiana( minX, span);
					for (int i = 0; i < movedAtoms.length; i++) {
						movedAtoms[i][0] = movedAtoms[i][0] + diffX;
					}
				}
				if(minY <0) {
					double diffY = diffCoordCartesiana( minY, span);
					for (int i = 0; i < movedAtoms.length; i++) {
						movedAtoms[i][1] = movedAtoms[i][1] + diffY;
					}
				}
				if(minZ <0) {
					double diffZ = diffCoordCartesiana( minZ, span);
					for (int i = 0; i < movedAtoms.length; i++) {
						movedAtoms[i][2] = movedAtoms[i][2] + diffZ;
					}
				}
				//tamanho da celula unitaria
				double ladoA = diffCoordCartesiana( minX, maxX ) + span*2 ;
				double ladoB = diffCoordCartesiana( minY, maxY ) + span*2 ;
				double ladoC = diffCoordCartesiana( minZ, maxZ ) + span*2 ;
				
				if(maxX+span > ladoA) {
					double diffX = diffCoordCartesiana( ladoA, maxX+span);
					for (int i = 0; i < movedAtoms.length; i++) {
						movedAtoms[i][0] = movedAtoms[i][0] - diffX;
					}
				}
				if(maxY+span > ladoB) {
					double diffY = diffCoordCartesiana( ladoB, maxY+span);
					for (int i = 0; i < movedAtoms.length; i++) {
						movedAtoms[i][1] = movedAtoms[i][1] - diffY;
					}
				}
				if(maxZ+span > ladoC) {
					double diffZ = diffCoordCartesiana( ladoC, maxZ+span);
					for (int i = 0; i < movedAtoms.length; i++) {
						movedAtoms[i][2] = movedAtoms[i][2] - diffZ;
					}
				}
				
				double novoVolume = ladoA * ladoB * ladoC;
				if(menorVolume > novoVolume) {
					//guardar lista de atomos e volume novo
					menorVolume = novoVolume;
					melhorAtoms = movedAtoms.clone();
					melhorCrys.setA( (int) Math.ceil( ladoA ) );
					melhorCrys.setB( (int) Math.ceil( ladoB ) );
					melhorCrys.setC( (int) Math.ceil( ladoC ) );
				}
				//}
			}
		}
		//definir lista de atomos de menorVolume
		List<AtomDTO> lista = new ArrayList<AtomDTO>();
		for (int i = 0; i < melhorAtoms.length; i++) {
			AtomDTO a = listAtomDTO.get(i);
			a.setX( melhorAtoms[i][0] );
			a.setY( melhorAtoms[i][1] );
			a.setZ( melhorAtoms[i][2] );
			lista.add(a);
		}
		complex.setListAtomDTO(lista);
		complex.setCrystalDTO(melhorCrys);
	}
	
	private double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
		double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

	    for (int row = 0; row < result.length; row++) {
	        for (int col = 0; col < result[row].length; col++) {
	            result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
	        }
	    }

	    return result;
	}
	
	private double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
		double cell = 0;
	    for (int i = 0; i < secondMatrix.length; i++) {
	        cell += firstMatrix[row][i] * secondMatrix[i][col];
	    }
	    return cell;
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
		
		double minX = 1000;
		double maxX = -1000;
		double minY = 1000;
		double maxY = -1000;
		double minZ = 1000;
		double maxZ = -1000;
		
		for (String linha: lns) {
			if(linha.startsWith("HETATM") || linha.startsWith("ATOM")) {
/* estrutura da linha
HETATM    1 Br   PM6     1     -23.342  12.943   6.922  1.00  0.00          Br  
 */
				AtomDTO ato = new AtomDTO();
				ato.setLinha(linha);
				ato.setElementId( linha.substring( 7, 11).trim() );
				ato.setElementType( linha.substring( 12, 14) );
				ato.setX( Double.parseDouble( linha.substring( 31, 38) ) );
				ato.setY( Double.parseDouble( linha.substring( 39, 46) ) );
				ato.setZ( Double.parseDouble( linha.substring( 47, 54) ) );
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

	public double diffCoordCartesiana(double num1, double num2) {
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
		
		String template = loadTextFileResource( this.getClass()
            .getClassLoader()
            .getResourceAsStream("lapfarsc/qe/firstrun/resources/"+tipo.getTemplate())  );
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
			template = template.replace("{FILENAME}", fileOutput.getName().replaceAll(Dominios.PREFIX,"").replaceAll(tipo.getExtensao(),""));
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
			if(sba.length()>0) {
				template = template.replace("{LIST_ATOM_CONNECT}", sba.toString().substring(0, sba.length()-1)); 
			}else {
				template = template.replace("\n{LIST_ATOM_CONNECT}", "");
			}
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
			template = template.replace("{FILENAME}", fileOutput.getName().replaceAll(Dominios.PREFIX,"").replaceAll(tipo.getExtensao(),""));
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
				String linha =  a.getElementType().trim().toUpperCase() + " " +
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

	
	public String loadTextFileResource(InputStream is) throws IOException {
		StringBuilder conteudo = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(is); 
                BufferedReader br = new BufferedReader(isr);) 
        {
            String line;
            while ((line = br.readLine()) != null) {
                conteudo.append(line).append("\n");
            }
            is.close();
        }
        return conteudo.toString();
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
