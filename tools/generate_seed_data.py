#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Genera el contenido semilla real de NiñoBiólogo: Vida en Miniatura.

Produce app/src/main/java/com/educalab/ninobiologo/data/local/seed/SeedContent.kt a partir de
estructuras de datos en Python, para que el contenido (biología real, en español, adaptada a
niños de 8-12 años) sea fácil de revisar y mantener sin miles de líneas hardcodeadas dentro de
un ViewModel.

Cantidades objetivo (prompt "Vida en Miniatura", sección CONTENIDO INICIAL):
  - 5 ambientes microscópicos
  - 20 muestras científicas
  - 30 descubrimientos de microscopio
  - 10 experimentos biológicos
  - 15 elementos coleccionables

Uso: python3 tools/generate_seed_data.py
"""

OUT_PATH = "app/src/main/java/com/educalab/ninobiologo/data/local/seed/SeedContent.kt"

# ---------------------------------------------------------------------------
# 1. AMBIENTES MICROSCÓPICOS (5 zonas del Laboratorio Vivo)
# ---------------------------------------------------------------------------
ENVIRONMENTS = [
    dict(id="micromundo", order=1, name="Micromundo", tagline="Un universo invisible en cada gota",
         desc="Analiza muestras de agua y descubre la vida microscópica que flota en ellas.",
         icon="env_micromundo", primary="#5B8C5A", secondary="#DCEFDC"),
    dict(id="bosque_de_vida", order=2, name="Bosque de Vida", tagline="Naturaleza para observar de cerca",
         desc="Recoge hojas, cortezas y huellas del bosque para examinarlas en el laboratorio.",
         icon="env_bosque", primary="#2E7D32", secondary="#E3F2E1"),
    dict(id="oceano_profundo", order=3, name="Océano Profundo", tagline="Muestras traídas desde el mar",
         desc="Examina muestras de agua salada, arrecifes y restos marinos en tu laboratorio.",
         icon="env_oceano", primary="#0277BD", secondary="#E1F5FE"),
    dict(id="cuerpo_humano", order=4, name="Cuerpo Humano", tagline="Un laboratorio viviente",
         desc="Observa muestras de células y tejidos para entender cómo funciona tu cuerpo.",
         icon="env_cuerpo", primary="#C62828", secondary="#FDE8E8"),
    dict(id="ecosistemas", order=5, name="Ecosistemas", tagline="Todo está conectado",
         desc="Investiga muestras de tierra y agua para descubrir cómo se relacionan los seres vivos.",
         icon="env_ecosistemas", primary="#EF6C00", secondary="#FFF3E0"),
]

# ---------------------------------------------------------------------------
# 2. DESCUBRIMIENTOS (30 en total) — lo que el microscopio revela dentro de una muestra
# category: PLANTA/ANIMAL/MICROORGANISMO/HONGO ; rarity: COMUN/POCO_COMUN/RARO/LEGENDARIO
# ---------------------------------------------------------------------------
DISCOVERIES = []

def disc(id, environment, name, sci, cat, habitat, diet, chars, curiosity, rarity, icon):
    DISCOVERIES.append(dict(id=id, environment=environment, name=name, sci=sci, cat=cat,
                             habitat=habitat, diet=diet, chars=chars, curiosity=curiosity,
                             rarity=rarity, icon=icon))

# --- Micromundo (7) ---
disc("disc_paramecio", "micromundo", "Paramecio", "Paramecium caudatum", "MICROORGANISMO",
     "Agua dulce estancada", "Bacterias y algas microscópicas",
     ["forma de zapatilla", "se mueve con cilios", "unicelular"],
     "Puede expulsar el agua que le sobra con una estructura llamada vacuola contráctil.",
     "COMUN", "disc_paramecio")
disc("disc_ameba", "micromundo", "Ameba", "Amoeba proteus", "MICROORGANISMO",
     "Estanques y suelos húmedos", "Bacterias y otros microorganismos",
     ["cambia de forma constantemente", "se mueve con seudópodos", "unicelular"],
     "No tiene una forma fija: la cambia para moverse y para atrapar su alimento.",
     "COMUN", "disc_ameba")
disc("disc_euglena", "micromundo", "Euglena", "Euglena viridis", "MICROORGANISMO",
     "Agua dulce con luz", "Fotosíntesis y también absorbe nutrientes",
     ["tiene un flagelo", "un ojo sensible a la luz", "verde por clorofila"],
     "Es mitad planta, mitad animal: hace fotosíntesis pero también nada activamente.",
     "POCO_COMUN", "disc_euglena")
disc("disc_levadura", "micromundo", "Levadura", "Saccharomyces cerevisiae", "HONGO",
     "Frutas maduras y masas de pan", "Azúcares",
     ["unicelular", "se reproduce por gemación", "produce burbujas de gas"],
     "Es la responsable de que el pan suba y de que crezca la espuma en algunas bebidas.",
     "COMUN", "disc_levadura")
disc("disc_tardigrado", "micromundo", "Tardígrado", "Milnesium tardigradum", "ANIMAL",
     "Musgos húmedos", "Líquidos de plantas y microorganismos",
     ["ocho patas diminutas", "resiste el espacio exterior", "se llama 'oso de agua'"],
     "Puede sobrevivir congelado, deshidratado e incluso en el vacío del espacio.",
     "RARO", "disc_tardigrado")
disc("disc_diatomea", "micromundo", "Diatomea", "Bacillariophyta", "MICROORGANISMO",
     "Océanos y lagos", "Fotosíntesis",
     ["caparazón de vidrio (sílice)", "produce gran parte del oxígeno del planeta", "forma parte del plancton"],
     "Las diatomeas producen alrededor de una quinta parte del oxígeno que respiramos.",
     "POCO_COMUN", "disc_diatomea")
disc("disc_volvox", "micromundo", "Volvox", "Volvox globator", "MICROORGANISMO",
     "Estanques y charcas", "Fotosíntesis",
     ["forma colonias esféricas", "miles de células trabajando juntas", "gira al nadar"],
     "Aunque es diminuto, el Volvox forma colonias que giran juntas como una pequeña esfera verde.",
     "RARO", "disc_volvox")

# --- Bosque de Vida (7) ---
disc("disc_helecho", "bosque_de_vida", "Helecho gigante", "Polypodiopsida", "PLANTA",
     "Bosque húmedo", "Fotosíntesis",
     ["hojas grandes y divididas", "se reproduce por esporas", "no tiene flores"],
     "Los helechos existían en la Tierra mucho antes que los dinosaurios.",
     "COMUN", "disc_helecho")
disc("disc_seta_amanita", "bosque_de_vida", "Seta roja", "Amanita muscaria", "HONGO",
     "Bosques de coníferas", "Materia orgánica en descomposición",
     ["sombrero rojo con puntos blancos", "no debe tocarse ni comerse", "ayuda a reciclar nutrientes"],
     "Los hongos no son plantas: forman su propio reino y reciclan la materia del bosque.",
     "POCO_COMUN", "disc_seta")
disc("disc_conejo", "bosque_de_vida", "Conejo silvestre", "Oryctolagus cuniculus", "ANIMAL",
     "Praderas y bosques", "Pasto y hierbas",
     ["orejas largas", "excelente oído", "se reproduce rápidamente"],
     "Un conejo puede girar sus orejas de forma independiente para escuchar en dos direcciones.",
     "COMUN", "disc_conejo")
disc("disc_zorro", "bosque_de_vida", "Zorro rojo", "Vulpes vulpes", "ANIMAL",
     "Bosques y zonas mixtas", "Roedores, conejos e insectos",
     ["cola espesa", "muy adaptable", "caza principalmente de noche"],
     "El zorro puede detectar el campo magnético de la Tierra para calcular sus saltos al cazar.",
     "POCO_COMUN", "disc_zorro")
disc("disc_mariposa", "bosque_de_vida", "Mariposa monarca", "Danaus plexippus", "ANIMAL",
     "Praderas y bosques", "Néctar de flores",
     ["migra miles de kilómetros", "colores naranja y negro", "se transforma desde oruga"],
     "Puede migrar más de 4.000 kilómetros usando el sol como brújula.",
     "RARO", "disc_mariposa")
disc("disc_buho", "bosque_de_vida", "Búho real", "Bubo bubo", "ANIMAL",
     "Bosques y acantilados", "Roedores y aves pequeñas",
     ["vuelo silencioso", "gira la cabeza casi 270°", "vista nocturna excelente"],
     "Sus plumas especiales le permiten volar sin hacer casi ningún ruido.",
     "POCO_COMUN", "disc_buho")
disc("disc_lince", "bosque_de_vida", "Lince ibérico", "Lynx pardinus", "ANIMAL",
     "Bosque mediterráneo", "Conejos principalmente",
     ["orejas con mechones de pelo", "vista y oído extraordinarios", "especie protegida"],
     "Es uno de los felinos más amenazados del mundo, y gracias a la protección va recuperándose.",
     "LEGENDARIO", "disc_lince")

# --- Océano Profundo (7) ---
disc("disc_alga_marina", "oceano_profundo", "Alga parda gigante", "Macrocystis pyrifera", "PLANTA",
     "Bosques submarinos", "Fotosíntesis",
     ["puede crecer más de 30 metros", "forma auténticos bosques bajo el agua", "crece muy rápido"],
     "Puede crecer hasta 50 centímetros en un solo día bajo condiciones ideales.",
     "COMUN", "disc_alga")
disc("disc_coral", "oceano_profundo", "Coral cerebro", "Diploria labyrinthiformis", "ANIMAL",
     "Arrecifes tropicales", "Plancton y luz solar (con algas asociadas)",
     ["forma colonias de miles de individuos", "construye el esqueleto del arrecife", "vive en simbiosis con algas"],
     "Un arrecife de coral puede albergar más especies distintas que cualquier otro ecosistema marino.",
     "POCO_COMUN", "disc_coral")
disc("disc_medusa", "oceano_profundo", "Medusa luna", "Aurelia aurita", "ANIMAL",
     "Aguas costeras", "Plancton",
     ["cuerpo casi transparente", "se mueve con pulsaciones", "no tiene cerebro ni corazón"],
     "Está formada en un 95% por agua y existe desde antes que los dinosaurios.",
     "POCO_COMUN", "disc_medusa")
disc("disc_pulpo", "oceano_profundo", "Pulpo común", "Octopus vulgaris", "ANIMAL",
     "Fondos rocosos", "Cangrejos y moluscos",
     ["ocho brazos con ventosas", "cambia de color y textura", "extremadamente inteligente"],
     "Tiene tres corazones y su sangre es de color azul.",
     "RARO", "disc_pulpo")
disc("disc_estrella_mar", "oceano_profundo", "Estrella de mar", "Asteroidea", "ANIMAL",
     "Fondos marinos", "Moluscos y materia orgánica",
     ["puede regenerar brazos perdidos", "no tiene cerebro", "se mueve con cientos de piececillos"],
     "Si pierde un brazo, puede regenerarlo por completo con el paso del tiempo.",
     "POCO_COMUN", "disc_estrella")
disc("disc_pez_payaso", "oceano_profundo", "Pez payaso", "Amphiprioninae", "ANIMAL",
     "Arrecifes de coral", "Algas y pequeños invertebrados",
     ["vive protegido entre las anémonas", "es inmune a su veneno", "cambia de sexo durante su vida"],
     "Vive en una relación de ayuda mutua con la anémona: ella lo protege y él la limpia.",
     "POCO_COMUN", "disc_pez_payaso")
disc("disc_ballena_azul", "oceano_profundo", "Ballena azul", "Balaenoptera musculus", "ANIMAL",
     "Océano abierto", "Kril (pequeños crustáceos)",
     ["el animal más grande del planeta", "corazón del tamaño de un auto pequeño", "canta bajo el agua"],
     "A pesar de ser gigantesca, se alimenta de uno de los animales más pequeños del mar: el kril.",
     "LEGENDARIO", "disc_ballena")

# --- Cuerpo Humano (5) ---
disc("disc_globulo_rojo", "cuerpo_humano", "Glóbulo rojo", "Erythrocyte", "MICROORGANISMO",
     "Torrente sanguíneo", "No se alimenta: transporta oxígeno",
     ["forma de disco", "transporta oxígeno por el cuerpo", "no tiene núcleo"],
     "Tu cuerpo produce alrededor de dos millones de glóbulos rojos nuevos cada segundo.",
     "COMUN", "disc_globulo_rojo")
disc("disc_globulo_blanco", "cuerpo_humano", "Glóbulo blanco", "Leukocyte", "MICROORGANISMO",
     "Torrente sanguíneo", "Bacterias y microbios invasores",
     ["defiende al cuerpo de infecciones", "puede salir de los vasos sanguíneos", "existen varios tipos"],
     "Son los soldados del sistema inmunitario: patrullan el cuerpo buscando invasores.",
     "POCO_COMUN", "disc_globulo_blanco")
disc("disc_neurona", "cuerpo_humano", "Neurona", "Neuron", "MICROORGANISMO",
     "Cerebro y sistema nervioso", "Glucosa y oxígeno",
     ["se comunica con impulsos eléctricos", "tiene ramificaciones llamadas dendritas", "forma redes complejísimas"],
     "El cerebro humano tiene unos 86.000 millones de neuronas conectadas entre sí.",
     "RARO", "disc_neurona")
disc("disc_celula_muscular", "cuerpo_humano", "Célula muscular", "Myocyte", "MICROORGANISMO",
     "Músculos del cuerpo", "Glucosa y oxígeno",
     ["se contrae para generar movimiento", "puede alargarse mucho", "trabaja en equipo con otras fibras"],
     "Algunas células musculares pueden medir varios centímetros de largo, muy alargadas.",
     "POCO_COMUN", "disc_celula_muscular")
disc("disc_plaqueta", "cuerpo_humano", "Plaqueta", "Thrombocyte", "MICROORGANISMO",
     "Torrente sanguíneo", "No se alimenta: repara heridas",
     ["ayuda a cerrar heridas", "es más pequeña que un glóbulo rojo", "forma coágulos"],
     "Cuando te cortas, miles de plaquetas corren al lugar para taponar la herida en minutos.",
     "COMUN", "disc_plaqueta")

# --- Ecosistemas (4) ---
disc("disc_pasto", "ecosistemas", "Pasto de sabana", "Poaceae", "PLANTA",
     "Sabana africana", "Fotosíntesis",
     ["crece rápido tras la lluvia", "base de la cadena en la sabana", "resiste el pisoteo"],
     "La sabana puede sostener manadas enteras de herbívoros gracias a su rápido crecimiento.",
     "COMUN", "disc_pasto")
disc("disc_leon", "ecosistemas", "León", "Panthera leo", "ANIMAL",
     "Sabana africana", "Cebras, ñus y otros herbívoros",
     ["vive en manadas llamadas orgullos", "las hembras cazan en equipo", "es el depredador tope de la sabana"],
     "Las leonas hacen la mayoría de las cacerías, a menudo trabajando en equipo.",
     "POCO_COMUN", "disc_leon")
disc("disc_nenufar", "ecosistemas", "Nenúfar", "Nymphaea", "PLANTA",
     "Estanque de agua dulce", "Fotosíntesis",
     ["hojas flotantes", "raíces ancladas al fondo", "flores que se abren de día"],
     "Sus hojas flotantes son tan resistentes que pueden sostener el peso de una pequeña rana.",
     "COMUN", "disc_nenufar")
disc("disc_hongo_reciclador", "ecosistemas", "Hongo reciclador de estanque", "Saprolegnia sp.", "HONGO",
     "Estanque de agua dulce", "Materia orgánica en descomposición",
     ["descompone restos de plantas y animales", "libera nutrientes de vuelta al agua", "invisible a simple vista en su mayoría"],
     "Sin descomponedores como este hongo, los nutrientes nunca volverían a estar disponibles.",
     "POCO_COMUN", "disc_hongo_reciclador")

assert len(DISCOVERIES) == 30, f"Se esperaban 30 descubrimientos, hay {len(DISCOVERIES)}"
print(f"Descubrimientos: {len(DISCOVERIES)}")

DISC_BY_ID = {d["id"]: d for d in DISCOVERIES}

# ---------------------------------------------------------------------------
# 3. MUESTRAS CIENTÍFICAS (20 total, 4 por ambiente) — agrupan descubrimientos
# ---------------------------------------------------------------------------
SAMPLE_GROUPS = {
    "micromundo": [
        ("Gota de agua de charca", "Recogida en una charca del jardín", ["disc_paramecio", "disc_ameba"]),
        ("Agua con luz de sol", "Tomada de un estanque bien iluminado", ["disc_euglena", "disc_diatomea"]),
        ("Trozo de masa de pan", "Guardada en un frasco durante dos días", ["disc_levadura"]),
        ("Musgo húmedo", "Recogido junto a una roca húmeda", ["disc_tardigrado", "disc_volvox"]),
    ],
    "bosque_de_vida": [
        ("Hojarasca del suelo", "Recogida bajo un helecho del bosque", ["disc_helecho", "disc_seta_amanita"]),
        ("Huellas junto al arroyo", "Encontradas cerca de un arroyo del bosque", ["disc_conejo", "disc_zorro"]),
        ("Ala caída", "Encontrada sobre una hoja del sendero", ["disc_mariposa"]),
        ("Plumas y pelos nocturnos", "Recogidos en un claro del bosque de noche", ["disc_buho", "disc_lince"]),
    ],
    "oceano_profundo": [
        ("Agua de un charco de marea", "Tomada de un charco dejado por la marea baja", ["disc_medusa", "disc_estrella_mar"]),
        ("Trozo de arrecife", "Recogido cerca de un arrecife de coral", ["disc_coral", "disc_alga_marina"]),
        ("Rastro de tinta", "Observado tras un encuentro con un pulpo", ["disc_pulpo", "disc_pez_payaso"]),
        ("Grabación submarina", "Un hidrófono capturó este sonido en altamar", ["disc_ballena_azul"]),
    ],
    "cuerpo_humano": [
        ("Gota de sangre", "Tomada con una lanceta segura de laboratorio", ["disc_globulo_rojo", "disc_globulo_blanco"]),
        ("Muestra de tejido nervioso", "Modelo de laboratorio del sistema nervioso", ["disc_neurona"]),
        ("Fibra muscular", "Modelo de laboratorio de tejido muscular", ["disc_celula_muscular"]),
        ("Muestra de una herida en curación", "Modelo de laboratorio de coagulación", ["disc_plaqueta"]),
    ],
    "ecosistemas": [
        ("Muestra de pasto", "Recogida en la sabana tras la lluvia", ["disc_pasto"]),
        ("Pelo junto a un rastro", "Encontrado junto a huellas de un gran felino", ["disc_leon"]),
        ("Hoja flotante", "Tomada de la superficie de un estanque", ["disc_nenufar"]),
        ("Madera en descomposición", "Recogida del suelo húmedo del bosque", ["disc_hongo_reciclador"]),
    ],
}

SAMPLES = []
for env_id, groups in SAMPLE_GROUPS.items():
    for i, (name, origin, disc_ids) in enumerate(groups):
        sample_id = f"sample_{env_id}_{i+1:02d}"
        for did in disc_ids:
            DISC_BY_ID[did]["sample"] = sample_id
        SAMPLES.append(dict(
            id=sample_id, environment=env_id, order=i + 1, name=name, origin=origin,
            difficulty=1 if i < 2 else 2, icon=sample_id, discovery_ids=disc_ids
        ))

assert len(SAMPLES) == 20, f"Se esperaban 20 muestras, hay {len(SAMPLES)}"
assert all("sample" in d for d in DISCOVERIES), "Hay descubrimientos sin muestra asignada"
print(f"Muestras: {len(SAMPLES)}")

ENV_BY_ID = {e["id"]: e for e in ENVIRONMENTS}
DISC_BY_ENV = {}
for d in DISCOVERIES:
    DISC_BY_ENV.setdefault(d["environment"], []).append(d)

# ---------------------------------------------------------------------------
# 4. EXPERIMENTOS BIOLÓGICOS (10 total, 2 por ambiente)
# ---------------------------------------------------------------------------
EXPERIMENTS_RAW = {
    "micromundo": [
        ("¿Qué pasa si el agua de la charca se calienta demasiado?",
         "Ajusta la temperatura del agua y observa cómo reaccionan los microorganismos.",
         "Temperatura del agua", "°C", 0, 40, 15, 25),
        ("¿Qué pasa si la Euglena no recibe suficiente luz?",
         "Ajusta las horas de luz solar que recibe el agua del estanque.",
         "Horas de luz solar", "horas", 0, 12, 6, 10),
    ],
    "bosque_de_vida": [
        ("¿Qué pasa si una planta recibe menos luz?",
         "Ajusta las horas de luz que recibe una planta del bosque cada día.",
         "Horas de luz al día", "horas", 0, 12, 6, 10),
        ("¿Qué pasa si llueve muy poco en el bosque?",
         "Ajusta la cantidad de lluvia semanal que recibe el bosque.",
         "Litros de lluvia por semana", "litros", 0, 50, 20, 35),
    ],
    "oceano_profundo": [
        ("¿Qué pasa si el agua del mar se contamina?",
         "Ajusta el nivel de contaminación del agua alrededor del arrecife.",
         "Nivel de contaminación", "nivel", 0, 10, 0, 3),
        ("¿Qué pasa si baja la temperatura del océano?",
         "Ajusta la temperatura del agua donde vive el arrecife.",
         "Temperatura del agua", "°C", 0, 30, 10, 20),
    ],
    "cuerpo_humano": [
        ("¿Qué pasa si el cuerpo no recibe suficiente agua?",
         "Ajusta cuántos vasos de agua toma el cuerpo al día.",
         "Vasos de agua al día", "vasos", 0, 12, 6, 8),
        ("¿Qué pasa si el cuerpo no descansa lo suficiente?",
         "Ajusta cuántas horas duerme el cuerpo cada noche.",
         "Horas de sueño", "horas", 0, 12, 8, 10),
    ],
    "ecosistemas": [
        ("¿Qué pasa si desaparecen los descomponedores?",
         "Ajusta cuántos descomponedores hay en el ecosistema.",
         "Cantidad de descomponedores", "individuos", 0, 10, 3, 6),
        ("¿Qué pasa si hay demasiados depredadores?",
         "Ajusta cuántos depredadores hay en el ecosistema.",
         "Cantidad de depredadores", "individuos", 0, 10, 1, 4),
    ],
}

EXPERIMENTS = []
for env_id, items in EXPERIMENTS_RAW.items():
    for i, (question, desc, var_name, var_unit, vmin, vmax, imin, imax) in enumerate(items):
        EXPERIMENTS.append(dict(
            id=f"exp_{env_id}_{i+1:02d}", environment=env_id, order=i + 1,
            question=question, desc=desc, var_name=var_name, var_unit=var_unit,
            var_min=vmin, var_max=vmax, ideal_min=imin, ideal_max=imax, reward_xp=30
        ))
assert len(EXPERIMENTS) == 10, f"Se esperaban 10 experimentos, hay {len(EXPERIMENTS)}"
print(f"Experimentos: {len(EXPERIMENTS)}")

# ---------------------------------------------------------------------------
# 5. CONSTRUCTOR BIOLÓGICO — catálogo de piezas para crear criaturas (16 piezas)
# ---------------------------------------------------------------------------
CREATURE_PART_OPTIONS = [
    dict(id="forma_redonda", cat="FORMA", name="Forma redonda", desc="Cuerpo esférico que resiste bien la presión del agua.", best="micromundo"),
    dict(id="forma_alargada", cat="FORMA", name="Forma alargada", desc="Cuerpo estilizado, ideal para nadar largas distancias.", best="oceano_profundo"),
    dict(id="forma_ramificada", cat="FORMA", name="Forma ramificada", desc="Se extiende en ramas finas para explorar el terreno.", best="bosque_de_vida"),
    dict(id="forma_plana", cat="FORMA", name="Forma plana", desc="Cuerpo en forma de disco, fácil de transportar por el flujo.", best="cuerpo_humano"),

    dict(id="mov_cilios", cat="MOVIMIENTO", name="Cilios", desc="Pequeños pelos que baten el agua para desplazarse.", best="micromundo"),
    dict(id="mov_aletas", cat="MOVIMIENTO", name="Aletas", desc="Permiten nadar con fuerza en aguas abiertas.", best="oceano_profundo"),
    dict(id="mov_esporas_viento", cat="MOVIMIENTO", name="Esporas al viento", desc="Se dispersan llevadas por el viento del bosque.", best="bosque_de_vida"),
    dict(id="mov_fija", cat="MOVIMIENTO", name="Fija al sustrato", desc="Permanece en un lugar reciclando lo que la rodea.", best="ecosistemas"),

    dict(id="alim_fotosintesis", cat="ALIMENTACION", name="Fotosíntesis", desc="Produce su propio alimento a partir de la luz.", best="bosque_de_vida"),
    dict(id="alim_filtra_particulas", cat="ALIMENTACION", name="Filtra partículas", desc="Cuela el agua para atrapar partículas de alimento.", best="oceano_profundo"),
    dict(id="alim_absorbe_nutrientes", cat="ALIMENTACION", name="Absorbe nutrientes", desc="Toma nutrientes directamente del medio que la rodea.", best="cuerpo_humano"),
    dict(id="alim_descompone", cat="ALIMENTACION", name="Descompone materia", desc="Recicla materia orgánica en descomposición.", best="ecosistemas"),

    dict(id="adapt_resiste_sequia", cat="ADAPTACION", name="Resiste la sequía", desc="Puede sobrevivir largos periodos sin agua.", best="ecosistemas"),
    dict(id="adapt_resiste_frio", cat="ADAPTACION", name="Resiste el frío extremo", desc="Tolera temperaturas muy bajas sin dañarse.", best="oceano_profundo"),
    dict(id="adapt_produce_toxinas", cat="ADAPTACION", name="Produce toxinas defensivas", desc="Se protege de depredadores con sustancias propias.", best="bosque_de_vida"),
    dict(id="adapt_repara_rapido", cat="ADAPTACION", name="Se repara rápido", desc="Puede regenerarse o repararse en poco tiempo.", best="cuerpo_humano"),
]
assert len(CREATURE_PART_OPTIONS) == 16, f"Se esperaban 16 piezas de criatura, hay {len(CREATURE_PART_OPTIONS)}"
print(f"Piezas del Constructor Biológico: {len(CREATURE_PART_OPTIONS)}")

# ---------------------------------------------------------------------------
# 6. ANALIZADOR — tareas de comparar/clasificar (30 total, 6 por ambiente)
# ---------------------------------------------------------------------------
# Cada tarea del Analizador declara qué pregunta de verdad, para que la consigna del desafío
# y la pregunta que ve el niño coincidan.
ANALYSIS_TASKS = [
    ("CATEGORIA", "Clasifica los seres vivos de {env}", "Agrupa cada hallazgo en su reino: planta, animal, microorganismo u hongo."),
    ("HABITAT", "¿Dónde vive cada uno? {env}", "Relaciona cada ser vivo con el lugar donde habita."),
    ("DIETA", "¿De qué se alimentan? {env}", "Averigua qué come cada uno de los seres vivos que descubriste."),
    ("RAREZA", "Rarezas de {env}", "Decide qué tan difícil es encontrar cada hallazgo."),
    ("MIXTO", "Repaso mixto de {env}", "Preguntas variadas sobre todo lo que descubriste en la zona."),
    ("MIXTO", "Desafío final de {env}", "El reto completo: grupo, hábitat, alimentación y rareza."),
]
CHALLENGES = []
for env_id in ENV_BY_ID:
    env_name = ENV_BY_ID[env_id]["name"]
    env_discoveries = DISC_BY_ENV.get(env_id, [])
    disc_ids = [d["id"] for d in env_discoveries][:6] or [d["id"] for d in env_discoveries]
    for i, (atype, template, instructions) in enumerate(ANALYSIS_TASKS):
        title = template.format(env=env_name)
        CHALLENGES.append(dict(
            id=f"cha_{env_id}_{i+1:02d}", environment=env_id, type=atype, title=title,
            instructions=instructions, disc_ids=disc_ids, reward_xp=15 + (i * 3)
        ))
assert len(CHALLENGES) == 30, f"Se esperaban 30 tareas de análisis, hay {len(CHALLENGES)}"
print(f"Tareas del Analizador: {len(CHALLENGES)}")

# ---------------------------------------------------------------------------
# 7. COLECCIONABLES DE "MI MUSEO DE LA VIDA" (15)
# ---------------------------------------------------------------------------
LAB_COLLECTIBLES = [
    dict(id="col_primer_descubrimiento", name="Primer Descubrimiento", desc="Descubriste tu primer hallazgo en el microscopio.", icon="col_primer_descubrimiento", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=1, env=None),
    dict(id="col_coleccionista_10", name="Coleccionista Curioso", desc="Descubriste 10 hallazgos distintos.", icon="col_coleccionista_10", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=10, env=None),
    dict(id="col_coleccionista_20", name="Coleccionista Experto", desc="Descubriste 20 hallazgos distintos.", icon="col_coleccionista_20", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=20, env=None),
    dict(id="col_museo_completo", name="Museo Completo", desc="Descubriste los 30 hallazgos del laboratorio.", icon="col_museo_completo", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=30, env=None),
    dict(id="col_primer_experimento", name="Primer Experimento", desc="Completaste tu primer experimento biológico.", icon="col_primer_experimento", ctype="EXPERIMENTOS_REALIZADOS", cvalue=1, env=None),
    dict(id="col_cientifico_experimentado", name="Científico Experimentado", desc="Completaste 10 experimentos biológicos.", icon="col_cientifico_experimentado", ctype="EXPERIMENTOS_REALIZADOS", cvalue=10, env=None),
    dict(id="col_primera_criatura", name="Primera Criatura", desc="Creaste tu primera criatura microscópica.", icon="col_primera_criatura", ctype="CRIATURAS_CREADAS", cvalue=1, env=None),
    dict(id="col_creador_criaturas", name="Creador de Criaturas", desc="Creaste 5 criaturas distintas.", icon="col_creador_criaturas", ctype="CRIATURAS_CREADAS", cvalue=5, env=None),
    dict(id="col_analista_10", name="Analista Aplicado", desc="Superaste 10 tareas del Analizador.", icon="col_analista_10", ctype="ANALISIS_SUPERADOS", cvalue=10, env=None),
    dict(id="col_analista_20", name="Analista Experto", desc="Superaste 20 tareas del Analizador.", icon="col_analista_20", ctype="ANALISIS_SUPERADOS", cvalue=20, env=None),
    dict(id="col_ambiente_micromundo", name="Maestro del Micromundo", desc="Completaste toda la colección del Micromundo.", icon="col_ambiente_micromundo", ctype="AMBIENTE_COMPLETO", cvalue=100, env="micromundo"),
    dict(id="col_ambiente_bosque", name="Guardián del Bosque", desc="Completaste toda la colección del Bosque de Vida.", icon="col_ambiente_bosque", ctype="AMBIENTE_COMPLETO", cvalue=100, env="bosque_de_vida"),
    dict(id="col_ambiente_oceano", name="Explorador del Océano", desc="Completaste toda la colección del Océano Profundo.", icon="col_ambiente_oceano", ctype="AMBIENTE_COMPLETO", cvalue=100, env="oceano_profundo"),
    dict(id="col_ambiente_cuerpo", name="Conocedor del Cuerpo", desc="Completaste toda la colección del Cuerpo Humano.", icon="col_ambiente_cuerpo", ctype="AMBIENTE_COMPLETO", cvalue=100, env="cuerpo_humano"),
    dict(id="col_legendario", name="Cazador de Leyendas", desc="Descubriste un hallazgo legendario.", icon="col_legendario", ctype="RAREZA_LEGENDARIA", cvalue=1, env=None),
]
assert len(LAB_COLLECTIBLES) == 15, f"Se esperaban 15 coleccionables, hay {len(LAB_COLLECTIBLES)}"
print(f"Coleccionables: {len(LAB_COLLECTIBLES)}")

# ---------------------------------------------------------------------------
# 8. MEJORAS DEL LABORATORIO (8) — progresión basada en herramientas/zonas, no solo insignias
# ---------------------------------------------------------------------------
LABORATORY_UPGRADES = [
    dict(id="up_analizador", name="Analizador de Muestras", desc="Desbloquea la herramienta Analizador en tu laboratorio.", icon="up_analizador", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=1, env=None),
    dict(id="up_diario_ampliado", name="Diario Ampliado", desc="Tu Diario Científico gana más espacio para fotos y notas.", icon="up_diario_ampliado", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=5, env=None),
    dict(id="up_microscopio_avanzado", name="Microscopio Avanzado", desc="Tu microscopio revela detalles más finos en cada muestra.", icon="up_microscopio_avanzado", ctype="EXPERIMENTOS_REALIZADOS", cvalue=3, env=None),
    dict(id="up_constructor_biologico", name="Constructor Biológico", desc="Desbloquea la mesa de creación de criaturas microscópicas.", icon="up_constructor_biologico", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=8, env=None),
    dict(id="up_zona_oceano", name="Zona: Océano Profundo", desc="Se habilita la exploración del Océano Profundo.", icon="up_zona_oceano", ctype="AMBIENTE_COMPLETO", cvalue=50, env="micromundo"),
    dict(id="up_zona_cuerpo", name="Zona: Cuerpo Humano", desc="Se habilita la exploración del Cuerpo Humano.", icon="up_zona_cuerpo", ctype="AMBIENTE_COMPLETO", cvalue=50, env="bosque_de_vida"),
    dict(id="up_acuario_decorativo", name="Acuario del Laboratorio", desc="Una decoración nueva para tu laboratorio personal.", icon="up_acuario_decorativo", ctype="DESCUBRIMIENTOS_TOTALES", cvalue=15, env=None),
    dict(id="up_rincon_investigador", name="Rincón del Investigador", desc="Tu laboratorio gana un rincón especial de investigador.", icon="up_rincon_investigador", ctype="RAREZA_LEGENDARIA", cvalue=1, env=None),
]
assert len(LABORATORY_UPGRADES) == 8, f"Se esperaban 8 mejoras de laboratorio, hay {len(LABORATORY_UPGRADES)}"
print(f"Mejoras del laboratorio: {len(LABORATORY_UPGRADES)}")

# ---------------------------------------------------------------------------
# 9. MODELOS DE CÉLULA (microscopio virtual y Viaje al Interior de la Célula)
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
# 10. SISTEMAS DEL CUERPO HUMANO
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
# 11. EMISIÓN DE SeedContent.kt
# ---------------------------------------------------------------------------
def kstr(s):
    return '"' + s.replace("\\", "\\\\").replace('"', '\\"').replace("$", "\\$") + '"'

def klist_str(items):
    return "listOf(" + ", ".join(kstr(x) for x in items) + ")"

lines = []
lines.append("package com.educalab.ninobiologo.data.local.seed")
lines.append("")
lines.append("import com.educalab.ninobiologo.data.local.entity.*")
lines.append("import com.educalab.ninobiologo.domain.model.AnalysisTaskType")
lines.append("import com.educalab.ninobiologo.domain.model.CreaturePartCategory")
lines.append("import com.educalab.ninobiologo.domain.model.DiscoveryCategory")
lines.append("import com.educalab.ninobiologo.domain.model.DiscoveryRarity")
lines.append("import com.educalab.ninobiologo.domain.model.UnlockCriteriaType")
lines.append("")
lines.append("/**")
lines.append(" * Contenido semilla de NiñoBiólogo: Vida en Miniatura.")
lines.append(" *")
lines.append(" * GENERADO AUTOMÁTICAMENTE por tools/generate_seed_data.py — no editar a mano.")
lines.append(" * Para modificar el contenido, edita el script y vuelve a ejecutarlo:")
lines.append(" *   python3 tools/generate_seed_data.py")
lines.append(" *")
lines.append(" * Cantidades: {} ambientes, {} muestras, {} descubrimientos, {} experimentos,".format(
    len(ENVIRONMENTS), len(SAMPLES), len(DISCOVERIES), len(EXPERIMENTS)))
lines.append(" * {} piezas de criatura, {} tareas de análisis, {} coleccionables, {} mejoras de laboratorio,".format(
    len(CREATURE_PART_OPTIONS), len(CHALLENGES), len(LAB_COLLECTIBLES), len(LABORATORY_UPGRADES)))
lines.append(" * {} modelos de célula, {} sistemas del cuerpo.".format(len(CELL_MODELS), len(BODY_SYSTEMS)))
lines.append(" */")
lines.append("object SeedContent {")
lines.append("")

# Environments
lines.append("    val environments: List<MicroscopicEnvironmentEntity> = listOf(")
for e in ENVIRONMENTS:
    lines.append(f"        MicroscopicEnvironmentEntity(id = {kstr(e['id'])}, orderIndex = {e['order']}, name = {kstr(e['name'])}, "
                 f"tagline = {kstr(e['tagline'])}, description = {kstr(e['desc'])}, iconKey = {kstr(e['icon'])}, "
                 f"primaryColorHex = {kstr(e['primary'])}, secondaryColorHex = {kstr(e['secondary'])}),")
lines.append("    )")
lines.append("")

# Samples
lines.append("    val samples: List<ScientificSampleEntity> = listOf(")
for s in SAMPLES:
    lines.append(f"        ScientificSampleEntity(id = {kstr(s['id'])}, environmentId = {kstr(s['environment'])}, orderIndex = {s['order']}, "
                 f"name = {kstr(s['name'])}, origin = {kstr(s['origin'])}, difficulty = {s['difficulty']}, iconKey = {kstr(s['icon'])}),")
lines.append("    )")
lines.append("")

# Discoveries
lines.append("    val discoveries: List<MicroscopeDiscoveryEntity> = listOf(")
for d in DISCOVERIES:
    lines.append(f"        MicroscopeDiscoveryEntity(id = {kstr(d['id'])}, sampleId = {kstr(d['sample'])}, environmentId = {kstr(d['environment'])}, "
                 f"name = {kstr(d['name'])}, scientificName = {kstr(d['sci'])}, category = DiscoveryCategory.{d['cat']}, habitat = {kstr(d['habitat'])}, "
                 f"diet = {kstr(d['diet'])}, characteristics = {klist_str(d['chars'])}, curiosity = {kstr(d['curiosity'])}, "
                 f"rarity = DiscoveryRarity.{d['rarity']}, iconKey = {kstr(d['icon'])}),")
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

# Experiments
lines.append("    val experiments: List<ExperimentEntity> = listOf(")
for e in EXPERIMENTS:
    lines.append(f"        ExperimentEntity(id = {kstr(e['id'])}, environmentId = {kstr(e['environment'])}, orderIndex = {e['order']}, "
                 f"question = {kstr(e['question'])}, description = {kstr(e['desc'])}, variableName = {kstr(e['var_name'])}, "
                 f"variableUnit = {kstr(e['var_unit'])}, variableMin = {e['var_min']}, variableMax = {e['var_max']}, "
                 f"idealMin = {e['ideal_min']}, idealMax = {e['ideal_max']}, rewardXp = {e['reward_xp']}),")
lines.append("    )")
lines.append("")

# Creature part options
lines.append("    val creaturePartOptions: List<CreaturePartOptionEntity> = listOf(")
for p in CREATURE_PART_OPTIONS:
    lines.append(f"        CreaturePartOptionEntity(id = {kstr(p['id'])}, category = CreaturePartCategory.{p['cat']}, name = {kstr(p['name'])}, "
                 f"description = {kstr(p['desc'])}, bestEnvironmentId = {kstr(p['best'])}),")
lines.append("    )")
lines.append("")

# Challenges (Analizador)
lines.append("    val challenges: List<ChallengeEntity> = listOf(")
for c in CHALLENGES:
    lines.append(f"        ChallengeEntity(id = {kstr(c['id'])}, environmentId = {kstr(c['environment'])}, type = AnalysisTaskType.{c['type']}, "
                 f"title = {kstr(c['title'])}, instructions = {kstr(c['instructions'])}, relatedDiscoveryIds = {klist_str(c['disc_ids'])}, rewardXp = {c['reward_xp']}),")
lines.append("    )")
lines.append("")

# Lab collectibles
lines.append("    val labCollectibles: List<LabCollectibleEntity> = listOf(")
for c in LAB_COLLECTIBLES:
    env_val = kstr(c['env']) if c['env'] else "null"
    lines.append(f"        LabCollectibleEntity(id = {kstr(c['id'])}, name = {kstr(c['name'])}, description = {kstr(c['desc'])}, "
                 f"iconKey = {kstr(c['icon'])}, criteriaType = UnlockCriteriaType.{c['ctype']}, criteriaValue = {c['cvalue']}, environmentId = {env_val}),")
lines.append("    )")
lines.append("")

# Laboratory upgrades
lines.append("    val laboratoryUpgrades: List<LaboratoryUpgradeEntity> = listOf(")
for u in LABORATORY_UPGRADES:
    env_val = kstr(u['env']) if u['env'] else "null"
    lines.append(f"        LaboratoryUpgradeEntity(id = {kstr(u['id'])}, name = {kstr(u['name'])}, description = {kstr(u['desc'])}, "
                 f"iconKey = {kstr(u['icon'])}, criteriaType = UnlockCriteriaType.{u['ctype']}, criteriaValue = {u['cvalue']}, environmentId = {env_val}),")
lines.append("    )")
lines.append("")

lines.append(f"    val avatarKeys: List<String> = {klist_str(AVATAR_KEYS)}")
lines.append("}")

with open(OUT_PATH, "w", encoding="utf-8") as f:
    f.write("\n".join(lines) + "\n")

print(f"\nEscrito {OUT_PATH} ({len(lines)} líneas)")

# ---------------------------------------------------------------------------
# 12. VALIDACIÓN DE INTEGRIDAD (equivalente a un test de integridad de datos semilla)
# ---------------------------------------------------------------------------
def check(condition, message, errors):
    if not condition:
        errors.append(message)

errors = []
env_ids = {e["id"] for e in ENVIRONMENTS}
sample_ids_all = {s["id"] for s in SAMPLES}
disc_ids_all = {d["id"] for d in DISCOVERIES}

check(len(disc_ids_all) == len(DISCOVERIES), "IDs de descubrimientos duplicados", errors)
check(len(sample_ids_all) == len(SAMPLES), "IDs de muestras duplicados", errors)
check(len({e['id'] for e in EXPERIMENTS}) == len(EXPERIMENTS), "IDs de experimentos duplicados", errors)
check(len({p['id'] for p in CREATURE_PART_OPTIONS}) == len(CREATURE_PART_OPTIONS), "IDs de piezas de criatura duplicados", errors)
check(len({c['id'] for c in CHALLENGES}) == len(CHALLENGES), "IDs de tareas de análisis duplicados", errors)
check(len({c['id'] for c in LAB_COLLECTIBLES}) == len(LAB_COLLECTIBLES), "IDs de coleccionables duplicados", errors)
check(len({u['id'] for u in LABORATORY_UPGRADES}) == len(LABORATORY_UPGRADES), "IDs de mejoras de laboratorio duplicados", errors)

for s in SAMPLES:
    check(s["environment"] in env_ids, f"Muestra {s['id']} referencia ambiente inexistente {s['environment']}", errors)
    for did in s["discovery_ids"]:
        check(did in disc_ids_all, f"Muestra {s['id']} referencia descubrimiento inexistente {did}", errors)
for d in DISCOVERIES:
    check(d["environment"] in env_ids, f"Descubrimiento {d['id']} referencia ambiente inexistente", errors)
    check(d["sample"] in sample_ids_all, f"Descubrimiento {d['id']} referencia muestra inexistente {d['sample']}", errors)
for e in EXPERIMENTS:
    check(e["environment"] in env_ids, f"Experimento {e['id']} referencia ambiente inexistente", errors)
    check(e["var_min"] < e["var_max"], f"Experimento {e['id']} tiene rango de variable inválido", errors)
    check(e["ideal_min"] >= e["var_min"] and e["ideal_max"] <= e["var_max"], f"Experimento {e['id']} tiene rango ideal fuera del rango de la variable", errors)
for p in CREATURE_PART_OPTIONS:
    check(p["best"] in env_ids, f"Pieza {p['id']} referencia ambiente inexistente", errors)
for c in CHALLENGES:
    check(c["environment"] in env_ids, f"Tarea de análisis {c['id']} referencia ambiente inexistente", errors)
    for did in c["disc_ids"]:
        check(did in disc_ids_all, f"Tarea de análisis {c['id']} referencia descubrimiento inexistente {did}", errors)
for c in LAB_COLLECTIBLES:
    if c["env"] is not None:
        check(c["env"] in env_ids, f"Coleccionable {c['id']} referencia ambiente inexistente", errors)
for u in LABORATORY_UPGRADES:
    if u["env"] is not None:
        check(u["env"] in env_ids, f"Mejora {u['id']} referencia ambiente inexistente", errors)
for c in CELL_MODELS:
    check(len(c["structures"]) >= 3, f"Modelo de célula {c['id']} tiene menos de 3 estructuras", errors)
    struct_ids = [st["id"] for st in c["structures"]]
    check(len(struct_ids) == len(set(struct_ids)), f"Estructuras duplicadas en {c['id']}", errors)
for s in BODY_SYSTEMS:
    check(len(s["organs"]) >= 2, f"Sistema {s['id']} tiene menos de 2 órganos", errors)

check(len(ENVIRONMENTS) == 5, "El total de ambientes no es 5", errors)
check(len(SAMPLES) == 20, "El total de muestras no es 20", errors)
check(len(DISCOVERIES) == 30, "El total de descubrimientos no es 30", errors)
check(len(EXPERIMENTS) == 10, "El total de experimentos no es 10", errors)
check(len(CREATURE_PART_OPTIONS) == 16, "El total de piezas de criatura no es 16", errors)
check(len(CHALLENGES) == 30, "El total de tareas de análisis no es 30", errors)
check(len(LAB_COLLECTIBLES) == 15, "El total de coleccionables no es 15", errors)
check(len(LABORATORY_UPGRADES) == 8, "El total de mejoras de laboratorio no es 8", errors)
check(len(AVATAR_KEYS) == 8, "El total de avatares no es 8", errors)

print("\n=== VALIDACIÓN DE INTEGRIDAD DE DATOS SEMILLA ===")
if errors:
    for err in errors:
        print(f"  [FALLO] {err}")
    print(f"\n{len(errors)} problema(s) encontrados.")
    raise SystemExit(1)
else:
    print("  Todas las verificaciones de integridad pasaron correctamente.")
    print(f"  Ambientes: {len(ENVIRONMENTS)} | Muestras: {len(SAMPLES)} | Descubrimientos: {len(DISCOVERIES)}")
    print(f"  Experimentos: {len(EXPERIMENTS)} | Piezas de criatura: {len(CREATURE_PART_OPTIONS)}")
    print(f"  Tareas de análisis: {len(CHALLENGES)} | Coleccionables: {len(LAB_COLLECTIBLES)} | Mejoras: {len(LABORATORY_UPGRADES)}")
    print(f"  Modelos de célula: {len(CELL_MODELS)} | Sistemas del cuerpo: {len(BODY_SYSTEMS)} | Avatares: {len(AVATAR_KEYS)}")

# ---------------------------------------------------------------------------
# 13. EMISIÓN DE database/schema.sql y database/sample_data.sql
# ---------------------------------------------------------------------------
SCHEMA_SQL = """-- NiñoBiólogo: Vida en Miniatura — esquema SQLite (Room, versión 2)
-- Generado a partir de las entidades reales en app/src/main/java/.../data/local/entity
-- Motor: SQLite (a través de Room 2.6.1).

