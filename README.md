# ProtMetricsV20

ProtMetrics is an open-source library to compute molecular descriptors. It can be used for building QSAR machine learning models.

Currently it supports the following descriptors:

- Amino Acid Sequence Autocorrelation Vectors 2D (AASA 2D) as in  [1]. 

- Amino Acid Sequence Autocorrelation Vectors 3D (AASA 3D) as in  [2].

- A variation of the 3D Morse Molecular Descriptor [3].

- Protein Radial Distribution Function (P-RDF) as in [4].

- Topological Autocorrelation Vectors  as in  [5].

#### Recomended Setup:
- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)  or compatible.
- [Apache NetBeans 12.6.](https://netbeans.apache.org/download/nb126/nb126.html)

#### Basic usage:

java -cp [jar file with dependencies] protmetrics.metrics.[Descriptor]  -cfg [path to configuration file]

Example, to compute the AASA 2D run:

java -cp ProtMetricsV20-2.0-SNAPSHOT-jar-with-dependencies.jar  protmetrics.metrics.Correlation2D -cfg correlation2D.cfg

The .cfg file is specific for each descriptor. See files within the "examples" folder for details.

### Citation 
ProMetrics is provided as free open-source software. If you have used ProMetrics in your research, authors will be grateful if you fill and submit to [joseias@gmail.com](joseias@gmail.com) the following [Usage-Acknowledgment-ProtMetrics](https://github.com/joseias/ProtMetricsV20/blob/master/Usage-Acknowledgment-ProtMetrics.docx) acknowledging our contribution. If citing, please refer to [1].


### References
[1] Caballero, J., Fernandez, L., Abreu, J. I., & Fernández, M. (2006). Amino Acid Sequence Autocorrelation vectors and ensembles of Bayesian-Regularized Genetic Neural Networks for prediction of conformational stability of human lysozyme mutants. Journal of chemical information and modeling, 46(3), 1255-1268.

[2] Fernandez, M., Abreu, J. I., Caballero, J., Garriga, M., & Fernandez, L. (2007). Comparative modeling of the conformational stability of chymotrypsin inhibitor 2 protein mutants using amino acid sequence autocorrelation (AASA) and amino acid 3D autocorrelation (AA3DA) vectors and ensembles of Bayesian-regularized genetic neural networks. Molecular Simulation, 33(13), 1045-1056.

[3] Schuur, J. H., Selzer, P., & Gasteiger, J. (1996). The coding of the three-dimensional structure of molecules by molecular transforms and its application to structure-spectra correlations and studies of biological activity. Journal of Chemical Information and Computer Sciences, 36(2), 334-344.

[4] Fernández, M., Caballero, J., Fernández, L., Abreu, J. I., & Garriga, M. (2007). Protein radial distribution function (P-RDF) and Bayesian-Regularized Genetic Neural Networks for modeling protein conformational stability: Chymotrypsin inhibitor 2 mutants. Journal of Molecular Graphics and Modelling, 26(4), 748-759.

[5] Fernandez, M., Abreu, J. I., Shi, H., & Barnard, A. S. (2016). Machine learning prediction of the energy gap of graphene nanoflakes using topological autocorrelation vectors. ACS combinatorial science, 18(11), 661-664.
