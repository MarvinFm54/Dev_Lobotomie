# Conception Lobotomie

![Bannière](https://i.imgur.com/vOS3CJ9.png)

**Lobotomie** — LOcal BOard TO Master Informatics Engineering  
Logiciel de révision local (algorithmes, calculs, langages) : cours, exercices, conversion / calculatrice.  
Langages cibles : **C**, **Java**, **Assembleur** (x86-64, MIPS, ARM), notation algorithmique.

Contrainte : l’application est codée **principalement en Java et en C** (objectif pédagogique du projet lui-même).

Le suivi du projet se fait par **commits Git** — pas de numérotation de versions produit (pas de « V1 », « MVP », etc.).

---

## 1. Stack technique


| Composant                  | Technologie         | Rôle / Description                                                      |
| -------------------------- | ------------------- | ----------------------------------------------------------------------- |
| **Interface graphique**    | JavaFX 21+          | Fenêtre, navigation par onglets, composants UI.                         |
| **Thème UI**               | AtlantaFX           | Design moderne sans styling JavaFX manuel lourd.                        |
| **Cœur application**       | Java 21+            | État global, onglets, chargement des fiches / exercices, orchestration. |
| **Moteur natif**           | C (bibliothèque)    | Conversions de bases, opérations bit à bit.                             |
| **Liaison inter-langages** | FFM API (Panama)    | Java appelle les fonctions de `liblobotomie`.                           |
| **Compilation réelle**     | GCC (externe)       | Sortie asm **sans optimisation**, mode libre + comparaison honnête.     |
| **Format des cours**       | Markdown + Flexmark | Fiches avec mise en forme et blocs de code.                             |
| **Format des données**     | JSON (Jackson)      | Métadonnées, banques d’exercices, progressions locales.                 |
| **Build natif**            | CMake               | Compilation de `liblobotomie` (`.so` / `.dll` / `.dylib`).              |
| **Build Java**             | Maven ou Gradle     | Application, tests, packaging.                                          |


Choix pédagogique : **conversions et bit-ops passent par le C** via FFM (interop Java↔C volontaire, pas le chemin le plus court).

GCC n’est **pas** embarqué dans `liblobotomie` : c’est un outil système invoqué depuis Java (processus), uniquement là où une compilation réelle est utile.

---



## 2. Architecture globale

Une fenêtre unique, plusieurs onglets. Java orchestre ; le C calcule ; GCC produit de l’asm « machine réelle » quand on le demande.


| Couche Java                          | Couche native C (via FFM)       | Outil externe    |
| ------------------------------------ | ------------------------------- | ---------------- |
| UI, navigation, état                 | Convertisseur bases 2 / 10 / 16 | —                |
| Affichage Markdown des cours         | Opérations bitwise              | —                |
| Chargement JSON des exercices        | Helpers de validation numérique | —                |
| Correction scolaire (contenu)        | —                               | —                |
| Mode compilation libre / comparaison | —                               | GCC (`-S -O0` …) |




### Onglets


| #   | Onglet                        | Contenu principal                                             |
| --- | ----------------------------- | ------------------------------------------------------------- |
| 1   | Cours / fiches                | Markdown par thème (C, Java, MIPS, x86, ARM, algo)            |
| 2   | Algo / Code                   | Exercices d’algorithmique et de code (réponse texte)          |
| 3   | Exercices traduction langages | C → asm scolaire (+ comparaison GCC optionnelle)              |
| 4   | Exercices conversion          | Bases 2 / 10 / 16                                             |
| 5   | Exercices calculs mentaux     | Ops arithmétiques / bit                                       |
| 6   | Conversion / calcul libre     | Outils libres : bases, bitwise, **compilation C → asm (GCC)** |




### Flux logique

```
Java (app)
 ├── ui/
 ├── courses/
 ├── exercises/
 ├── validation/
 ├── native/          → FFM → liblobotomie (conversion, bitwise)
 └── gcc/             → ProcessBuilder → gcc -S -O0 …

liblobotomie (C)
 ├── conversion
 └── bitwise
```

---



## 3. Traduction C → assembleur (scolaire)

Pas de compilateur maison. Deux sources d’asm, volontairement distinctes :

1. **Correction scolaire** — écrite à la main, stockée avec l’exercice (vues en cours).
2. **Compilation GCC** — asm réel, sans optimisation, pour le mode libre et pour comparer « ce que le cours dit » vs « ce que produit un vrai compilateur ».

Un compilateur réel privilégie souvent la **mémoire** (pile) là où un énoncé scolaire utilise beaucoup de **registres**. Lobotomie assume cet écart : la correction scolaire reste la référence pédagogique ; GCC sert l’**honnêteté intellectuelle**, pas la note automatique.

### Exercices de traduction

Chaque exercice enregistre au minimum :

- le **code C** source ;
- la **correction scolaire** (asm cible : MIPS, x86-64 ou ARM, selon l’exercice) ;
- métadonnées utiles (énoncé, archi, indices éventuels).

La validation de l’élève se fait par rapport à la **correction scolaire** (texte normalisé, QCM, à trous, etc.).

La comparaison avec GCC est une **option d’affichage** : même source C → `gcc -S` (paramètres sans optimisation) → asm réel à côté de la correction. Ce n’est pas le critère de réussite de l’exercice.

### Mode compilation libre

Dans l’onglet outils : l’utilisateur saisit (ou charge) du C, Lobotomie appelle GCC et affiche l’asm produit. Paramètres typiques : `-S -O0` (et options d’archi / dialecte selon le besoin), **sans** optimisation agressive, pour rester lisible.

Prérequis : GCC installé sur la machine. Si absent, message clair — pas de repli compilateur maison.

---



## 4. Validation des exercices


| Type d’exercice         | Stratégie                                                        |
| ----------------------- | ---------------------------------------------------------------- |
| Conversion / bases      | Appel natif (`liblobotomie`) + comparaison                       |
| Calcul mental / bitwise | Idem                                                             |
| QCM / à trous           | Identifiants / chaînes normalisées (Java)                        |
| Traduction C → asm      | Référence = **correction scolaire** stockée                      |
| Comparaison GCC         | Affichage côte à côte ; pas de scoring automatique sur l’asm GCC |
| Algo / code (texte)     | Réponse modèle normalisée (pas d’exécution de code arbitraire)   |


Pas de juge en ligne qui exécute du code utilisateur arbitraire.  
Progression locale : `~/.lobotomie/progress.json` (scores, séries, dates). Pas de backend requis.

---



## 5. Architecture des dossiers

```
Dev_Lobotomie/
├── README.md
├── Conception.md
├── .gitignore
│
├── app/
│   ├── pom.xml                   # ou build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── java/fr/marvinfm/lobotomie/
│       │   │   ├── LobotomieApp.java
│       │   │   ├── ui/
│       │   │   │   ├── MainWindow.java
│       │   │   │   ├── theme/
│       │   │   │   └── tabs/
│       │   │   │       ├── CoursesTab.java
│       │   │   │       ├── AlgoTab.java
│       │   │   │       ├── TranslationTab.java
│       │   │   │       ├── ConversionDrillTab.java
│       │   │   │       ├── MentalMathTab.java
│       │   │   │       └── FreeToolsTab.java
│       │   │   ├── courses/
│       │   │   │   ├── CourseLoader.java
│       │   │   │   └── MarkdownRenderer.java
│       │   │   ├── exercises/
│       │   │   │   ├── ExerciseBank.java
│       │   │   │   └── ExerciseModels.java
│       │   │   ├── validation/
│       │   │   │   ├── Validator.java
│       │   │   │   └── Normalizers.java
│       │   │   ├── progress/
│       │   │   │   └── ProgressStore.java
│       │   │   ├── gcc/
│       │   │   │   └── GccRunner.java       # invoque gcc -S -O0, récupère stdout/fichier
│       │   │   └── nativebridge/
│       │   │       ├── LobotomieLibrary.java
│       │   │       ├── ConversionBridge.java
│       │   │       └── BitwiseBridge.java
│       │   └── resources/
│       │       ├── courses/
│       │       │   ├── c/
│       │       │   ├── java/
│       │       │   ├── mips/
│       │       │   ├── x86/
│       │       │   ├── arm/
│       │       │   └── algo/
│       │       └── exercises/
│       │           ├── conversion/         # bases 2/10/16
│       │           ├── algorithms/
│       │           ├── assembly/           # C + correction scolaire (+ meta)
│       │           └── mental/
│       └── test/
│           └── java/fr/marvinfm/lobotomie/
│
├── native/
│   ├── CMakeLists.txt
│   ├── include/lobotomie/
│   │   ├── conversion.h
│   │   └── bitwise.h
│   ├── src/
│   │   ├── conversion.c
│   │   └── bitwise.c
│   └── tests/
│       ├── test_conversion.c
│       └── test_bitwise.c
│
└── scripts/
    ├── build-native.sh
    └── package.sh
```



### Runtime / build (hors suivi utile)

```
~/.lobotomie/progress.json

app/target/
native/build/
app/src/main/resources/native/    # copie générée de liblobotomie.*
```

---



## 6. Contrats (aperçu)

**liblobotomie** (headers `native/include/lobotomie/`) :

- `lob_parse_base` / `lob_format_base` — bases 2 / 10 / 16
- opérations bitwise sur `uint64_t` avec codes d’erreur

**GCC** (via `GccRunner`) :

- entrée : source C (fichier temporaire ou stdin selon le flux retenu)
- flags de base : `-S -O0` (+ cible d’architecture si besoin)
- sortie : asm texte à afficher
- en cas d’échec / GCC absent : message d’erreur exploitable dans l’UI

Java ne duplique pas la logique numérique déjà en C ; GCC reste un appel externe, pas un binding FFM.

---



## 7. Principes

1. **Java** = UI et orchestration ; **C** = conversions / bitwise ; **GCC** = asm réel quand on le demande.
2. Contenu (cours, exercices) en **Markdown / JSON**, éditable sans recompiler l’app.
3. Exercices de traduction : **C + correction scolaire** à la main ; GCC = comparaison optionnelle.
4. Pas de compilateur maison, pas de scoring sur l’asm GCC.
5. Build reproductible : script natif + lancement de l’app Java.
6. Tests : JUnit (bridges, validation, `GccRunner`) + tests C (conversion, bitwise).

---

