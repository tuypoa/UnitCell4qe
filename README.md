# UnitCell4qe
best way to configure molecular structure in unit cell for first run on Quantum Espresso

**
This script will place inside a unit cell and export the files extracted directly from Molegro (.pdb) to .CIF to be used in BURAI.


[HOW TO EXECUTE]
$ java -jar UnitCell4qe.jar /home/tuy/Desktop/DrugDesign/ Macitentan

Arg0 = Work path
Arg1 = Molecule name path

[Expected files in your system]
DrugDesign/
 | Macitentan/
   | 04-unitcell/
     | UnitCell4qe/
       *PDB files written by MVD (molegro virtual docker) 
 | scripts/
   TEMPLATE_CIF_FILE.cif
   TEMPLATE_PDB_FILE.pdb
