# slag-invest

Architekturdokumentation

# 1. Einf�hrung und Ziele

|Use-Case|
|---|
|Der Anwender m�chte den akutellen Wert seiner Investments automatisiert berechnen und dokumentieren lassen.|

## 1.1 Aufgabenstellung
Die Anwendung soll Berechnungen im Bereich b�rsengehandeter Titel durchf�hren k�nnen.

## 1.2 Qualit�tsziele
1. Korrektheit - Die Ergebnisse sollen richtig sein.
1. Erweiterbarkeit
1. Portabilit�t - Die Anwendung soll m�glichst ohne Aufwand auf anderen Systemen lauff�hig gemacht werden.
1. Nachvollziehbarkeit

## 1.3 Stakeholder

|Rolle|Erwartungshaltung|
|---|---|
|User|technisch und fachlich mittelm��ig versiert, wenig Unterst�tzung notwendig, hoher Freiheitsgrad ben�tigt|

# 2. Randbedingungen

* Lauff�higkeit auf handels�blichen Computern ohne DB-Anbindung sicherstellen.

# 3. Kontextabgrenzung

*Fachliche Schnittstellen*

 * Alphavantage: Schnittstelle zum Bezug von B�rsendaten

## 3.1 Fachlicher Kontext

|Externe fachliche Schnittstelle|Bedeutung|
|---|---|
|Alphavantage|Grundlage aller Berechnungen sollen Daten von Alphavantage sein, von der automatisiert Kurse und Preise abgerufen werden k�nnen.|

## 3.2 Technischer Kontext

Erfordernis: Lauff�higkeit auf einem handels�blichen Computer.

# 4. L�sungsstrategie

*Business-Log*

Zur Verbesserung der Nachvollziehbarkeit soll ein Business-Log geschaffen werden, dass m�glichst einfach protokolliert, was auf Gesch�ftsebene in der Anwendung passiert.

<< weiteres noch offen >>

# 5. Bausteinsicht

## 5.1 Whitebox Gesamtsystem

***\<�bersichtsdiagramm\>***

<< noch offen >>

**Begr�ndung**

<< noch offen >>

:   *\<Erl�uternder Text\>*

**Enthaltene Bausteine**

*Fachdom�ne*

|Modul-Name|K�rzel|Abh�ngigkeiten|Beschreibung|
|---|---|---|---|
|invest-model|IM|-|Persistierbares Datenmodell|
|invest-persist|IP|IM|Abstrahierung der Speicherlogik, die Entitys aus **IM** speichern kann. Da die Anwendung DB-unabh�ngig sein soll, erfolgt die Persistierung auf andere Weise.
|invest-service|IS|IM|Abstrahierung der Module der Service-Schicht|

*Anwendungs-Services*

|Modul-Name|K�rzel|Abh�ngigkeiten|Beschreibung|
|---|---|---|---|
|invest-service-report|ISR|IM|Report-Logik und -Ausgabe|
|invest-service-import|ISI|IM,IP|Organsiert den Datenimport|
|invest-service-importcache|ISC|IM,IP|Organisiert den Zugriff auf den Cache der zuvor bereits heruntergeladenen Daten.|


**Wichtige Schnittstellen**

Die einzige wichtige externe Schnittstelle ist die zum Datenlieferant AlphaVantage.

-----

**<< OFFEN: Beschreibungen der Module von *Anwendungskern* und *Anwendungs-Services* anhand des blackbox-template>>**

-----

### Schnittstelle: Alphavantage

*Zweck*

Abruf von B�rsendaten

*Schnittstellen*

Die Herstellung der Verbindung zu Alphavantage soll �ber alphavantage4j erfolgen.

*Offene Punkte/Probleme/Risiken*

Der Alphavantage-Free-Account l�sst nur f�nf Abfragen pro Minute zu, das muss entsprechend organisert werden.

## 5.2 Ebene 2

<< offen >>


# 6. Laufzeitsicht

Use-Cases:
* Abruf von B�rsendaten.
* Laden von B�rsendaten aus der Historie.

*Use-Case: Abruf von B�rsendaten*

Ablauf:

app -> ISI (->IP) -> ISC (->IP) -> ISR

Beschreibung:

Die Anwendung spricht die externe Schnittstelle �ber das Modul ISI an. Ist die Schnittstelle erreichbar und werden Daten geliefert, so werden diese in Entities �berf�hrt und per IP gespeichert. Anschlie�end werden ggf. zuvor geladene Daten durch ISC aus dem cache geladen und ebenfalls per IP gespeichert; sofern diese nicht bereits in der DB vorhanden sind. So ist sichergestellt, dass neue Daten Vorang gg�. alten haben.

Anschlie�end wird das Modul ISR aufgerufen, dass die Ausertungen vornimmt und ausgibt (exportiert).

# 7. Verteilungssicht

## Infrastruktur Ebene 1

<< offen >>

***\<�bersichtsdiagramm\>***

*Begr�ndung*

Die Installation l�uft auf einem einzelnen Host, das Deployment erfolgt manuell. Der Aufruf entweder manuell oder per zeitgesteuertem Job des Betriebssystems. 

*Qualit�ts- und/oder Leistungsmerkmale*

Da die Performance zweitrangig ist, kann die Ausf�hrung problemlos 'low level' auf einem handels�blichen Computer erfolgen. 

*Zuordnung von Bausteinen zu Infrastruktur*

Alle Bausteine laufen monolithisch in einer Anwendung.

Infrastruktur Ebene 2
---------------------

<< noch offen >>

# 8. Querschnittliche Konzepte

**Naming von Modulen**

