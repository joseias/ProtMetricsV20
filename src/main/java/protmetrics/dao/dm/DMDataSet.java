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

public class DMDataSet {

    private final String dsID;
    private Set<DMInstance> instances;
    private Set<DMAtt> atts;

    public DMDataSet(String dsID) {
        this.dsID = dsID;
        this.instances = new HashSet<>();
        this.atts = new HashSet<>();
    }

    public void addAtt(DMAtt att) {
        this.atts.add(att);
    }

    public void addInstance(DMInstance inst) {
        instances.add(inst);
    }

    /**
     * @return the instances
     */
    public Set<DMInstance> getInstances() {
        return instances;
    }

    /**
     * @param instances the instances to set
     */
    public void setInstances(Set<DMInstance> instances) {
        this.instances = instances;
    }

    /**
     * @return the atts
     */
    public Set<DMAtt> getAtts() {
        return atts;
    }

    /**
     * @param atts the atts to set
     */
    public void setAtts(Set<DMAtt> atts) {
        this.atts = atts;
    }

    /**
     * *
     * Prints de data set to arff file...
     *
     * @param path
     */
    public void toARFF(String path) {

        try {
            PrintStream ps = new PrintStream(path);

            /* print the header */
            ps.println("@relation " + dsID);

            List<DMAtt> attList = this.atts.stream().collect(Collectors.toList());
            Collections.sort(attList, (o1, o2) -> {
                int order = Integer.compare(o1.getAttOrder(), o2.getAttOrder());

                if (order == 0) {
                    if (o1.getAttName().equals(DMAtt.getSPECIAL_ATT_NAME())) {
                        return -1;
                    }

                    if (o2.getAttName().equals(DMAtt.getSPECIAL_ATT_NAME())) {
                        return 1;
                    }

                    return o1.getAttName().compareTo(o2.getAttName());
                } else {
                    return order;
                }
            });

            attList.stream().forEach(at -> {
                ps.println("@attribute " + at.getAttName() + " " + at.getAttType().getSimpleName());
            });

            ps.println(" ");
            ps.println("@data");

            instances.stream().forEach(i -> {
                List<String> attAsStr = attList.stream().map(at -> {
                    return i.getAttValue(at).getValue();
                }).collect(Collectors.toList());

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
     * Prints de data set to csv file...
     *
     * @param path
     */
    public void toCSV(String path) {
        try {
            PrintStream ps = new PrintStream(path);

            /* print the header */
            List<DMAtt> attList = this.atts.stream().collect(Collectors.toList());
            Collections.sort(attList, (o1, o2) -> {
                int order = Integer.compare(o1.getAttOrder(), o2.getAttOrder());

                if (order == 0) {
                    if (o1.getAttName().equals(DMAtt.getSPECIAL_ATT_NAME())) {
                        return -1;
                    }

                    if (o2.getAttName().equals(DMAtt.getSPECIAL_ATT_NAME())) {
                        return 1;
                    }

                    return o1.getAttName().compareTo(o2.getAttName());
                } else {
                    return order;
                }
            });

            List<String> headerAsStr = attList.stream().map(at -> {
                return at.getAttName();
            }).collect(Collectors.toList());

            String headerCSV = String.join(",", headerAsStr);
            ps.println(headerCSV);

            instances.stream().forEach(i -> {
                List<String> attAsStr = attList.stream().map(at -> {
                    return i.getAttValue(at).getValue();
                }).collect(Collectors.toList());

                String attAsCSV = String.join(",", attAsStr);
                ps.println(attAsCSV);
            });

            ps.close();
        } catch (IOException ioe) {
            Logger.getLogger(Correlation2D.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }
}
