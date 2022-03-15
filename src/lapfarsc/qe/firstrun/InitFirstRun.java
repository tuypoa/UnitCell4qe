/**
 * @author tuypoa
 *
 *
 */
package lapfarsc.qe.firstrun;

import java.io.File;
import java.io.FileFilter;

import lapfarsc.qe.firstrun.business.FirstRunBusiness;
import lapfarsc.qe.firstrun.dto.ComplexDTO;
import lapfarsc.qe.firstrun.util.Dominios;
import lapfarsc.qe.firstrun.util.Dominios.FileTypeEnum;

public class InitFirstRun {


	public static void main(String[] args) throws Exception{
		int countGravados = 0;
		int countLidos = 0;
		
		try {
			//System.out.println("InitFirstRun...");
			if(args==null || args.length < 1) {
				System.out.println("--> Arg0 (PDB'S FILE PATH) IS REQUIRED.");
				return;
			}
			String rootPath = args[0];
			
			File path = new File(rootPath);
			
			if(!path.exists()) {
				System.out.println("--> PATH DOES NOT EXIST: "+path.getAbsolutePath());
				return;
			}
			
			File[] arquivos = path.listFiles(new FileFilter() {	
				@Override
				public boolean accept(File arg0) {
					return !arg0.getName().startsWith(Dominios.PREFIX) && 
							arg0.getName().toLowerCase().endsWith(".pdb");
				}
			});
			
			if(arquivos==null || arquivos.length == 0) {
				System.out.println("--> PDB'S FILE NOT FOUND: "+path.getAbsolutePath());
				return;
			}
			
			FirstRunBusiness firstrun = new FirstRunBusiness();
			
			
			if(arquivos!=null && arquivos.length > 0) {
				for (File file : arquivos) {
					ComplexDTO complex = firstrun.loadPDBComplexDTO(file);
					countLidos++;
					if(complex!=null) {
						//reposicionar atomos
						firstrun.firstFitAtomsCrystal(complex);
						
						//gravar arquivo de resultado em CIF
						firstrun.gravarComplexDTO(path.getAbsolutePath()+File.separator+ Dominios.PREFIX+"-default_"+file.getName(), 
								FileTypeEnum.CIF, complex);
						countGravados++;
						/*firstrun.gravarComplexDTO(path.getAbsolutePath()+File.separator+ Dominios.PREFIX+"_"+file.getName(), 
								FileTypeEnum.IN, complex);
						countGravados++;
						*/
						
						//caixa com menor volume atomos
						firstrun.bestFitAtomsCrystal(complex);
						
						firstrun.gravarComplexDTO(path.getAbsolutePath()+File.separator+ Dominios.PREFIX+"-rotated_"+file.getName(), 
								FileTypeEnum.CIF, complex);
						countGravados++;
						/*firstrun.gravarComplexDTO(path.getAbsolutePath()+File.separator+ Dominios.PREFIX+"_BestFit_"+file.getName(), 
								FileTypeEnum.IN, complex);
						countGravados++;*/
					}
	
					//break;
				}
			}
		}finally {
			System.out.println( ">> "+ countLidos + " READ OK.");
			System.out.println( ">> "+ countGravados + " SAVED OK.");
			System.out.println("END.");	
		}
	}
	
	
	
	
}
