package protmetrics.dao.dm;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import protmetrics.metrics.Correlation2D;

/**
 * Represents the result matrix (DataSet) when computing an index for several
 * molecules.
 */
public class DMDataSet {

    private final String dsID;
    private Set<DMInstance> instances;
    private Set<DMAtt> atts;

    /**
     * @param dsID id of the dataset.
     */
    public DMDataSet(String dsID) {
        this.dsID = dsID;
        this.instances = new HashSet<>();
        this.atts = new HashSet<>();
    }

    /**
     * @param att attribute.
     */
    public void addAtt(DMAtt att) {
        this.atts.add(att);
    }

    /**
     * @param inst dataset instance.
     */
    public void addInstance(DMInstance inst) {
        instances.add(inst);
    }

    /**
     * @return the instances.
     */
    public Set<DMInstance> getInstances() {
        return instances;
    }

    /**
     * @param instances the instances to set.
     */
    public void setInstances(Set<DMInstance> instances) {
        this.instances = instances;
    }

    /**
     * @return the attributes.
     */
    public Set<DMAtt> getAtts() {
        return atts;
    }

    /**
     * @param atts the attributes to set.
     */
    public void setAtts(Set<DMAtt> atts) {
        this.atts = atts;
    }

    /**
     * *
     * Prints de data set to .arff file.
     *
     * @param path the path to file to write .arff file.
     */
    public void toARFF(String path) {

        try {
            PrintStream ps = new PrintStream(path);

            /* print the header */
            ps.println("@relation " + dsID);

            List<DMAtt> attList = this.atts.stream().collect(Collectors.toList());
            Collections.sort(attList, (o1, o2) -> {
                int order = Integer.compare(o1.getOrder(), o2.getOrder());

                if (order == 0) {
                    if (o1.getName().equals(DMAtt.getSName())) {
                        return -1;
                    }

                    if (o2.getName().equals(DMAtt.getSName())) {
                        return 1;
                    }

                    return o1.getName().compareTo(o2.getName());
                } else {
                    return order;
                }
            });

            attList.stream().forEach(at -> ps.println("@attribute " + at.getName() + " " + at.getType()));

            ps.println(" ");
            ps.println("@data");

            instances.stream().forEach(i -> {
                List<String> attAsStr = attList.stream().map(at -> i.getAttValue(at).getValue()).collect(Collectors.toList());
                String attAsCSV = String.join(",", attAsStr);
                ps.println(attAsCSV);
            });

            ps.close();
        } catch (IOException ioe) {
            Logger.getLogger(Correlation2D.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    /**
     * *
     * Prints de data set to csv file.
     *
     * @param path path to file to write .csv file.
     */
    public void toCSV(String path) {
        try (PrintStream ps = new PrintStream(path);) {

            /* print the header */
            List<DMAtt> attList = this.atts.stream().collect(Collectors.toList());
            Collections.sort(attList, (o1, o2) -> {
                int order = Integer.compare(o1.getOrder(), o2.getOrder());

                if (order == 0) {
                    if (o1.getName().equals(DMAtt.getSName())) {
                        return -1;
                    }

                    if (o2.getName().equals(DMAtt.getSName())) {
                        return 1;
                    }

                    return o1.getName().compareTo(o2.getName());
                } else {
                    return order;
                }
            });

            List<String> headerAsStr = attList.stream().map(at -> at.getName()).collect(Collectors.toList());

            String headerCSV = String.join(",", headerAsStr);
            ps.println(headerCSV);

            instances.stream().forEach(i -> {
                List<String> attAsStr = attList.stream().map(at -> i.getAttValue(at).getValue()).collect(Collectors.toList());
                String attAsCSV = String.join(",", attAsStr);
                ps.println(attAsCSV);
            });
        } catch (IOException ioe) {
            Logger.getLogger(Correlation2D.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }
}
