-- NiñoBiólogo: Vida en Miniatura — esquema SQLite (Room, versión 2)
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
