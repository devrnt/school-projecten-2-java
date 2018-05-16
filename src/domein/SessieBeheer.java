package domein;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import exceptions.NotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
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
public final class SessieBeheer implements Observer {

    private SessieDao sessieRepo;
    private GenericDao<Klas> klasRepo;
    private ObservableList<Sessie> sessieLijst;
    private FilteredList<Sessie> gefilterdeSessieLijst;

    public SessieBeheer() {
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

    public void createSessie(
            String naam, String omschrijving, Klas klas,
            BreakOutBox box, Date datum, SoortOnderwijsEnum soortOnderwijs,
            FoutAntwoordActieEnum foutAntwoordActie, Boolean isGedaan) {

        if (bestaatSessieNaam(naam)) {
            throw new IllegalArgumentException("Een sessie met deze naam bestaat al");
        } else {
            sessieRepo.insert(new Sessie(naam, omschrijving, klas, box,
                    datum, soortOnderwijs, foutAntwoordActie, isGedaan));

        }
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    public Sessie getSessie(int id) {
        return sessieRepo.get(id);
    }

    public ObservableList<Sessie> getAllSessies() {
        return gefilterdeSessieLijst.sorted(Comparator.comparing(Sessie::getNaam));
    }

    public Sessie getMeestRecenteSessie() {
        return sessieRepo.findAll().stream().sorted(Comparator.comparing(Sessie::getId).reversed()).collect(Collectors.toList()).get(0);
    }

    public void deleteSessie(int id) {
        Sessie sessie = sessieRepo.get(id);
        if (sessie == null) {
            throw new NotFoundException("De sessie werd niet gevonden");
        }
        sessieRepo.delete(sessie);
    }

    public boolean zitBoxInSessie(int boxId) {
        //dit moet beter -Yanis
        return sessieLijst.stream().anyMatch(s -> s.getBox().getId() == boxId);
    }

    public boolean bestaatSessieNaam(String naam) {
        Sessie sessie = sessieRepo.getByNaam(naam);
        return sessie != null;
    }

    public void updateSessie(int id, String naam, String omschrijving,
            Klas klas, BreakOutBox box, Date datum,
            SoortOnderwijsEnum soortOnderwijs, FoutAntwoordActieEnum foutAntwoordActie
    ) {
        Sessie sessie = sessieRepo.get(id);
        if (sessie == null) {
            throw new NotFoundException("De sessie werd niet gevonden");
        }

        sessie.setNaam(naam);
        sessie.setOmschrijving(omschrijving);
        sessie.setKlas(klas);
        sessie.setDatum(datum);
        sessie.setSoortOnderwijs(soortOnderwijs);
        sessie.setFoutAntwoordActie(foutAntwoordActie);

        sessieRepo.update(sessie);
    }

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

    public void createSamenvattingSessie(int id) throws IOException, FileNotFoundException, DocumentException {
        String DEST = "src/pdf/" + sessieRepo.get(id).getNaam() + "-samenvatting.pdf";
        File file = new File(DEST);
        new SessieBeheer().createPdf(DEST, id);
    }

    private void createPdf(String dest, int id) throws IOException, DocumentException {
        Sessie ses = sessieRepo.get(id);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(ses.getNaam() + "- samenvatting", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD)));
        //addEmptyLine(preface, 1);
        preface.add(new Paragraph("Samenvatting gemaakt op: " + new Date(), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        addEmptyLine(preface, 3);
        document.add(preface);

        Paragraph info = new Paragraph();
        document.add(new Paragraph("Omschrijving: "));
        document.add(addInfoParagraph(ses.getOmschrijving()));
        document.add(new Paragraph("Klas: "));
        document.add(addInfoParagraph(ses.getKlas().getNaam()));
        document.add(new Paragraph("BreakOutBox: "));
        document.add(addInfoParagraph(ses.getBoxNaam()));
        document.add(new Paragraph("Sessiecode: "));
        document.add(addInfoParagraph(ses.getSessieCode()));
        document.add(new Paragraph("FoutAnrwoord: "));
        document.add(addInfoParagraph(ses.getFoutAntwoordActie().name()));
        document.add(new Paragraph(" "));
        Paragraph groepen = new Paragraph();
        for (Groep groep : ses.getGroepen()) {
            groepen.add(new DottedLineSeparator());
            addEmptyLine(groepen, 1);
            groepen.add(new Paragraph("Pad voor " + groep.getNaam()));
            addEmptyLine(groepen, 1);
            SessiePad pad = groep.getSessiePad();
            String oefOpl = "";
            int a = 1;
            for (Opdracht opdracht : pad.getOpdrachten()) {
                oefOpl += a + ". " + opdracht.getOefening().getAntwoord() + " | ";
                a++;
            }
            oefOpl = oefOpl.substring(0, oefOpl.length() - 2);
            groepen.add(new Paragraph("Oplossingen Oefeningen:"));
            groepen.add(new Paragraph(oefOpl));
            addEmptyLine(groepen, 1);
            if (ses.getSoortOnderwijs() == SoortOnderwijsEnum.dagonderwijs) {
                String actOpl = "";
                for (int i = 1; i <= pad.getActies().size(); i++) {
                    actOpl += i + ". " + pad.getActies().get(i - 1).getOmschrijving() + " = " + pad.getOpdrachten().get(i).getToegangscode() + " | ";
                }
                actOpl = actOpl.substring(0, actOpl.length() - 2);

                groepen.add(new Paragraph("Acties & Oplossingen: "));
                groepen.add(new Paragraph(actOpl));
                addEmptyLine(groepen, 1);
            }

        }
        groepen.add(new DottedLineSeparator());

        document.add(info);
        document.add(groepen);
        document.close();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private Paragraph addInfoParagraph(String string) {
        Paragraph p = new Paragraph(string);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }
}
