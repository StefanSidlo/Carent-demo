###### Carent-demo
Java Swing application for car renting.

####Popis semestrálneho projektu

##Stručný popis a užívateľské role
V rámci svojho semestrálneho projektu som sa rozhodol vytvoriť jednoduchú aplikáciu Carent, ktorá simuluje systém požičovne áut.
V tejto aplikácií budú vystupovať dve užívateľské role, a to majiteľ požičovne áut (admin) a obyčajný zákazník (user). 
Hlavnou úlohou aplikácie je umožniť zákazníkovi rezervovať si nejaké z aktuálne dostupných áut a vytvoriť tak pre majiteľa prehľad o všetkých objednávkach, o dostupnosti ponúkaných vozidiel alebo o iných systémových atribútov. 
Zákazník môže vytvoriť objednávku na rôzne auto, ktoré je dostupné. Dostupné autá vie prehľadávať v zozname dostupných áut. Auto bude po vytvorení objednávky dovezené zákazníkovi na miesto, ktoré špecifikuje pri objednávke. 
Po vykonaní objednávky bude auto na špecifikované miesto dovezené v priebehu jednej hodiny. 
V priebehu tejto hodiny má zákazník ešte šancu zrušiť svoju objednávku, ale bude ho to stáť pokutu za zrušenie objednávky. 
Po dovezení auta na špecifikované miesto bude zákazník kontaktovaný pomocou telefónneho čísla alebo emailu, ktoré má uvedené v jeho profile. 
Auto sa po zadaní objednávky stane nedostupné pre iných zákazníkov. Všetky svoje prebiehajúce objednávky si môže zákazník prezerať v zozname aktuálnych objednávok. 
Keď chce zákazník auto vrátiť, zadá najprv adresu, kde sa práve nachádza, a teda, kde bude auto vrátené.
Po vrátení sa vypočíta výsledná cena, ktorá sa skladá z ceny za zapožičanie vrátane ceny za jednu hodinu zapožičania. 
Táto suma sa odráta z profilu zákazníka, kam si vie cez bankový prevod pridať peniaze, ktoré slúžia na zaplatenie objednávok. 
Auto je po zaplatení znovu dostupné a pripravené na ďalšiu objednávku. 
Okrem vytvárania objednávok a zapožičania áut si môže zákazník ako už bolo spomenuté pridávať peniaze do systému ale taktiež aj meniť informácie uložené v jeho profile.
Zmena hesla je tiež možná. Hlavnou funkcionalitou pre admina je hlavne správa áut, ale aj získavanie všeobecných informácií o fungovaní systému (autá, užívatelia, objednávky). 
Ako bolo spomenuté, aplikácia rozlišuje dve užívateľské role a teda aj funkcionalita bude pre tieto role odlišná. Toto rozdelenie aplikácie bude zabezpečené prihlásením sa po spustení aplikácie pomocou username a hesla. 
Pre nových zákazníkov bude potrebné sa najprv zaregistrovať, aby si ich systém zapamätal a mohli sa následne cez prihlásenie k svojim objednávkam vracať.


####Základná funkcionalita
##Všeobecne
- prihlásenie/odhlásenie sa do/z aplikácie
- vytvorenie nového konta
- zmena hesla
- zobrazenie podrobných informácií o aute, objednávke a užívateľoch


##User - zákazník
- vytvorenie objednávky
- zrušenie objednávky
- prezeranie dostupných áut
- zobrazenie aktuálnych objednávok
- zobrazenie všetkých už ukončených resp. zaplatených objednávok
- editácia informácií uložených v profile
- pridávanie peňazí do systému


##Admin - správca
- pridávanie a odoberanie áut na požičanie
- prezeranie všetkých ponúkaných áut
- prezeranie všetkých užívateľov systému
- prezeranie všetkých objednávok
- Prístup k všeobecným informáciám a systéme a jeho atribútov
