#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Genera el contenido semilla real de NiñoBiólogo: Exploradores de la Vida.

Produce app/src/main/java/com/educalab/ninobiologo/data/local/seed/SeedContent.kt a partir de
estructuras de datos en Python, para que el contenido (biología real, en español, adaptada a
niños de 8-12 años) sea fácil de revisar y mantener sin miles de líneas hardcodeadas dentro de
un ViewModel (sección 11 de la especificación maestra).

Cantidades objetivo (prompt específico V4, sección CONTENIDO INICIAL):
  - 40 expediciones
  - 50 organismos coleccionables
  - 20 ecosistemas
  - 30 desafíos interactivos
  - 15 recompensas visuales (insignias)

Uso: python3 tools/generate_seed_data.py
"""
import textwrap

OUT_PATH = "app/src/main/java/com/educalab/ninobiologo/data/local/seed/SeedContent.kt"

# ---------------------------------------------------------------------------
# 1. BIOMAS (5 zonas del Mapa de Expediciones Biológicas)
# ---------------------------------------------------------------------------
BIOMES = [
    dict(id="micromundo", order=1, name="Micromundo", tagline="Un universo invisible te espera",
         desc="Usa el microscopio virtual para descubrir células y microorganismos.",
         icon="zone_micromundo", primary="#5B8C5A", secondary="#DCEFDC"),
    dict(id="bosque_de_vida", order=2, name="Bosque de Vida", tagline="Plantas y animales en equilibrio",
         desc="Explora un bosque lleno de especies para observar y clasificar.",
         icon="zone_bosque", primary="#2E7D32", secondary="#E3F2E1"),
    dict(id="oceano_profundo", order=3, name="Océano Profundo", tagline="Adaptaciones bajo el agua",
         desc="Sumérgete para conocer criaturas marinas y sus asombrosas adaptaciones.",
         icon="zone_oceano", primary="#0277BD", secondary="#E1F5FE"),
    dict(id="cuerpo_humano", order=4, name="Cuerpo Humano", tagline="Un laboratorio viviente",
         desc="Construye modelos del cuerpo y descubre cómo funcionan sus sistemas.",
         icon="zone_cuerpo", primary="#C62828", secondary="#FDE8E8"),
    dict(id="ecosistemas", order=5, name="Ecosistemas", tagline="Todo está conectado",
         desc="Construye cadenas alimentarias y equilibra ecosistemas completos.",
         icon="zone_ecosistemas", primary="#EF6C00", secondary="#FFF3E0"),
]

# ---------------------------------------------------------------------------
# 2. ORGANISMOS (50 en total)
# category: PLANTA/ANIMAL/MICROORGANISMO/HONGO ; trophicRole: PRODUCTOR/HERBIVORO/CARNIVORO/DESCOMPONEDOR
# rarity: COMUN/POCO_COMUN/RARO/LEGENDARIO
# ---------------------------------------------------------------------------
ORGANISMS = []

def org(id, biome, name, sci, cat, habitat, diet, role, chars, fact, rarity, icon):
    ORGANISMS.append(dict(id=id, biome=biome, name=name, sci=sci, cat=cat, habitat=habitat,
                           diet=diet, role=role, chars=chars, fact=fact, rarity=rarity, icon=icon))

# --- Micromundo (8) ---
org("org_paramecio", "micromundo", "Paramecio", "Paramecium caudatum", "MICROORGANISMO",
    "Agua dulce estancada", "Bacterias y algas microscópicas", "HERBIVORO",
    ["forma de zapatilla", "se mueve con cilios", "unicelular"],
    "Puede expulsar el agua que le sobra con una estructura llamada vacuola contráctil.",
    "COMUN", "org_paramecio")
org("org_ameba", "micromundo", "Ameba", "Amoeba proteus", "MICROORGANISMO",
    "Estanques y suelos húmedos", "Bacterias y otros microorganismos", "HERBIVORO",
    ["cambia de forma constantemente", "se mueve con seudópodos", "unicelular"],
    "No tiene una forma fija: la cambia para moverse y para atrapar su alimento.",
    "COMUN", "org_ameba")
org("org_euglena", "micromundo", "Euglena", "Euglena viridis", "MICROORGANISMO",
    "Agua dulce con luz", "Fotosíntesis y también absorbe nutrientes", "PRODUCTOR",
    ["tiene un flagelo", "un ojo sensible a la luz", "verde por clorofila"],
    "Es mitad planta, mitad animal: hace fotosíntesis pero también nada activamente.",
    "POCO_COMUN", "org_euglena")
org("org_levadura", "micromundo", "Levadura", "Saccharomyces cerevisiae", "HONGO",
    "Frutas maduras y masas de pan", "Azúcares", "DESCOMPONEDOR",
    ["unicelular", "se reproduce por gemación", "produce burbujas de gas"],
    "Es la responsable de que el pan suba y de que crezca la espuma en algunas bebidas.",
    "COMUN", "org_levadura")
org("org_tardigrado", "micromundo", "Tardígrado", "Milnesium tardigradum", "ANIMAL",
    "Musgos húmedos", "Líquidos de plantas y microorganismos", "HERBIVORO",
    ["ocho patas diminutas", "resiste el espacio exterior", "se llama 'oso de agua'"],
    "Puede sobrevivir congelado, deshidratado e incluso en el vacío del espacio.",
    "RARO", "org_tardigrado")
org("org_bacteria_intestinal", "micromundo", "Bacteria intestinal beneficiosa", "Lactobacillus sp.",
    "MICROORGANISMO", "Intestino de animales", "Restos de alimentos digeridos", "DESCOMPONEDOR",
    ["ayuda a digerir alimentos", "vive en grandes colonias", "es beneficiosa, no todas causan enfermedades"],
    "Tu cuerpo convive con billones de bacterias buenas que te ayudan a digerir la comida.",
    "COMUN", "org_bacteria")
org("org_diatomea", "micromundo", "Diatomea", "Bacillariophyta", "MICROORGANISMO",
    "Océanos y lagos", "Fotosíntesis", "PRODUCTOR",
    ["caparazón de vidrio (sílice)", "produce gran parte del oxígeno del planeta", "forma parte del plancton"],
    "Las diatomeas producen alrededor de una quinta parte del oxígeno que respiramos.",
    "POCO_COMUN", "org_diatomea")
org("org_volvox", "micromundo", "Volvox", "Volvox globator", "MICROORGANISMO",
    "Estanques y charcas", "Fotosíntesis", "PRODUCTOR",
    ["forma colonias esféricas", "miles de células trabajando juntas", "gira al nadar"],
    "Aunque es diminuto, el Volvox forma colonias que giran juntas como una pequeña esfera verde.",
    "RARO", "org_volvox")

# --- Bosque de Vida (14) ---
org("org_helecho", "bosque_de_vida", "Helecho gigante", "Polypodiopsida", "PLANTA",
    "Bosque húmedo", "Fotosíntesis", "PRODUCTOR",
    ["hojas grandes y divididas", "se reproduce por esporas", "no tiene flores"],
    "Los helechos existían en la Tierra mucho antes que los dinosaurios.",
    "COMUN", "org_helecho")
org("org_roble", "bosque_de_vida", "Roble", "Quercus robur", "PLANTA",
    "Bosque templado", "Fotosíntesis", "PRODUCTOR",
    ["puede vivir cientos de años", "produce bellotas", "raíces muy profundas"],
    "Un roble adulto puede alimentar a cientos de especies distintas de insectos y animales.",
    "COMUN", "org_roble")
org("org_musgo", "bosque_de_vida", "Musgo", "Bryophyta", "PLANTA",
    "Troncos y rocas húmedas", "Fotosíntesis", "PRODUCTOR",
    ["no tiene raíces verdaderas", "absorbe agua por toda su superficie", "forma alfombras verdes"],
    "El musgo ayuda a que la humedad se mantenga en el suelo del bosque.",
    "COMUN", "org_musgo")
org("org_seta_amanita", "bosque_de_vida", "Seta roja", "Amanita muscaria", "HONGO",
    "Bosques de coníferas", "Materia orgánica en descomposición", "DESCOMPONEDOR",
    ["sombrero rojo con puntos blancos", "no debe tocarse ni comerse", "ayuda a reciclar nutrientes"],
    "Los hongos no son plantas: forman su propio reino y reciclan la materia del bosque.",
    "POCO_COMUN", "org_seta")
org("org_conejo", "bosque_de_vida", "Conejo silvestre", "Oryctolagus cuniculus", "ANIMAL",
    "Praderas y bosques", "Pasto y hierbas", "HERBIVORO",
    ["orejas largas", "excelente oído", "se reproduce rápidamente"],
    "Un conejo puede girar sus orejas de forma independiente para escuchar en dos direcciones.",
    "COMUN", "org_conejo")
org("org_ciervo", "bosque_de_vida", "Ciervo rojo", "Cervus elaphus", "ANIMAL",
    "Bosques templados", "Hojas, hierba y cortezas", "HERBIVORO",
    ["astas que renueva cada año", "vive en manadas", "gran olfato"],
    "Las astas del ciervo se caen y vuelven a crecer cada año, más grandes.",
    "COMUN", "org_ciervo")
org("org_zorro", "bosque_de_vida", "Zorro rojo", "Vulpes vulpes", "ANIMAL",
    "Bosques y zonas mixtas", "Roedores, conejos e insectos", "CARNIVORO",
    ["cola espesa", "muy adaptable", "caza principalmente de noche"],
    "El zorro puede detectar el campo magnético de la Tierra para calcular sus saltos al cazar.",
    "POCO_COMUN", "org_zorro")
org("org_buho", "bosque_de_vida", "Búho real", "Bubo bubo", "ANIMAL",
    "Bosques y acantilados", "Roedores y aves pequeñas", "CARNIVORO",
    ["vuelo silencioso", "gira la cabeza casi 270°", "vista nocturna excelente"],
    "Sus plumas especiales le permiten volar sin hacer casi ningún ruido.",
    "RARO", "org_buho")
org("org_ardilla", "bosque_de_vida", "Ardilla roja", "Sciurus vulgaris", "ANIMAL",
    "Bosques de coníferas y frondosos", "Semillas, frutos secos y hongos", "HERBIVORO",
    ["cola grande y espesa", "entierra reservas de comida", "trepa con gran agilidad"],
    "Al enterrar semillas que luego olvida, la ardilla ayuda a plantar nuevos árboles.",
    "COMUN", "org_ardilla")
org("org_escarabajo", "bosque_de_vida", "Escarabajo peloteros", "Scarabaeinae", "ANIMAL",
    "Suelo del bosque", "Estiércol y materia orgánica", "DESCOMPONEDOR",
    ["forma bolas con su alimento", "recicla nutrientes del suelo", "muy fuerte para su tamaño"],
    "Puede arrastrar hasta 1.000 veces su propio peso mientras recicla el suelo del bosque.",
    "POCO_COMUN", "org_escarabajo")
org("org_lombriz", "bosque_de_vida", "Lombriz de tierra", "Lumbricus terrestris", "ANIMAL",
    "Suelo húmedo", "Materia orgánica en descomposición", "DESCOMPONEDOR",
    ["no tiene ojos ni pulmones", "airea el suelo al moverse", "respira por la piel"],
    "Las lombrices mejoran tanto el suelo que se las llama 'los ingenieros del jardín'.",
    "COMUN", "org_lombriz")
org("org_rana_arborea", "bosque_de_vida", "Rana arbórea", "Hylidae", "ANIMAL",
    "Bosque húmedo", "Insectos", "CARNIVORO",
    ["piel húmeda", "salta grandes distancias", "canta para comunicarse"],
    "Respira parcialmente a través de su piel, por eso necesita mantenerla húmeda.",
    "POCO_COMUN", "org_rana")
org("org_mariposa", "bosque_de_vida", "Mariposa monarca", "Danaus plexippus", "ANIMAL",
    "Praderas y bosques", "Néctar de flores", "HERBIVORO",
    ["migra miles de kilómetros", "colores naranja y negro", "se transforma desde oruga"],
    "Puede migrar más de 4.000 kilómetros usando el sol como brújula.",
    "RARO", "org_mariposa")
org("org_lince", "bosque_de_vida", "Lince ibérico", "Lynx pardinus", "ANIMAL",
    "Bosque mediterráneo", "Conejos principalmente", "CARNIVORO",
    ["orejas con mechones de pelo", "vista y oído extraordinarios", "especie protegida"],
    "Es uno de los felinos más amenazados del mundo, y gracias a la protección va recuperándose.",
    "LEGENDARIO", "org_lince")

# --- Océano Profundo (14) ---
org("org_ballena_azul", "oceano_profundo", "Ballena azul", "Balaenoptera musculus", "ANIMAL",
    "Océano abierto", "Kril (pequeños crustáceos)", "HERBIVORO",
    ["el animal más grande del planeta", "corazón del tamaño de un auto pequeño", "canta bajo el agua"],
    "A pesar de ser gigantesca, se alimenta de uno de los animales más pequeños del mar: el kril.",
    "LEGENDARIO", "org_ballena")
org("org_tiburon_blanco", "oceano_profundo", "Tiburón blanco", "Carcharodon carcharias", "ANIMAL",
    "Océanos templados", "Peces, focas y otros animales marinos", "CARNIVORO",
    ["detecta el olor de la sangre a gran distancia", "puede percibir campos eléctricos", "nada muy rápido"],
    "Puede detectar una sola gota de sangre diluida en un millón de gotas de agua.",
    "RARO", "org_tiburon")
org("org_pulpo", "oceano_profundo", "Pulpo común", "Octopus vulgaris", "ANIMAL",
    "Fondos rocosos", "Cangrejos y moluscos", "CARNIVORO",
    ["ocho brazos con ventosas", "cambia de color y textura", "extremadamente inteligente"],
    "Tiene tres corazones y su sangre es de color azul.",
    "RARO", "org_pulpo")
org("org_medusa", "oceano_profundo", "Medusa luna", "Aurelia aurita", "ANIMAL",
    "Aguas costeras", "Plancton", "HERBIVORO",
    ["cuerpo casi transparente", "se mueve con pulsaciones", "no tiene cerebro ni corazón"],
    "Está formada en un 95% por agua y existe desde antes que los dinosaurios.",
    "POCO_COMUN", "org_medusa")
org("org_coral", "oceano_profundo", "Coral cerebro", "Diploria labyrinthiformis", "ANIMAL",
    "Arrecifes tropicales", "Plancton y luz solar (con algas asociadas)", "HERBIVORO",
    ["forma colonias de miles de individuos", "construye el esqueleto del arrecife", "vive en simbiosis con algas"],
    "Un arrecife de coral puede albergar más especies distintas que cualquier otro ecosistema marino.",
    "POCO_COMUN", "org_coral")
org("org_alga_marina", "oceano_profundo", "Alga parda gigante", "Macrocystis pyrifera", "PLANTA",
    "Bosques submarinos", "Fotosíntesis", "PRODUCTOR",
    ["puede crecer más de 30 metros", "forma auténticos bosques bajo el agua", "crece muy rápido"],
    "Puede crecer hasta 50 centímetros en un solo día bajo condiciones ideales.",
    "COMUN", "org_alga")
org("org_pez_payaso", "oceano_profundo", "Pez payaso", "Amphiprioninae", "ANIMAL",
    "Arrecifes de coral", "Algas y pequeños invertebrados", "HERBIVORO",
    ["vive protegido entre las anémonas", "es inmune a su veneno", "cambia de sexo durante su vida"],
    "Vive en una relación de ayuda mutua con la anémona: ella lo protege y él la limpia.",
    "POCO_COMUN", "org_pez_payaso")
org("org_estrella_mar", "oceano_profundo", "Estrella de mar", "Asteroidea", "ANIMAL",
    "Fondos marinos", "Moluscos y materia orgánica", "CARNIVORO",
    ["puede regenerar brazos perdidos", "no tiene cerebro", "se mueve con cientos de piececillos"],
    "Si pierde un brazo, puede regenerarlo por completo con el paso del tiempo.",
    "POCO_COMUN", "org_estrella")
org("org_kril", "oceano_profundo", "Kril antártico", "Euphausia superba", "ANIMAL",
    "Aguas frías antárticas", "Fitoplancton", "HERBIVORO",
    ["forma enormes cardúmenes", "base de la cadena alimentaria antártica", "mide pocos centímetros"],
    "A pesar de su pequeño tamaño, alimenta a ballenas, focas y pingüinos por igual.",
    "COMUN", "org_kril")
org("org_tortuga_marina", "oceano_profundo", "Tortuga marina verde", "Chelonia mydas", "ANIMAL",
    "Océanos tropicales", "Algas y pastos marinos", "HERBIVORO",
    ["puede vivir más de 80 años", "regresa a la misma playa donde nació para poner huevos", "nada miles de kilómetros"],
    "Usa el campo magnético de la Tierra como un mapa para regresar siempre a la misma playa.",
    "RARO", "org_tortuga")
org("org_pez_globo", "oceano_profundo", "Pez globo", "Tetraodontidae", "ANIMAL",
    "Arrecifes tropicales", "Moluscos y crustáceos", "CARNIVORO",
    ["se infla al sentirse amenazado", "algunas especies son venenosas", "no es buen nadador"],
    "Se infla tragando agua para parecer demasiado grande para ser tragado por un depredador.",
    "POCO_COMUN", "org_pez_globo")
org("org_plancton_fito", "oceano_profundo", "Fitoplancton", "Varias especies", "MICROORGANISMO",
    "Superficie de todos los océanos", "Fotosíntesis", "PRODUCTOR",
    ["invisible a simple vista", "base de toda la cadena marina", "produce gran parte del oxígeno mundial"],
    "El fitoplancton produce más de la mitad del oxígeno que respiramos en todo el planeta.",
    "COMUN", "org_plancton")
org("org_calamar_gigante", "oceano_profundo", "Calamar gigante", "Architeuthis dux", "ANIMAL",
    "Aguas profundas", "Peces y otros calamares", "CARNIVORO",
    ["ojos del tamaño de un plato", "puede medir más de 12 metros", "vive en la oscuridad total"],
    "Tiene los ojos más grandes de todo el reino animal, para ver en la oscuridad de las profundidades.",
    "LEGENDARIO", "org_calamar")
org("org_pez_linterna", "oceano_profundo", "Pez linterna", "Myctophidae", "ANIMAL",
    "Aguas profundas", "Plancton", "HERBIVORO",
    ["produce su propia luz (bioluminiscencia)", "vive en la zona sin luz solar", "migra cada noche hacia la superficie"],
    "Fabrica su propia luz gracias a una reacción química en su cuerpo, como una linterna viviente.",
    "RARO", "org_pez_linterna")

# --- Cuerpo Humano (6, representando tipos celulares/microhabitantes del cuerpo) ---
org("org_globulo_rojo", "cuerpo_humano", "Glóbulo rojo", "Erythrocyte", "MICROORGANISMO",
    "Torrente sanguíneo", "No se alimenta: transporta oxígeno", "HERBIVORO",
    ["forma de disco", "transporta oxígeno por el cuerpo", "no tiene núcleo"],
    "Tu cuerpo produce alrededor de dos millones de glóbulos rojos nuevos cada segundo.",
    "COMUN", "org_globulo_rojo")
org("org_globulo_blanco", "cuerpo_humano", "Glóbulo blanco", "Leukocyte", "MICROORGANISMO",
    "Torrente sanguíneo", "Bacterias y microbios invasores", "CARNIVORO",
    ["defiende al cuerpo de infecciones", "puede salir de los vasos sanguíneos", "existen varios tipos"],
    "Son los soldados del sistema inmunitario: patrullan el cuerpo buscando invasores.",
    "POCO_COMUN", "org_globulo_blanco")
org("org_neurona", "cuerpo_humano", "Neurona", "Neuron", "MICROORGANISMO",
    "Cerebro y sistema nervioso", "Glucosa y oxígeno", "HERBIVORO",
    ["se comunica con impulsos eléctricos", "tiene ramificaciones llamadas dendritas", "forma redes complejísimas"],
    "El cerebro humano tiene unos 86.000 millones de neuronas conectadas entre sí.",
    "RARO", "org_neurona")
org("org_bacteria_beneficiosa_intestino", "cuerpo_humano", "Microbiota intestinal", "Bifidobacterium sp.",
    "MICROORGANISMO", "Intestino grueso", "Fibra vegetal no digerida", "DESCOMPONEDOR",
    ["ayuda a digerir la fibra", "produce vitaminas útiles", "forma parte de un ecosistema interno"],
    "Tu intestino alberga billones de bacterias que te ayudan a digerir y a mantenerte sano.",
    "COMUN", "org_bacteria_intestino")
org("org_celula_muscular", "cuerpo_humano", "Célula muscular", "Myocyte", "MICROORGANISMO",
    "Músculos del cuerpo", "Glucosa y oxígeno", "HERBIVORO",
    ["se contrae para generar movimiento", "puede alargarse mucho", "trabaja en equipo con otras fibras"],
    "Algunas células musculares pueden medir varios centímetros de largo, muy alargadas.",
    "POCO_COMUN", "org_celula_muscular")
org("org_plaqueta", "cuerpo_humano", "Plaqueta", "Thrombocyte", "MICROORGANISMO",
    "Torrente sanguíneo", "No se alimenta: repara heridas", "HERBIVORO",
    ["ayuda a cerrar heridas", "es más pequeña que un glóbulo rojo", "forma coágulos"],
    "Cuando te cortas, miles de plaquetas corren al lugar para taponar la herida en minutos.",
    "COMUN", "org_plaqueta")

# --- Ecosistemas (8, organismos de rol claro para cadenas alimentarias) ---
org("org_pasto", "ecosistemas", "Pasto de sabana", "Poaceae", "PLANTA",
    "Sabana africana", "Fotosíntesis", "PRODUCTOR",
    ["crece rápido tras la lluvia", "base de la cadena en la sabana", "resiste el pisoteo"],
    "La sabana puede sostener manadas enteras de herbívoros gracias a su rápido crecimiento.",
    "COMUN", "org_pasto")
org("org_cebra", "ecosistemas", "Cebra", "Equus quagga", "ANIMAL",
    "Sabana africana", "Pasto", "HERBIVORO",
    ["rayas únicas en cada individuo", "vive en manadas", "gran resistencia para correr"],
    "El patrón de rayas de cada cebra es único, como una huella dactilar.",
    "COMUN", "org_cebra")
org("org_leon", "ecosistemas", "León", "Panthera leo", "ANIMAL",
    "Sabana africana", "Cebras, ñus y otros herbívoros", "CARNIVORO",
    ["vive en manadas llamadas orgullos", "las hembras cazan en equipo", "es el depredador tope de la sabana"],
    "Las leonas hacen la mayoría de las cacerías, a menudo trabajando en equipo.",
    "POCO_COMUN", "org_leon")
org("org_hiena", "ecosistemas", "Hiena manchada", "Crocuta crocuta", "ANIMAL",
    "Sabana africana", "Carroña y presas pequeñas", "DESCOMPONEDOR",
    ["mandíbula muy potente", "vive en clanes organizados", "recicla restos de otros cazadores"],
    "Su digestión es tan potente que puede aprovechar hasta los huesos de sus presas.",
    "POCO_COMUN", "org_hiena")
org("org_nenufar", "ecosistemas", "Nenúfar", "Nymphaea", "PLANTA",
    "Estanque de agua dulce", "Fotosíntesis", "PRODUCTOR",
    ["hojas flotantes", "raíces ancladas al fondo", "flores que se abren de día"],
    "Sus hojas flotantes son tan resistentes que pueden sostener el peso de una pequeña rana.",
    "COMUN", "org_nenufar")
org("org_libelula", "ecosistemas", "Libélula", "Anisoptera", "ANIMAL",
    "Estanque de agua dulce", "Mosquitos y otros insectos", "CARNIVORO",
    ["vuela en todas direcciones", "vista compuesta con miles de lentes", "pasa su juventud bajo el agua"],
    "Puede volar hacia adelante, atrás y de lado, como un pequeño helicóptero.",
    "POCO_COMUN", "org_libelula")
org("org_rana_estanque", "ecosistemas", "Rana de estanque", "Pelophylax", "ANIMAL",
    "Estanque de agua dulce", "Insectos", "CARNIVORO",
    ["croa para comunicarse", "salta grandes distancias", "vive entre el agua y la tierra"],
    "Empieza su vida como renacuajo respirando bajo el agua, y de adulta respira aire.",
    "COMUN", "org_rana_estanque")
org("org_hongo_reciclador", "ecosistemas", "Hongo reciclador de estanque", "Saprolegnia sp.", "HONGO",
    "Estanque de agua dulce", "Materia orgánica en descomposición", "DESCOMPONEDOR",
    ["descompone restos de plantas y animales", "libera nutrientes de vuelta al agua", "invisible a simple vista en su mayoría"],
    "Sin descomponedores como este hongo, los nutrientes nunca volverían a estar disponibles.",
    "POCO_COMUN", "org_hongo_reciclador")

assert len(ORGANISMS) == 50, f"Se esperaban 50 organismos, hay {len(ORGANISMS)}"
print(f"Organismos: {len(ORGANISMS)}")

# ---------------------------------------------------------------------------
# 3. EXPEDICIONES (40 total, 8 por bioma) con pasos reales basados en los organismos de la zona
# ---------------------------------------------------------------------------
ORG_BY_BIOME = {}
for o in ORGANISMS:
    ORG_BY_BIOME.setdefault(o["biome"], []).append(o)

MISSION_CYCLE = ["OBSERVAR", "COMPARAR", "INVESTIGAR", "CLASIFICAR", "OBSERVAR", "COMPARAR", "INVESTIGAR", "MICROSCOPIO"]

EXPEDITION_TITLES = {
    "micromundo": [
        "Primeras gotas de agua", "El misterio de la vacuola", "Colonias en movimiento",
        "La levadura despierta", "El oso de agua imposible", "Luz bajo el lente",
        "Fotosíntesis en miniatura", "El caparazón de cristal"
    ],
    "bosque_de_vida": [
        "El susurro del bosque", "Huellas entre los árboles", "El reciclaje del bosque",
        "Vecinos del roble", "La cadena bajo las hojas", "Alas que migran",
        "El cazador silencioso", "El regreso del lince"
    ],
    "oceano_profundo": [
        "El gigante silencioso", "Ocho brazos y un plan", "El arrecife viviente",
        "Luces en la oscuridad", "La ruta de la tortuga", "El bosque bajo el mar",
        "Cardumen en peligro", "El ojo más grande del océano"
    ],
    "cuerpo_humano": [
        "Un viaje por tus venas", "Soldados invisibles", "Chispas del pensamiento",
        "El ejército intestinal", "Fibras en movimiento", "Reparando una herida"
    ],
    "ecosistemas": [
        "El equilibrio de la sabana", "Cazadores y presas", "El reciclaje final",
        "Vida en el estanque", "Alas sobre el agua", "De renacuajo a explorador",
        "La cadena completa", "Guardianes del planeta"
    ],
}
# Cuerpo humano solo tiene 6 organismos pero necesita 8 expediciones: se completan dos
# expediciones de repaso/integración usando temas de sistemas del cuerpo (no organismos nuevos).
EXPEDITION_TITLES["cuerpo_humano"] += ["El mapa de tus sistemas", "Construye un cuerpo"]

NARRATIVE_TEMPLATES = {
    "OBSERVAR": "BIA necesita que observes con atención a {name} antes de registrar el hallazgo en el diario.",
    "COMPARAR": "Algo cambió en la zona. Compara a {name} con otro organismo cercano para entender qué ocurre.",
    "INVESTIGAR": "Un fenómeno extraño rodea a {name}. Investiga sus características para resolver el misterio.",
    "CLASIFICAR": "Antes de continuar, ayuda a BIA a clasificar correctamente a {name} en el registro biológico.",
    "MICROSCOPIO": "Usa el microscopio virtual para explorar de cerca la estructura relacionada con {name}.",
    "CONSTRUIR": "Es momento de construir un modelo relacionado con {name} para comprenderlo mejor.",
}

STEP_PROMPTS = {
    "OBSERVAR": [
        "Observa con calma la ilustración de {name} antes de continuar.",
        "¿Qué detalle llama más tu atención en {name}?",
        "Anota en tu diario una característica visible de {name}.",
    ],
    "COMPARAR": [
        "Compara el hábitat de {name} con el de otro organismo de la zona.",
        "¿En qué se parece {name} a otros organismos que ya descubriste?",
        "Señala una diferencia clara entre {name} y su vecino más cercano.",
    ],
    "INVESTIGAR": [
        "Investiga de qué se alimenta {name} para completar la ficha.",
        "Descubre por qué {name} tiene la característica que observaste.",
        "Reúne una pista más sobre {name} antes de cerrar el caso.",
    ],
    "CLASIFICAR": [
        "Clasifica a {name} según su categoría biológica.",
        "Indica el rol de {name} en su cadena alimentaria.",
        "Ubica a {name} en el grupo que le corresponde.",
    ],
    "MICROSCOPIO": [
        "Acerca el microscopio virtual para revelar una estructura relacionada con {name}.",
        "Identifica el nombre de la estructura que acabas de revelar.",
        "Explica con tus palabras para qué sirve esa estructura.",
    ],
    "CONSTRUIR": [
        "Arrastra las piezas necesarias para construir el modelo de {name}.",
        "Revisa que el modelo de {name} esté completo.",
        "Comprueba el resultado final de tu construcción.",
    ],
}

HINTS = {
    "OBSERVAR": "Fíjate en la forma, el color y el tamaño antes de decidir.",
    "COMPARAR": "Piensa en el hábitat y la dieta de cada uno.",
    "INVESTIGAR": "Revisa el dato curioso de la ficha si tienes dudas.",
    "CLASIFICAR": "Recuerda las cuatro categorías: planta, animal, microorganismo u hongo.",
    "MICROSCOPIO": "Toca cada punto brillante para revelar una estructura nueva.",
    "CONSTRUIR": "No hay una única forma correcta: experimenta con las piezas.",
}

EXPEDITIONS = []
EXPEDITION_STEPS = []
RANKS_CYCLE = ["EXPLORADOR_DE_VIDA", "EXPLORADOR_DE_VIDA", "EXPLORADOR_DE_VIDA", "BIOLOGO_JUNIOR",
               "BIOLOGO_JUNIOR", "INVESTIGADOR_NATURAL", "INVESTIGADOR_NATURAL", "GUARDIAN_DEL_PLANETA"]

for biome_id, titles in EXPEDITION_TITLES.items():
    biome_orgs = ORG_BY_BIOME.get(biome_id, [])
    for i, title in enumerate(titles):
        mission = MISSION_CYCLE[i % len(MISSION_CYCLE)]
        # Cuerpo humano expediciones extra (índices 6,7) no referencian organismo nuevo: usan el primero.
        related_org = biome_orgs[i % len(biome_orgs)] if biome_orgs else None
        related_ids = [related_org["id"]] if related_org else []
        name_for_text = related_org["name"] if related_org else "los sistemas del cuerpo"
        exp_id = f"exp_{biome_id}_{i+1:02d}"
        difficulty = 1 if i < 3 else (2 if i < 6 else 3)
        expedition = dict(
            id=exp_id, biome=biome_id, order=i + 1, title=title,
            narrative=NARRATIVE_TEMPLATES[mission].format(name=name_for_text),
            mission=mission, difficulty=difficulty, related_ids=related_ids,
            reward_xp=20 + difficulty * 10, required_rank=RANKS_CYCLE[i % len(RANKS_CYCLE)]
        )
        EXPEDITIONS.append(expedition)
        for s in range(3):
            EXPEDITION_STEPS.append(dict(
                expedition_id=exp_id, order=s + 1,
                prompt=STEP_PROMPTS[mission][s].format(name=name_for_text),
                type=mission, hint=HINTS[mission]
            ))

assert len(EXPEDITIONS) == 40, f"Se esperaban 40 expediciones, hay {len(EXPEDITIONS)}"
print(f"Expediciones: {len(EXPEDITIONS)}, pasos: {len(EXPEDITION_STEPS)}")

# ---------------------------------------------------------------------------
# 4. ECOSISTEMAS (20 plantillas para el Constructor de Ecosistemas, 4 por bioma)
# ---------------------------------------------------------------------------
ECOSYSTEM_TEMPLATES = []
ECOSYSTEM_NAMES = {
    "micromundo": ["Charca de laboratorio", "Gota de estanque", "Cultivo de levaduras", "Película de musgo húmedo"],
    "bosque_de_vida": ["Claro del bosque templado", "Sotobosque profundo", "Copa de los árboles", "Suelo forestal"],
    "oceano_profundo": ["Arrecife de coral", "Bosque de algas", "Fondo abisal", "Costa rocosa"],
    "cuerpo_humano": ["Torrente sanguíneo", "Intestino delgado", "Red neuronal", "Tejido muscular"],
    "ecosistemas": ["Sabana africana", "Estanque de agua dulce", "Pradera templada", "Humedal costero"],
}
for biome_id, names in ECOSYSTEM_NAMES.items():
    biome_orgs = ORG_BY_BIOME.get(biome_id, [])
    org_ids = [o["id"] for o in biome_orgs]
    for i, name in enumerate(names):
        ECOSYSTEM_TEMPLATES.append(dict(
            id=f"eco_{biome_id}_{i+1:02d}", biome=biome_id, name=name,
            desc=f"Equilibra la energía de {name.lower()} colocando productores, herbívoros, carnívoros y descomponedores.",
            org_ids=org_ids, ideal_p=5 + (i % 3), ideal_h=3 + (i % 2), ideal_c=1 + (i % 2), ideal_d=2
        ))
assert len(ECOSYSTEM_TEMPLATES) == 20, f"Se esperaban 20 ecosistemas, hay {len(ECOSYSTEM_TEMPLATES)}"
print(f"Ecosistemas: {len(ECOSYSTEM_TEMPLATES)}")

# ---------------------------------------------------------------------------
# 5. DESAFÍOS (30 total, 6 por bioma, variando tipo de mecánica)
# ---------------------------------------------------------------------------
CHALLENGE_TYPE_CYCLE = ["CLASIFICADOR", "CONSTRUCTOR_ECOSISTEMA", "CADENA_ALIMENTARIA", "CONSTRUCTOR_CELULA", "MEMORIA_BIOLOGICA", "CLASIFICADOR"]
CHALLENGE_TITLES = {
    "micromundo": ["Clasifica lo invisible", "Equilibra el cultivo", "Cadena microscópica", "Arma la célula", "Memoria celular", "Productores o consumidores"],
    "bosque_de_vida": ["Quién vive dónde", "Equilibra el bosque", "La cadena del bosque", "Arma una hoja", "Memoria del bosque", "Plantas o animales"],
    "oceano_profundo": ["Vida marina en orden", "Equilibra el arrecife", "Cadena del océano", "Arma una célula marina", "Memoria del océano", "Depredador o presa"],
    "cuerpo_humano": ["Clasifica las células", "Equilibra el sistema", "Cadena de energía del cuerpo", "Arma la célula muscular", "Memoria del cuerpo", "Función o sistema"],
    "ecosistemas": ["Roles en la sabana", "Equilibra el humedal", "La cadena completa", "Arma un ecosistema", "Memoria de ecosistemas", "Productor o descomponedor"],
}
CHALLENGES = []
for biome_id, titles in CHALLENGE_TITLES.items():
    biome_orgs = ORG_BY_BIOME.get(biome_id, [])
    org_ids = [o["id"] for o in biome_orgs][:6] or [o["id"] for o in biome_orgs]
    for i, title in enumerate(titles):
        ctype = CHALLENGE_TYPE_CYCLE[i % len(CHALLENGE_TYPE_CYCLE)]
        CHALLENGES.append(dict(
            id=f"cha_{biome_id}_{i+1:02d}", biome=biome_id, type=ctype, title=title,
            instructions=f"Completa el desafío '{title}' usando lo que descubriste en esta zona.",
            org_ids=org_ids, reward_xp=15 + (i * 3)
        ))
assert len(CHALLENGES) == 30, f"Se esperaban 30 desafíos, hay {len(CHALLENGES)}"
print(f"Desafíos: {len(CHALLENGES)}")

# ---------------------------------------------------------------------------
# 6. INSIGNIAS (15 recompensas visuales)
# ---------------------------------------------------------------------------
BADGES = [
    dict(id="badge_primer_descubrimiento", name="Primer Descubrimiento", desc="Descubriste tu primer organismo.", icon="badge_primer_descubrimiento", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=1, biome=None),
    dict(id="badge_coleccionista_10", name="Coleccionista Curioso", desc="Descubriste 10 organismos distintos.", icon="badge_coleccionista_10", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=10, biome=None),
    dict(id="badge_coleccionista_25", name="Coleccionista Experto", desc="Descubriste 25 organismos distintos.", icon="badge_coleccionista_25", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=25, biome=None),
    dict(id="badge_coleccionista_50", name="Museo Completo", desc="Descubriste los 50 organismos del planeta.", icon="badge_coleccionista_50", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=50, biome=None),
    dict(id="badge_primera_expedicion", name="Primera Expedición", desc="Completaste tu primera expedición.", icon="badge_primera_expedicion", ctype="EXPEDICIONES_COMPLETADAS", cvalue=1, biome=None),
    dict(id="badge_explorador_10", name="Explorador Constante", desc="Completaste 10 expediciones.", icon="badge_explorador_10", ctype="EXPEDICIONES_COMPLETADAS", cvalue=10, biome=None),
    dict(id="badge_explorador_25", name="Explorador Incansable", desc="Completaste 25 expediciones.", icon="badge_explorador_25", ctype="EXPEDICIONES_COMPLETADAS", cvalue=25, biome=None),
    dict(id="badge_explorador_40", name="Maestro de Expediciones", desc="Completaste las 40 expediciones disponibles.", icon="badge_explorador_40", ctype="EXPEDICIONES_COMPLETADAS", cvalue=40, biome=None),
    dict(id="badge_ecosistema_estable", name="Guardián del Equilibrio", desc="Lograste tu primer ecosistema estable.", icon="badge_ecosistema_estable", ctype="ECOSISTEMAS_ESTABLES", cvalue=1, biome=None),
    dict(id="badge_ecosistema_5", name="Ingeniero Ecológico", desc="Equilibraste 5 ecosistemas distintos.", icon="badge_ecosistema_5", ctype="ECOSISTEMAS_ESTABLES", cvalue=5, biome=None),
    dict(id="badge_desafios_10", name="Mente Científica", desc="Superaste 10 desafíos interactivos.", icon="badge_desafios_10", ctype="DESAFIOS_SUPERADOS", cvalue=10, biome=None),
    dict(id="badge_desafios_20", name="Investigador Brillante", desc="Superaste 20 desafíos interactivos.", icon="badge_desafios_20", ctype="DESAFIOS_SUPERADOS", cvalue=20, biome=None),
    dict(id="badge_zona_micromundo", name="Maestro del Micromundo", desc="Completaste toda la colección del Micromundo.", icon="badge_zona_micromundo", ctype="ZONA_COMPLETA", cvalue=100, biome="micromundo"),
    dict(id="badge_zona_bosque", name="Guardián del Bosque", desc="Completaste toda la colección del Bosque de Vida.", icon="badge_zona_bosque", ctype="ZONA_COMPLETA", cvalue=100, biome="bosque_de_vida"),
    dict(id="badge_legendario", name="Cazador de Leyendas", desc="Descubriste un organismo legendario.", icon="badge_legendario", ctype="RAREZA_LEGENDARIA", cvalue=1, biome=None),
]
assert len(BADGES) == 15, f"Se esperaban 15 insignias, hay {len(BADGES)}"
print(f"Insignias: {len(BADGES)}")

# ---------------------------------------------------------------------------
# 7. MODELOS DE CÉLULA (microscopio virtual)
# ---------------------------------------------------------------------------
CELL_MODELS = [
    dict(id="cell_animal", name="Célula animal", ctype="Animal", desc="La unidad básica de todos los animales, incluido tú.",
         structures=[
             dict(id="cell_animal_nucleo", name="Núcleo", fn="Controla las actividades de la célula y guarda el ADN.", x=0.5, y=0.5),
             dict(id="cell_animal_membrana", name="Membrana celular", fn="Protege la célula y controla lo que entra y sale.", x=0.12, y=0.5),
             dict(id="cell_animal_citoplasma", name="Citoplasma", fn="Sustancia gelatinosa donde ocurren las reacciones de la célula.", x=0.3, y=0.75),
             dict(id="cell_animal_mitocondria", name="Mitocondria", fn="Genera la energía que la célula necesita para funcionar.", x=0.7, y=0.3),
         ]),
    dict(id="cell_vegetal", name="Célula vegetal", ctype="Vegetal", desc="La unidad básica de todas las plantas del Bosque de Vida.",
         structures=[
             dict(id="cell_vegetal_nucleo", name="Núcleo", fn="Controla las actividades de la célula y guarda el ADN.", x=0.5, y=0.5),
             dict(id="cell_vegetal_pared", name="Pared celular", fn="Capa rígida que da forma y soporte a la célula.", x=0.08, y=0.5),
             dict(id="cell_vegetal_cloroplasto", name="Cloroplasto", fn="Realiza la fotosíntesis para producir energía a partir de la luz.", x=0.7, y=0.35),
             dict(id="cell_vegetal_vacuola", name="Vacuola central", fn="Almacena agua y nutrientes, y da rigidez a la planta.", x=0.5, y=0.75),
         ]),
    dict(id="cell_bacteriana", name="Célula bacteriana", ctype="Bacteriana", desc="Mucho más simple: no tiene núcleo definido.",
         structures=[
             dict(id="cell_bacteriana_adn", name="ADN libre", fn="El material genético flota libremente, sin núcleo que lo envuelva.", x=0.5, y=0.5),
             dict(id="cell_bacteriana_pared", name="Pared celular", fn="Protege a la bacteria del medio externo.", x=0.1, y=0.5),
             dict(id="cell_bacteriana_flagelo", name="Flagelo", fn="Una cola que le permite moverse en el agua.", x=0.9, y=0.5),
         ]),
]
print(f"Modelos de célula: {len(CELL_MODELS)}, estructuras: {sum(len(c['structures']) for c in CELL_MODELS)}")

# ---------------------------------------------------------------------------
# 8. SISTEMAS DEL CUERPO HUMANO
# ---------------------------------------------------------------------------
BODY_SYSTEMS = [
    dict(id="sys_digestivo", name="Sistema digestivo", desc="Transforma los alimentos en energía utilizable.",
         organs=[("Estómago", "Descompone el alimento con jugos digestivos."),
                 ("Intestino delgado", "Absorbe los nutrientes hacia la sangre."),
                 ("Hígado", "Produce bilis y filtra sustancias de la sangre.")]),
    dict(id="sys_circulatorio", name="Sistema circulatorio", desc="Transporta sangre, oxígeno y nutrientes por todo el cuerpo.",
         organs=[("Corazón", "Bombea la sangre por todo el cuerpo."),
                 ("Venas", "Llevan la sangre de vuelta al corazón."),
                 ("Arterias", "Llevan la sangre con oxígeno desde el corazón.")]),
    dict(id="sys_respiratorio", name="Sistema respiratorio", desc="Permite el intercambio de oxígeno y dióxido de carbono.",
         organs=[("Pulmones", "Intercambian oxígeno y dióxido de carbono con la sangre."),
                 ("Tráquea", "Conduce el aire hacia los pulmones."),
                 ("Diafragma", "Músculo que ayuda a inhalar y exhalar aire.")]),
    dict(id="sys_nervioso", name="Sistema nervioso", desc="Controla el cuerpo mediante señales eléctricas.",
         organs=[("Cerebro", "Centro de control de pensamientos y movimientos."),
                 ("Médula espinal", "Transporta señales entre el cerebro y el cuerpo."),
                 ("Nervios", "Ramificaciones que llevan señales a cada parte del cuerpo.")]),
    dict(id="sys_oseo", name="Sistema óseo", desc="Da estructura y protección al cuerpo.",
         organs=[("Cráneo", "Protege el cerebro."),
                 ("Columna vertebral", "Sostiene el cuerpo y protege la médula espinal."),
                 ("Fémur", "El hueso más largo y fuerte del cuerpo.")]),
    dict(id="sys_muscular", name="Sistema muscular", desc="Permite el movimiento del cuerpo.",
         organs=[("Bíceps", "Flexiona el brazo."),
                 ("Cuádriceps", "Permite estirar la pierna al caminar o correr."),
                 ("Corazón (músculo cardíaco)", "Se contrae sin descanso durante toda la vida.")]),
]
print(f"Sistemas del cuerpo: {len(BODY_SYSTEMS)}, órganos: {sum(len(s['organs']) for s in BODY_SYSTEMS)}")

AVATAR_KEYS = [f"avatar_explorador_{i+1}" for i in range(8)]

# ---------------------------------------------------------------------------
# 9. EMISIÓN DE SeedContent.kt
# ---------------------------------------------------------------------------
def kstr(s):
    return '"' + s.replace("\\", "\\\\").replace('"', '\\"').replace("$", "\\$") + '"'

def klist_str(items):
    return "listOf(" + ", ".join(kstr(x) for x in items) + ")"

lines = []
lines.append("package com.educalab.ninobiologo.data.local.seed")
lines.append("")
lines.append("import com.educalab.ninobiologo.data.local.entity.*")
lines.append("import com.educalab.ninobiologo.domain.model.BadgeCriteriaType")
lines.append("import com.educalab.ninobiologo.domain.model.BiologistRank")
lines.append("import com.educalab.ninobiologo.domain.model.ChallengeType")
lines.append("import com.educalab.ninobiologo.domain.model.MissionType")
lines.append("import com.educalab.ninobiologo.domain.model.OrganismCategory")
lines.append("import com.educalab.ninobiologo.domain.model.OrganismRarity")
lines.append("import com.educalab.ninobiologo.domain.model.TrophicRole")
lines.append("")
lines.append("/**")
lines.append(" * Contenido semilla de NiñoBiólogo: Exploradores de la Vida.")
lines.append(" *")
lines.append(" * GENERADO AUTOMÁTICAMENTE por tools/generate_seed_data.py — no editar a mano.")
lines.append(" * Para modificar el contenido, edita el script y vuelve a ejecutarlo:")
lines.append(" *   python3 tools/generate_seed_data.py")
lines.append(" *")
lines.append(" * Cantidades: {} biomas, {} organismos, {} expediciones ({} pasos), {} ecosistemas,".format(
    len(BIOMES), len(ORGANISMS), len(EXPEDITIONS), len(EXPEDITION_STEPS), len(ECOSYSTEM_TEMPLATES)))
lines.append(" * {} desafíos, {} insignias, {} modelos de célula, {} sistemas del cuerpo.".format(
    len(CHALLENGES), len(BADGES), len(CELL_MODELS), len(BODY_SYSTEMS)))
lines.append(" */")
lines.append("object SeedContent {")
lines.append("")

# Biomes
lines.append("    val biomes: List<BiomeEntity> = listOf(")
for b in BIOMES:
    lines.append(f"        BiomeEntity(id = {kstr(b['id'])}, orderIndex = {b['order']}, name = {kstr(b['name'])}, "
                 f"tagline = {kstr(b['tagline'])}, description = {kstr(b['desc'])}, iconKey = {kstr(b['icon'])}, "
                 f"primaryColorHex = {kstr(b['primary'])}, secondaryColorHex = {kstr(b['secondary'])}),")
lines.append("    )")
lines.append("")

# Organisms
lines.append("    val organisms: List<OrganismEntity> = listOf(")
for o in ORGANISMS:
    lines.append(f"        OrganismEntity(id = {kstr(o['id'])}, biomeId = {kstr(o['biome'])}, name = {kstr(o['name'])}, "
                 f"scientificName = {kstr(o['sci'])}, category = OrganismCategory.{o['cat']}, habitat = {kstr(o['habitat'])}, "
                 f"diet = {kstr(o['diet'])}, trophicRole = TrophicRole.{o['role']}, characteristics = {klist_str(o['chars'])}, "
                 f"funFact = {kstr(o['fact'])}, rarity = OrganismRarity.{o['rarity']}, iconKey = {kstr(o['icon'])}),")
lines.append("    )")
lines.append("")

# Expeditions
lines.append("    val expeditions: List<ExpeditionEntity> = listOf(")
for e in EXPEDITIONS:
    lines.append(f"        ExpeditionEntity(id = {kstr(e['id'])}, biomeId = {kstr(e['biome'])}, orderIndex = {e['order']}, "
                 f"title = {kstr(e['title'])}, narrative = {kstr(e['narrative'])}, missionType = MissionType.{e['mission']}, "
                 f"difficulty = {e['difficulty']}, relatedOrganismIds = {klist_str(e['related_ids'])}, rewardXp = {e['reward_xp']}, "
                 f"requiredRank = BiologistRank.{e['required_rank']}),")
lines.append("    )")
lines.append("")

lines.append("    val expeditionSteps: List<ExpeditionStepEntity> = listOf(")
for s in EXPEDITION_STEPS:
    lines.append(f"        ExpeditionStepEntity(expeditionId = {kstr(s['expedition_id'])}, orderIndex = {s['order']}, "
                 f"prompt = {kstr(s['prompt'])}, type = MissionType.{s['type']}, hint = {kstr(s['hint'])}),")
lines.append("    )")
lines.append("")

# Cell models + structures
lines.append("    val cellModels: List<CellModelEntity> = listOf(")
for c in CELL_MODELS:
    lines.append(f"        CellModelEntity(id = {kstr(c['id'])}, name = {kstr(c['name'])}, cellType = {kstr(c['ctype'])}, description = {kstr(c['desc'])}),")
lines.append("    )")
lines.append("")
lines.append("    val cellStructures: List<CellStructureEntity> = listOf(")
for c in CELL_MODELS:
    for st in c["structures"]:
        lines.append(f"        CellStructureEntity(id = {kstr(st['id'])}, cellModelId = {kstr(c['id'])}, name = {kstr(st['name'])}, "
                     f"function = {kstr(st['fn'])}, xPercent = {st['x']}f, yPercent = {st['y']}f),")
lines.append("    )")
lines.append("")

# Body systems + organs
lines.append("    val bodySystems: List<BodySystemEntity> = listOf(")
for s in BODY_SYSTEMS:
    lines.append(f"        BodySystemEntity(id = {kstr(s['id'])}, name = {kstr(s['name'])}, description = {kstr(s['desc'])}),")
lines.append("    )")
lines.append("")
lines.append("    val bodyOrgans: List<BodyOrganEntity> = listOf(")
for s in BODY_SYSTEMS:
    for idx, (oname, ofn) in enumerate(s["organs"]):
        organ_id = f"{s['id']}_organ_{idx+1}"
        lines.append(f"        BodyOrganEntity(id = {kstr(organ_id)}, bodySystemId = {kstr(s['id'])}, name = {kstr(oname)}, function = {kstr(ofn)}),")
lines.append("    )")
lines.append("")

# Ecosystem templates
lines.append("    val ecosystemTemplates: List<EcosystemTemplateEntity> = listOf(")
for e in ECOSYSTEM_TEMPLATES:
    lines.append(f"        EcosystemTemplateEntity(id = {kstr(e['id'])}, biomeId = {kstr(e['biome'])}, name = {kstr(e['name'])}, "
                 f"description = {kstr(e['desc'])}, availableOrganismIds = {klist_str(e['org_ids'])}, "
                 f"idealProducers = {e['ideal_p']}, idealHerbivores = {e['ideal_h']}, idealCarnivores = {e['ideal_c']}, idealDecomposers = {e['ideal_d']}),")
lines.append("    )")
lines.append("")

# Challenges
lines.append("    val challenges: List<ChallengeEntity> = listOf(")
for c in CHALLENGES:
    lines.append(f"        ChallengeEntity(id = {kstr(c['id'])}, biomeId = {kstr(c['biome'])}, type = ChallengeType.{c['type']}, "
                 f"title = {kstr(c['title'])}, instructions = {kstr(c['instructions'])}, relatedOrganismIds = {klist_str(c['org_ids'])}, rewardXp = {c['reward_xp']}),")
lines.append("    )")
lines.append("")

# Badges
lines.append("    val badges: List<BadgeEntity> = listOf(")
for b in BADGES:
    biome_val = kstr(b['biome']) if b['biome'] else "null"
    lines.append(f"        BadgeEntity(id = {kstr(b['id'])}, name = {kstr(b['name'])}, description = {kstr(b['desc'])}, "
                 f"iconKey = {kstr(b['icon'])}, criteriaType = BadgeCriteriaType.{b['ctype']}, criteriaValue = {b['cvalue']}, biomeId = {biome_val}),")
lines.append("    )")
lines.append("")

lines.append(f"    val avatarKeys: List<String> = {klist_str(AVATAR_KEYS)}")
lines.append("}")

with open(OUT_PATH, "w", encoding="utf-8") as f:
    f.write("\n".join(lines) + "\n")

print(f"\nEscrito {OUT_PATH} ({len(lines)} líneas)")

# ---------------------------------------------------------------------------
# 10. VALIDACIÓN DE INTEGRIDAD (equivalente a un test de integridad de datos semilla)
# ---------------------------------------------------------------------------
def check(condition, message, errors):
    if not condition:
        errors.append(message)

errors = []
biome_ids = {b["id"] for b in BIOMES}
org_ids_all = {o["id"] for o in ORGANISMS}

check(len(org_ids_all) == len(ORGANISMS), "IDs de organismos duplicados", errors)
check(len({e['id'] for e in EXPEDITIONS}) == len(EXPEDITIONS), "IDs de expediciones duplicados", errors)
check(len({e['id'] for e in ECOSYSTEM_TEMPLATES}) == len(ECOSYSTEM_TEMPLATES), "IDs de ecosistemas duplicados", errors)
check(len({c['id'] for c in CHALLENGES}) == len(CHALLENGES), "IDs de desafíos duplicados", errors)
check(len({b['id'] for b in BADGES}) == len(BADGES), "IDs de insignias duplicados", errors)

for o in ORGANISMS:
    check(o["biome"] in biome_ids, f"Organismo {o['id']} referencia bioma inexistente {o['biome']}", errors)
for e in EXPEDITIONS:
    check(e["biome"] in biome_ids, f"Expedición {e['id']} referencia bioma inexistente", errors)
    for rid in e["related_ids"]:
        check(rid in org_ids_all, f"Expedición {e['id']} referencia organismo inexistente {rid}", errors)
for s in EXPEDITION_STEPS:
    check(any(e["id"] == s["expedition_id"] for e in EXPEDITIONS), f"Paso huérfano para {s['expedition_id']}", errors)
for e in ECOSYSTEM_TEMPLATES:
    check(e["biome"] in biome_ids, f"Ecosistema {e['id']} referencia bioma inexistente", errors)
    for rid in e["org_ids"]:
        check(rid in org_ids_all, f"Ecosistema {e['id']} referencia organismo inexistente {rid}", errors)
for c in CHALLENGES:
    check(c["biome"] in biome_ids, f"Desafío {c['id']} referencia bioma inexistente", errors)
    for rid in c["org_ids"]:
        check(rid in org_ids_all, f"Desafío {c['id']} referencia organismo inexistente {rid}", errors)
for b in BADGES:
    if b["biome"] is not None:
        check(b["biome"] in biome_ids, f"Insignia {b['id']} referencia bioma inexistente", errors)
for c in CELL_MODELS:
    check(len(c["structures"]) >= 3, f"Modelo de célula {c['id']} tiene menos de 3 estructuras", errors)
    struct_ids = [st["id"] for st in c["structures"]]
    check(len(struct_ids) == len(set(struct_ids)), f"Estructuras duplicadas en {c['id']}", errors)
for s in BODY_SYSTEMS:
    check(len(s["organs"]) >= 2, f"Sistema {s['id']} tiene menos de 2 órganos", errors)

check(len(ORGANISMS) == 50, "El total de organismos no es 50", errors)
check(len(EXPEDITIONS) == 40, "El total de expediciones no es 40", errors)
check(len(ECOSYSTEM_TEMPLATES) == 20, "El total de ecosistemas no es 20", errors)
check(len(CHALLENGES) == 30, "El total de desafíos no es 30", errors)
check(len(BADGES) == 15, "El total de insignias no es 15", errors)
check(len(AVATAR_KEYS) == 8, "El total de avatares no es 8", errors)

print("\n=== VALIDACIÓN DE INTEGRIDAD DE DATOS SEMILLA ===")
if errors:
    for err in errors:
        print(f"  [FALLO] {err}")
    print(f"\n{len(errors)} problema(s) encontrados.")
    raise SystemExit(1)
else:
    print("  Todas las verificaciones de integridad pasaron correctamente.")
    print(f"  Organismos: {len(ORGANISMS)} | Expediciones: {len(EXPEDITIONS)} (pasos: {len(EXPEDITION_STEPS)})")
    print(f"  Ecosistemas: {len(ECOSYSTEM_TEMPLATES)} | Desafíos: {len(CHALLENGES)} | Insignias: {len(BADGES)}")
    print(f"  Modelos de célula: {len(CELL_MODELS)} | Sistemas del cuerpo: {len(BODY_SYSTEMS)} | Avatares: {len(AVATAR_KEYS)}")

# ---------------------------------------------------------------------------
# 11. EMISIÓN DE database/schema.sql y database/sample_data.sql
# ---------------------------------------------------------------------------
SCHEMA_SQL = """-- NiñoBiólogo: Exploradores de la Vida — esquema SQLite (Room, versión 1)
-- Generado a partir de las entidades reales en app/src/main/java/.../data/local/entity
-- Motor: SQLite (a través de Room 2.6.1). Ver docs/BASE_DE_DATOS.md para el DER completo.