(Wegen 'Schale', siehe Glossar)

|Template|Definition|
|---|---|
|invest-*|Modul der Fachdom�ne|
|invest-service-*|Modul der Service-Schale|
|invest-data-*|Modul einer Daten-Schale|
|invest-interface-*|Modul einer Interface-Schale|
|invest-app-*|Modul einer Anwendungs-Schale|
|import/export|Modul der Adapter-Schale|

**Packages**

**Mustersprache**

|Muster|Definition|
|---|---|
|*Entity*|Klasse speicherbarer Gesch�ftsobjekte (muss als Ausnahme nicht mit dem Schl�sselwort enden)|
|Dto|Data Transfer Object, beinhaltet die Daten eines Gesch�ftsobjekts (vollst�ndig oder teilweise)|
|Service|zustandsloses, injizierbares Logik-Klasse|
|Utils|Logik-Klasse mit ausschlie�lich statischem Zugriff|
|Factory|Objekt zur Erzeugung von Objekt-B�umen (mehr als ein neu erzeugtes Objekt)|
|Builder|Objekt zur Erzeugung eines parametrisierbaren Objekts|
|Support|Klasse technischer Objekte, die von der Erzeugung, Verwendung oder Lebenszyklus in kein anderes Schema hineinpasst|
|Repostiory|Abstraktion eines Beans der Persistenz-Schicht|

**Anti-Patterns**

|Pattern|Definition und Begr�ndung|Alternative|
|---|---|---|
|*Konstruktor*|...mit mehr als zwei Parametern; un�bersichtlich, schlechte Kontrolle des Erzeugungs-Vorgangs|Builder|


<< weiteres noch offen >>

# 9. Entwurfsentscheidungen

## Programmiersprache

**Entscheidung:** Java

**Begr�ndung:** Weit verbreitet, viele freie Frameworks, Kenntnisse der Entwickler sind am weitesten fortgeschritten.

## Architektur

**Entscheidung:** Onion Architektur

**Begr�ndung:** Um die Anwendung m�glichst erweiterbar zu halten wurde eine starke Modularisierung und strenge Schichtung gew�hlt.

## Version Programmiersprache

**Entscheidung:** Java 11

**Begr�ndung:** Aktuelle LTS-Version

## JDK

**Entscheidung:** Adopt-OpenJDK

**Begr�ndung:** Einige gro�e, namhafte Anbieter von Software-Dienstleistungen haben sich hier zusammengeschlossen. Oracle kann wegen seiner intransparenten Verhaltens- und Informationspolitik nicht mehr als zuverl�ssig betrachtet werden.

## Weiteres

### Zugriff auf AlphaVantage

**Entscheidung:** Zugriff soll �ber alphavantage4j erfolgen.

**Begr�ndung:** In alphavantage4j sind ist die Vorkonfiguration und der eigentliche Aufruf bereits implementiert und steht unter einer nicht-toxischen Lizenz.

# 10. Qualit�tsanforderungen

Qualit�tsbaum 

<< noch offen >>

**Qualit�tsszenarien**

*Korrektheit:*

Bekommt der Anwender ein Ergebnis berechnet, dann soll es h�chstens eine Abweichung von 1,25% gegen�ber professionell bzw. offiziell erstellten Zahlen geben.

*Erweiterbarkeit:*

Kommen neue Funktionsanforderungen hinzu, so sollen diese mit einem Aufwand von max. 10 PT prototypisch verf�gbar sein, ohne den Rest der Anwendung zu beeintr�chtigen.

*Portablilit�t:*

Die Anwendung soll als ein einzelnes Artefakt auf einem anderen Computer lauff�hig sein; Bedingung: eine aktuelle Java-Laufzeitumgebung ist verf�gbar.

*Nachvollziehbarkeit*

Werden �nderungen am fachlichen Datenbestand gemacht, so soll erkennbar sein, was gemacht wurde.

<< noch offen >>

# 11. Risiken und technische Schulden

## Risiken

**Entwickler/Tester**

Das Risiko ist die geringe Entwicklerkapazit�t von durchschnittlich < 1h pro Tag. Die Entwickler f�hren auch s�mtliche fachliche Tests durch.

*Mitigierung:* keine

**alphavantage4j**

Das Framework ist offensichtlich in einem privaten Projekt entwickelt worden und wird nicht regelm��ig gepflegt. Es ist in keinem g�ngigen Repository verf�gbar, einer konsistene Versionierung und Build findet nicht statt. Die aktuell einzige Bezugsm�glichkeit sind ist der Quellcode auf Github.

*Mitigierung:* Aus dem aktuellen Snapshot des Frameworks wird ein eigenes Modul bef�llt und Release und Wartung selbst �bernommen.

## Technische Schulden

Es handelt sich um ein neues System, daher gibt es keine technischen Schulden.

# 12. Glossar

|Begriff|Definition|
|---|---|
|StockValue|Handelbarer B�rsentitel; beispielseweise Aktie, Derivat, ETF, etc.|
|Schale|"Schicht" der Anwendung, da keine Schichten- sondern Onion-Architektur, hier "Schale" genannt|

Ende der Architekturdokumentation

-----

**�ber arc42**

arc42, das Template zur Dokumentation von Software- und
Systemarchitekturen.

Erstellt von Dr. Gernot Starke, Dr. Peter Hruschka und Mitwirkenden.

Template Revision: 7.0 DE (asciidoc-based), January 2017

� We acknowledge that this document uses material from the arc42
architecture template, <http://www.arc42.de>. Created by Dr. Peter
Hruschka & Dr. Gernot Starke.
