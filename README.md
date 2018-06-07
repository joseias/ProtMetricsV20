# ProtMetricsV20

ProtMetrics

Library to compute protein descriptors.

Basic usage:

java -cp [jar file with dependencies] protmetrics.metrics.[Descriptor]  -cfg [path to configuration file]

Example, to compute:

java -cp ProtMetricsV20-1.0-SNAPSHOT-jar-with-dependencies.jar  protmetrics.metrics.Correlation2D -cfg 2DC.cfg

The .cfg file is specific for each descriptor, for Correlation2D must specify:

# PDBs or FASTAs folder (take care no spaces are included at the end)
PDBS_DIRECTORY_PATH = PDBs

# Output file format (csv or arff)
OUTPUT_FORMAT = csv

# Output File Path (including name)
OUTPUT_FILE_PATH = 2DIndex.csv

# Path to the file containing the Property Matrix
PROP_MATRIX_PATH = propertiesStandard.csv

# Indices of the properties which will be included from the Property Matrix (comma separated list)
SELECTED_INDICES = 1,2,3

# Min topological distance considered
MIN_DIST = 2

# Max topological distance considered
MAX_DIST = 10

# Step
STEP = 1

# true or false whether the product index will be included
DO_PRODUCT = true

# true or false whether the max index will be included
DO_MAX = true

# true or false whether the min index will be included
DO_MIN = true