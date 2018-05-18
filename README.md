# java-g16
projectenII

## Details wijziging opdracht

* filteren op naam of doelstelling of vak
* oefening wijzigen behalve als die in een sessie zit
* nieuwe oefening; naam, opgave(pdf), feedback(pdf)
* tijdslimiet is optioneel
* groepsbewerkingen kiezen per oefening
* box is verzameling van oefeningen
* bij overzicht bobs filteren op naam of doelstelling of vak
* Bij filteren is filter op naam het min
* detailsessie; naam, doelstelling, oef

* nieuwe box; oefeningen(hier kan je filteren voor een oef. te selecteren)
* nieuwe box; acties (aantal oef -1 = aantal acties) (UI: 2 selectlists)
* nieuwe box; afstandsonderwijs valt weg
* mogelijkheid om pdf te maken van bob
* een bob heeft geen toegangscode
* bob kan niet verwijderd worden als het in een actieve sessie zit
* kopie van een bob is nice to have

* Sessie beheren uc is niet meer van toepassing
* volg verhaaltje
* enkel sessie creeren of verwijderen
* Aanmaken sessie;
* unieke naam
* datum 
* sessiecode (uniek en gegenereerd)
* type onderwijs
* klas ingeven
* acties zijn zelfde volgorde voor alle groepen
* toegcode allemaal anders
* PDF: per groep volgorde welke oef. + antwoord na groepsbew

## Technical Review 1
* Oefening is pdf 
* Sessie heeft omschrijving
* Sessie lesuur mag weg
* Sessie is actief als datum van de dag na de startdatum ligt
* Toegangscodes van volgende oefening wordt gegenereerd bij het aanmaken van de sessie
* Per groep moeten we ook een 'Pad' laten genereren 
* Repository  package -- > Dao package

## Presentatie opmerkingen
* Productie-waardig ipv productwaardig
* Duidelijk de nadruk leggen op welk deel van de schermen klaar is (functionaliteit of uiterlijk)
* ==== TIPS ====
* Bij de presentatie aan de klant zet op je desktop de jar file van de applicatie en open het zodat het vertrouwelijk overkomt bij de klant

## Analyse - Eind document
Vul de use cases aan:
- [ ]	Alle functionaliteiten opgenomen = groen, niet opgenomen = rood
- [ ]	Bij elke use case: Alles dat niet werd opgenomen = rood, Alles dat werd aangepast/toegevoegd groen.

## Technical Review 2
* [X] Delen door 0 mag niet groepsbewerking 
* [ ] Groepsbewddking delete werkt nie 
* [ ] Elke lijst continu alfabetisch
* [X] Klas lln toevoegen mag geen cijfers/tekens 
* [X] Oefeningen, filter op absolutut pad
* [X] Oefeningen sorteren
* [X] Kopie zonder iets te selecteren
* [X] Bij aanmaken sessie lijst boxen van soortonderwijs
* [ ] pdf box is mss verwarrend je zou kunnen denken dat de acties altijd opvolgen
* [ ] Geselecteerde acties en oefening doen zoals in oefening
* [X] Box naam bestaat al checken
* [X] Verwijderen ja / nee is de eerste keer altijd buggy
* [X] Tests
