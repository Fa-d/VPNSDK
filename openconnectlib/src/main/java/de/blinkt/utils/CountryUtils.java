package de.blinkt.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import de.blinkt.openconnect.R;


public class CountryUtils {

    public static int getFlagImage(String countryCode){
        switch (countryCode) {
            case "004": //afghanistan
                return R.drawable.flag_afghanistan;
            case "008": //albania
                return R.drawable.flag_albania;
            case "012": //algeria
                return R.drawable.flag_algeria;
            case "020": //andorra
                return R.drawable.flag_andorra;
            case "024": //angola
                return R.drawable.flag_angola;
            case "010": //antarctica
                return R.drawable.flag_antarctica;
            case "032": //argentina
                return R.drawable.flag_argentina;
            case "051": //armenia
                return R.drawable.flag_armenia;
            case "533": //aruba
                return R.drawable.flag_aruba;
            case "036": //australia
                return R.drawable.flag_australia;
            case "040": //austria
                return R.drawable.flag_austria;
            case "031": //azerbaijan
                return R.drawable.flag_azerbaijan;
            case "048": //bahrain
                return R.drawable.flag_bahrain;
            case "050": //bangladesh
                return R.drawable.flag_bangladesh;
            case "112": //belarus
                return R.drawable.flag_belarus;
            case "056": //belgium
                return R.drawable.flag_belgium;
            case "084": //belize
                return R.drawable.flag_belize;
            case "204": //benin
                return R.drawable.flag_benin;
            case "064": //bhutan
                return R.drawable.flag_bhutan;
            case "068": //bolivia, plurinational state of
                return R.drawable.flag_bolivia;
            case "070": //bosnia and herzegovina
                return R.drawable.flag_bosnia;
            case "072": //botswana
                return R.drawable.flag_botswana;
            case "076": //brazil
                return R.drawable.flag_brazil;
            case "096": //brunei darussalam
                return R.drawable.flag_brunei;
            case "100": //bulgaria
                return R.drawable.flag_bulgaria;
            case "854": //burkina faso
                return R.drawable.flag_burkina_faso;
            case "104": //myanmar
                return R.drawable.flag_myanmar;
            case "108": //burundi
                return R.drawable.flag_burundi;
            case "116": //cambodia
                return R.drawable.flag_cambodia;
            case "120": //cameroon
                return R.drawable.flag_cameroon;
            case "124": //canada
                return R.drawable.flag_canada;
            case "132": //cape verde......
                return R.drawable.flag_cape_verde;
            case "140": //central african republic
                return R.drawable.flag_central_african_republic;
            case "148": //chad
                return R.drawable.flag_chad;
            case "152": //chile
                return R.drawable.flag_chile;
            case "156": //china
                return R.drawable.flag_china;
            case "170": //colombia
                return R.drawable.flag_colombia;
            case "174": //comoros
                return R.drawable.flag_comoros;
            case "178": //congo
                return R.drawable.flag_republic_of_the_congo;
            case "180": //congo, the democratic republic of the
                return R.drawable.flag_democratic_republic_of_the_congo;
            case "184": //cook islands
                return R.drawable.flag_cook_islands;
            case "188": //costa rica
                return R.drawable.flag_costa_rica;
            case "191": //croatia
                return R.drawable.flag_croatia;
            case "192": //cuba
                return R.drawable.flag_cuba;
            case "196": //cyprus
                return R.drawable.flag_cyprus;
            case "203": //czech republic
                return R.drawable.flag_czech_republic;
            case "208": //denmark
                return R.drawable.flag_denmark;
            case "262": //djibouti
                return R.drawable.flag_djibouti;
            case "626": //timor-leste
                return R.drawable.flag_timor_leste;
            case "218": //ecuador
                return R.drawable.flag_ecuador;
            case "818": //egypt
                return R.drawable.flag_egypt;
            case "222": //el salvador
                return R.drawable.flag_el_salvador;
            case "226": //equatorial guinea
                return R.drawable.flag_equatorial_guinea;
            case "232": //eritrea
                return R.drawable.flag_eritrea;
            case "233": //estonia
                return R.drawable.flag_estonia;
            case "231": //ethiopia
                return R.drawable.flag_ethiopia;
            case "238": //falkland islands (malvinas)
                return R.drawable.flag_falkland_islands;
            case "234": //faroe islands
                return R.drawable.flag_faroe_islands;
            case "242": //fiji
                return R.drawable.flag_fiji;
            case "246": //finland
                return R.drawable.flag_finland;
            case "250": //france
                return R.drawable.flag_france;
            case "258": //french polynesia
                return R.drawable.flag_french_polynesia;
            case "266": //gabon
                return R.drawable.flag_gabon;
            case "270": //gambia
                return R.drawable.flag_gambia;
            case "268": //georgia
                return R.drawable.flag_georgia;
            case "276": //germany
                return R.drawable.flag_germany;
            case "288": //ghana
                return R.drawable.flag_ghana;
            case "292": //gibraltar
                return R.drawable.flag_gibraltar;
            case "300": //greece
                return R.drawable.flag_greece;
            case "304": //greenland
                return R.drawable.flag_greenland;
            case "320": //guatemala
                return R.drawable.flag_guatemala;
            case "324": //guinea
                return R.drawable.flag_guinea;
            case "624": //guinea-bissau
                return R.drawable.flag_guinea_bissau;
            case "328": //guyana
                return R.drawable.flag_guyana;
            case "gf": //guyane.....
                return R.drawable.flag_guyane;
            case "332": //haiti
                return R.drawable.flag_haiti;
            case "340": //honduras
                return R.drawable.flag_honduras;
            case "344": //hong kong
                return R.drawable.flag_hong_kong;
            case "348": //hungary
                return R.drawable.flag_hungary;
            case "356": //india
                return R.drawable.flag_india;
            case "360": //indonesia
                return R.drawable.flag_indonesia;
            case "364": //iran, islamic republic of
                return R.drawable.flag_iran;
            case "368": //iraq
                return R.drawable.flag_iraq;
            case "372": //ireland
                return R.drawable.flag_ireland;
            case "833": //isle of man
                return R.drawable.flag_isleof_man;
            case "376": //israel
                return R.drawable.flag_israel;
            case "380": //italy
                return R.drawable.flag_italy;
            case "384": //côte d\'ivoire
                return R.drawable.flag_cote_divoire;
            case "392": //japan
                return R.drawable.flag_japan;
            case "400": //jordan
                return R.drawable.flag_jordan;
            case "398": //kazakhstan
                return R.drawable.flag_kazakhstan;
            case "404": //kenya
                return R.drawable.flag_kenya;
            case "296": //kiribati
                return R.drawable.flag_kiribati;
            case "414": //kuwait
                return R.drawable.flag_kuwait;
            case "417": //kyrgyzstan
                return R.drawable.flag_kyrgyzstan;
            case "136": // Cayman Islands
                return R.drawable.flag_cayman_islands;
            case "418": //lao people\'s democratic republic
                return R.drawable.flag_laos;
            case "428": //latvia
                return R.drawable.flag_latvia;
            case "422": //lebanon
                return R.drawable.flag_lebanon;
            case "426": //lesotho
                return R.drawable.flag_lesotho;
            case "430": //liberia
                return R.drawable.flag_liberia;
            case "434": //libya
                return R.drawable.flag_libya;
            case "438": //liechtenstein
                return R.drawable.flag_liechtenstein;
            case "440": //lithuania
                return R.drawable.flag_lithuania;
            case "442": //luxembourg
                return R.drawable.flag_luxembourg;
            case "446": //macao
                return R.drawable.flag_macao;
            case "450": //madagascar
                return R.drawable.flag_madagascar;
            case "454": //malawi
                return R.drawable.flag_malawi;
            case "458": //malaysia
                return R.drawable.flag_malaysia;
            case "462": //maldives
                return R.drawable.flag_maldives;
            case "466": //mali
                return R.drawable.flag_mali;
            case "470": //malta
                return R.drawable.flag_malta;
            case "584": //marshall islands
                return R.drawable.flag_marshall_islands;
            case "478": //mauritania
                return R.drawable.flag_mauritania;
            case "480": //mauritius
                return R.drawable.flag_mauritius;
            case "474": //martinique
                return R.drawable.flag_martinique;
            case "484": //mexico
                return R.drawable.flag_mexico;
            case "583": //micronesia, federated states of
                return R.drawable.flag_micronesia;
            case "498": //moldova, republic of
                return R.drawable.flag_moldova;
            case "492": //monaco
                return R.drawable.flag_monaco;
            case "496": //mongolia
                return R.drawable.flag_mongolia;
            case "499": //montenegro
                return R.drawable.flag_of_montenegro;// custom
            case "504": //morocco
                return R.drawable.flag_morocco;
            case "508": //mozambique
                return R.drawable.flag_mozambique;
            case "516": //namibia
                return R.drawable.flag_namibia;
            case "520": //nauru
                return R.drawable.flag_nauru;
            case "524": //nepal
                return R.drawable.flag_nepal;
            case "528": //netherlands
                return R.drawable.flag_netherlands;
            case "540": //new caledonia
                return R.drawable.flag_new_caledonia;
            case "554": //new zealand
                return R.drawable.flag_new_zealand;
            case "558": //nicaragua
                return R.drawable.flag_nicaragua;
            case "562": //niger
                return R.drawable.flag_niger;
            case "566": //nigeria
                return R.drawable.flag_nigeria;
            case "570": //niue
                return R.drawable.flag_niue;
            case "410": //north korea
                return R.drawable.flag_north_korea;
            case "578": //norway
                return R.drawable.flag_norway;
            case "512": //oman
                return R.drawable.flag_oman;
            case "586": //pakistan
                return R.drawable.flag_pakistan;
            case "585": //palau
                return R.drawable.flag_palau;
            case "591": //panama
                return R.drawable.flag_panama;
            case "598": //papua new guinea
                return R.drawable.flag_papua_new_guinea;
            case "600": //paraguay
                return R.drawable.flag_paraguay;
            case "604": //peru
                return R.drawable.flag_peru;
            case "608": //philippines
                return R.drawable.flag_philippines;
            case "612": //pitcairn
                return R.drawable.flag_pitcairn_islands;
            case "616": //poland
                return R.drawable.flag_poland;
            case "620": //portugal
                return R.drawable.flag_portugal;
            case "630": //puerto rico
                return R.drawable.flag_puerto_rico;
            case "634": //qatar
                return R.drawable.flag_qatar;
            case "642": //romania
                return R.drawable.flag_romania;
            case "643": //russian federation
                return R.drawable.flag_russian_federation;
            case "646": //rwanda
                return R.drawable.flag_rwanda;
            case "652": //saint barthélemy
                return R.drawable.flag_saint_barthelemy;// custom
            case "882": //samoa
                return R.drawable.flag_samoa;
            case "674": //san marino
                return R.drawable.flag_san_marino;
            case "678": //sao tome and principe
                return R.drawable.flag_sao_tome_and_principe;
            case "682": //saudi arabia
                return R.drawable.flag_saudi_arabia;
            case "686": //senegal
                return R.drawable.flag_senegal;
            case "688": //serbia
                return R.drawable.flag_serbia; // custom
            case "690": //seychelles
                return R.drawable.flag_seychelles;
            case "694": //sierra leone
                return R.drawable.flag_sierra_leone;
            case "702": //singapore
                return R.drawable.flag_singapore;
            case "703": //slovakia
                return R.drawable.flag_slovakia;
            case "705": //slovenia
                return R.drawable.flag_slovenia;
            case "090": //solomon islands
                return R.drawable.flag_soloman_islands;
            case "706": //somalia
                return R.drawable.flag_somalia;
            case "710": //south africa
                return R.drawable.flag_south_africa;
            case "408": //south korea
                return R.drawable.flag_south_korea;
            case "724": //spain
                return R.drawable.flag_spain;
            case "144": //sri lanka
                return R.drawable.flag_sri_lanka;
            case "654": //saint helena, ascension and tristan da cunha
                return R.drawable.flag_saint_helena; // custom
            case "666": //saint pierre and miquelon
                return R.drawable.flag_saint_pierre;
            case "729": //sudan
                return R.drawable.flag_sudan;
            case "740": //suriname
                return R.drawable.flag_suriname;
//            case "268": //swaziland.......
//                return R.drawable.flag_swaziland;
            case "752": //sweden
                return R.drawable.flag_sweden;
            case "756": //switzerland
                return R.drawable.flag_switzerland;
            case "760": //syrian arab republic
                return R.drawable.flag_syria;
            case "158": //taiwan, province of china
                return R.drawable.flag_taiwan;
            case "762": //tajikistan
                return R.drawable.flag_tajikistan;
            case "834": //tanzania, united republic of
                return R.drawable.flag_tanzania;
            case "764": //thailand
                return R.drawable.flag_thailand;
            case "768": //togo
                return R.drawable.flag_togo;
            case "772": //tokelau
                return R.drawable.flag_tokelau;
            case "776": //tonga
                return R.drawable.flag_tonga;
            case "788": //tunisia
                return R.drawable.flag_tunisia;
            case "792": //turkey
                return R.drawable.flag_turkey;
            case "795": //turkmenistan
                return R.drawable.flag_turkmenistan;
            case "798": //tuvalu
                return R.drawable.flag_tuvalu;
            case "784": //united arab emirates
                return R.drawable.flag_uae;
            case "800": //uganda
                return R.drawable.flag_uganda;
            case "826": //united kingdom
                return R.drawable.flag_united_kingdom;
            case "804": //ukraine
                return R.drawable.flag_ukraine;
            case "858": //uruguay
                return R.drawable.flag_uruguay;
            case "840": //united states
                return R.drawable.flag_united_states_of_america;
            case "860": //uzbekistan
                return R.drawable.flag_uzbekistan;
            case "548": //vanuatu
                return R.drawable.flag_vanuatu;
//            case "va": //holy see (vatican city state)......
//                return R.drawable.flag_vatican_city;
            case "862": //venezuela, bolivarian republic of
                return R.drawable.flag_venezuela;
            case "704": //vietnam
                return R.drawable.flag_vietnam;
            case "876": //wallis and futuna
                return R.drawable.flag_wallis_and_futuna;
            case "887": //yemen
                return R.drawable.flag_yemen;
            case "894": //zambia
                return R.drawable.flag_zambia;
            case "716": //zimbabwe
                return R.drawable.flag_zimbabuwe;

            // Caribbean Islands
            case "660": //anguilla
                return R.drawable.flag_anguilla;
            case "028": //antigua & barbuda
                return R.drawable.flag_antigua_and_barbuda;
            case "044": //bahamas
                return R.drawable.flag_bahamas;
            case "052": //barbados
                return R.drawable.flag_barbados;
            case "bm": //bermuda.....
                return R.drawable.flag_bermuda;
            case "092": //british virgin islands
                return R.drawable.flag_british_virgin_islands;
            case "212": //dominica
                return R.drawable.flag_dominica;
            case "214": //dominican republic
                return R.drawable.flag_dominican_republic;
            case "308": //grenada
                return R.drawable.flag_grenada;
            case "388": //jamaica
                return R.drawable.flag_jamaica;
            case "500": //montserrat
                return R.drawable.flag_montserrat;
            case "659": //st kitts & nevis
                return R.drawable.flag_saint_kitts_and_nevis;
            case "662": //st lucia
                return R.drawable.flag_saint_lucia;
            case "670": //st vincent & the grenadines
                return R.drawable.flag_saint_vicent_and_the_grenadines;
            case "780": //trinidad & tobago
                return R.drawable.flag_trinidad_and_tobago;
            case "796": //turks & caicos islands
                return R.drawable.flag_turks_and_caicos_islands;
            case "850": //us virgin islands
                return R.drawable.flag_us_virgin_islands;
            case "728": // south sudan
                return R.drawable.flag_south_sudan;
            default:
                return R.drawable.no_image;
        }
    }

    public static String getDeviceCountryCode(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkCountryIso();
    }
}
