/**
 * @author tuypoa
 *
 *
 */
package lapfarsc.qe.firstrun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import lapfarsc.qe.firstrun.dto.AtomoDTO;

public class InitFirstRun {

	
	public static void main(String[] args) throws Exception{		
		//System.out.println("InitFirstRun...");
		
		String p = args[0];
		
		
		File file = new File(p);
		
		List<AtomoDTO> lista = new ArrayList<AtomoDTO>();
		BufferedReader br = null;
		try{
    		if(file.isFile()){		
	    		//abrir aqruivo
	    		br = new BufferedReader(new FileReader(file)); 
	    		//System.out.println(Filename+"> lendo.");
				
	    		String st;
	    		boolean isData = false;
	    		while ((st = br.readLine()) != null) {
	    			if(isData && !st.contains("Angle")){
			    		//preencher objeto DataExpDTO
			    		AtomoDTO dto = new AtomoDTO();
			    		String[] d = st.split(",");
			    		//dto.set( Double.parseDouble( d[0].trim() ) );
			    		lista.add(dto);
	    			}
		    		
	    		}
    		}
		}catch(Exception ex){
			System.out.println(file+"> Erro arquivo.");
			throw ex;
		}finally{
			if(br!=null){
				br.close();
			}
		}
		
		//System.out.println("END.");
	}
	
	


}
