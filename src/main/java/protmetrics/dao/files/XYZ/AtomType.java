package protmetrics.dao.files.XYZ;

public enum AtomType{
	H("H"),
	He("He"),
	Li("Li"),
	Be("Be"),
	B("B"),
	C("C"),
	N("N"),
	O("O"),
	F("F"),
	Ne("Ne"),
	Na("Na"),
	Mg("Mg"),
	Al("Al"),
	Si("Si"),
	P("P"),
	S("S"),
	Cl("Cl"),
	Ar("Ar"),
	K("K"),
	Ca("Ca"),
	Sc("Sc"),
	Ti("Ti"),
	V("V"),
	Cr("Cr"),
	Mn("Mn"),
	Fe("Fe"),
	Co("Co"),
	Ni("Ni"),
	Cu("Cu"),
	Zn("Zn"),
	Ga("Ga"),
	Ge("Ge"),
	As("As"),
	Se("Se"),
	Br("Br"),
	Kr("Kr"),
	Rb("Rb"),
	Sr("Sr"),
	Y("Y"),
	Zr("Zr"),
	Nb("Nb"),
	Mo("Mo"),
	Tc("Tc"),
	Ru("Ru"),
	Rh("Rh"),
	Pd("Pd"),
	Ag("Ag"),
	Cd("Cd"),
	In("In"),
	Sn("Sn"),
	Sb("Sb"),
	Te("Te"),
	I("I"),
	Xe("Xe"),
	Cs("Cs"),
	Ba("Ba"),
	La("La"),
	Ce("Ce"),
	Pr("Pr"),
	Nd("Nd"),
	Pm("Pm"),
	Sm("Sm"),
	Eu("Eu"),
	Gd("Gd"),
	Tb("Tb"),
	Dy("Dy"),
	Ho("Ho"),
	Er("Er"),
	Tm("Tm"),
	Yb("Yb"),
	Lu("Lu"),
	Hf("Hf"),
	Ta("Ta"),
	W("W"),
	Re("Re"),
	Os("Os"),
	Ir("Ir"),
	Pt("Pt"),
	Au("Au"),
	Hg("Hg"),
	Tl("Tl"),
	Pb("Pb"),
	Bi("Bi"),
	Po("Po"),
	At("At"),
	Rn("Rn"),
	Fr("Fr"),
	Ra("Ra"),
	Ac("Ac"),
	Th("Th"),
	Pa("Pa"),
	U("U"),
	Np("Np"),
	Pu("Pu"),
	Am("Am"),
	Cm("Cm"),
	Bk("Bk"),
	Cf("Cf"),
	Es("Es"),
	Fm("Fm"),
	Md("Md"),
	No("No"),
	Lr("Lr"),
	Rf("Rf"),
	Db("Db"),
	Sg("Sg"),
	Bh("Bh"),
	Hs("Hs"),
	Mt("Mt"),
	Ds("Ds"),
	Rg("Rg"),
	Cn("Cn"),
	Uut("Uut"),
	Fl("Fl"),
	Uup("Uup"),
	Lv("Lv"),
	Uus("Uus"),
	Uuo("Uuo"),
	Unknown("?");
	
	private final String code;
	
	private AtomType(String acode){
		code=acode;
	}
	
	public String toString(){
		return this.code;
	}
	
	public int getIndex(){
		
		/*There could be a better way to do this*/
		switch(code){
		case "H": return 0;
		case "He": return 1;
		case "Li": return 2;
		case "Be": return 3;
		case "B": return 4;
		case "C": return 5;
		case "N": return 6;
		case "O": return 7;
		case "F": return 8;
		case "Ne": return 9;
		case "Na": return 10;
		case "Mg": return 11;
		case "Al": return 12;
		case "Si": return 13;
		case "P": return 14;
		case "S": return 15;
		case "Cl": return 16;
		case "Ar": return 17;
		case "K": return 18;
		case "Ca": return 19;
		case "Sc": return 20;
		case "Ti": return 21;
		case "V": return 22;
		case "Cr": return 23;
		case "Mn": return 24;
		case "Fe": return 25;
		case "Co": return 26;
		case "Ni": return 27;
		case "Cu": return 28;
		case "Zn": return 29;
		case "Ga": return 30;
		case "Ge": return 31;
		case "As": return 32;
		case "Se": return 33;
		case "Br": return 34;
		case "Kr": return 35;
		case "Rb": return 36;
		case "Sr": return 37;
		case "Y": return 38;
		case "Zr": return 39;
		case "Nb": return 40;
		case "Mo": return 41;
		case "Tc": return 42;
		case "Ru": return 43;
		case "Rh": return 44;
		case "Pd": return 45;
		case "Ag": return 46;
		case "Cd": return 47;
		case "In": return 48;
		case "Sn": return 49;
		case "Sb": return 50;
		case "Te": return 51;
		case "I": return 52;
		case "Xe": return 53;
		case "Cs": return 54;
		case "Ba": return 55;
		case "La": return 56;
		case "Ce": return 57;
		case "Pr": return 58;
		case "Nd": return 59;
		case "Pm": return 60;
		case "Sm": return 61;
		case "Eu": return 62;
		case "Gd": return 63;
		case "Tb": return 64;
		case "Dy": return 65;
		case "Ho": return 66;
		case "Er": return 67;
		case "Tm": return 68;
		case "Yb": return 69;
		case "Lu": return 70;
		case "Hf": return 71;
		case "Ta": return 72;
		case "W": return 73;
		case "Re": return 74;
		case "Os": return 75;
		case "Ir": return 76;
		case "Pt": return 77;
		case "Au": return 78;
		case "Hg": return 79;
		case "Tl": return 80;
		case "Pb": return 81;
		case "Bi": return 82;
		case "Po": return 83;
		case "At": return 84;
		case "Rn": return 85;
		case "Fr": return 86;
		case "Ra": return 87;
		case "Ac": return 88;
		case "Th": return 89;
		case "Pa": return 90;
		case "U": return 91;
		case "Np": return 92;
		case "Pu": return 93;
		case "Am": return 94;
		case "Cm": return 95;
		case "Bk": return 96;
		case "Cf": return 97;
		case "Es": return 98;
		case "Fm": return 99;
		case "Md": return 100;
		case "No": return 101;
		case "Lr": return 102;
		case "Rf": return 103;
		case "Db": return 104;
		case "Sg": return 105;
		case "Bh": return 106;
		case "Hs": return 107;
		case "Mt": return 108;
		case "Ds": return 109;
		case "Rg": return 110;
		case "Cn": return 111;
		case "Uut": return 112;
		case "Fl": return 113;
		case "Uup": return 114;
		case "Lv": return 115;
		case "Uus": return 116;
		case "Uuo": return 117;
		default: return 118;
		}
	}