PRAGMA foreign_keys = ON;

-- ===================== CONTENIDO (semilla, solo lectura para el usuario) =====================

CREATE TABLE microscopic_environments (
    id TEXT NOT NULL PRIMARY KEY,
    orderIndex INTEGER NOT NULL,
    name TEXT NOT NULL,
    tagline TEXT NOT NULL,
    description TEXT NOT NULL,
    iconKey TEXT NOT NULL,
    primaryColorHex TEXT NOT NULL,
    secondaryColorHex TEXT NOT NULL
);

CREATE TABLE scientific_samples (
    id TEXT NOT NULL PRIMARY KEY,
    environmentId TEXT NOT NULL,
    orderIndex INTEGER NOT NULL,
    name TEXT NOT NULL,
    origin TEXT NOT NULL,
    difficulty INTEGER NOT NULL,
    iconKey TEXT NOT NULL,
    FOREIGN KEY (environmentId) REFERENCES microscopic_environments(id) ON DELETE CASCADE
);
CREATE INDEX index_scientific_samples_environmentId ON scientific_samples(environmentId);

CREATE TABLE microscope_discoveries (
    id TEXT NOT NULL PRIMARY KEY,
    sampleId TEXT NOT NULL,
    environmentId TEXT NOT NULL,
    name TEXT NOT NULL,
    scientificName TEXT NOT NULL,
    category TEXT NOT NULL,
    habitat TEXT NOT NULL,
    diet TEXT NOT NULL,
    characteristics TEXT NOT NULL,
    curiosity TEXT NOT NULL,
    rarity TEXT NOT NULL,
    iconKey TEXT NOT NULL,
    FOREIGN KEY (sampleId) REFERENCES scientific_samples(id) ON DELETE CASCADE,
    FOREIGN KEY (environmentId) REFERENCES microscopic_environments(id) ON DELETE CASCADE
);
CREATE INDEX index_microscope_discoveries_sampleId ON microscope_discoveries(sampleId);
CREATE INDEX index_microscope_discoveries_environmentId ON microscope_discoveries(environmentId);
CREATE INDEX index_microscope_discoveries_name ON microscope_discoveries(name);

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

