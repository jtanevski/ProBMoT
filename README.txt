Dependencies: Java JDK 7+

Make sure that you have Java JDK version 7 or higher installed on your system and that the PATH system variable contains the path to the Java compiler from the JDK.

Please read the user guide for preparing the inputs and running ProBMoT, which is available at http://probmot.ijs.si/userguide.html.

The ProBMoT relese available at http://probmot.ijs.si/ contains input files for running ProBMoT in two domains of modeling population dynamics aquatic ecosystems and modeling biochemical dynamics of endocytosis. The folder 'examples' contains all the example files, while further description thereof can be found at http://probmot.ijs.si/examples.html.

To test the correctness of the installation you can run a simple task of simulating an aquatic ecosystem model using the command

    java -jar probmot-1.2.jar examples/aquatic/taskSimulate.xml

that should produce the following terminal output

/time_stamp/ [main] INFO  xml.TaskSpec - Task 'examples/aquatic/taskSimulate.xml' read successfully
/time_stamp/ [main] INFO  traverse.Traverse - Library 'examples/aquatic/AquaticEcosystem.pbl' compiled successfully
/time_stamp/ [main] INFO  traverse.Traverse - Incomplete Model 'examples/aquatic/BledComplete.pbm' compiled successfully
/time_stamp/ [main] INFO  temp.Dataset - Dataset 'examples/aquatic/data/96.data' read successfully -
/time_stamp/ [main] INFO  traverse.Traverse - Library 'examples/aquatic/AquaticEcosystem.pbl' compiled successfully
/time_stamp/ [main] INFO  traverse.Traverse - Incomplete Model 'examples/aquatic/BledComplete.pbm' compiled successfully
/time_stamp/ [main] INFO  temp.Dataset - Dataset 'examples/aquatic/data/96.data' read successfully -
/time_stamp/ [main] INFO  task.Task - Task started
/time_stamp/ [main] INFO  task.Task - Task ended

For further description of the output files produced consult the user guide and examples description on the ProBMoT website.


All files and data used to run ProBMoT and generate the results for the paper titled "Combinatorial search for the structure of models of dynamical systems" are available in the folder data_search.