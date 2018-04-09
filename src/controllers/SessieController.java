package controllers;

import domein.Klas;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
import exceptions.NotFoundException;
import java.util.Comparator;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.SessieDao;
import repository.SessieDaoJpa;

/**
 *
 * @author devri
 */
public final class SessieController implements Observer {

    private SessieDao sessieRepo;
    private GenericDao<Klas> klasRepo;
    private ObservableList<Sessie> sessieLijst;
    private FilteredList<Sessie> gefilterdeSessieLijst;

    public SessieController() {
        setSessieRepo(new SessieDaoJpa(Sessie.class));
        setKlasRepo(new GenericDaoJpa(Klas.class));
    }

    public void setSessieRepo(SessieDao sessieRepo) {
        this.sessieRepo = sessieRepo;
        this.sessieRepo.addObserver(this);
        sessieLijst = FXCollections.observableArrayList(sessieRepo.findAll());
        gefilterdeSessieLijst = new FilteredList<>(sessieLijst, s -> true);
    }

    public void setKlasRepo(GenericDaoJpa klasRepo) {
        this.klasRepo = klasRepo;
    }

    /**
     * Maakt nieuwe sessie aan en voegt deze toe aan de databank
     *
     * @param naam String met de naam van de sessie (uniek)
     * @param omschrijving String met de omschrijving van de sessie
     * @param klas Klas met de klasnaam en de betreffende leerlingen in die klas
     * @param lesuur int met het lesuur wanneer deze sessie zal plaatsvinden
     * @param datum Date met de dag wanneer deze sessie geactiveerd kan worden
     * @param soortOnderwijs SoortOnderwijsEnum voor welke doelgroep deze sessie
     * is (afstands of dagonderwijs)
     * @param foutAntwActie String met wat er gebeurt als de groep 3 maal een
     * fout antwoord ingeeft
     */
    public void createSessie(
            String naam, String omschrijving,
            Klas klas, int lesuur, Date datum,
            SoortOnderwijsEnum soortOnderwijs, String foutAntwActie) {

        if (bestaatSessieNaam(naam)) {
            throw new IllegalArgumentException("Een sessie met deze naam bestaat al");
        } else {
            sessieRepo.insert(new Sessie(naam, omschrijving, klas,
                    lesuur, datum, soortOnderwijs, foutAntwActie));

        }
    }

    /**
     * Sluit de persistentie
     */
    public void close() {
        GenericDaoJpa.closePersistency();
    }

    /**
     * Geeft een Sessie terug
     *
     * @param id id van te op te vragen sessie
     * @return sessie
     */
    public Sessie getSessie(int id) {
        return sessieRepo.get(id);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden van alle sessies
     * in de databank
     *
     * @return een ObservableList met Sessies
     */
    public ObservableList<Sessie> getAllSessies() {
        return gefilterdeSessieLijst.sorted(Comparator.comparing(Sessie::getNaam));
    }

    /**
     * Verwijdert een sessie uit de databank
     *
     * @param id id van de te verwijderen sessie
     * @throws NotFoundException indien er geen sessie werd gevonden met
     * meegegeven id
     *
     */
    public void deleteSessie(int id) {
        Sessie sessie = sessieRepo.get(id);
        if (sessie == null) {
            throw new NotFoundException("De sessie werd niet gevonden");
        }
        sessieRepo.delete(sessie);
    }

    /**
     * Valideert of een sessie reeds bestaat
     *
     * @param naam van de te valideren sessie
     * @return true als een sessie reeds bestaat anders false
     *
     */
    public boolean bestaatSessieNaam(String naam) {
        Sessie sessie = sessieRepo.getByNaam(naam);
        return sessie != null;
    }

    /**
     * Wijzigt een sessie via de setters en past deze wijziging toe in de
     * databank
     *
     * @param id id van de te wijzigen sessie
     * @param naam
     * @param omschrijving
     * @param klas
     * @param lesuur
     * @param datum
     * @param soortOnderwijs
     * @param foutAntwActie
     * @throws NotFoundException als de te wijzigen sessie niet gevonden werd
     */
    public void updateSessie(int id, String naam, String omschrijving,
            Klas klas, int lesuur, Date datum,
            SoortOnderwijsEnum soortOnderwijs, String foutAntwActie
    ) {
        Sessie sessie = sessieRepo.get(id);
        if (sessie == null) {
            throw new NotFoundException("De sessie werd niet gevonden");
        }

        sessie.setNaam(naam);
        sessie.setOmschrijving(omschrijving);
        sessie.setKlas(klas);
        sessie.setLesuur(lesuur);
        sessie.setDatum(datum);
        sessie.setSoortOnderwijs(soortOnderwijs);
        sessie.setFoutAntwActie(foutAntwActie);

        sessieRepo.update(sessie);
    }



    /**
     * Filtert de sessieLijst obv de meegegeven String
     *
     * @param toFilter Tekst waarop de lijst moet gefilterd worden
     */
    public void applyFilter(String toFilter) {
        gefilterdeSessieLijst.setPredicate(sessie -> {
            if (toFilter == null || toFilter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = toFilter.toLowerCase();
            lowerCaseFilter = lowerCaseFilter.trim().replaceAll("\\s+", "");

            if (sessie.getNaam().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            } else if (sessie.getOmschrijving().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            }
            return false; // No matches
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        sessieLijst.clear();
        sessieLijst.addAll(sessieRepo.findAll());
    }

}