CREATE TABLE experiments (
    id TEXT NOT NULL PRIMARY KEY,
    environmentId TEXT NOT NULL,
    orderIndex INTEGER NOT NULL,
    question TEXT NOT NULL,
    description TEXT NOT NULL,
    variableName TEXT NOT NULL,
    variableUnit TEXT NOT NULL,
    variableMin INTEGER NOT NULL,
    variableMax INTEGER NOT NULL,
    idealMin INTEGER NOT NULL,
    idealMax INTEGER NOT NULL,
    rewardXp INTEGER NOT NULL,
    FOREIGN KEY (environmentId) REFERENCES microscopic_environments(id) ON DELETE CASCADE
);
CREATE INDEX index_experiments_environmentId ON experiments(environmentId);

CREATE TABLE creature_part_options (
    id TEXT NOT NULL PRIMARY KEY,
    category TEXT NOT NULL,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    bestEnvironmentId TEXT NOT NULL
);

CREATE TABLE challenges (
    id TEXT NOT NULL PRIMARY KEY,
    environmentId TEXT NOT NULL,
    type TEXT NOT NULL,
    title TEXT NOT NULL,
    instructions TEXT NOT NULL,
    relatedDiscoveryIds TEXT NOT NULL,
    rewardXp INTEGER NOT NULL,
    FOREIGN KEY (environmentId) REFERENCES microscopic_environments(id) ON DELETE CASCADE
);
CREATE INDEX index_challenges_environmentId ON challenges(environmentId);

