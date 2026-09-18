# Lobotomie

![Bannière](https://i.imgur.com/vOS3CJ9.png)

**LOcal BOard TO Master Informatics Engineering**  
*Tableau local pour maîtriser le génie informatique*

Logiciel de révision **local** : cours, exercices, conversions et outils (calcul / compilation).  
Cibles pédagogiques : **C**, **Java**, **assembleur** (x86-64, MIPS, ARM), notation algorithmique.

L’application est écrite **principalement en Java et en C**. Conception détaillée : `[Conception.md](Conception.md)`.

---

## Objectif / Aim


| FR                                                                                                                                  | EN                                                                                                    |
| ----------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| Réviser algorithmes, calculs et langages via des fiches et des exercices.                                                           | Review algorithms, math, and languages through notes and drills.                                      |
| Outils libres : conversion de bases, bitwise, compilation C → asm via **GCC** (`-S -O0`).                                           | Free tools: base conversion, bitwise ops, C → asm via **GCC** (`-S -O0`).                             |
| Traduction C → asm : **correction scolaire** à la main ; GCC en comparaison optionnelle (honnêteté intellectuelle, pas pour noter). | C → asm drills use a **hand-written school solution**; optional GCC compare for honesty, not grading. |


---



## Stack (aperçu)

- **Java 21+ / JavaFX / AtlantaFX** — interface
- **C + CMake** (`liblobotomie`) — conversions & bitwise, appelés via **FFM**
- **GCC** (outil système) — asm réel, mode libre / comparaison
- **Markdown + JSON** — cours et banques d’exercices

---



## Dépôt

```
app/        Application Java
native/     Bibliothèque C (liblobotomie)
scripts/    Build / packaging
Conception.md
```

Le suivi du projet se fait par **commits Git** (pas de numérotation de versions produit).

---



## Contact


| Email                                                             | Website                            | GitHub                                                 | Ko-Fi                                            |
| ----------------------------------------------------------------- | ---------------------------------- | ------------------------------------------------------ | ------------------------------------------------ |
| [marvin.fmandras@outlook.com](mailto:marvin.fmandras@outlook.com) | [marvinfm.fr](https://marvinfm.fr) | [github.com/MarvinFm54](https://github.com/MarvinFm54) | [Ko-fi.com/marvinfm](https://Ko-fi.com/marvinfm) |


