package protmetrics.dao.json;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Wrapper to represent a bond description file (json format).
 */
public class BondDescFileJSON {

    @SerializedName("bondAtomDesc")
    @Expose
    private List<BondAtomDescJSON> bondAtomDes = new ArrayList<BondAtomDescJSON>();

    /**
     *
     * @return The bondAtomDes
     */
    public List<BondAtomDescJSON> getBondAtomDesc() {
        return bondAtomDes;
    }

    /**
     *
     * @param bondAtomDes The bondAtomDes
     */
    public void setBondAtomDes(List<BondAtomDescJSON> bondAtomDes) {
        this.bondAtomDes = bondAtomDes;
    }

    /**
     *
     * @param jsonFilePath
     * @return
     * @throws FileNotFoundException
     */
    public static BondDescFileJSON buildFromFile(String jsonFilePath) throws FileNotFoundException {

        Gson gson = new Gson();
        FileReader fr = new FileReader(jsonFilePath);
        return gson.fromJson(fr, BondDescFileJSON.class);
    }
}