CREATE TABLE lab_collectibles (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    iconKey TEXT NOT NULL,
    criteriaType TEXT NOT NULL,
    criteriaValue INTEGER NOT NULL,
    environmentId TEXT,
    FOREIGN KEY (environmentId) REFERENCES microscopic_environments(id) ON DELETE SET NULL
);
CREATE INDEX index_lab_collectibles_environmentId ON lab_collectibles(environmentId);

CREATE TABLE laboratory_upgrades (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    iconKey TEXT NOT NULL,
    criteriaType TEXT NOT NULL,
    criteriaValue INTEGER NOT NULL,
    environmentId TEXT,
    FOREIGN KEY (environmentId) REFERENCES microscopic_environments(id) ON DELETE SET NULL
);
CREATE INDEX index_laboratory_upgrades_environmentId ON laboratory_upgrades(environmentId);

-- ===================== PROGRESO (datos reales del jugador, mutables) =====================

CREATE TABLE explorer_profile (
    id INTEGER NOT NULL PRIMARY KEY,
    alias TEXT NOT NULL,
    avatarKey TEXT NOT NULL,
    totalXp INTEGER NOT NULL,
    onboardingCompleted INTEGER NOT NULL,
    soundEnabled INTEGER NOT NULL,
    hapticsEnabled INTEGER NOT NULL,
    createdAtEpochMillis INTEGER NOT NULL
);

