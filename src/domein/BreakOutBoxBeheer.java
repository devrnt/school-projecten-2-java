package domein;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import exceptions.NotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDao;
import repository.GenericDaoJpa;

public final class BreakOutBoxBeheer implements Observer {

    private GenericDao<BreakOutBox> breakOutBoxRepo;
    private GenericDao<Actie> actieRepo;
    private GenericDao<Oefening> oefeningRepo;

    private ObservableList<BreakOutBox> boxLijst;
    private FilteredList<BreakOutBox> gefilterdeBoxLijst;

    public BreakOutBoxBeheer() {
        setBreakOutBoxRepo(new GenericDaoJpa<>(BreakOutBox.class));
        setActieRepo(new GenericDaoJpa<>(Actie.class));
        setOefeningRepo(new GenericDaoJpa<>(Oefening.class));
    }

    public void setBreakOutBoxRepo(GenericDao<BreakOutBox> breakOutBoxRepo) {
        this.breakOutBoxRepo = breakOutBoxRepo;
        this.breakOutBoxRepo.addObserver(this);
        boxLijst = FXCollections.observableArrayList(breakOutBoxRepo.findAll());
        gefilterdeBoxLijst = new FilteredList<>(boxLijst, b -> true);
    }

    public void setActieRepo(GenericDao<Actie> actieRepo) {
        this.actieRepo = actieRepo;
    }

    public void setOefeningRepo(GenericDao<Oefening> oefeningRepo) {
        this.oefeningRepo = oefeningRepo;
    }

