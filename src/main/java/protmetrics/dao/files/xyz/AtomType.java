package protmetrics.dao.files.xyz;

import java.util.HashMap;

/**
 * Enum representing atom types.
 */
public enum AtomType {

    /**
     * Actinium
     */
    Ac("Ac", 1),
    /**
     * Aluminum
     */
    Al("Al", 2),
    /**
     * Americium
     */
    Am("Am", 3),
    /**
     * Antimony
     */
    Sb("Sb", 4),
    /**
     * Argon
     */
    Ar("Ar", 5),
    /**
     * Arsenic
     */
    As("As", 6),
    /**
     * Astatine
     */
    At("At", 7),
    /**
     * Barium
     */
    Ba("Ba", 8),
    /**
     * Berkelium
     */
    Bk("Bk", 9),
    /**
     * Beryllium
     */
    Be("Be", 10),
    /**
     * Bismuth
     */
    Bi("Bi", 11),
    /**
     * Bohrium
     */
    Bh("Bh", 12),
    /**
     * Boron
     */
    B("B", 13),
    /**
     * Bromine
     */
    Br("Br", 14),
    /**
     * Cadmium
     */
    Cd("Cd", 15),
    /**
     * Calcium
     */
    Ca("Ca", 16),
    /**
     * Californium
     */
    Cf("Cf", 17),
    /**
     * Carbon
     */
    C("C", 18),
    /**
     * Cerium
     */
    Ce("Ce", 19),
    /**
     * Cesium
     */
    Cs("Cs", 20),
    /**
     * Chlorine
     */
    Cl("Cl", 21),
    /**
     * Chromium
     */
    Cr("Cr", 22),
    /**
     * Cobalt
     */
    Co("Co", 23),
    /**
     * Copper
     */
    Cu("Cu", 24),
    /**
     * Curium
     */
    Cm("Cm", 25),
    /**
     * Darmstadtium
     */
    Ds("Ds", 26),
    /**
     * Dubnium
     */
    Db("Db", 27),
    /**
     * Dysprosium
     */
    Dy("Dy", 28),
    /**
     * Einsteinium
     */
    Es("Es", 29),
    /**
     * Erbium
     */
    Er("Er", 30),
    /**
     * Europium
     */
    Eu("Eu", 31),
    /**
     * Fermium
     */
    Fm("Fm", 32),
    /**
     * Fluorine
     */
    F("F", 33),
    /**
     * Francium
     */
    Fr("Fr", 34),
    /**
     * Gadolinium
     */
    Gd("Gd", 35),
    /**
     * Gallium
     */
    Ga("Ga", 36),
    /**
     * Germanium
     */
    Ge("Ge", 37),
    /**
     * Gold
     */
    Au("Au", 38),
    /**
     * Hafnium
     */
    Hf("Hf", 39),
    /**
     * Hassium
     */
    Hs("Hs", 40),
    /**
     * Helium
     */
    He("He", 41),
    /**
     * Holmium
     */
    Ho("Ho", 42),
    /**
     * Hydrogen
     */
    H("H", 43),
    /**
     * Indium
     */
    In("In", 44),
    /**
     * Iodine
     */
    I("I", 45),
    /**
     * Iridium
     */
    Ir("Ir", 46),
    /**
     * Iron
     */
    Fe("Fe", 47),
    /**
     * Krypton
     */
    Kr("Kr", 48),
    /**
     * Lanthanum
     */
    La("La", 49),
    /**
     * Lawrencium
     */
    Lr("Lr", 50),
    /**
     * Lead
     */
    Pb("Pb", 51),
    /**
     * Lithium
     */
    Li("Li", 52),
    /**
     * Lutetium
     */
    Lu("Lu", 53),
    /**
     * Magnesium
     */
    Mg("Mg", 54),
    /**
     * Manganese
     */
    Mn("Mn", 55),
    /**
     * Meitnerium
     */
    Mt("Mt", 56),
    /**
     * Mendelevium
     */
    Md("Md", 57),
    /**
     * Mercury
     */
    Hg("Hg", 58),
    /**
     * Molybdenum
     */
    Mo("Mo", 59),
    /**
     * Neodymium
     */
    Nd("Nd", 60),
    /**
     * Neon
     */
    Ne("Ne", 61),
    /**
     * Neptunium
     */
    Np("Np", 62),
    /**
     * Nickel
     */
    Ni("Ni", 63),
    /**
     * Niobium
     */
    Nb("Nb", 64),
    /**
     * Nitrogen
     */
    N("N", 65),
    /**
     * Nobelium
     */
    No("No", 66),
    /**
     * Oganesson
     */
    Uuo("Uuo", 67),
    /**
     * Osmium
     */
    Os("Os", 68),
    /**
     * Oxygen
     */
    O("O", 69),
    /**
     * Palladium
     */
    Pd("Pd", 70),
    /**
     * Phosphorus
     */
    P("P", 71),
    /**
     * Platinum
     */
    Pt("Pt", 72),
    /**
     * Plutonium
     */
    Pu("Pu", 73),
    /**
     * Polonium
     */
    Po("Po", 74),
    /**
     * Potassium
     */
    K("K", 75),
    /**
     * Praseodymium
     */
    Pr("Pr", 76),
    /**
     * Promethium
     */
    Pm("Pm", 77),
    /**
     * Protactinium
     */
    Pa("Pa", 78),
    /**
     * Radium
     */
    Ra("Ra", 79),
    /**
     * Radon
     */
    Rn("Rn", 80),
    /**
     * Rhenium
     */
    Re("Re", 81),
    /**
     * Rhodium
     */
    Rh("Rh", 82),
    /**
     * Roentgenium
     */
    Rg("Rg", 83),
    /**
     * Rubidium
     */
    Rb("Rb", 84),
    /**
     * Ruthenium
     */
    Ru("Ru", 85),
    /**
     * Rutherfordium
     */
    Rf("Rf", 86),
    /**
     * Samarium
     */
    Sm("Sm", 87),
    /**
     * Scandium
     */
    Sc("Sc", 88),
    /**
     * Seaborgium
     */
    Sg("Sg", 89),
    /**
     * Selenium
     */
    Se("Se", 90),
    /**
     * Silicon
     */
    Si("Si", 91),
    /**
     * Silver
     */
    Ag("Ag", 92),
    /**
     * Sodium
     */
    Na("Na", 93),
    /**
     * Strontium
     */
    Sr("Sr", 94),
    /**
     * Sulfur
     */
    S("S", 95),
    /**
     * Tantalum
     */
    Ta("Ta", 96),
    /**
     * Technetium
     */
    Tc("Tc", 97),
    /**
     * Tellurium
     */
    Te("Te", 98),
    /**
     * Terbium
     */
    Tb("Tb", 99),
    /**
     * Thallium
     */
    Tl("Tl", 100),
    /**
     * Thorium
     */
    Th("Th", 101),
    /**
     * Thulium
     */
    Tm("Tm", 102),
    /**
     * Tin
     */
    Sn("Sn", 103),
    /**
     * Titanium
     */
    Ti("Ti", 104),
    /**
     * Tungsten
     */
    W("W", 105),
    /**
     * Ununbium
     */
    Uub("Uub", 106),
    /**
     * Ununhexium
     */
    Uuh("Uuh", 107),
    /**
     * Ununpentium
     */
    Uup("Uup", 108),
    /**
     * Ununquadium
     */
    Uuq("Uuq", 109),
    /**
     * Ununseptium
     */
    Uus("Uus", 110),
    /**
     * Ununtrium
     */
    Uut("Uut", 111),
    /**
     * Uranium
     */
    U("U", 112),
    /**
     * Vanadium
     */
    V("V", 113),
    /**
     * Xenon
     */
    Xe("Xe", 114),
    /**
     * Ytterbium
     */
    Yb("Yb", 115),
    /**
     * Yttrium
     */
    Y("Y", 116),
    /**
     * Zinc
     */
    Zn("Zn", 117),
    /**
     * Zirconium
     */
    Zr("Zr", 118),
    /**
     * Unknown
     */
    Unknown("?", 0);

    private final String code;
    private final int index;
    private static final HashMap<String, AtomType> map;

    static {
        map = new HashMap<>();
        for (AtomType d : AtomType.values()) {
            map.put(d.getCode(), d);
        }
    }

    private AtomType(String code, int index) {
        this.code = code;
        this.index = index;

    }

    @Override
    public String toString() {
        return this.code;
    }

    /**
     * @return atom type code.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @return atom type index.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * @param code the code of the AtomType.
     * @return AtomType of the code or null if no AtomType is associated with code.
     */
    public static AtomType getTypeFromCode(String code) {
        return map.containsKey(code) ? map.get(code) : null;
    }
}