CREATE TABLE discoveries_found (
    discoveryId TEXT NOT NULL PRIMARY KEY,
    discoveredAtEpochMillis INTEGER NOT NULL,
    viaSampleId TEXT,
    FOREIGN KEY (discoveryId) REFERENCES microscope_discoveries(id) ON DELETE CASCADE
);
CREATE INDEX index_discoveries_found_discoveryId ON discoveries_found(discoveryId);

CREATE TABLE sample_exploration (
    sampleId TEXT NOT NULL PRIMARY KEY,
    state TEXT NOT NULL,
    discoveriesFound INTEGER NOT NULL,
    totalDiscoveries INTEGER NOT NULL,
    lastAttemptEpochMillis INTEGER,
    FOREIGN KEY (sampleId) REFERENCES scientific_samples(id) ON DELETE CASCADE
);
CREATE INDEX index_sample_exploration_sampleId ON sample_exploration(sampleId);

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

CREATE TABLE collectible_unlocks (
    collectibleId TEXT NOT NULL PRIMARY KEY,
    unlockedAtEpochMillis INTEGER NOT NULL,
    FOREIGN KEY (collectibleId) REFERENCES lab_collectibles(id) ON DELETE CASCADE
);
CREATE INDEX index_collectible_unlocks_collectibleId ON collectible_unlocks(collectibleId);

