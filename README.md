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

## Presentatie klant - Sprint 2 Opmerkingen
* Herbekijken Alert boxes
* Default alert actie op annuleer
* Acties bij Box autocomplete
* Foutmeldingen niet uitroepingsteken 
