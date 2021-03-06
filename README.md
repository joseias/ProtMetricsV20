# ProtMetricsV20

ProtMetrics is an open-source library to compute molecular descriptors. It can be used for building QSAR machine learning models.

Basic usage:

java -cp [jar file with dependencies] protmetrics.metrics.[Descriptor]  -cfg [path to configuration file]

Example, to compute:

java -cp ProtMetricsV20-1.0-SNAPSHOT-jar-with-dependencies.jar  protmetrics.metrics.Correlation2D -cfg 2DC.cfg

The .cfg file is specific for each descriptor, for Correlation2D must specify:

#### PDBs or FASTAs folder (take care no spaces are included at the end)
PDBS_DIRECTORY_PATH = PDBs

#### Output file format (csv or arff)
OUTPUT_FORMAT = csv

#### Output File Path (including name)
OUTPUT_FILE_PATH = 2DIndex.csv

#### Path to the file containing the Property Matrix
PROP_MATRIX_PATH = propertiesStandard.csv

#### Indices of the properties which will be included from the Property Matrix (comma separated list)
SELECTED_INDICES = 1,2,3

#### Min topological distance considered
MIN_DIST = 2

#### Max topological distance considered
MAX_DIST = 10

#### Step
STEP = 1

#### true or false whether the product index will be included
DO_PRODUCT = true

#### true or false whether the max index will be included
DO_MAX = true

#### true or false whether the min index will be included
DO_MIN = true

### Citation 
ProMetrics is provided as free open-source software. If you have used ProMetrics in your research, authors will be grateful if you fill and submit to [joseias@gmail.com](joseias@gmail.com) the following [Usage-Acknowledgment-ProtMetrics](https://github.com/joseias/ProtMetricsV20/blob/master/Usage-Acknowledgment-ProtMetrics.docx) acknowledging our contribution. If citing, please reference to:

Caballero J, Fernández L, Abreu J, Fernández M. "[Amino Acid Sequence Autocorrelation vectors and ensembles of Bayesian-Regularized Genetic Neural Networks for prediction of conformational stability of human lysozyme mutants](https://pubs.acs.org/doi/abs/10.1021/ci050507z)" Journal of Chemical Information and Modeling. Vol. 46(3), pp. 1255-168.


### Other related works
[1] Fernández M, Abreu J, Shi H, Barnard AS. “Machine Learning Prediction of the Energy Gap of Graphene Nanoflakes Using Topological Autocorrelation Vectors” ACS Combinatorial Science.  Vol. 18, pp. 661-664, 2016.

[2] Fernández M, Abreu J, Caballero J, Garriga M, and Fernández L, "Comparative modeling of the conformational stability of chymotrypsin inhibitor 2 protein mutants using amino acid sequence autocorrelation (AASA) and amino acid 3D autocorrelation (AA3DA) vectors and ensembles of Bayesian-regularized genetic neural networks", Molecular Simulation. Vol. 33(13), pp. 1045-1056, 2007

[3] Fernández M, Caballero J, Fernández L, Abreu J and Garriga M, "Protein radial distribution function (P-RDF) and Bayesian-Regularized Genetic Neural Networks for modeling protein conformational stability: Chymotrypsin inhibitor 2 mutants", Journal of Molecular Graphics and Modelling 26(4), pp. 748-759, 2007.

