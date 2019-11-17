# slag-invest

Architekturdokumentation

# 1. Einführung und Ziele

|Use-Case|
|---|
|Der Anwender möchte den akutellen Wert seiner Investments automatisiert berechnen und dokumentieren lassen.|

## 1.1 Aufgabenstellung
Die Anwendung soll Berechnungen im Bereich börsengehandeter Titel durchführen können.

## 1.2 Qualitätsziele
1. Korrektheit - Die Ergebnisse sollen richtig sein.
1. Erweiterbarkeit
1. Portabilität - Die Anwendung soll möglichst ohne Aufwand auf anderen Systemen lauffähig gemacht werden.
1. Nachvollziehbarkeit

## 1.3 Stakeholder

|Rolle|Erwartungshaltung|
|---|---|
|User|technisch und fachlich mittelmäßig versiert, wenig Unterstützung notwendig, hoher Freiheitsgrad benötigt|

# 2. Randbedingungen

* Lauffähigkeit auf handelsüblichen Computern ohne DB-Anbindung sicherstellen.

# 3. Kontextabgrenzung

*Fachliche Schnittstellen*

 * Alphavantage: Schnittstelle zum Bezug von Börsendaten

## 3.1 Fachlicher Kontext

|Externe fachliche Schnittstelle|Bedeutung|
|---|---|
|Alphavantage|Grundlage aller Berechnungen sollen Daten von Alphavantage sein, von der automatisiert Kurse und Preise abgerufen werden können.|

## 3.2 Technischer Kontext

Erfordernis: Lauffähigkeit auf einem handelsüblichen Computer.

# 4. Lösungsstrategie

*Business-Log*

Zur Verbesserung der Nachvollziehbarkeit soll ein Business-Log geschaffen werden, dass möglichst einfach protokolliert, was auf Geschäftsebene in der Anwendung passiert.

<< weiteres noch offen >>

# 5. Bausteinsicht

## 5.1 Whitebox Gesamtsystem

***\<Übersichtsdiagramm\>***

<< noch offen >>

**Begründung**

<< noch offen >>

:   *\<Erläuternder Text\>*

**Enthaltene Bausteine**

*Fachdomäne*

|Modul-Name|Kürzel|Abhängigkeiten|Beschreibung|
|---|---|---|---|
|invest-model|IM|-|Persistierbares Datenmodell|
|invest-persist|IP|IM|Abstrahierung der Speicherlogik, die Entitys aus **IM** speichern kann. Da die Anwendung DB-unabhängig sein soll, erfolgt die Persistierung auf andere Weise.
|invest-service|IS|IM|Abstrahierung der Module der Service-Schicht|

*Anwendungs-Services*

|Modul-Name|Kürzel|Abhängigkeiten|Beschreibung|
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

Abruf von Börsendaten

*Schnittstellen*

Die Herstellung der Verbindung zu Alphavantage soll über alphavantage4j erfolgen.

*Offene Punkte/Probleme/Risiken*

Der Alphavantage-Free-Account lässt nur fünf Abfragen pro Minute zu, das muss entsprechend organisert werden.

## 5.2 Ebene 2

<< offen >>


# 6. Laufzeitsicht

Use-Cases:
* Abruf von Börsendaten.
* Laden von Börsendaten aus der Historie.

*Use-Case: Abruf von Börsendaten*

Ablauf:

app -> ISI (->IP) -> ISC (->IP) -> ISR

Beschreibung:

Die Anwendung spricht die externe Schnittstelle über das Modul ISI an. Ist die Schnittstelle erreichbar und werden Daten geliefert, so werden diese in Entities überführt und per IP gespeichert. Anschließend werden ggf. zuvor geladene Daten durch ISC aus dem cache geladen und ebenfalls per IP gespeichert; sofern diese nicht bereits in der DB vorhanden sind. So ist sichergestellt, dass neue Daten Vorang ggü. alten haben.

Anschließend wird das Modul ISR aufgerufen, dass die Ausertungen vornimmt und ausgibt (exportiert).

# 7. Verteilungssicht

## Infrastruktur Ebene 1

<< offen >>

***\<Übersichtsdiagramm\>***

*Begründung*

Die Installation läuft auf einem einzelnen Host, das Deployment erfolgt manuell. Der Aufruf entweder manuell oder per zeitgesteuertem Job des Betriebssystems. 

*Qualitäts- und/oder Leistungsmerkmale*

Da die Performance zweitrangig ist, kann die Ausführung problemlos 'low level' auf einem handelsüblichen Computer erfolgen. 

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
|invest-*|Modul der Fachdomäne|
|invest-service-*|Modul der Service-Schale|
|invest-data-*|Modul einer Daten-Schale|
|invest-interface-*|Modul einer Interface-Schale|
|invest-app-*|Modul einer Anwendungs-Schale|
|import/export|Modul der Adapter-Schale|

**Packages**

**Mustersprache**

