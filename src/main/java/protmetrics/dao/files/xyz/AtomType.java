package protmetrics.dao.files.xyz;

import java.util.HashMap;

/**
 * Enum representing atom types.
 */
public enum AtomType {

    /**
     * Actinium
     */
    AC("Ac", 1),
    /**
     * Aluminum
     */
    AL("Al", 2),
    /**
     * Americium
     */
    AM("Am", 3),
    /**
     * Antimony
     */
    SB("Sb", 4),
    /**
     * Argon
     */
    AR("Ar", 5),
    /**
     * Arsenic
     */
    AS("As", 6),
    /**
     * Astatine
     */
    AT("At", 7),
    /**
     * Barium
     */
    BA("Ba", 8),
    /**
     * Berkelium
     */
    BK("Bk", 9),
    /**
     * Beryllium
     */
    BE("Be", 10),
    /**
     * Bismuth
     */
    BI("Bi", 11),
    /**
     * Bohrium
     */
    BH("Bh", 12),
    /**
     * Boron
     */
    B("B", 13),
    /**
     * Bromine
     */
    BR("Br", 14),
    /**
     * Cadmium
     */
    CD("Cd", 15),
    /**
     * Calcium
     */
    CA("Ca", 16),
    /**
     * Californium
     */
    CF("Cf", 17),
    /**
     * Carbon
     */
    C("C", 18),
    /**
     * Cerium
     */
    CE("Ce", 19),
    /**
     * Cesium
     */
    CS("Cs", 20),
    /**
     * Chlorine
     */
    CL("Cl", 21),
    /**
     * Chromium
     */
    CR("Cr", 22),
    /**
     * Cobalt
     */
    CO("Co", 23),
    /**
     * Copper
     */
    CU("Cu", 24),
    /**
     * Curium
     */
    CM("Cm", 25),
    /**
     * Darmstadtium
     */
    DS("Ds", 26),
    /**
     * Dubnium
     */
    DB("Db", 27),
    /**
     * Dysprosium
     */
    DY("Dy", 28),
    /**
     * Einsteinium
     */
    ES("Es", 29),
    /**
     * Erbium
     */
    ER("Er", 30),
    /**
     * Europium
     */
    EU("Eu", 31),
    /**
     * Fermium
     */
    FM("Fm", 32),
    /**
     * Fluorine
     */
    F("F", 33),
    /**
     * Francium
     */
    FR("Fr", 34),
    /**
     * Gadolinium
     */
    GD("Gd", 35),
    /**
     * Gallium
     */
    GA("Ga", 36),
    /**
     * Germanium
     */
    GE("Ge", 37),
    /**
     * Gold
     */
    AU("Au", 38),
    /**
     * Hafnium
     */
    HF("Hf", 39),
    /**
     * Hassium
     */
    HS("Hs", 40),
    /**
     * Helium
     */
    HE("He", 41),
    /**
     * Holmium
     */
    HO("Ho", 42),
    /**
     * Hydrogen
     */
    H("H", 43),
    /**
     * Indium
     */
    IN("In", 44),
    /**
     * Iodine
     */
    I("I", 45),
    /**
     * Iridium
     */
    IR("Ir", 46),
    /**
     * Iron
     */
    FE("Fe", 47),
    /**
     * Krypton
     */
    KR("Kr", 48),
    /**
     * Lanthanum
     */
    LA("La", 49),
    /**
     * Lawrencium
     */
    LR("Lr", 50),
    /**
     * Lead
     */
    PB("Pb", 51),
    /**
     * Lithium
     */
    LI("Li", 52),
    /**
     * Lutetium
     */
    LU("Lu", 53),
    /**
     * Magnesium
     */
    MG("Mg", 54),
    /**
     * Manganese
     */
    MN("Mn", 55),
    /**
     * Meitnerium
     */
    MT("Mt", 56),
    /**
     * Mendelevium
     */
    MD("Md", 57),
    /**
     * Mercury
     */
    HG("Hg", 58),
    /**
     * Molybdenum
     */
    MO("Mo", 59),
    /**
     * Neodymium
     */
    ND("Nd", 60),
    /**
     * Neon
     */
    NE("Ne", 61),
    /**
     * Neptunium
     */
    NP("Np", 62),
    /**
     * Nickel
     */
    NI("Ni", 63),
    /**
     * Niobium
     */
    NB("Nb", 64),
    /**
     * Nitrogen
     */
    N("N", 65),
    /**
     * Nobelium
     */
    NO("No", 66),
    /**
     * Oganesson
     */
    UUO("Uuo", 67),
    /**
     * Osmium
     */
    OS("Os", 68),
    /**
     * Oxygen
     */
    O("O", 69),
    /**
     * Palladium
     */
    PD("Pd", 70),
    /**
     * Phosphorus
     */
    P("P", 71),
    /**
     * Platinum
     */
    PT("Pt", 72),
    /**
     * Plutonium
     */
    PU("Pu", 73),
    /**
     * Polonium
     */
    PO("Po", 74),
    /**
     * Potassium
     */
    K("K", 75),
    /**
     * Praseodymium
     */
    PR("Pr", 76),
    /**
     * Promethium
     */
    PM("Pm", 77),
    /**
     * Protactinium
     */
    PA("Pa", 78),
    /**
     * Radium
     */
    RA("Ra", 79),
    /**
     * Radon
     */
    RN("Rn", 80),
    /**
     * Rhenium
     */
    RE("Re", 81),
    /**
     * Rhodium
     */
    RH("Rh", 82),
    /**
     * Roentgenium
     */
    RG("Rg", 83),
    /**
     * Rubidium
     */
    RB("Rb", 84),
    /**
     * Ruthenium
     */
    RU("Ru", 85),
    /**
     * Rutherfordium
     */
    RF("Rf", 86),
    /**
     * Samarium
     */
    SM("Sm", 87),
    /**
     * Scandium
     */
    SC("Sc", 88),
    /**
     * Seaborgium
     */
    SG("Sg", 89),
    /**
     * Selenium
     */
    SE("Se", 90),
    /**
     * Silicon
     */
    SI("Si", 91),
    /**
     * Silver
     */
    AG("Ag", 92),
    /**
     * Sodium
     */
    NA("Na", 93),
    /**
     * Strontium
     */
    SR("Sr", 94),
    /**
     * Sulfur
     */
    S("S", 95),
    /**
     * Tantalum
     */
    TA("Ta", 96),
    /**
     * Technetium
     */
    TC("Tc", 97),
    /**
     * Tellurium
     */
    TE("Te", 98),
    /**
     * Terbium
     */
    TB("Tb", 99),
    /**
     * Thallium
     */
    TL("Tl", 100),
    /**
     * Thorium
     */
    TH("Th", 101),
    /**
     * Thulium
     */
    TM("Tm", 102),
    /**
     * Tin
     */
    SN("Sn", 103),
    /**
     * Titanium
     */
    TI("Ti", 104),
    /**
     * Tungsten
     */
    W("W", 105),
    /**
     * Ununbium
     */
    UUB("Uub", 106),
    /**
     * Ununhexium
     */
    UUH("Uuh", 107),
    /**
     * Ununpentium
     */
    UUP("Uup", 108),
    /**
     * Ununquadium
     */
    UUQ("Uuq", 109),
    /**
     * Ununseptium
     */
    UUS("Uus", 110),
    /**
     * Ununtrium
     */
    UUT("Uut", 111),
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
    XE("Xe", 114),
    /**
     * Ytterbium
     */
    YB("Yb", 115),
    /**
     * Yttrium
     */
    Y("Y", 116),
    /**
     * Zinc
     */
    ZN("Zn", 117),
    /**
     * Zirconium
     */
    ZR("Zr", 118),
    /**
     * Unknown
     */
    UNKWNOWN("?", 0);

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
     * @return AtomType of the code or null if no AtomType is associated with
     * code.
     */
    public static AtomType getTypeFromCode(String code) {
        return map.containsKey(code) ? map.get(code) : null;
    }
}