    public void createBreakOutBox(String naam, String omschrijving, SoortOnderwijsEnum soortOnderwijs, List<Oefening> oefeningen, List<Actie> acties) {
        breakOutBoxRepo.insert(new BreakOutBox(naam, omschrijving, soortOnderwijs, oefeningen, acties));
        // gebruik maken van add?
        //  boxLijst = FXCollections.observableArrayList(breakOutBoxRepo.findAll());
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    public ObservableList<BreakOutBox> getAllBreakOutBoxen() {
        return gefilterdeBoxLijst;
    }

    public ObservableList<BreakOutBox> getAllBreakOutBoxen(SoortOnderwijsEnum soortOnderwijs) {
        return breakOutBoxRepo.findAll().stream()
                .filter(box -> box.getSoortOnderwijsEnum() == soortOnderwijs)
                .sorted(Comparator.comparing(BreakOutBox::getNaam))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public BreakOutBox getBreakOutBox(int id) {
        return breakOutBoxRepo.get(id);
    }

    public BreakOutBox getMeestRecenteBreakOutBox() {
        return breakOutBoxRepo.findAll().stream().sorted(Comparator.comparing(BreakOutBox::getId).reversed()).collect(Collectors.toList()).get(0);
    }

    public ObservableList<Actie> getActiesByBox(int id) {
        List<Actie> acties = breakOutBoxRepo.get(id).getActies();
        return FXCollections.observableArrayList(acties);
    }

    public ObservableList<Oefening> getOefeningenByBox(int id) {
        List<Oefening> oefeningen = breakOutBoxRepo.get(id).getOefeningen();
        return FXCollections.observableArrayList(oefeningen);
    }

    public void applyFilter(String toFilter) {
        gefilterdeBoxLijst.setPredicate(box -> {
            if (toFilter == null || toFilter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = toFilter.toLowerCase();
            lowerCaseFilter = lowerCaseFilter.trim().replaceAll("\\s+", "");

            if (box.getNaam().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            } else if (box.getOmschrijving().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            }
            return false; // No matches
        });
    }

    public void deleteBreakOutBox(int boxId) {
        BreakOutBox box = breakOutBoxRepo.get(boxId);
        if (box == null) {
            throw new NotFoundException("De breakoutbox werd niet gevonden");
        }
        breakOutBoxRepo.delete(box);
    }

    public boolean zitBoxinSessie(int boxId) {
        SessieBeheer sessieBeheer = new SessieBeheer();
        return sessieBeheer.zitBoxInSessie(boxId);
    }

    @Override
    public void update(Observable o, Object arg) {
        boxLijst.clear();
        boxLijst.addAll(breakOutBoxRepo.findAll());
    }

    public ObservableList<Actie> getActies() {
        return FXCollections.observableArrayList(actieRepo.findAll());
    }

    public ObservableList<Oefening> getOefeningen() {
        return FXCollections.observableArrayList(oefeningRepo.findAll());
    }

    public void updateBreakOutBox(int id, String naam, String omschrijving, SoortOnderwijsEnum onderwijs, List<Oefening> geselecteerdeOefeningen, List<Actie> geselecteerdeActies) {
        BreakOutBox box = breakOutBoxRepo.get(id);
        if (box == null) {
            throw new NotFoundException("De BreakOutBox werd niet gevonden");
        }
        box.setNaam(naam);
        box.setOmschrijving(omschrijving);
        box.setSoortOnderwijs(onderwijs);
        box.setActies(geselecteerdeActies);
        box.setOefeningen(geselecteerdeOefeningen);
        breakOutBoxRepo.update(box);
    }

    public void createSamenvattingBox(int id) throws IOException, FileNotFoundException, DocumentException {

        String DEST = "src/pdf/" + breakOutBoxRepo.get(id).getNaam() + "-samenvatting.pdf";
        File file = new File(DEST);
//        int i = 1;
//        
//        while (file.exists() && !file.isDirectory()) {
//            
//            file = new File(DEST.substring(0, DEST.length()-4) + String.valueOf(i++)+"pdf");
//            
//        }
//        
        new BreakOutBoxBeheer().createPdf(DEST, id);

    }

    public void createPdf(String dest, int id) throws IOException, DocumentException {
        BreakOutBox box = breakOutBoxRepo.get(id);
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        //informatie/intro van de pdf
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(box.getNaam() + " samenvatting", new Font(Font.FontFamily.COURIER, 18, Font.BOLD)));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Samenvatting gemaakt op: " + new Date(), new Font(Font.FontFamily.COURIER, 8)));
        addEmptyLine(preface, 4);
        document.add(preface);

        //logo van de BoB plaatsen op de pdf
        PdfContentByte canvas = writer.getDirectContentUnder();
        Image image = Image.getInstance("src/main/iconBlue128x128.png");
        image.setAbsolutePosition(440, 640);
        canvas.addImage(image);

        //informatie over de BoB op de pdf plaatsen
        Paragraph info = new Paragraph();
        info.add(new Paragraph("Naam:               " + box.getNaam()));
        addEmptyLine(info, 1);
        info.add(new Paragraph("Omschrijving:   " + box.getOmschrijving()));
        addEmptyLine(info, 1);
        info.add(new Paragraph("Doelstellinen:   " + box.getDoelstellingen().toString().substring(1, box.getDoelstellingen().toString().length() - 1)));
        addEmptyLine(info, 2);
        document.add(info);

        //tabel met de overzicht van de BoB
        PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Oefening"));

        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Antwoord"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Doelstelling"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Actie"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        int size = box.getOefeningen().size();
        for (int i = 0; i < size; i++) {
            if (box.getOefeningen().size() - 1 >= i) {
                table.addCell(new File(box.getOefeningen().get(i).getOpgave()).getName());
                table.addCell(String.valueOf(box.getOefeningen().get(i).getAntwoord()));
                table.addCell(box.getOefeningen().get(i).getDoelstellingen().toString().substring(1, box.getOefeningen().get(i).getDoelstellingen().toString().length() - 1));
            } else {
                table.addCell("");
                table.addCell("");
                table.addCell("");
            }
            if (box.getActies().size() - 1 >= i) {
                table.addCell(box.getActies().get(i).getOmschrijving());
            } else {
                table.addCell("SCHATKIST");
            }
        }
        document.add(table);

        //Overzicht vervolg Pdf
        Paragraph inhoudstabel = new Paragraph();
        addEmptyLine(inhoudstabel, 2);
        inhoudstabel.add(new Paragraph("In de volgende bladzijden worden de oefeningen chronologisch afgelopen. "));
        inhoudstabel.add(new Paragraph("Hieronder vindt u een inhoudstabel.   "));
        int paginaTeller = 2;
        int actieTeller = 0;
        for (Oefening oef : box.getOefeningen()) {
            if (actieTeller <= size - 2) {
                inhoudstabel.add(new Paragraph("Oefening/feedback/actie " + (actieTeller + 1), new Font(Font.FontFamily.COURIER, 12, Font.BOLD)));
            } else {
                inhoudstabel.add(new Paragraph("Oefening/feedback " + (actieTeller + 1), new Font(Font.FontFamily.COURIER, 12, Font.BOLD)));
            }
            inhoudstabel.add(new Paragraph("Pagina " + paginaTeller++ + ": " + new File(oef.getOpgave()).getName()));
            inhoudstabel.add(new Paragraph("Pagina " + paginaTeller++ + ": " + new File(oef.getFeedback()).getName()));
            if (actieTeller <= size - 2) {
                inhoudstabel.add(new Paragraph("Pagina " + paginaTeller++ + ": " + box.getActies().get(actieTeller++)));
            } else {
                inhoudstabel.add(new Paragraph("einde"));
            }

        }

        for (int i = 2; i < (size * 3); i++) {

        }
        document.add(inhoudstabel);
        document.close();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public boolean bestaatBoxNaam(String text) {
        return this.boxLijst.filtered(it -> it.getNaam().equals(text)).size() > 0;
    }
}