|Muster|Definition|
|---|---|
|*Entity*|Klasse speicherbarer Geschäftsobjekte (muss als Ausnahme nicht mit dem Schlüsselwort enden)|
|Dto|Data Transfer Object, beinhaltet die Daten eines Geschäftsobjekts (vollständig oder teilweise)|
|Service|zustandsloses, injizierbares Logik-Klasse|
|Utils|Logik-Klasse mit ausschließlich statischem Zugriff|
|Factory|Objekt zur Erzeugung von Objekt-Bäumen (mehr als ein neu erzeugtes Objekt)|
|Builder|Objekt zur Erzeugung eines parametrisierbaren Objekts|
|Support|Klasse technischer Objekte, die von der Erzeugung, Verwendung oder Lebenszyklus in kein anderes Schema hineinpasst|
|Repostiory|Abstraktion eines Beans der Persistenz-Schicht|

**Anti-Patterns**

|Pattern|Definition und Begründung|Alternative|
|---|---|---|
|*Konstruktor*|...mit mehr als zwei Parametern; unübersichtlich, schlechte Kontrolle des Erzeugungs-Vorgangs|Builder|


<< weiteres noch offen >>

# 9. Entwurfsentscheidungen

## Programmiersprache

**Entscheidung:** Java

**Begründung:** Weit verbreitet, viele freie Frameworks, Kenntnisse der Entwickler sind am weitesten fortgeschritten.

## Architektur

**Entscheidung:** Onion Architektur

**Begründung:** Um die Anwendung möglichst erweiterbar zu halten wurde eine starke Modularisierung und strenge Schichtung gewählt.

## Version Programmiersprache

**Entscheidung:** Java 11

**Begründung:** Aktuelle LTS-Version

## JDK

**Entscheidung:** Adopt-OpenJDK

**Begründung:** Einige große, namhafte Anbieter von Software-Dienstleistungen haben sich hier zusammengeschlossen. Oracle kann wegen seiner intransparenten Verhaltens- und Informationspolitik nicht mehr als zuverlässig betrachtet werden.

## Weiteres

### Zugriff auf AlphaVantage

**Entscheidung:** Zugriff soll über alphavantage4j erfolgen.

**Begründung:** In alphavantage4j sind ist die Vorkonfiguration und der eigentliche Aufruf bereits implementiert und steht unter einer nicht-toxischen Lizenz.

# 10. Qualitätsanforderungen

Qualitätsbaum 

<< noch offen >>

**Qualitätsszenarien**

*Korrektheit:*

Bekommt der Anwender ein Ergebnis berechnet, dann soll es höchstens eine Abweichung von 1,25% gegenüber professionell bzw. offiziell erstellten Zahlen geben.

*Erweiterbarkeit:*

Kommen neue Funktionsanforderungen hinzu, so sollen diese mit einem Aufwand von max. 10 PT prototypisch verfügbar sein, ohne den Rest der Anwendung zu beeinträchtigen.

*Portablilität:*

Die Anwendung soll als ein einzelnes Artefakt auf einem anderen Computer lauffähig sein; Bedingung: eine aktuelle Java-Laufzeitumgebung ist verfügbar.

*Nachvollziehbarkeit*

Werden Änderungen am fachlichen Datenbestand gemacht, so soll erkennbar sein, was gemacht wurde.

<< noch offen >>

# 11. Risiken und technische Schulden

## Risiken

**Entwickler/Tester**

Das Risiko ist die geringe Entwicklerkapazität von durchschnittlich < 1h pro Tag. Die Entwickler führen auch sämtliche fachliche Tests durch.

*Mitigierung:* keine

**alphavantage4j**

Das Framework ist offensichtlich in einem privaten Projekt entwickelt worden und wird nicht regelmäßig gepflegt. Es ist in keinem gängigen Repository verfügbar, einer konsistene Versionierung und Build findet nicht statt. Die aktuell einzige Bezugsmöglichkeit sind ist der Quellcode auf Github.

*Mitigierung:* Aus dem aktuellen Snapshot des Frameworks wird ein eigenes Modul befüllt und Release und Wartung selbst übernommen.

## Technische Schulden

Es handelt sich um ein neues System, daher gibt es keine technischen Schulden.

# 12. Glossar

|Begriff|Definition|
|---|---|
|StockValue|Handelbarer Börsentitel; beispielseweise Aktie, Derivat, ETF, etc.|
|Schale|"Schicht" der Anwendung, da keine Schichten- sondern Onion-Architektur, hier "Schale" genannt|

Ende der Architekturdokumentation

-----

**Über arc42**

arc42, das Template zur Dokumentation von Software- und
Systemarchitekturen.

Erstellt von Dr. Gernot Starke, Dr. Peter Hruschka und Mitwirkenden.

Template Revision: 7.0 DE (asciidoc-based), January 2017

© We acknowledge that this document uses material from the arc42
architecture template, <http://www.arc42.de>. Created by Dr. Peter
Hruschka & Dr. Gernot Starke.