CREATE TABLE lab_upgrade_unlocks (
    upgradeId TEXT NOT NULL PRIMARY KEY,
    unlockedAtEpochMillis INTEGER NOT NULL,
    FOREIGN KEY (upgradeId) REFERENCES laboratory_upgrades(id) ON DELETE CASCADE
);
CREATE INDEX index_lab_upgrade_unlocks_upgradeId ON lab_upgrade_unlocks(upgradeId);

CREATE TABLE creature_collection (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    formaId TEXT NOT NULL,
    movimientoId TEXT NOT NULL,
    alimentacionId TEXT NOT NULL,
    adaptacionId TEXT NOT NULL,
    targetEnvironmentId TEXT NOT NULL,
    fitScore INTEGER NOT NULL,
    createdAtEpochMillis INTEGER NOT NULL
);

CREATE TABLE experiment_results (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    experimentId TEXT NOT NULL,
    variableValue INTEGER NOT NULL,
    outcome TEXT NOT NULL,
    message TEXT NOT NULL,
    xpAwarded INTEGER NOT NULL,
    savedAtEpochMillis INTEGER NOT NULL,
    FOREIGN KEY (experimentId) REFERENCES experiments(id) ON DELETE CASCADE
);
CREATE INDEX index_experiment_results_experimentId ON experiment_results(experimentId);

