[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/y_e6Wbp5)
# Zadání projektu 
How to build:
1. Sestavte závislosti libqalculate: [(návod)](https://developer.android.com/ndk/guides/other_build_systems#autoconf)
    - GMP: https://gmplib.org/download/gmp/gmp-6.3.0.tar.xz (SHA256: `a3c2b80201b89e68616f4ad30bc66aee4927c3ce50e33929ca819d5c43538898`)
    - MPFR: https://www.mpfr.org/mpfr-current/mpfr-4.2.1.tar.xz (SHA256: `277807353a6726978996945af13e52829e3abd7a9a5b7fb2793894e18f1fcbb2`)
    - Gettext: https://ftp.gnu.org/pub/gnu/gettext/gettext-0.23.tar.gz (SHA256: `945dd7002a02dd7108ad0510602e13416b41d327898cf8522201bc6af10907a6`)
    - libxml2: https://download.gnome.org/sources/libxml2/2.13/libxml2-2.13.5.tar.xz (SHA256: `74fc163217a3964257d3be39af943e08861263c4231f9ef5b496b6f6d4c7b2b6`)
    - libicu: https://github.com/unicode-org/icu/releases/download/release-76-1/icu4c-76_1-src.tgz (SHA256: `dfacb46bfe4747410472ce3e1144bf28a102feeaa4e3875bac9b4c6cf30f4f3e`)
1. Sestavte libqalculate:
   - stažení: https://github.com/Qalculate/libqalculate/releases/download/v5.4.0/libqalculate-5.4.0.tar.gz (SHA256: `1fe956877ff1bbb1f4b470c41cdf3d971cebbeda6a35e92282f0eea5193ac343`)
   - pro sestavení nutný patch: https://github.com/termux/termux-packages/blob/758d007218e3753243cb829171739fe2e8fdad7f/packages/qalc/libqalculate-util.cc.patch


Vytvořte Android aplikaci v jazyce Kotlin na libovolné téma dle vašeho výběru. Aplikace by měla mít uživatelsky přívětivé konvenční UI a měla by být schopna plynule běžet na typickém Android zařízení nebo emulátoru.

Kritéria hodnocení:

Známka 5: Neodevzdáno

-   Projekt není odevzdán v daném termínu nebo nelze spustit žádný funkční celek.

Známka 4: Nefunkční, nedodělané řešení

-   Aplikaci se nepodaří spustit, nebo se ihned po spuštění zhroutí.
-   Základní funkcionality nejsou implementovány do té míry, aby aplikace měla praktický smysl.

Známka 3: Základní úroveň aplikace (např. jednoduchá TODO s filtrací):

-   Aplikace je funkční, jde spustit a má praktický smysl.
-   Data jsou persistentně ukládána alespoň v jedné tabulce v lokální databázi (např. Room) a po ukončení a opětovném spuštění aplikace jsou dostupná.
-   Uživatel může provádět základní CRUD operace (např. přidat záznam, zobrazit seznam, případně smazat položku).
-   Aplikace musí kromě základní správy dat (CRUD) implementovat alespoň jednu jednoduchou logiku nebo operaci s daty (např. seřazení úkolů podle priority, filtraci podle stavu, výpočet procentuálního progresu dokončených úkolů atd.).
-   Uživatelské rozhraní je základní, ale přehledné a odpovídá konvenčním standardům Android aplikací.

Známka 2: Více tabulek, více obrazovek:

-   Aplikace využívá více propojených tabulek (např. vztah 1:N mezi dvěma entitami).
-   Rozhraní umožňuje přepínání mezi více obrazovkami (např. seznam záznamů, detailní pohled na konkrétní záznam, správa kategorií).
-   Data jsou navzájem provázána a uživatel může s těmito vztahy pracovat (např. filtrovat data dle kategorií).
-   Uživatelské prostředí je přehledné, funkčně rozdělené a splňuje základní pravidla pro Android UI (např. využití Fragmentů, Navigation Component atd.).

Známka 1: Vazby N:M, senzory a další

-   Aplikace obsahuje nějaký pokročilý prvek (např. N:M vazby v databázi, integrace senzorů telefonu jako je GPS, akcelerometr nebo jiné, případně práci s webovým API či další sofistikované techniky).
-   Uživatelské rozhraní je propracované a intuitivní, aplikace nabízí přidanou hodnotu a komplexnější funkcionalitu.

Při závěrečné prezentaci aplikace by měly být předvedeny všechny klíčové funkcionality a pro získání lepší známky dokázat odpovědět na odborné otázky týkající se implementace zvolených řešení.
