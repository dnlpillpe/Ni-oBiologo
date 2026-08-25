-- NiñoBiólogo: Exploradores de la Vida — esquema SQLite (Room, versión 1)
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