CREATE TABLE discovery_journal (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL,
    title TEXT NOT NULL,
    note TEXT NOT NULL,
    filePath TEXT,
    relatedEnvironmentId TEXT,
    createdAtEpochMillis INTEGER NOT NULL,
    FOREIGN KEY (relatedEnvironmentId) REFERENCES microscopic_environments(id) ON DELETE SET NULL
);
CREATE INDEX index_discovery_journal_relatedEnvironmentId ON discovery_journal(relatedEnvironmentId);
"""

with open("database/schema.sql", "w", encoding="utf-8") as f:
    f.write(SCHEMA_SQL)
print("Escrito database/schema.sql")

def sql_str(s):
    return "'" + str(s).replace("'", "''") + "'"

def sql_list(items):
    return sql_str("|".join(items))

sample_lines = []
sample_lines.append("-- NiñoBiólogo: Vida en Miniatura — datos semilla reales")
sample_lines.append("-- Generado por tools/generate_seed_data.py. El contenido completo se inserta en tiempo de")
sample_lines.append("-- ejecución por DatabaseSeeder.kt a partir de SeedContent.kt (misma fuente de datos).")
sample_lines.append("")
sample_lines.append(f"-- Ambientes ({len(ENVIRONMENTS)}/{len(ENVIRONMENTS)})")
for e in ENVIRONMENTS:
    sample_lines.append(
        f"INSERT INTO microscopic_environments (id, orderIndex, name, tagline, description, iconKey, primaryColorHex, secondaryColorHex) VALUES "
        f"({sql_str(e['id'])}, {e['order']}, {sql_str(e['name'])}, {sql_str(e['tagline'])}, {sql_str(e['desc'])}, {sql_str(e['icon'])}, {sql_str(e['primary'])}, {sql_str(e['secondary'])});"
    )

sample_lines.append("")
sample_lines.append(f"-- Muestras ({len(SAMPLES)}/{len(SAMPLES)})")
for s in SAMPLES:
    sample_lines.append(
        f"INSERT INTO scientific_samples (id, environmentId, orderIndex, name, origin, difficulty, iconKey) VALUES "
        f"({sql_str(s['id'])}, {sql_str(s['environment'])}, {s['order']}, {sql_str(s['name'])}, {sql_str(s['origin'])}, {s['difficulty']}, {sql_str(s['icon'])});"
    )

sample_lines.append("")
sample_lines.append(f"-- Descubrimientos ({len(DISCOVERIES)}/{len(DISCOVERIES)})")
for d in DISCOVERIES:
    sample_lines.append(
        f"INSERT INTO microscope_discoveries (id, sampleId, environmentId, name, scientificName, category, habitat, diet, characteristics, curiosity, rarity, iconKey) VALUES "
        f"({sql_str(d['id'])}, {sql_str(d['sample'])}, {sql_str(d['environment'])}, {sql_str(d['name'])}, {sql_str(d['sci'])}, {sql_str(d['cat'])}, {sql_str(d['habitat'])}, {sql_str(d['diet'])}, {sql_list(d['chars'])}, {sql_str(d['curiosity'])}, {sql_str(d['rarity'])}, {sql_str(d['icon'])});"
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
sample_lines.append(f"-- Experimentos ({len(EXPERIMENTS)}/{len(EXPERIMENTS)})")
for e in EXPERIMENTS:
    sample_lines.append(
        f"INSERT INTO experiments (id, environmentId, orderIndex, question, description, variableName, variableUnit, variableMin, variableMax, idealMin, idealMax, rewardXp) VALUES "
        f"({sql_str(e['id'])}, {sql_str(e['environment'])}, {e['order']}, {sql_str(e['question'])}, {sql_str(e['desc'])}, {sql_str(e['var_name'])}, {sql_str(e['var_unit'])}, {e['var_min']}, {e['var_max']}, {e['ideal_min']}, {e['ideal_max']}, {e['reward_xp']});"
    )

sample_lines.append("")
sample_lines.append(f"-- Piezas del Constructor Biológico ({len(CREATURE_PART_OPTIONS)}/{len(CREATURE_PART_OPTIONS)})")
for p in CREATURE_PART_OPTIONS:
    sample_lines.append(
        f"INSERT INTO creature_part_options (id, category, name, description, bestEnvironmentId) VALUES "
        f"({sql_str(p['id'])}, {sql_str(p['cat'])}, {sql_str(p['name'])}, {sql_str(p['desc'])}, {sql_str(p['best'])});"
    )

sample_lines.append("")
sample_lines.append(f"-- Tareas del Analizador ({len(CHALLENGES)}/{len(CHALLENGES)})")
for c in CHALLENGES:
    sample_lines.append(
        f"INSERT INTO challenges (id, environmentId, type, title, instructions, relatedDiscoveryIds, rewardXp) VALUES "
        f"({sql_str(c['id'])}, {sql_str(c['environment'])}, {sql_str(c['type'])}, {sql_str(c['title'])}, {sql_str(c['instructions'])}, {sql_list(c['disc_ids'])}, {c['reward_xp']});"
    )

sample_lines.append("")
sample_lines.append(f"-- Coleccionables ({len(LAB_COLLECTIBLES)}/{len(LAB_COLLECTIBLES)})")
for c in LAB_COLLECTIBLES:
    env_val = sql_str(c['env']) if c['env'] else "NULL"
    sample_lines.append(
        f"INSERT INTO lab_collectibles (id, name, description, iconKey, criteriaType, criteriaValue, environmentId) VALUES "
        f"({sql_str(c['id'])}, {sql_str(c['name'])}, {sql_str(c['desc'])}, {sql_str(c['icon'])}, {sql_str(c['ctype'])}, {c['cvalue']}, {env_val});"
    )

sample_lines.append("")
sample_lines.append(f"-- Mejoras del laboratorio ({len(LABORATORY_UPGRADES)}/{len(LABORATORY_UPGRADES)})")
for u in LABORATORY_UPGRADES:
    env_val = sql_str(u['env']) if u['env'] else "NULL"
    sample_lines.append(
        f"INSERT INTO laboratory_upgrades (id, name, description, iconKey, criteriaType, criteriaValue, environmentId) VALUES "
        f"({sql_str(u['id'])}, {sql_str(u['name'])}, {sql_str(u['desc'])}, {sql_str(u['icon'])}, {sql_str(u['ctype'])}, {u['cvalue']}, {env_val});"
    )

sample_lines.append("")
sample_lines.append("-- Perfil inicial (se crea automáticamente en el primer arranque)")
sample_lines.append(
    "INSERT INTO explorer_profile (id, alias, avatarKey, totalXp, onboardingCompleted, soundEnabled, hapticsEnabled, createdAtEpochMillis) VALUES "
    "(1, 'Joven Biólogo', 'avatar_explorador_1', 0, 0, 1, 1, 0);"
)

with open("database/sample_data.sql", "w", encoding="utf-8") as f:
    f.write("\n".join(sample_lines) + "\n")

total_inserts = sum(1 for l in sample_lines if l.startswith("INSERT"))
print(f"Escrito database/sample_data.sql ({total_inserts} INSERT reales)")