PRAGMA foreign_keys = ON;

-- ===================== CONTENIDO (semilla, solo lectura para el usuario) =====================

CREATE TABLE biomes (
    id TEXT NOT NULL PRIMARY KEY,
    orderIndex INTEGER NOT NULL,
    name TEXT NOT NULL,
    tagline TEXT NOT NULL,
    description TEXT NOT NULL,
    iconKey TEXT NOT NULL,
    primaryColorHex TEXT NOT NULL,
    secondaryColorHex TEXT NOT NULL
);

CREATE TABLE organisms (
    id TEXT NOT NULL PRIMARY KEY,
    biomeId TEXT NOT NULL,
    name TEXT NOT NULL,
    scientificName TEXT NOT NULL,
    category TEXT NOT NULL,
    habitat TEXT NOT NULL,
    diet TEXT NOT NULL,
    trophicRole TEXT NOT NULL,
    characteristics TEXT NOT NULL,
    funFact TEXT NOT NULL,
    rarity TEXT NOT NULL,
    iconKey TEXT NOT NULL,
    FOREIGN KEY (biomeId) REFERENCES biomes(id) ON DELETE CASCADE
);
CREATE INDEX index_organisms_biomeId ON organisms(biomeId);
CREATE INDEX index_organisms_name ON organisms(name);

