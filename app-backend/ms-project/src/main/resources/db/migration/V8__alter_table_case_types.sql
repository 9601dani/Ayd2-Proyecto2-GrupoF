ALTER TABLE cases
    CHANGE COLUMN Fk_Case_Phase FK_Case_Type INT NOT NULL;

ALTER TABLE case_phases
    CHANGE COLUMN FK_Case_Types FK_Case_Type INT NOT NULL;
