package minicap.concordia.campusnav.beans;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class SGWBuildingCoordinates
{
    // polygon creation for buildings
    private static final List<PolygonOptions> SGWbuildingCoordinates = new ArrayList<>();

    // B Annex
    public static PolygonOptions BAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.497829032708815, -73.57955660351597),
                    new LatLng(45.49792657919675, -73.57946235345098),
                    new LatLng(45.497888386412065, -73.57937546667229),
                    new LatLng(45.49778722701939, -73.57947487103772)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(BAnnex);
    }

    // CI Annex
    public static PolygonOptions CIAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.497597381689516, -73.57983976442554),
                    new LatLng(45.49755226010348, -73.57974923986802),
                    new LatLng(45.49736284305092, -73.5799443705809),
                    new LatLng(45.49740561469915, -73.58003355403389)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(CIAnnex);
    }

    // CL Annex
    public static PolygonOptions CLAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49423985440496, -73.57894727690089),
                    new LatLng(45.49419755039689, -73.57899957997859),
                    new LatLng(45.49426335661795, -73.57911022110444),
                    new LatLng(45.494304720488955, -73.57905590636993)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(CLAnnex);
    }

    // D Annex
    public static PolygonOptions DAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.497896527766656, -73.57938263699297),
                    new LatLng(45.4978581588858, -73.57929930220156),
                    new LatLng(45.49775450550975, -73.57941123226453),
                    new LatLng(45.49776195023373, -73.57943901052835),
                    new LatLng(45.497698956384404, -73.57949783508698),
                    new LatLng(45.49768463959064, -73.57947250784646),
                    new LatLng(45.49758556727713, -73.57957054877741),
                    new LatLng(45.49762450901616, -73.57964489648347)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(DAnnex);
    }

    // EN Annex
    public static PolygonOptions ENAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.496994023282085, -73.57974083034186),
                    new LatLng(45.49695454147324, -73.57965567020256),
                    new LatLng(45.49684314622046, -73.57976429967158),
                    new LatLng(45.4968718175935, -73.57982599048115),
                    new LatLng(45.496897198796795, -73.57979648618094),
                    new LatLng(45.4969108294383, -73.57982599048115)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(ENAnnex);
    }

    // ER Building
    public static PolygonOptions ERBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.496693140830324, -73.58013985998319),
                    new LatLng(45.4966641372069, -73.58007188256518),
                    new LatLng(45.49668588992588, -73.58006006040554),
                    new LatLng(45.496527405638105, -73.57964037373789),
                    new LatLng(45.49633577371401, -73.57977337303399),
                    new LatLng(45.496346132213084, -73.57979701735329),
                    new LatLng(45.496222865950735, -73.57988272801076),
                    new LatLng(45.4962404754333, -73.5799255833395),
                    new LatLng(45.496150356258596, -73.57998617190772),
                    new LatLng(45.496188682821796, -73.58008222695489),
                    new LatLng(45.49616589405763, -73.58009700465445),
                    new LatLng(45.49629123214638, -73.58046053606375),
                    new LatLng(45.496438322770715, -73.58036448101657),
                    new LatLng(45.496430035985206, -73.58032605899771)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(ERBuilding);
    }

    // EV
    public static PolygonOptions EV = new PolygonOptions()
            .add(
                    new LatLng(45.4958886367119, -73.57850021839808),
                    new LatLng(45.49545434898357, -73.57758571498482),
                    new LatLng(45.49534302398431, -73.57767995770297),
                    new LatLng(45.49534791739549, -73.57773580523967),
                    new LatLng(45.4952243586329, -73.57786320743274),
                    new LatLng(45.495214571788644, -73.57792778114703),
                    new LatLng(45.495240262251215, -73.57792603591152),
                    new LatLng(45.495257389219766, -73.5779906096258),
                    new LatLng(45.495229252054386, -73.57801329768759),
                    new LatLng(45.495596257454736, -73.57878818225912)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(EV);
    }

    // FA Annex
    public static PolygonOptions FAAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49695259821862, -73.57966117792891),
                    new LatLng(45.49691076627047, -73.57957668834187),
                    new LatLng(45.496810651481795, -73.57967593007902),
                    new LatLng(45.496852483504355, -73.57976176077061)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(FAAnnex);
    }

    // FB Building
    public static PolygonOptions FBBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49470409705913, -73.5780524617211),
                    new LatLng(45.49438341110954, -73.57750042600122),
                    new LatLng(45.494663218538264, -73.57720157281172),
                    new LatLng(45.494718886866174, -73.57730502199266),
                    new LatLng(45.49470643474524, -73.57731756128706),
                    new LatLng(45.494787007243744, -73.57744817893712),
                    new LatLng(45.49477382266097, -73.57746489799632),
                    new LatLng(45.49477455513787, -73.57746698787872),
                    new LatLng(45.49481557382916, -73.57754222364515),
                    new LatLng(45.49484853525588, -73.57762686388239),
                    new LatLng(45.49484047802001, -73.57763835823559),
                    new LatLng(45.49488808894244, -73.57770000976643),
                    new LatLng(45.49488003171225, -73.57771463894322),
                    new LatLng(45.494925445178275, -73.57778882975775)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(FBBuilding);
    }

    // FG building
    public static PolygonOptions FGBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49429924052074, -73.57854132809493),
                    new LatLng(45.49439183908542, -73.57843336917819),
                    new LatLng(45.494171858490084, -73.5780370727819),
                    new LatLng(45.49411310284377, -73.57811217463704),
                    new LatLng(45.49410558211662, -73.57809943414375),
                    new LatLng(45.49405246695251, -73.57816514826699),
                    new LatLng(45.49426304713173, -73.578560774111),
                    new LatLng(45.494294070038, -73.57851987042204)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(FGBuilding);
    }

    // Grey Nuns Annex
    public static PolygonOptions GreyNunsAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.494361793770864, -73.57774273625124),
                    new LatLng(45.494282639236516, -73.57760697869772),
                    new LatLng(45.49404517496589, -73.57786326865858),
                    new LatLng(45.49406740949066, -73.57790767533497),
                    new LatLng(45.49378191752553, -73.57825150988643),
                    new LatLng(45.49384773200099, -73.57837965486685),
                    new LatLng(45.49414745370709, -73.57801678888266),
                    new LatLng(45.49413589177174, -73.57799268240119)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(GreyNunsAnnex);
    }

    // GM building
    public static PolygonOptions GMBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49614518242806, -73.57881625434038),
                    new LatLng(45.4959552197847, -73.57842102299838),
                    new LatLng(45.49560785155352, -73.57875040684296),
                    new LatLng(45.49576887356329, -73.57908548051198),
                    new LatLng(45.49574950251881, -73.57910793390216),
                    new LatLng(45.49578340184228, -73.57915974941798)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(GMBuilding);
    }

    // Grey Nuns building
    public static PolygonOptions GNBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49398121670738, -73.5775982900348),
                    new LatLng(45.494147292449334, -73.57741329901731),
                    new LatLng(45.49411999236099, -73.57735163534481),
                    new LatLng(45.49441801760893, -73.57705629880809),
                    new LatLng(45.494049467071555, -73.5762773892607),
                    new LatLng(45.49415639247583, -73.57618651648018),
                    new LatLng(45.49404491704987, -73.5759690708982),
                    new LatLng(45.49393799143395, -73.57605994367871),
                    new LatLng(45.49373096466433, -73.57559908886319),
                    new LatLng(45.49354896248002, -73.57577434351136),
                    new LatLng(45.49376281498611, -73.57623844378332),
                    new LatLng(45.49350573687479, -73.57649808029913),
                    new LatLng(45.49347161137356, -73.57645264390885),
                    new LatLng(45.493316908841955, -73.5766051803619),
                    new LatLng(45.49336013459209, -73.57667658040374),
                    new LatLng(45.493038391380686, -73.57696897900775),
                    new LatLng(45.49275662302619, -73.5763660490016),
                    new LatLng(45.49256601422897, -73.57653451474185),
                    new LatLng(45.492717258218754, -73.57684484636862),
                    new LatLng(45.49264060035688, -73.57691873485116),
                    new LatLng(45.49271104272032, -73.57706651181628),
                    new LatLng(45.49280841878352, -73.57699853441234),
                    new LatLng(45.49287264545648, -73.57713153368094),
                    new LatLng(45.492818777929294, -73.5771936000063),
                    new LatLng(45.49292029745681, -73.57740639883606),
                    new LatLng(45.49299281129301, -73.5773354658928),
                    new LatLng(45.49309433050682, -73.57754826472258),
                    new LatLng(45.49320828042679, -73.57740344329676),
                    new LatLng(45.4931336950507, -73.5772172443207),
                    new LatLng(45.49339267162709, -73.57697784563722),
                    new LatLng(45.493376097173794, -73.57693942361368),
                    new LatLng(45.493434107780736, -73.57688326836693),
                    new LatLng(45.49346104125649, -73.57692760145646),
                    new LatLng(45.493429964167916, -73.57695124577089),
                    new LatLng(45.49352112357908, -73.57714040028623),
                    new LatLng(45.49357062320889, -73.57710263064332),
                    new LatLng(45.493585126901, -73.5771257551217),
                    new LatLng(45.4935637979407, -73.57714766252226),
                    new LatLng(45.49362863795496, -73.57729858017062),
                    new LatLng(45.493765143004076, -73.57715496498912),
                    new LatLng(45.493696890520866, -73.57700526441856),
                    new LatLng(45.493743814111966, -73.57695171299495),
                    new LatLng(45.49367982738714, -73.57679836119098),
                    new LatLng(45.49361669374741, -73.57684582722554),
                    new LatLng(45.49357915479291, -73.57675819762328),
                    new LatLng(45.493894878437544, -73.57643097742016),
                    new LatLng(45.49417653027626, -73.57701183361903),
                    new LatLng(45.49385507969457, -73.5773393841071)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(GNBuilding);
    }

    // GS building
    public static PolygonOptions GSBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49659954966484, -73.58148052561765),
                    new LatLng(45.49675913245137, -73.58137279069528),
                    new LatLng(45.49674915854049, -73.581329086906),
                    new LatLng(45.49676910636054, -73.58131790686689),
                    new LatLng(45.4967306355584, -73.58120915557731),
                    new LatLng(45.49671353741565, -73.58121322104608),
                    new LatLng(45.49665654356909, -73.5810404386234),
                    new LatLng(45.496638020556546, -73.58105161866253),
                    new LatLng(45.4965575166235, -73.58080260870044),
                    new LatLng(45.496542555702916, -73.58080769053639),
                    new LatLng(45.49653543145362, -73.58078228135658),
                    new LatLng(45.49641218179809, -73.58085444342723),
                    new LatLng(45.496451365243836, -73.58096421108398),
                    new LatLng(45.49643497944257, -73.58097437475591)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(GSBuilding);
    }


    // hall building
    public static PolygonOptions HallBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.497792802486664, -73.57903920534217),
                    new LatLng(45.49740094139054, -73.57823087975878),
                    new LatLng(45.49675631547283, -73.5788117125133),
                    new LatLng(45.497165144871346, -73.5796611804168)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(HallBuilding);
    }

    // K Annex
    public static PolygonOptions KAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49782077479732, -73.57955881250189),
                    new LatLng(45.49778206582103, -73.57948149799543),
                    new LatLng(45.49769742210014, -73.5795595488305),
                    new LatLng(45.49773458277374, -73.57964496295192)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(KAnnex);
    }

    // LB Building
    public static PolygonOptions LBBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.4972803678713, -73.57806760602064),
                    new LatLng(45.49714441591139, -73.57779904186108),
                    new LatLng(45.49711061177144, -73.57782315512303),
                    new LatLng(45.497095822453836, -73.57778999938786),
                    new LatLng(45.497131739361315, -73.5777568436527),
                    new LatLng(45.497045116192545, -73.5775699658727),
                    new LatLng(45.49700497370333, -73.57761216408109),
                    new LatLng(45.4969944098856, -73.57756695171496),
                    new LatLng(45.497019763044804, -73.57754585261077),
                    new LatLng(45.49695426736024, -73.5773830880927),
                    new LatLng(45.496867643918485, -73.57747954114045),
                    new LatLng(45.496808486369524, -73.5773469181998),
                    new LatLng(45.496599321680115, -73.57754585261077),
                    new LatLng(45.496624675017166, -73.57759407913464),
                    new LatLng(45.49657819389051, -73.57762120655431),
                    new LatLng(45.496493682647156, -73.57744337124872),
                    new LatLng(45.49623592258883, -73.57768450386808),
                    new LatLng(45.49666693054793, -73.5785917653484),
                    new LatLng(45.49670496050361, -73.57855860961325),
                    new LatLng(45.49672820101941, -73.5785917653484),
                    new LatLng(45.49690989926693, -73.57841995835712),
                    new LatLng(45.4968866588261, -73.57837776014873),
                    new LatLng(45.49692891416593, -73.57833857609809),
                    new LatLng(45.49691201203382, -73.57829939204744),
                    new LatLng(45.49693947799592, -73.57826925047003),
                    new LatLng(45.496962718414956, -73.5783114486784),
                    new LatLng(45.496999355192116, -73.57827510857436),
                    new LatLng(45.497014299104904, -73.57832383913468)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(LBBuilding);
    }

    // LD Building
    public static PolygonOptions LDBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49695310311201, -73.57738871886386),
                    new LatLng(45.49689556051122, -73.57727362826385),
                    new LatLng(45.49681375965397, -73.57734847739532),
                    new LatLng(45.49687186648203, -73.57746920180092)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(LDBuilding);
    }

    // LS Building
    public static PolygonOptions LSBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49655046807988, -73.57956485950501),
                    new LatLng(45.49638647970136, -73.57921727584123),
                    new LatLng(45.49615220976075, -73.57943976758204),
                    new LatLng(45.49626934485289, -73.57969472592882),
                    new LatLng(45.496369746166486, -73.57960019081148),
                    new LatLng(45.49641927741527, -73.57969854552952)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(LSBuilding);
    }

    // M Annex
    public static PolygonOptions MAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49743180430555, -73.5797515902517),
                    new LatLng(45.49739091274948, -73.57967447673974),
                    new LatLng(45.49727951835995, -73.57978444731333),
                    new LatLng(45.49732229007144, -73.57985887861618)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(MAnnex);
    }

    // MB Building
    public static PolygonOptions MBBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49553495679224, -73.57920819154843),
                    new LatLng(45.495450588360725, -73.57895556708101),
                    new LatLng(45.49518821100222, -73.57850719171735),
                    new LatLng(45.4949842418931, -73.57873071807857),
                    new LatLng(45.49502132724061, -73.57878759164979),
                    new LatLng(45.49499258609843, -73.57881933503836),
                    new LatLng(45.495163178469504, -73.57918702928939),
                    new LatLng(45.49521695204458, -73.57913015571819),
                    new LatLng(45.49536158416411, -73.57938807075037)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(MBBuilding);
    }

    // MI Annex
    public static PolygonOptions MIAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49785769165868, -73.57930305042639),
                    new LatLng(45.49782103054226, -73.57922660746671),
                    new LatLng(45.4977002366949, -73.57934395411536),
                    new LatLng(45.497736427874806, -73.57942039707504)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(MIAnnex);
    }

    // MU Annex
    public static PolygonOptions MUAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49797145699018, -73.57953576002467),
                    new LatLng(45.497926516853106, -73.57945441034337),
                    new LatLng(45.49774385657195, -73.579635034212),
                    new LatLng(45.49778058196705, -73.57971224746882)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(MUAnnex);
    }

    // P Annex
    public static PolygonOptions PAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.496748945524416, -73.57938877437017),
                    new LatLng(45.49670711342497, -73.57930227312632),
                    new LatLng(45.49666199112554, -73.57934317681527),
                    new LatLng(45.4967066434012, -73.57942900750686)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(PAnnex);
    }

    // PR Annex
    public static PolygonOptions PRAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49704723898695, -73.57995687341459),
                    new LatLng(45.497000236874825, -73.57987037217072),
                    new LatLng(45.49697203558874, -73.57990322923236),
                    new LatLng(45.497015277555, -73.57998905992392)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(PRAnnex);
    }

    // Q Annex
    public static PolygonOptions QAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.496681922393726, -73.57907446665605),
                    new LatLng(45.496654190972436, -73.57901344639876),
                    new LatLng(45.49654890545192, -73.57912006421095),
                    new LatLng(45.496578046999616, -73.57918376667735)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(QAnnex);
    }

    // R Annex
    public static PolygonOptions RAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49684173032006, -73.57941913051715),
                    new LatLng(45.49680553856511, -73.57934402866202),
                    new LatLng(45.496668761722745, -73.57947411580396),
                    new LatLng(45.496707303684445, -73.57955726428644),
                    new LatLng(45.496732684961884, -73.57953111274759),
                    new LatLng(45.49673926529121, -73.57954184158403),
                    new LatLng(45.4968393802068, -73.57944192929462),
                    new LatLng(45.49683373993459, -73.57943052990589)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(RAnnex);
    }

    // RR Annex
    public static PolygonOptions RRAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49680553131917, -73.57933224806276),
                    new LatLng(45.496767245876505, -73.57924877309502),
                    new LatLng(45.49660976963103, -73.57940747858923),
                    new LatLng(45.49664877754939, -73.57949301466728)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(RRAnnex);
    }

    // S Annex
    public static PolygonOptions SAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.4974941034378, -73.57980940284756),
                    new LatLng(45.497461202224535, -73.57974435927659),
                    new LatLng(45.49743817136382, -73.57976648750177),
                    new LatLng(45.49742971104527, -73.57975173535164),
                    new LatLng(45.49732301691891, -73.57985768261157),
                    new LatLng(45.49736484856082, -73.57994150164632)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(SAnnex);
    }

    // SB Building
    public static PolygonOptions SBBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49654123160751, -73.58622144847146),
                    new LatLng(45.49669284263967, -73.58609101209323),
                    new LatLng(45.49666217326532, -73.58602001507722),
                    new LatLng(45.49667316794863, -73.58601423625035),
                    new LatLng(45.49658752509572, -73.5858144539495),
                    new LatLng(45.496573058384755, -73.58582436050987),
                    new LatLng(45.49655743433272, -73.58578143208159),
                    new LatLng(45.49651808484913, -73.58578225762828),
                    new LatLng(45.4965186635182, -73.58576904888112),
                    new LatLng(45.49649551675054, -73.58572859709294),
                    new LatLng(45.4964590605722, -73.58572777154623),
                    new LatLng(45.49643475644016, -73.58622227401817),
                    new LatLng(45.49650535412796, -73.58621484409788),
                    new LatLng(45.49651056215061, -73.58614549817528),
                    new LatLng(45.4965186635182, -73.58614632372198),
                    new LatLng(45.49651808484913, -73.58616696238943),
                    new LatLng(45.49654065293867, -73.58622227401817)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(SBBuilding);
    }

    // T Annex
    public static PolygonOptions TAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.496804408260125, -73.57933378908338),
                    new LatLng(45.496765866364875, -73.57924929949635),
                    new LatLng(45.49670805347257, -73.5792995909172),
                    new LatLng(45.496750355594706, -73.57938743326561)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(TAnnex);
    }

    // TD Building
    public static PolygonOptions TDBuilding = new PolygonOptions()
            .add(
                    new LatLng(45.49476752068638, -73.57880708344051),
                    new LatLng(45.49472803731646, -73.57874472207865),
                    new LatLng(45.49465612110724, -73.57882720000885),
                    new LatLng(45.49469842477087, -73.57889358468438)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(TDBuilding);
    }

    // V Annex
    public static PolygonOptions VAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.4970148075338, -73.57998771881937),
                    new LatLng(45.496970625524064, -73.57990457033691),
                    new LatLng(45.49693960409227, -73.5799347451894),
                    new LatLng(45.49698472616924, -73.58001990532871)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(VAnnex);
    }

    // VA Building
    public static PolygonOptions VABuilding = new PolygonOptions()
            .add(
                    new LatLng(45.496204747484015, -73.57380202519636),
                    new LatLng(45.49607683576995, -73.57352914005833),
                    new LatLng(45.49581983796153, -73.57378026135099),
                    new LatLng(45.49567197567723, -73.57347724165784),
                    new LatLng(45.49538681160129, -73.5737685423565),
                    new LatLng(45.49567197567456, -73.57433272819402)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(VABuilding);
    }

    // X Annex
    public static PolygonOptions XAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49695566630714, -73.57965813911137),
                    new LatLng(45.496910074184825, -73.57957230841978),
                    new LatLng(45.496811839486526, -73.57967691457516),
                    new LatLng(45.496852731463086, -73.57975872195307)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(XAnnex);
    }

    // Z Annex
    public static PolygonOptions ZAnnex = new PolygonOptions()
            .add(
                    new LatLng(45.49699232798697, -73.57974128759385),
                    new LatLng(45.49695425624206, -73.57966216242504),
                    new LatLng(45.49684568112442, -73.57976676858041),
                    new LatLng(45.49687341245143, -73.57982242441948),
                    new LatLng(45.49689691356534, -73.57979895508974),
                    new LatLng(45.49690819409651, -73.5798190716581)
            )
            .strokeColor(0xFF912338);
    static{
        SGWbuildingCoordinates.add(ZAnnex);
    }


    // get all building coordinates
    public static List<PolygonOptions> getBuildingCoordinates() {
        return new ArrayList<>(SGWbuildingCoordinates);
    }
}
