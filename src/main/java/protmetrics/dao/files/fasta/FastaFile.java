package protmetrics.dao.files.fasta;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import protmetrics.errors.SomeErrorException;

/**
 * Wrapper to represent a .fasta file.
 */
public class FastaFile {

    private String[][] sequences;
    private String path = "";

    /**
     * @param path the path to the .fasta file.
     * @throws IOException for problems while loading the file.
     * @throws SomeErrorException for other errors.
     */
    public FastaFile(String path) throws IOException, SomeErrorException {
        this.path = path;
        this.init(path);
    }

    /**
     * @param path the path to the .fasta file.
     * @throws IOException for problems while loading the file.
     * @throws SomeErrorException for other errors.
     */
    private void init(String path) throws IOException, SomeErrorException {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(path))) {
            String actualLine = lnr.readLine();
            String[] tokens;
            String protName;
            ArrayList<String> pNames = new ArrayList<>();
            ArrayList<String> pSequences = new ArrayList<>();
            boolean newProt;
            while (actualLine != null) {
                tokens = actualLine.trim().split("[\\s]+", 0);
                newProt = false;
                StringBuilder protseq = new StringBuilder();
                if (tokens[0].charAt(0) == '>') {
                    protName = tokens[0].substring(1, tokens[0].length());

                    actualLine = lnr.readLine();
                    while ((actualLine != null) && (!newProt)) {
                        tokens = actualLine.trim().split("[\\s]+", 0);
                        if (tokens[0].charAt(0) != '>') {
                            protseq.append(actualLine);
                            actualLine = lnr.readLine();
                        } else {
                            newProt = true;
                            pSequences.add(protseq.toString());
                            pNames.add(protName);
                            protName = tokens[0].substring(1, tokens[0].length());

                        }
                    }
                    if (actualLine == null) {
                        pSequences.add(protseq.toString());
                        pNames.add(protName);
                    }
                } else {
                    actualLine = lnr.readLine();
                }
            }

            sequences = new String[pNames.size()][2];
            for (int i = 0; i < pNames.size(); ++i) {
                sequences[i][0] = pNames.get(i);
                sequences[i][1] = pSequences.get(i);
            }
        } catch (Exception e) {
            throw new SomeErrorException("ERROR AT->" + this.path);
        }
    }

    /**
     * @return sequences of the molecules in the .fasta file.
     */
    public String[][] getSequences() {
        return sequences;
    }

    /**
     * @param sequences the sequences of the molecules to set.
     */
    public void setSequences(String[][] sequences) {
        this.sequences = sequences;
    }
}
