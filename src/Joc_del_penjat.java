import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Joc_del_penjat {
    public String nodePare = "PARAULES";
    public String nodeFill = "PARAULA";
    public String JUGADOR = "JUGADOR";
    public String MOMENT = "MOMENT";
    public String RESULTAT = "RESULTAT";
    private final static String PUNTUACIONS = "PUNTUACIONS";
    public String paraula = "";
    public String moment = "";
    private final static String ARXIU = "paraules.json";
    private final static String PARTIDES = "partides.json";
    private JSONObject json = new JSONObject();
    private JSONObject jsonPartides = new JSONObject();
    private JSONArray paraules = new JSONArray();
    private JSONObject paraulaJson = new JSONObject();

    public static void main(String[] args) throws IOException {
        Joc_del_penjat programa = new Joc_del_penjat();
        programa.controlador();
    }

    private void controlador() throws IOException {
        CarregarJSON();
        CarregarPartides();
        int valorMenu;

        valorMenu = MostrarMenu();

        switch (valorMenu){
            case 0:
                System.out.println("Adéu, gracies per utilitzar aquest software!");
                break;
            case 1:
                Jugar();
                controlador();
                break;
            case 2:
                Resultats();
                controlador();
                break;
            case 3:
                Buidar();
                controlador();
                break;
        }
    }

    private JSONObject AgafarParaula() throws JSONException {
        JSONArray ar = new JSONArray();
        JSONObject ob = null;
        try {
            // tria un
            ar = json.getJSONArray(nodePare);
            int i = (int) (Math.random() * ar.length());
            ob = ar.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ob;
    }

    private void PasarParaulaString(JSONObject ob) {
        paraula = ob.getString(nodeFill);
    }

    private void CarregarJSON() throws IOException {
        try {
            File fitxer = new File(ARXIU);

            FileReader fr = new FileReader(ARXIU);
            BufferedReader br = new BufferedReader(fr);

            String s = "";
            String jsonString = "";

            // Llegim totes les línies del fitxerºº
            while ((s = br.readLine()) != null) {
                jsonString = jsonString.concat(s);
            }

            fr.close();

            json = new JSONObject(jsonString);  // serialitzem


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void CarregarPartides()  {
        // TODO Auto-generated method stub
        try {
            File fitxer = new File(PARTIDES);

            // Preguntem si el fitxer existeix en el disc
            // la primera vegada no existirà
            if (fitxer.exists()) {
                FileReader fr = new FileReader(PARTIDES);
                BufferedReader br = new BufferedReader(fr);

                String s = "";
                String jsonString = "";

                // Llegim totes les línies del fitxer
                while((s = br.readLine()) != null) {
                    jsonString = jsonString + s;
                }

                fr.close();
                jsonPartides = new JSONObject(jsonString);  // serialitzem
            }
            else {
                fitxer.createNewFile();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void AfegirPuntuacio(String jugador, String resultat) {
        try {
            JSONObject puntuacio = new JSONObject();

            puntuacio.put(JUGADOR, jugador);
            puntuacio.put(MOMENT, moment);
            puntuacio.put(RESULTAT, resultat);


            jsonPartides.getJSONArray(PUNTUACIONS).put(puntuacio);

            // Gravem els canvis
            saveJson();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void PintarLinies(char[] lletres, int T) {
        for (int i = 0; i < T; i++) {
            if (lletres[i] != ' ') {
                System.out.print(lletres[i]);
            } else {
                System.out.print("-");
            }
        }
        System.out.println("");
    }

    private char IntroduirLletres(char lletra) {
        Scanner teclat = new Scanner(System.in);
        System.out.print("Introdueix una lletra: ");
        lletra = teclat.next().charAt(0);
        lletra = Character.toUpperCase(lletra);
        while (!Character.isLetter(lletra)) {
            System.out.println("Valor incorrecte, torna a probar-ho: ");
            lletra = teclat.next().charAt(0);
        }


        return lletra;
    }

    private char[] ComprovarLletra(char lletra, char[] caracters, char[] lletres, int T) {
        for (int i = 0; i < T; i++) {
            if (caracters[i] == lletra) {
                lletres[i] = lletra;
            }
        }
        return lletres;
    }

    private int ContadorErrors(char lletra, char[] caracters, char[] lletres, int errors, int T) {
        boolean com = false;
        for (int i = 0; i < T; i++) {
            if (caracters[i] == lletra) {
                com = true;
            }
        }
        if (com == false) {
            errors++;
        }
        return errors;
    }

    private int ContadorEncerts(char lletra, char[] caracters, char[] lletres, int encerts, int T) {
        boolean com = false;
        for (int i = 0; i < T; i++) {
            if (caracters[i] == lletra) {
                com = true;
            }
        }
        if (com != false) {
            encerts++;
        }
        return encerts;
    }

    private char[] farcirTaula(char[] lletres, int T) {
        for (int i = 0; i < T; i++) {
            lletres[i] = ' ';
        }
        return lletres;
    }

    private void MostrarErrors(int errors) {
        System.out.println("El numero de errors que has comès és: " + errors);

    }

    private void MostrarEncerts(int encerts) {
        System.out.println("El numero de encerts que has comès és: " + encerts);
    }

    private boolean ComprovarPerdedor(boolean perdre, int errors) {
        if (errors >= 8) {
            perdre = true;
        }
        return perdre;
    }

    private boolean ComprovarGuananyador(boolean guanyar, char[] lletres, int T) {
        guanyar = true;
        for (int i = 0; i < T; i++) {
            if (lletres[i] == ' ') {
                guanyar = false;
            }
        }
        return guanyar;
    }

    private void saveJson() {
        // Guardem el json serialitzat
        try {

            FileWriter file = new FileWriter(PARTIDES);
            file.write(jsonPartides.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String IntroduirNomUsuari() {
        String jugador;
        Scanner teclat = new Scanner(System.in);

        System.out.println("Introdueix el teu nom: ");
        jugador = teclat.nextLine();

        return jugador;
    }

    public void DataSistema() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        moment = dateFormat.format(cal.getTime());
    }

    private int MostrarMenu() {
        Scanner teclat = new Scanner(System.in);
        int valorMenu;

        System.out.println("Benvingut al PENJAT GSS 2019");
        System.out.println("Menu->");
        System.out.println("0)Sortir");
        System.out.println("1)Jugar");
        System.out.println("2)Resultats");
        System.out.println("3)Buidar");

        valorMenu=ComprobarNumero();
        valorMenu=ComprobarIntroduccioMenu(valorMenu);

        return valorMenu;
    }
    public int ComprobarNumero(){
        Scanner teclat = new Scanner(System.in);
        int valorMenu = 0;
        while (!teclat.hasNextInt()){
            teclat.next();
            System.out.print("No es un numero enter!");
        }
        valorMenu=teclat.nextInt();
        teclat.nextLine();
        return valorMenu;
    }
    private int ComprobarIntroduccioMenu(int valorMenu){
        Scanner teclat = new Scanner(System.in);

        while(valorMenu!=0 && valorMenu!=1 && valorMenu!=2 && valorMenu!=3){
            System.out.println("Valor incorrecte has de introduir 0/1/2/3");
            valorMenu = ComprobarNumero();
        }
        return valorMenu;
    }
    private void Jugar() throws IOException {
        char[] caracters;
        char[] lletres;
        char lletra = ' ';
        boolean guanyar = true;
        boolean perdre = false;
        int errors = 0;
        int encerts = 0;

        String jugador;
        int T;
        String resultat;
        JSONObject ob = null;
        Scanner teclat = new Scanner(System.in);

        ob = AgafarParaula();
        PasarParaulaString(ob);

        T = paraula.length();
        lletres = new char[T];

        caracters = paraula.toCharArray();

        lletres = farcirTaula(lletres, T);

        do {
            PintarLinies(lletres, T);
            lletra = IntroduirLletres(lletra);
            lletres = ComprovarLletra(lletra, caracters, lletres, T);
            encerts = ContadorEncerts(lletra, caracters, lletres, encerts, T);
            errors = ContadorErrors(lletra, caracters, lletres, errors, T);
            MostrarEncerts(encerts);
            MostrarErrors(errors);
            perdre = ComprovarPerdedor(perdre, errors);
            guanyar = ComprovarGuananyador(guanyar, lletres, T);
        } while (guanyar == false && perdre == false);
        if (guanyar == true) {
            PintarLinies(lletres, T);
            System.out.println("Feliçitats has guanyat");
            resultat = "GUANYADA";
        } else {
            System.out.println("Has perdut");
            System.out.println("La paraula era " + paraula);
            resultat = "PERDUDA";
        }
        jugador = IntroduirNomUsuari();
        DataSistema();
        AfegirPuntuacio(jugador, resultat);
    }
    private void Resultats() throws JSONException {
        // TODO Auto-generated method stub
        JSONArray ar = new JSONArray();
        JSONObject ob;
        try {
            // Passem per tots les tasques
            ar = jsonPartides.getJSONArray(PUNTUACIONS);
            if(ar.length()==0){
                System.out.println("");
                System.out.println("El registre està buit!");
            }
            else {
                for (int i = 0; i < ar.length(); i++) {

                    ob = ar.getJSONObject(i);

                    System.out.println("");

                    System.out.println("Jugador:         " + ob.getString(JUGADOR));
                    System.out.println("Moment: " + ob.getString(MOMENT));
                    System.out.println("Resultats:      " + ob.getString(RESULTAT));

                    System.out.println("-------------------------");
                }
            }
            saltar(5);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void saltar(int salts) {
        for (int i = 0; i<salts; i++)
            System.out.println("");
    }
    private void Buidar() throws JSONException {
            JSONArray ar = jsonPartides.getJSONArray(PUNTUACIONS);

            for (int i = ar.length(); i > -1; i--) {
                ar.remove(i);
            }

            saveJson();
            System.out.println("Registres borrats!");
            System.out.println("");
    }
}