	public static AtomType getTypeFromCode(String code){
		
		/*There could be a better way to do this*/
		switch(code){
		case "H":	return 	H;
		case "He":	return 	He;
		case "Li":	return 	Li;
		case "Be":	return 	Be;
		case "B":	return 	B;
		case "C":	return 	C;
		case "N":	return 	N;
		case "O":	return 	O;
		case "F":	return 	F;
		case "Ne":	return 	Ne;
		case "Na":	return 	Na;
		case "Mg":	return 	Mg;
		case "Al":	return 	Al;
		case "Si":	return 	Si;
		case "P":	return 	P;
		case "S":	return 	S;
		case "Cl":	return 	Cl;
		case "Ar":	return 	Ar;
		case "K":	return 	K;
		case "Ca":	return 	Ca;
		case "Sc":	return 	Sc;
		case "Ti":	return 	Ti;
		case "V":	return 	V;
		case "Cr":	return 	Cr;
		case "Mn":	return 	Mn;
		case "Fe":	return 	Fe;
		case "Co":	return 	Co;
		case "Ni":	return 	Ni;
		case "Cu":	return 	Cu;
		case "Zn":	return 	Zn;
		case "Ga":	return 	Ga;
		case "Ge":	return 	Ge;
		case "As":	return 	As;
		case "Se":	return 	Se;
		case "Br":	return 	Br;
		case "Kr":	return 	Kr;
		case "Rb":	return 	Rb;
		case "Sr":	return 	Sr;
		case "Y":	return 	Y;
		case "Zr":	return 	Zr;
		case "Nb":	return 	Nb;
		case "Mo":	return 	Mo;
		case "Tc":	return 	Tc;
		case "Ru":	return 	Ru;
		case "Rh":	return 	Rh;
		case "Pd":	return 	Pd;
		case "Ag":	return 	Ag;
		case "Cd":	return 	Cd;
		case "In":	return 	In;
		case "Sn":	return 	Sn;
		case "Sb":	return 	Sb;
		case "Te":	return 	Te;
		case "I":	return 	I;
		case "Xe":	return 	Xe;
		case "Cs":	return 	Cs;
		case "Ba":	return 	Ba;
		case "La":	return 	La;
		case "Ce":	return 	Ce;
		case "Pr":	return 	Pr;
		case "Nd":	return 	Nd;
		case "Pm":	return 	Pm;
		case "Sm":	return 	Sm;
		case "Eu":	return 	Eu;
		case "Gd":	return 	Gd;
		case "Tb":	return 	Tb;
		case "Dy":	return 	Dy;
		case "Ho":	return 	Ho;
		case "Er":	return 	Er;
		case "Tm":	return 	Tm;
		case "Yb":	return 	Yb;
		case "Lu":	return 	Lu;
		case "Hf":	return 	Hf;
		case "Ta":	return 	Ta;
		case "W":	return 	W;
		case "Re":	return 	Re;
		case "Os":	return 	Os;
		case "Ir":	return 	Ir;
		case "Pt":	return 	Pt;
		case "Au":	return 	Au;
		case "Hg":	return 	Hg;
		case "Tl":	return 	Tl;
		case "Pb":	return 	Pb;
		case "Bi":	return 	Bi;
		case "Po":	return 	Po;
		case "At":	return 	At;
		case "Rn":	return 	Rn;
		case "Fr":	return 	Fr;
		case "Ra":	return 	Ra;
		case "Ac":	return 	Ac;
		case "Th":	return 	Th;
		case "Pa":	return 	Pa;
		case "U":	return 	U;
		case "Np":	return 	Np;
		case "Pu":	return 	Pu;
		case "Am":	return 	Am;
		case "Cm":	return 	Cm;
		case "Bk":	return 	Bk;
		case "Cf":	return 	Cf;
		case "Es":	return 	Es;
		case "Fm":	return 	Fm;
		case "Md":	return 	Md;
		case "No":	return 	No;
		case "Lr":	return 	Lr;
		case "Rf":	return 	Rf;
		case "Db":	return 	Db;
		case "Sg":	return 	Sg;
		case "Bh":	return 	Bh;
		case "Hs":	return 	Hs;
		case "Mt":	return 	Mt;
		case "Ds":	return 	Ds;
		case "Rg":	return 	Rg;
		case "Cn":	return 	Cn;
		case "Uut":	return 	Uut;
		case "Fl":	return 	Fl;
		case "Uup":	return 	Uup;
		case "Lv":	return 	Lv;
		case "Uus":	return 	Uus;
		case "Uuo":	return 	Uuo;
		default: return Unknown;
		}
	}

	public boolean equals(AtomType t){
		return code.equals(t.code);
	}

};