CREATE TABLE expeditions (
    id TEXT NOT NULL PRIMARY KEY,
    biomeId TEXT NOT NULL,
    orderIndex INTEGER NOT NULL,
    title TEXT NOT NULL,
    narrative TEXT NOT NULL,
    missionType TEXT NOT NULL,
    difficulty INTEGER NOT NULL,
    relatedOrganismIds TEXT NOT NULL,
    rewardXp INTEGER NOT NULL,
    requiredRank TEXT NOT NULL,
    FOREIGN KEY (biomeId) REFERENCES biomes(id) ON DELETE CASCADE
);
CREATE INDEX index_expeditions_biomeId ON expeditions(biomeId);

CREATE TABLE expedition_steps (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    expeditionId TEXT NOT NULL,
    orderIndex INTEGER NOT NULL,
    prompt TEXT NOT NULL,
    type TEXT NOT NULL,
    hint TEXT NOT NULL,
    FOREIGN KEY (expeditionId) REFERENCES expeditions(id) ON DELETE CASCADE
);
CREATE INDEX index_expedition_steps_expeditionId ON expedition_steps(expeditionId);

CREATE TABLE cell_models (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    cellType TEXT NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE cell_structures (
    id TEXT NOT NULL PRIMARY KEY,
    cellModelId TEXT NOT NULL,
    name TEXT NOT NULL,
    function TEXT NOT NULL,
    xPercent REAL NOT NULL,
    yPercent REAL NOT NULL,
    FOREIGN KEY (cellModelId) REFERENCES cell_models(id) ON DELETE CASCADE
);
CREATE INDEX index_cell_structures_cellModelId ON cell_structures(cellModelId);

CREATE TABLE body_systems (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE body_organs (
    id TEXT NOT NULL PRIMARY KEY,
    bodySystemId TEXT NOT NULL,
    name TEXT NOT NULL,
    function TEXT NOT NULL,
    FOREIGN KEY (bodySystemId) REFERENCES body_systems(id) ON DELETE CASCADE
);
CREATE INDEX index_body_organs_bodySystemId ON body_organs(bodySystemId);

CREATE TABLE ecosystem_templates (
    id TEXT NOT NULL PRIMARY KEY,
    biomeId TEXT NOT NULL,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    availableOrganismIds TEXT NOT NULL,
    idealProducers INTEGER NOT NULL,
    idealHerbivores INTEGER NOT NULL,
    idealCarnivores INTEGER NOT NULL,
    idealDecomposers INTEGER NOT NULL,
    FOREIGN KEY (biomeId) REFERENCES biomes(id) ON DELETE CASCADE
);
CREATE INDEX index_ecosystem_templates_biomeId ON ecosystem_templates(biomeId);

CREATE TABLE challenges (
    id TEXT NOT NULL PRIMARY KEY,
    biomeId TEXT NOT NULL,
    type TEXT NOT NULL,
    title TEXT NOT NULL,
    instructions TEXT NOT NULL,
    relatedOrganismIds TEXT NOT NULL,
    rewardXp INTEGER NOT NULL,
    FOREIGN KEY (biomeId) REFERENCES biomes(id) ON DELETE CASCADE
);
CREATE INDEX index_challenges_biomeId ON challenges(biomeId);

CREATE TABLE badges (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    iconKey TEXT NOT NULL,
    criteriaType TEXT NOT NULL,
    criteriaValue INTEGER NOT NULL,
    biomeId TEXT,
    FOREIGN KEY (biomeId) REFERENCES biomes(id) ON DELETE SET NULL
);
CREATE INDEX index_badges_biomeId ON badges(biomeId);

-- ===================== PROGRESO (datos reales del jugador, mutables) =====================

CREATE TABLE biologist_profile (
    id INTEGER NOT NULL PRIMARY KEY,
    alias TEXT NOT NULL,
    avatarKey TEXT NOT NULL,
    totalXp INTEGER NOT NULL,
    onboardingCompleted INTEGER NOT NULL,
    soundEnabled INTEGER NOT NULL,
    hapticsEnabled INTEGER NOT NULL,
    createdAtEpochMillis INTEGER NOT NULL
);

CREATE TABLE organism_discoveries (
    organismId TEXT NOT NULL PRIMARY KEY,
    discoveredAtEpochMillis INTEGER NOT NULL,
    viaExpeditionId TEXT,
    FOREIGN KEY (organismId) REFERENCES organisms(id) ON DELETE CASCADE
);
CREATE INDEX index_organism_discoveries_organismId ON organism_discoveries(organismId);

CREATE TABLE expedition_progress (
    expeditionId TEXT NOT NULL PRIMARY KEY,
    state TEXT NOT NULL,
    stepsCompleted INTEGER NOT NULL,
    totalSteps INTEGER NOT NULL,
    bestStars INTEGER NOT NULL,
    timesCompleted INTEGER NOT NULL,
    lastAttemptEpochMillis INTEGER,
    FOREIGN KEY (expeditionId) REFERENCES expeditions(id) ON DELETE CASCADE
);
CREATE INDEX index_expedition_progress_expeditionId ON expedition_progress(expeditionId);

CREATE TABLE challenge_attempts (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    challengeId TEXT NOT NULL,
    correctCount INTEGER NOT NULL,
    totalCount INTEGER NOT NULL,
    stars INTEGER NOT NULL,
    xpAwarded INTEGER NOT NULL,
    attemptedAtEpochMillis INTEGER NOT NULL,
    FOREIGN KEY (challengeId) REFERENCES challenges(id) ON DELETE CASCADE
);
CREATE INDEX index_challenge_attempts_challengeId ON challenge_attempts(challengeId);

CREATE TABLE badge_unlocks (
    badgeId TEXT NOT NULL PRIMARY KEY,
    unlockedAtEpochMillis INTEGER NOT NULL,
    FOREIGN KEY (badgeId) REFERENCES badges(id) ON DELETE CASCADE
);
CREATE INDEX index_badge_unlocks_badgeId ON badge_unlocks(badgeId);

CREATE TABLE ecosystem_builds (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    templateId TEXT NOT NULL,
    producers INTEGER NOT NULL,
    herbivores INTEGER NOT NULL,
    carnivores INTEGER NOT NULL,
    decomposers INTEGER NOT NULL,
    balanceScore INTEGER NOT NULL,
    status TEXT NOT NULL,
    savedAtEpochMillis INTEGER NOT NULL,
    FOREIGN KEY (templateId) REFERENCES ecosystem_templates(id) ON DELETE CASCADE
);
CREATE INDEX index_ecosystem_builds_templateId ON ecosystem_builds(templateId);

CREATE TABLE journal_entries (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL,
    title TEXT NOT NULL,
    note TEXT NOT NULL,
    filePath TEXT,
    relatedBiomeId TEXT,
    createdAtEpochMillis INTEGER NOT NULL,
    FOREIGN KEY (relatedBiomeId) REFERENCES biomes(id) ON DELETE SET NULL
);
CREATE INDEX index_journal_entries_relatedBiomeId ON journal_entries(relatedBiomeId);
"""

with open("database/schema.sql", "w", encoding="utf-8") as f:
    f.write(SCHEMA_SQL)
print("Escrito database/schema.sql")

def sql_str(s):
    return "'" + str(s).replace("'", "''") + "'"

def sql_list(items):
    return sql_str("|".join(items))

sample_lines = []
sample_lines.append("-- NiñoBiólogo: Exploradores de la Vida — datos semilla reales (subconjunto representativo)")
sample_lines.append("-- Generado por tools/generate_seed_data.py. El contenido completo se inserta en tiempo de")
sample_lines.append("-- ejecución por DatabaseSeeder.kt a partir de SeedContent.kt (misma fuente de datos).")
sample_lines.append("")
sample_lines.append("-- Biomas (5/5)")
for b in BIOMES:
    sample_lines.append(
        f"INSERT INTO biomes (id, orderIndex, name, tagline, description, iconKey, primaryColorHex, secondaryColorHex) VALUES "
        f"({sql_str(b['id'])}, {b['order']}, {sql_str(b['name'])}, {sql_str(b['tagline'])}, {sql_str(b['desc'])}, {sql_str(b['icon'])}, {sql_str(b['primary'])}, {sql_str(b['secondary'])});"
    )

sample_lines.append("")
sample_lines.append(f"-- Organismos ({len(ORGANISMS)}/{len(ORGANISMS)})")
for o in ORGANISMS:
    sample_lines.append(
        f"INSERT INTO organisms (id, biomeId, name, scientificName, category, habitat, diet, trophicRole, characteristics, funFact, rarity, iconKey) VALUES "
        f"({sql_str(o['id'])}, {sql_str(o['biome'])}, {sql_str(o['name'])}, {sql_str(o['sci'])}, {sql_str(o['cat'])}, {sql_str(o['habitat'])}, {sql_str(o['diet'])}, {sql_str(o['role'])}, {sql_list(o['chars'])}, {sql_str(o['fact'])}, {sql_str(o['rarity'])}, {sql_str(o['icon'])});"
    )

sample_lines.append("")
sample_lines.append(f"-- Expediciones ({len(EXPEDITIONS)}/{len(EXPEDITIONS)})")
for e in EXPEDITIONS:
    sample_lines.append(
        f"INSERT INTO expeditions (id, biomeId, orderIndex, title, narrative, missionType, difficulty, relatedOrganismIds, rewardXp, requiredRank) VALUES "
        f"({sql_str(e['id'])}, {sql_str(e['biome'])}, {e['order']}, {sql_str(e['title'])}, {sql_str(e['narrative'])}, {sql_str(e['mission'])}, {e['difficulty']}, {sql_list(e['related_ids'])}, {e['reward_xp']}, {sql_str(e['required_rank'])});"
    )

sample_lines.append("")
sample_lines.append(f"-- Pasos de expedición ({len(EXPEDITION_STEPS)})")
for s in EXPEDITION_STEPS:
    sample_lines.append(
        f"INSERT INTO expedition_steps (expeditionId, orderIndex, prompt, type, hint) VALUES "
        f"({sql_str(s['expedition_id'])}, {s['order']}, {sql_str(s['prompt'])}, {sql_str(s['type'])}, {sql_str(s['hint'])});"
    )

sample_lines.append("")
sample_lines.append(f"-- Ecosistemas ({len(ECOSYSTEM_TEMPLATES)}/{len(ECOSYSTEM_TEMPLATES)})")
for e in ECOSYSTEM_TEMPLATES:
    sample_lines.append(
        f"INSERT INTO ecosystem_templates (id, biomeId, name, description, availableOrganismIds, idealProducers, idealHerbivores, idealCarnivores, idealDecomposers) VALUES "
        f"({sql_str(e['id'])}, {sql_str(e['biome'])}, {sql_str(e['name'])}, {sql_str(e['desc'])}, {sql_list(e['org_ids'])}, {e['ideal_p']}, {e['ideal_h']}, {e['ideal_c']}, {e['ideal_d']});"
    )

sample_lines.append("")
sample_lines.append(f"-- Desafíos ({len(CHALLENGES)}/{len(CHALLENGES)})")
for c in CHALLENGES:
    sample_lines.append(
        f"INSERT INTO challenges (id, biomeId, type, title, instructions, relatedOrganismIds, rewardXp) VALUES "
        f"({sql_str(c['id'])}, {sql_str(c['biome'])}, {sql_str(c['type'])}, {sql_str(c['title'])}, {sql_str(c['instructions'])}, {sql_list(c['org_ids'])}, {c['reward_xp']});"
    )

sample_lines.append("")
sample_lines.append(f"-- Insignias ({len(BADGES)}/{len(BADGES)})")
for b in BADGES:
    biome_val = sql_str(b['biome']) if b['biome'] else "NULL"
    sample_lines.append(
        f"INSERT INTO badges (id, name, description, iconKey, criteriaType, criteriaValue, biomeId) VALUES "
        f"({sql_str(b['id'])}, {sql_str(b['name'])}, {sql_str(b['desc'])}, {sql_str(b['icon'])}, {sql_str(b['ctype'])}, {b['cvalue']}, {biome_val});"
    )

sample_lines.append("")
sample_lines.append(f"-- Modelos de célula ({len(CELL_MODELS)}) y estructuras ({sum(len(c['structures']) for c in CELL_MODELS)})")
for c in CELL_MODELS:
    sample_lines.append(f"INSERT INTO cell_models (id, name, cellType, description) VALUES ({sql_str(c['id'])}, {sql_str(c['name'])}, {sql_str(c['ctype'])}, {sql_str(c['desc'])});")
for c in CELL_MODELS:
    for st in c["structures"]:
        sample_lines.append(
            f"INSERT INTO cell_structures (id, cellModelId, name, function, xPercent, yPercent) VALUES "
            f"({sql_str(st['id'])}, {sql_str(c['id'])}, {sql_str(st['name'])}, {sql_str(st['fn'])}, {st['x']}, {st['y']});"
        )

sample_lines.append("")
sample_lines.append(f"-- Sistemas del cuerpo ({len(BODY_SYSTEMS)}) y órganos ({sum(len(s['organs']) for s in BODY_SYSTEMS)})")
for s in BODY_SYSTEMS:
    sample_lines.append(f"INSERT INTO body_systems (id, name, description) VALUES ({sql_str(s['id'])}, {sql_str(s['name'])}, {sql_str(s['desc'])});")
for s in BODY_SYSTEMS:
    for idx, (oname, ofn) in enumerate(s["organs"]):
        organ_id = f"{s['id']}_organ_{idx+1}"
        sample_lines.append(f"INSERT INTO body_organs (id, bodySystemId, name, function) VALUES ({sql_str(organ_id)}, {sql_str(s['id'])}, {sql_str(oname)}, {sql_str(ofn)});")

sample_lines.append("")
sample_lines.append("-- Perfil inicial (se crea automáticamente en el primer arranque)")
sample_lines.append(
    "INSERT INTO biologist_profile (id, alias, avatarKey, totalXp, onboardingCompleted, soundEnabled, hapticsEnabled, createdAtEpochMillis) VALUES "
    "(1, 'Joven Biólogo', 'avatar_explorador_1', 0, 0, 1, 1, 0);"
)

with open("database/sample_data.sql", "w", encoding="utf-8") as f:
    f.write("\n".join(sample_lines) + "\n")

total_inserts = sum(1 for l in sample_lines if l.startswith("INSERT"))
print(f"Escrito database/sample_data.sql ({total_inserts} INSERT reales)")
