package protmetrics.dao.files.pdb;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import protmetrics.errors.SomeErrorException;
import protmetrics.utils.BioUtils;

/**
 * Wrapper to represent a .pdb file.
 *
 */
public final class PdbFile {

    private int[] caLinesIndex;
    /* indices of the mlines with CA */
    private PdbLine[] lines;
    private String proteinName;
    private String path = "";

    /**
     * @param path Path to the .pdb file.
     * @throws IOException for problems while loading the file.
     * @throws SomeErrorException for other errors.
     */
    public PdbFile(String path) throws IOException, SomeErrorException {
        this.path = path;
        this.intPDB(path, "~");
    }

    /**
     * @return the CA lines within the .pdb file.
     */
    public PdbAtomLine[] getCALines() {

        PdbAtomLine[] result = new PdbAtomLine[this.caLinesIndex.length];
        int resultlength = result.length;

        for (int index = 0; index < resultlength; index++) {
            result[index] = new PdbAtomLine(this.lines[this.caLinesIndex[index]].line, this.lines[this.caLinesIndex[index]].lineTokens);
        }
        return result;

    }

    /**
     * @param path the path to the .pdb file.
     * @param seq sequence to be searched.
     * @throws IOException for problems while loading the file.
     * @throws SomeErrorException for other errors.
     */
    public void intPDB(String path, String seq) throws IOException, SomeErrorException {

        try (LineNumberReader lnr = new LineNumberReader(new FileReader(path))) {

            String actualLine = lnr.readLine();

            String[] tokens;

            String sep = "[\\s]+";

            PdbAtomLine alaux;

            ArrayList<PdbLine> mlines = new ArrayList<>();
            ArrayList<Integer> mcalinesindex = new ArrayList<>();

            int totalPdbLines = 0;

            /* read the protein name, check if always is the first line */
            tokens = BioUtils.procSplitString(actualLine.trim().split(sep, 0));
            this.proteinName = tokens[1];
            while (actualLine != null) {

                tokens = BioUtils.procSplitString(actualLine.trim().split(sep, 0));

                if (tokens[0].equals("ATOM")) {

                    /* if it is from the selected sequence*/
                    alaux = new PdbAtomLine(actualLine, tokens);
                    if (alaux.getSequence().equals(seq)) {

                        /* if it is CA line, add it to CALinesIndex */
                        if (alaux.getAtomType().equals("CA")) {
                            mcalinesindex.add(totalPdbLines);
                        }
                        mlines.add(alaux);
                    }
                } else {
                    mlines.add(new PdbLine(actualLine));
                }

                actualLine = lnr.readLine();
                totalPdbLines++;
            }

            this.lines = mlines.toArray(new PdbLine[0]);
            this.caLinesIndex = mcalinesindex.stream().mapToInt(Integer::intValue).toArray();
        } catch (Exception e) {
            throw new SomeErrorException("ERROR AT->" + path);
        }
    }

    /**
     * @return the sequence within the .pdb file.
     */
    public String getSequence() {
        PdbAtomLine[] mcalines = this.getCALines();
        StringBuilder result = new StringBuilder();
        for (PdbAtomLine caLine : mcalines) {
            result.append(BioUtils.aminoThree2One(caLine.getAminoType()));
        }
        return result.toString();
    }

    /**
     * @return the name of the protein in .pdb file.
     */
    public String getProteinName() {
        return proteinName;
    }

    /**
     *
     * @return the path from this .pdb file.
     */
    public String getPath() {
        return this.path;
    }
}
