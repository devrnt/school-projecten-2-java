package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import domein.Toegangscode;
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

public final class BoxController implements Observer {

    private GenericDao<BreakOutBox> breakOutBoxRepo;
    private GenericDao<Actie> actieRepo;
    private GenericDao<Oefening> oefeningRepo;
    private GenericDao<Toegangscode> toegangscodeRepo;

    private ObservableList<BreakOutBox> boxLijst;
    private FilteredList<BreakOutBox> gefilterdeBoxLijst;

    public BoxController() {
        setBreakOutBoxRepo(new GenericDaoJpa<>(BreakOutBox.class));
        setActieRepo(new GenericDaoJpa<>(Actie.class));
        setToegangscodeRepo(new GenericDaoJpa<>(Toegangscode.class));
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

    public void setToegangscodeRepo(GenericDao<Toegangscode> toegangscodeRepo) {
        this.toegangscodeRepo = toegangscodeRepo;
    }

    public void createBreakOutBox(String naam, String omschrijving, List<Oefening> oefeningen, List<Actie> acties, List<Toegangscode> toegangscodes) {
        breakOutBoxRepo.insert(new BreakOutBox(naam, omschrijving, oefeningen, acties, toegangscodes));
        // gebruik maken van add?
        boxLijst = FXCollections.observableArrayList(breakOutBoxRepo.findAll());
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    public ObservableList<BreakOutBox> getAllBreakOutBoxen() {
        return gefilterdeBoxLijst.sorted(Comparator.comparing(BreakOutBox::getNaam));
    }

    public BreakOutBox GeefBreakOutBox(int id) {
        return breakOutBoxRepo.get(id);
    }

    public ObservableList<Actie> getActiesByBox(int id) {
        List<Actie> acties = breakOutBoxRepo.get(id).getActies();
        return FXCollections.observableArrayList(acties);
    }

    public ObservableList<Oefening> getOefeningenByBox(int id) {
        List<Oefening> oefeningen = breakOutBoxRepo.get(id).getOefeningen();
        return FXCollections.observableArrayList(oefeningen);
    }

    public ObservableList<Toegangscode> getToegangscodesByBox(int id) {
        List<Toegangscode> toegangscodes = breakOutBoxRepo.get(id).getToegangscodes();
        return FXCollections.observableArrayList(toegangscodes);
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
            throw new NotFoundException("De oefening werd niet gevonden");
        }
        breakOutBoxRepo.delete(box);
//        oefeningenLijst.remove(oefening);
    }

    @Override
    public void update(Observable o, Object arg) {
        boxLijst.clear();
        boxLijst.addAll(breakOutBoxRepo.findAll());
    }

    public ObservableList<Actie> getActies() {
        return FXCollections.observableArrayList(actieRepo.findAll());
    }

    public ObservableList<Toegangscode> getToegangscodes() {
        return FXCollections.observableArrayList(toegangscodeRepo.findAll());
    }

    public ObservableList<Oefening> getOefeningen() {
        return FXCollections.observableArrayList(oefeningRepo.findAll());
    }

    public void updateBreakOutBox(int id, String naam, String omschrijving, List<Oefening> geselecteerdeOefeningen, List<Actie> geselecteerdeActies, List<Toegangscode> geselecteerdeToegangscodes) {
        BreakOutBox box = breakOutBoxRepo.get(id);
        if (box == null) {
            throw new NotFoundException("De BreakOutBox werd niet gevonden");
        }
        box.setNaam(naam);
        box.setOmschrijving(omschrijving);
        box.setActies(geselecteerdeActies);
        box.setOefeningen(geselecteerdeOefeningen);
        box.setToegangscodes(geselecteerdeToegangscodes);
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
        new BoxController().createPdf(DEST, id);

    }

    private void createPdf(String dest, int id) throws IOException, DocumentException {
        BreakOutBox box = breakOutBoxRepo.get(id);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(box.getNaam() + " samenvatting", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD)));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Samenvatting gemaakt op: " + new Date()));
        addEmptyLine(preface, 3);
        document.add(preface);

        Paragraph info = new Paragraph();
        info.add(new Paragraph("Naam:" + box.getNaam()));
        addEmptyLine(info, 1);
        info.add(new Paragraph("Omschrijving: " + box.getOmschrijving()));
        addEmptyLine(info, 1);
        document.add(info);

        PdfPTable table = new PdfPTable(5);
        PdfPCell c1 = new PdfPCell(new Phrase("Toegangscodes"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Oefeningen"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Antwoord"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Feedback"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Acties"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        int size = Integer.max(box.getActies().size(), Integer.max(box.getOefeningen().size(), box.getToegangscodes().size()));
        for (int i = 0; i < size; i++) {
            if (box.getToegangscodes().size() - 1 >= i) {
                table.addCell(box.getToegangscodes().get(i).getCode());
            } else {
                table.addCell("");
            }
            if (box.getOefeningen().size() - 1 >= i) {
                table.addCell(box.getOefeningen().get(i).getOpgave());
                table.addCell(String.valueOf(box.getOefeningen().get(i).getAntwoord()));
                table.addCell(box.getOefeningen().get(i).getFeedback());
            } else {
                table.addCell("");
                table.addCell("");
                table.addCell("");
            }

            if (box.getActies().size()-1 >= i) {
                table.addCell(box.getActies().get(i).getOmschrijving());
            } else {
                table.addCell("");
            }
        }

        document.add(table);
        document.close();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
