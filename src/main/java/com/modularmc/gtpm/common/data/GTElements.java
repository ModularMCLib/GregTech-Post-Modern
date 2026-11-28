package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.data.chemical.Element;
import com.modularmc.gtpm.api.registry.GTRegistries;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTElements {

    public static final Element H = REGISTRATE.element("Hydrogen").nucleus(1, 0).symbol("H").register();
    public static final Element D = REGISTRATE.element("Deuterium")
            .nucleus(1, 1).decay(-1, "H").isotope().symbol("D").register();
    public static final Element T = REGISTRATE.element("Tritium")
            .nucleus(1, 2).decay(-1, "D").isotope().symbol("T").register();
    public static final Element He = REGISTRATE.element("Helium").nucleus(2, 2).symbol("He").register();
    public static final Element He3 = REGISTRATE.element("Helium-3")
            .nucleus(2, 1).decay(-1, "H&D").isotope().symbol("He-3").register();
    public static final Element Li = REGISTRATE.element("Lithium").nucleus(3, 4).symbol("Li").register();
    public static final Element Be = REGISTRATE.element("Beryllium").nucleus(4, 5).symbol("Be").register();
    public static final Element B = REGISTRATE.element("Boron").nucleus(5, 5).symbol("B").register();
    public static final Element C = REGISTRATE.element("Carbon").nucleus(6, 6).symbol("C").register();
    public static final Element N = REGISTRATE.element("Nitrogen").nucleus(7, 7).symbol("N").register();
    public static final Element O = REGISTRATE.element("Oxygen").nucleus(8, 8).symbol("O").register();
    public static final Element F = REGISTRATE.element("Fluorine").nucleus(9, 9).symbol("F").register();
    public static final Element Ne = REGISTRATE.element("Neon").nucleus(10, 10).symbol("Ne").register();
    public static final Element Na = REGISTRATE.element("Sodium").nucleus(11, 11).symbol("Na").register();
    public static final Element Mg = REGISTRATE.element("Magnesium").nucleus(12, 12).symbol("Mg").register();
    public static final Element Al = REGISTRATE.element("Aluminium").nucleus(13, 13).symbol("Al").register();
    public static final Element Si = REGISTRATE.element("Silicon").nucleus(14, 14).symbol("Si").register();
    public static final Element P = REGISTRATE.element("Phosphorus").nucleus(15, 15).symbol("P").register();
    public static final Element S = REGISTRATE.element("Sulfur").nucleus(16, 16).symbol("S").register();
    public static final Element Cl = REGISTRATE.element("Chlorine").nucleus(17, 18).symbol("Cl").register();
    public static final Element Ar = REGISTRATE.element("Argon").nucleus(18, 22).symbol("Ar").register();
    public static final Element K = REGISTRATE.element("Potassium").nucleus(19, 20).symbol("K").register();
    public static final Element Ca = REGISTRATE.element("Calcium").nucleus(20, 20).symbol("Ca").register();
    public static final Element Sc = REGISTRATE.element("Scandium").nucleus(21, 24).symbol("Sc").register();
    public static final Element Ti = REGISTRATE.element("Titanium").nucleus(22, 26).symbol("Ti").register();
    public static final Element V = REGISTRATE.element("Vanadium").nucleus(23, 28).symbol("V").register();
    public static final Element Cr = REGISTRATE.element("Chrome").nucleus(24, 28).symbol("Cr").register();
    public static final Element Mn = REGISTRATE.element("Manganese").nucleus(25, 30).symbol("Mn").register();
    public static final Element Fe = REGISTRATE.element("Iron").nucleus(26, 30).symbol("Fe").register();
    public static final Element Co = REGISTRATE.element("Cobalt").nucleus(27, 32).symbol("Co").register();
    public static final Element Ni = REGISTRATE.element("Nickel").nucleus(28, 30).symbol("Ni").register();
    public static final Element Cu = REGISTRATE.element("Copper").nucleus(29, 34).symbol("Cu").register();
    public static final Element Zn = REGISTRATE.element("Zinc").nucleus(30, 35).symbol("Zn").register();
    public static final Element Ga = REGISTRATE.element("Gallium").nucleus(31, 39).symbol("Ga").register();
    public static final Element Ge = REGISTRATE.element("Germanium").nucleus(32, 40).symbol("Ge").register();
    public static final Element As = REGISTRATE.element("Arsenic").nucleus(33, 42).symbol("As").register();
    public static final Element Se = REGISTRATE.element("Selenium").nucleus(34, 45).symbol("Se").register();
    public static final Element Br = REGISTRATE.element("Bromine").nucleus(35, 45).symbol("Br").register();
    public static final Element Kr = REGISTRATE.element("Krypton").nucleus(36, 48).symbol("Kr").register();
    public static final Element Rb = REGISTRATE.element("Rubidium").nucleus(37, 48).symbol("Rb").register();
    public static final Element Sr = REGISTRATE.element("Strontium").nucleus(38, 49).symbol("Sr").register();
    public static final Element Y = REGISTRATE.element("Yttrium").nucleus(39, 50).symbol("Y").register();
    public static final Element Zr = REGISTRATE.element("Zirconium").nucleus(40, 51).symbol("Zr").register();
    public static final Element Nb = REGISTRATE.element("Niobium").nucleus(41, 53).symbol("Nb").register();
    public static final Element Mo = REGISTRATE.element("Molybdenum").nucleus(42, 53).symbol("Mo").register();
    public static final Element Tc = REGISTRATE.element("Technetium").nucleus(43, 55).symbol("Tc").register();
    public static final Element Ru = REGISTRATE.element("Ruthenium").nucleus(44, 57).symbol("Ru").register();
    public static final Element Rh = REGISTRATE.element("Rhodium").nucleus(45, 58).symbol("Rh").register();
    public static final Element Pd = REGISTRATE.element("Palladium").nucleus(46, 60).symbol("Pd").register();
    public static final Element Ag = REGISTRATE.element("Silver").nucleus(47, 60).symbol("Ag").register();
    public static final Element Cd = REGISTRATE.element("Cadmium").nucleus(48, 64).symbol("Cd").register();
    public static final Element In = REGISTRATE.element("Indium").nucleus(49, 65).symbol("In").register();
    public static final Element Sn = REGISTRATE.element("Tin").nucleus(50, 68).symbol("Sn").register();
    public static final Element Sb = REGISTRATE.element("Antimony").nucleus(51, 70).symbol("Sb").register();
    public static final Element Te = REGISTRATE.element("Tellurium").nucleus(52, 75).symbol("Te").register();
    public static final Element I = REGISTRATE.element("Iodine").nucleus(53, 74).symbol("I").register();
    public static final Element Xe = REGISTRATE.element("Xenon").nucleus(54, 77).symbol("Xe").register();
    public static final Element Cs = REGISTRATE.element("Caesium").nucleus(55, 77).symbol("Cs").register();
    public static final Element Ba = REGISTRATE.element("Barium").nucleus(56, 81).symbol("Ba").register();
    public static final Element La = REGISTRATE.element("Lanthanum").nucleus(57, 81).symbol("La").register();
    public static final Element Ce = REGISTRATE.element("Cerium").nucleus(58, 82).symbol("Ce").register();
    public static final Element Pr = REGISTRATE.element("Praseodymium").nucleus(59, 81).symbol("Pr").register();
    public static final Element Nd = REGISTRATE.element("Neodymium").nucleus(60, 84).symbol("Nd").register();
    public static final Element Pm = REGISTRATE.element("Promethium").nucleus(61, 83).symbol("Pm").register();
    public static final Element Sm = REGISTRATE.element("Samarium").nucleus(62, 88).symbol("Sm").register();
    public static final Element Eu = REGISTRATE.element("Europium").nucleus(63, 88).symbol("Eu").register();
    public static final Element Gd = REGISTRATE.element("Gadolinium").nucleus(64, 93).symbol("Gd").register();
    public static final Element Tb = REGISTRATE.element("Terbium").nucleus(65, 93).symbol("Tb").register();
    public static final Element Dy = REGISTRATE.element("Dysprosium").nucleus(66, 96).symbol("Dy").register();
    public static final Element Ho = REGISTRATE.element("Holmium").nucleus(67, 97).symbol("Ho").register();
    public static final Element Er = REGISTRATE.element("Erbium").nucleus(68, 99).symbol("Er").register();
    public static final Element Tm = REGISTRATE.element("Thulium").nucleus(69, 99).symbol("Tm").register();
    public static final Element Yb = REGISTRATE.element("Ytterbium").nucleus(70, 103).symbol("Yb").register();
    public static final Element Lu = REGISTRATE.element("Lutetium").nucleus(71, 103).symbol("Lu").register();
    public static final Element Hf = REGISTRATE.element("Hafnium").nucleus(72, 106).symbol("Hf").register();
    public static final Element Ta = REGISTRATE.element("Tantalum").nucleus(73, 107).symbol("Ta").register();
    public static final Element W = REGISTRATE.element("Tungsten").nucleus(74, 109).symbol("W").register();
    public static final Element Re = REGISTRATE.element("Rhenium").nucleus(75, 111).symbol("Re").register();
    public static final Element Os = REGISTRATE.element("Osmium").nucleus(76, 114).symbol("Os").register();
    public static final Element Ir = REGISTRATE.element("Iridium").nucleus(77, 115).symbol("Ir").register();
    public static final Element Pt = REGISTRATE.element("Platinum").nucleus(78, 117).symbol("Pt").register();
    public static final Element Au = REGISTRATE.element("Gold").nucleus(79, 117).symbol("Au").register();
    public static final Element Hg = REGISTRATE.element("Mercury").nucleus(80, 120).symbol("Hg").register();
    public static final Element Tl = REGISTRATE.element("Thallium").nucleus(81, 123).symbol("Tl").register();
    public static final Element Pb = REGISTRATE.element("Lead").nucleus(82, 125).symbol("Pb").register();
    public static final Element Bi = REGISTRATE.element("Bismuth").nucleus(83, 125).symbol("Bi").register();
    public static final Element Po = REGISTRATE.element("Polonium").nucleus(84, 124).symbol("Po").register();
    public static final Element At = REGISTRATE.element("Astatine").nucleus(85, 124).symbol("At").register();
    public static final Element Rn = REGISTRATE.element("Radon").nucleus(86, 134).symbol("Rn").register();
    public static final Element Fr = REGISTRATE.element("Francium").nucleus(87, 134).symbol("Fr").register();
    public static final Element Ra = REGISTRATE.element("Radium").nucleus(88, 136).symbol("Ra").register();
    public static final Element Ac = REGISTRATE.element("Actinium").nucleus(89, 136).symbol("Ac").register();
    public static final Element Th = REGISTRATE.element("Thorium").nucleus(90, 140).symbol("Th").register();
    public static final Element Pa = REGISTRATE.element("Protactinium").nucleus(91, 138).symbol("Pa").register();
    public static final Element U = REGISTRATE.element("Uranium").nucleus(92, 146).symbol("U").register();
    public static final Element U238 = REGISTRATE.element("Uranium-238").nucleus(92, 146).symbol("U-238").register();
    public static final Element U235 = REGISTRATE.element("Uranium-235").nucleus(92, 143).isotope().symbol("U-235")
            .register();
    public static final Element Np = REGISTRATE.element("Neptunium").nucleus(93, 144).symbol("Np").register();
    public static final Element Pu = REGISTRATE.element("Plutonium").nucleus(94, 152).symbol("Pu").register();
    public static final Element Pu239 = REGISTRATE.element("Plutonium-239").nucleus(94, 145).symbol("Pu-239")
            .register();
    public static final Element Pu241 = REGISTRATE.element("Plutonium-241").nucleus(94, 149).isotope().symbol("Pu-241")
            .register();
    public static final Element Am = REGISTRATE.element("Americium").nucleus(95, 150).symbol("Am").register();
    public static final Element Cm = REGISTRATE.element("Curium").nucleus(96, 153).symbol("Cm").register();
    public static final Element Bk = REGISTRATE.element("Berkelium").nucleus(97, 152).symbol("Bk").register();
    public static final Element Cf = REGISTRATE.element("Californium").nucleus(98, 153).symbol("Cf").register();
    public static final Element Es = REGISTRATE.element("Einsteinium").nucleus(99, 153).symbol("Es").register();
    public static final Element Fm = REGISTRATE.element("Fermium").nucleus(100, 157).symbol("Fm").register();
    public static final Element Md = REGISTRATE.element("Mendelevium").nucleus(101, 157).symbol("Md").register();
    public static final Element No = REGISTRATE.element("Nobelium").nucleus(102, 157).symbol("No").register();
    public static final Element Lr = REGISTRATE.element("Lawrencium").nucleus(103, 159).symbol("Lr").register();
    public static final Element Rf = REGISTRATE.element("Rutherfordium").nucleus(104, 161).symbol("Rf").register();
    public static final Element Db = REGISTRATE.element("Dubnium").nucleus(105, 163).symbol("Db").register();
    public static final Element Sg = REGISTRATE.element("Seaborgium").nucleus(106, 165).symbol("Sg").register();
    public static final Element Bh = REGISTRATE.element("Bohrium").nucleus(107, 163).symbol("Bh").register();
    public static final Element Hs = REGISTRATE.element("Hassium").nucleus(108, 169).symbol("Hs").register();
    public static final Element Mt = REGISTRATE.element("Meitnerium").nucleus(109, 167).symbol("Mt").register();
    public static final Element Ds = REGISTRATE.element("Darmstadtium").nucleus(110, 171).symbol("Ds").register();
    public static final Element Rg = REGISTRATE.element("Roentgenium").nucleus(111, 169).symbol("Rg").register();
    public static final Element Cn = REGISTRATE.element("Copernicium").nucleus(112, 173).symbol("Cn").register();
    public static final Element Nh = REGISTRATE.element("Nihonium").nucleus(113, 171).symbol("Nh").register();
    public static final Element Fl = REGISTRATE.element("Flerovium").nucleus(114, 175).symbol("Fl").register();
    public static final Element Mc = REGISTRATE.element("Moscovium").nucleus(115, 173).symbol("Mc").register();
    public static final Element Lv = REGISTRATE.element("Livermorium").nucleus(116, 177).symbol("Lv").register();
    public static final Element Ts = REGISTRATE.element("Tennessine").nucleus(117, 177).symbol("Ts").register();
    public static final Element Og = REGISTRATE.element("Oganesson").nucleus(118, 176).symbol("Og").register();
    public static final Element Tr = REGISTRATE.element("Tritanium").nucleus(119, 178).symbol("Tr").register();
    public static final Element Dr = REGISTRATE.element("Duranium").nucleus(120, 180).symbol("Dr").register();
    public static final Element Ke = REGISTRATE.element("Trinium").nucleus(125, 198).symbol("Ke").register();
    public static final Element Nq = REGISTRATE.element("Naquadah")
            .nucleus(174, 352).decay(140, null).isotope().symbol("Nq").register();
    public static final Element Nq1 = REGISTRATE.element("NaquadahEnriched")
            .nucleus(174, 354).decay(140, null).isotope().symbol("Nq+").register();
    public static final Element Nq2 = REGISTRATE.element("Naquadria")
            .nucleus(174, 348).decay(140, null).isotope().symbol("*Nq*").register();
    public static final Element Nt = REGISTRATE.element("Neutronium").nucleus(0, 1000).symbol("Nt").register();
    public static final Element Sp = REGISTRATE.element("Space").nucleus(1, 0).symbol("Sp").register();
    public static final Element Ma = REGISTRATE.element("Magic").nucleus(1, 0).symbol("Ma").register();

    public static void init() {}

    public static Element get(String name) {
        return GTRegistries.ELEMENTS.get(GTPM.id(name));
    }
}
