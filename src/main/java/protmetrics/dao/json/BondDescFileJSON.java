package protmetrics.dao.json;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Wrapper to represent a bond description file (JSON format).
 */
public class BondDescFileJSON {

    @SerializedName("bondAtomDesc")
    @Expose
    private List<BondAtomDescJSON> bondAtomDes = new ArrayList<>();

    /**
     * @return the bondAtomDesc.
     */
    public List<BondAtomDescJSON> getBondAtomDesc() {
        return bondAtomDes;
    }

    /**
     * @param bondAtomDes the bond description.
     */
    public void setBondAtomDes(List<BondAtomDescJSON> bondAtomDes) {
        this.bondAtomDes = bondAtomDes;
    }

    /**
     * @param path the path to .json file with bond descriptions.
     * @return bond description wrapper.
     * @throws FileNotFoundException for problems while loading the file.
     */
    public static BondDescFileJSON buildFromFile(String path) throws FileNotFoundException {

        Gson gson = new Gson();
        FileReader fr = new FileReader(path);
        return gson.fromJson(fr, BondDescFileJSON.class);
    }
}
