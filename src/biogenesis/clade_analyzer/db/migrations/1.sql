CREATE TABLE IF NOT EXISTS colors(
  ID INTEGER PRIMARY KEY ASC,
  COLOR            TEXT     UNIQUE NOT NULL,
  NAME             TEXT     NOT NULL
);
CREATE TABLE IF NOT EXISTS  summary_files(
  ID INTEGER PRIMARY KEY ASC,
  FILENAME       TEXT    UNIQUE NOT NULL,
  STATE          TEXT    NOT NULL
);
CREATE TABLE IF NOT EXISTS  clade_summaries(
  ID INTEGER PRIMARY KEY ASC,
  SUMMARY_FILE_ID INTEGER    NOT NULL,
  CLADEID        TEXT        NOT NULL,
  TIME           INTEGER     NOT NULL,
  GENETIC_CODE   TEXT        NOT NULL,  -- this is the most common genetic code for this clade in this time
  POPULATION     INTEGER     NOT NULL
);
CREATE TABLE IF NOT EXISTS  clades(
  ID INTEGER PRIMARY KEY ASC,
  CLADEID          TEXT     UNIQUE NOT NULL,
  FIRST_SEEN_TIME  INTEGER  NOT NULL,
  LAST_SEEN_TIME   INTEGER  NOT NULL,
  GENETIC_CODE     TEXT     NOT NULL,  -- this is the genetic code from the clade_summaries table at the last seen time
  MAX_POPULATION   INTEGER  NOT NULL
);
CREATE INDEX IF NOT EXISTS CLADES_SUMMARIES_TIME_INDEX ON clade_summaries(TIME);
CREATE INDEX IF NOT EXISTS CLADE_SUMMARIES_CLADEID_INDEX ON clade_summaries(CLADEID);

INSERT OR IGNORE INTO colors(name, color) VALUES ('green', '{"r":0,"g":255,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('forest', '{"r":0,"g":128,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('ivy', '{"r":136,"g":164,"b":128,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('spring', '{"r":0,"g":255,"b":128,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('summer', '{"r":128,"g":255,"b":64,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('lime', '{"r":176,"g":255,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('leaf', '{"r":92,"g":184,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('c4', '{"r":96,"g":192,"b":96,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('jade', '{"r":0,"g":168,"b":107,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('grass', '{"r":144,"g":176,"b":64,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('bark', '{"r":96,"g":128,"b":64,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('purple', '{"r":168,"g":0,"b":84,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('plankton', '{"r":96,"g":192,"b":192,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('red', '{"r":255,"g":0,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('fire', '{"r":255,"g":100,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('orange', '{"r":255,"g":200,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('maroon', '{"r":128,"g":0,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('crimson', '{"r":220,"g":40,"b":60,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('pink', '{"r":255,"g":175,"b":175,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('cream', '{"r":208,"g":192,"b":140,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('silver', '{"r":192,"g":192,"b":192,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('spike', '{"r":164,"g":132,"b":100,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('lilac', '{"r":192,"g":128,"b":255,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('gray', '{"r":128,"g":128,"b":128,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('violet', '{"r":128,"g":0,"b":128,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('olive', '{"r":176,"g":176,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('sky', '{"r":128,"g":192,"b":255,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('blue', '{"r":0,"g":0,"b":255,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('ochre', '{"r":204,"g":119,"b":34,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('fallow', '{"r":150,"g":113,"b":23,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('spore', '{"r":0,"g":80,"b":160,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('white', '{"r":255,"g":255,"b":255,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('plague', '{"r":255,"g":192,"b":255,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('coral', '{"r":255,"g":100,"b":138,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('mint', '{"r":160,"g":224,"b":160,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('lavender', '{"r":128,"g":96,"b":176,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('magenta', '{"r":255,"g":0,"b":255,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('rose', '{"r":255,"g":0,"b":128,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('cyan', '{"r":0,"g":255,"b":255,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('teal', '{"r":0,"g":128,"b":128,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('drift', '{"r":64,"g":160,"b":160,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('spin', '{"r":128,"g":255,"b":224,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('yellow', '{"r":255,"g":255,"b":0,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('auburn', '{"r":128,"g":48,"b":48,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('indigo', '{"r":111,"g":0,"b":255,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('blond', '{"r":255,"g":255,"b":128,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('flower', '{"r":128,"g":128,"b":255,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('darkgray', '{"r":64,"g":64,"b":64,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('gold', '{"r":212,"g":175,"b":55,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('dark', '{"r":64,"g":32,"b":16,"a":255}');
INSERT OR IGNORE INTO colors(name, color) VALUES ('eye', '{"r":0,"g":64,"b":64,"a":255}');
